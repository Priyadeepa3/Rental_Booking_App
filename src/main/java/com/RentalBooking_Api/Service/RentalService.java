package com.RentalBooking_Api.Service;

import com.RentalBooking_Api.Repository.RentalRepository;
import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Rental;
import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Repository.BookRepository;
import com.RentalBooking_Api.Repository.BrowsinghistoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
	private final RentalRepository rentalRepository;
	private final BookRepository bookRepository;

	public RentalService(RentalRepository rentalRepository, BookRepository bookRepository) {
		this.rentalRepository = rentalRepository;
		this.bookRepository = bookRepository;
	}

	// Rent a book to a user: decrement copies and create rental
	@Transactional
	public Rental rentBook(Long userId, Long bookId) {
		
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found"));
		
		if (book.getCopies() == null || book.getCopies() <= 0) {
			
			throw new IllegalStateException("No copies available to rent.");
		}
		
		User user = new User();
        user.setId(userId); // only setting ID
		
		book.setCopies(book.getCopies() - 1);
		bookRepository.save(book);

		Rental rental = new Rental();
		rental.setUser(user);
		rental.setBook(book);
		rental.setRentalDate(LocalDate.now());
		rental.setRentedAt(LocalDateTime.now());
		rental.setStatus("RENTED");

		return rentalRepository.save(rental);
	}

	// Return a book: set returnedAt and increment copies
	@Transactional
	public Rental returnBook(Long rentalId) {
		 Rental rental = rentalRepository.findById(rentalId)
	                .orElseThrow(() -> new IllegalArgumentException("No such rental"));

	        if (rental.getReturnedAt() != null) {
	            throw new IllegalStateException("Book already returned.");
	        }

	        rental.setReturnedAt(LocalDateTime.now());
	        rental.setStatus("RETURNED");

	        Book book = rental.getBook();
	        book.setCopies(book.getCopies() + 1);
	        bookRepository.save(book);

	        return rentalRepository.save(rental);
	}

	// --- CRUD + Helper Methods ---

	public List<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}

	public Optional<Rental> getRentalById(Long id) {
		return rentalRepository.findById(id);
	}

	// Instead of raw delete → use returnBook for consistency
	public boolean deleteRental(Long id) {
		if (rentalRepository.existsById(id)) {
			rentalRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	public Rental saveRental(Rental rental) {
	    return rentalRepository.save(rental);
	}

	// --- User & Book based queries (for recommendations) ---

	public List<Rental> activeRentalsForUser(User user) {
		return rentalRepository.findByUserAndReturnedAtIsNull(user);
	}

	public List<Rental> historyForUser(User user) {
		return rentalRepository.findByUser(user);
	}

	public List<Rental> rentalsForBook(Book book) {
		return rentalRepository.findByBook(book);
	}
	
}
