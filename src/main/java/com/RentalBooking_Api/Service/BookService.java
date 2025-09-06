package com.RentalBooking_Api.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RentalBooking_Api.*;
import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Repository.BookRepository;
import com.RentalBooking_Api.dto.BookDTO;
import com.RentalBooking_Api.util.MapperUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final GoogleBooksService googleBookService;
	private final UserService userService; // 📌 for browsing history

	public BookService(BookRepository bookRepository, GoogleBooksService googleBookService, UserService userService) {
		this.bookRepository = bookRepository;
		this.googleBookService = googleBookService;
		this.userService = userService;
	}

	/**
	 * 🔍 Hybrid Search (DB → Google Books API) Tracks browsing history silently
	 * (Amazon style).
	 * @param userId 
	 */
	public List<BookDTO> searchBooks(String query, Long userId) {
		List<Book> localBooks = bookRepository.findByTitleContainingIgnoreCase(query);

		if (!localBooks.isEmpty()) {
			userService.recordSearchEvent(userId, query, "LOCAL");
			return MapperUtils.mapList(localBooks, BookDTO.class);
		}

		List<BookDTO> googleBooks = googleBookService.searchBooks(query);

		if (!googleBooks.isEmpty()) {
			userService.recordSearchEvent(userId, query, "GOOGLE_API");
		}

		return googleBooks;
	}
	
	public List<BookDTO> getAllBooks() {
	    List<Book> books = bookRepository.findAll();
	    return MapperUtils.mapList(books, BookDTO.class);
	}

	/**
	 * When user picks a GoogleBook result, save it into DB → supports "Pick one →
	 * Rent/Return/Browse".
	 */
	@Transactional
	public BookDTO saveGoogleBookSelection(BookDTO bookDTO) {
		Book book = MapperUtils.map(bookDTO, Book.class);
		Book saved = bookRepository.save(book);
		return MapperUtils.map(saved, BookDTO.class);
	}

	// ------------------- CRUD operations -------------------

	public BookDTO createBook(BookDTO dto) {
		Book book = MapperUtils.map(dto, Book.class);
		return MapperUtils.map(bookRepository.save(book), BookDTO.class);
	}

	public BookDTO updateBook(Long id, BookDTO dto) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));

		book.setTitle(dto.getTitle());
		book.setAuthors(dto.getAuthors());
		book.setCategory(dto.getCategory());
		book.setIsbn(dto.getIsbn());

		return MapperUtils.map(bookRepository.save(book), BookDTO.class);
	}

	public Optional<BookDTO> patchBook(Long id, BookDTO updatedDto) {
	    return bookRepository.findById(id).map(book -> {
	        if (updatedDto.getTitle() != null) book.setTitle(updatedDto.getTitle());
	        if (updatedDto.getAuthors() != null) book.setAuthors(updatedDto.getAuthors());
	        if (updatedDto.getPublisher() != null) book.setPublisher(updatedDto.getPublisher());
	        if (updatedDto.getIsbn() != null) book.setIsbn(updatedDto.getIsbn());
	        if (updatedDto.getGenres() != null) book.setGenres(updatedDto.getGenres());
	        if (updatedDto.getCategory() != null) book.setCategory(updatedDto.getCategory());
	        if (updatedDto.getGoogleBookId() != null) book.setGoogleBookId(updatedDto.getGoogleBookId());
	        if (updatedDto.getCopies() != null) book.setCopies(updatedDto.getCopies());
	        if (updatedDto.getPublishedDate() != null) book.setPublishedDate(updatedDto.getPublishedDate());
	        if (updatedDto.getDescription() != null) book.setDescription(updatedDto.getDescription());
	        if (updatedDto.getCoverImageUrl() != null) book.setCoverImageUrl(updatedDto.getCoverImageUrl());
	        if (updatedDto.getAverageRating() != null) book.setAverageRating(updatedDto.getAverageRating());
	        if (updatedDto.getRatingsCount() != null) book.setRatingsCount(updatedDto.getRatingsCount());

	        Book saved = bookRepository.save(book);
	        return MapperUtils.map(saved, BookDTO.class);
	    });
	}

	public void deleteBook(Long id) {
		if (!bookRepository.existsById(id)) {
			throw new RuntimeException("Book not found");
		}
		bookRepository.deleteById(id);
	}

	public Optional<BookDTO> getBookById(Long id) {
		return bookRepository.findById(id).map(book -> MapperUtils.map(book, BookDTO.class));
	}

	// ------------------- Recommendation Hooks -------------------

	public List<Book> getBooksByAuthor(String author) {
		return bookRepository.findByAuthorNameLike(author);
	}

	public List<Book> getBooksByGenre(String genre) {
		return bookRepository.findByGenresContainingIgnoreCase(genre);
	}

	public List<Book> getPopularBooks() {
		return bookRepository.findTop10ByOrderByRentalCountDesc();
	}

}
