package com.RentalBooking_Api.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Browsinghistory;
import com.RentalBooking_Api.Entity.Recommendation;
import com.RentalBooking_Api.Entity.Rental;
import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Repository.BookRepository;
import com.RentalBooking_Api.Repository.BrowsinghistoryRepository;
import com.RentalBooking_Api.Repository.UserRepository;
import com.RentalBooking_Api.dto.BookDTO;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BrowsinghistoryRepository browsingHistoryRepository;
	private final BookRepository bookrepository;

	// Constructor injection
	public UserService(UserRepository userRepository, BrowsinghistoryRepository browsingHistoryRepository,
			BookRepository bookrepository) {
		this.userRepository = userRepository;
		this.browsingHistoryRepository = browsingHistoryRepository;
		this.bookrepository = bookrepository;
	}

	// 📌 record when user searches or views a book
	public void recordSearchEvent(Long userId, String query, String source) {
		if (userId == null)
			return;

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Browsinghistory history = new Browsinghistory();
		history.setUser(user);
		history.setQuery(query);
		history.setSource(source);
		history.setViewedAt(LocalDateTime.now());

		browsingHistoryRepository.save(history);
	}

	public void recordBookView(Long userId, String bookIsbn) {
		if (userId == null)
			return;

		if (!browsingHistoryRepository.existsByUserIdAndBookIsbn(userId, bookIsbn)) {
			User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			
			// ✅ Fetch the book entity from DB by ISBN
		    Book book = bookrepository.findByIsbn(bookIsbn)
		            .stream()
		            .findFirst()
		            .orElseThrow(() -> new RuntimeException("Book not found"));

			Browsinghistory history = new Browsinghistory();
			history.setUser(user);
			history.setBook(book);
			history.setViewedAt(LocalDateTime.now());
			history.setSource("BOOK_VIEW");

			browsingHistoryRepository.save(history);
		}
	}

	public List<Browsinghistory> getRecentHistory(Long userId, int limit) {
		return browsingHistoryRepository.findTop10ByUserIdOrderByViewedAtDesc(userId);
	}

	// ✅ Get all users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// ✅ Get a user by ID
	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}

	// ✅ Create or update a user
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	// ✅ Update an existing user (supports PUT and PATCH)
	public Optional<User> updateUser(Long id, User userDetails) {
		return userRepository.findById(id).map(user -> {
			// Only update fields that are not null in userDetails (for PATCH)
			if (userDetails.getName() != null)
				user.setName(userDetails.getName());
			if (userDetails.getEmail() != null)
				user.setEmail(userDetails.getEmail());
			if (userDetails.getPassword() != null)
				user.setPassword(userDetails.getPassword());
			// Add other fields as needed
			return userRepository.save(user);
		});
	}

	// ✅ Delete a user, return boolean
	public boolean deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// ✅ Find a user by email (common in login flows)
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
