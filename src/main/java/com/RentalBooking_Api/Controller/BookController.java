package com.RentalBooking_Api.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Service.BookService;
import com.RentalBooking_Api.dto.BookDTO;
import com.RentalBooking_Api.util.MapperUtils;


@RestController
@RequestMapping("/books")
public class BookController {
	
	private final BookService bookservice;

	@Autowired
	public BookController(BookService bookservice) {
		this.bookservice = bookservice;
	}
	
	 // ✅ Get all books
	@GetMapping("/allbooks")
	public List<BookDTO>Getallbooks(){
		return bookservice.getAllBooks();	
	}
	
	// ✅ Get by id
	@GetMapping("/{id}")
	public ResponseEntity<BookDTO> getByBookId(@PathVariable Long id) {
	    return bookservice.getBookById(id)
	            .map(ResponseEntity::ok)
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchBooks(@RequestParam String query,  @RequestParam Long userId) {
	    List<BookDTO> books = bookservice.searchBooks(query,userId	);
	    if (books.isEmpty()) {
	        return ResponseEntity.ok("Thank you! We couldn’t find this book. Here are some suggestions...");
	    }
	    return ResponseEntity.ok(books);
	}

	
	@PostMapping("/import")
	public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO dto) {
	    dto.setCopies(dto.getCopies() == null ? 1 : dto.getCopies()); // defaults
	    BookDTO saved = bookservice.createBook(dto);
	    return ResponseEntity.ok(saved);
	}
	
	@PutMapping("/major/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO updatedBook) {
        return bookservice.getBookById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthors(updatedBook.getAuthors());
            book.setPublisher(updatedBook.getPublisher());
            book.setIsbn(updatedBook.getIsbn());
            book.setGenres(updatedBook.getGenres());
            book.setCategory(updatedBook.getCategory());
            book.setGoogleBookId(updatedBook.getGoogleBookId());
            book.setCopies(updatedBook.getCopies());
            book.setPublishedDate(updatedBook.getPublishedDate());
            book.setDescription(updatedBook.getDescription());
            book.setCoverImageUrl(updatedBook.getCoverImageUrl());
            book.setAverageRating(updatedBook.getAverageRating());
            book.setRatingsCount(updatedBook.getRatingsCount());
            return ResponseEntity.ok(bookservice.createBook(book));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

	//patchmapping 
	@PatchMapping("/Minor/{id}")
	public ResponseEntity<BookDTO> patchBook(@PathVariable Long id, @RequestBody BookDTO updatedBook) {
	    return bookservice.patchBook(id, updatedBook)
	            .map(ResponseEntity::ok)
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}

	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void>DeleteBook(@PathVariable Long id){
		bookservice.deleteBook(id);
        return ResponseEntity.noContent().build();
		
	}

}
