package com.RentalBooking_Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Rental;
import com.RentalBooking_Api.Entity.User;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
	List<Rental> findByUserAndReturnedAtIsNull(User user);  // Active rentals
    List<Rental> findByBook(Book book);                     // Rentals for a book
    List<Rental> findByUser(User user);                     // Rental history

    // Extra queries for recommendations
    List<Rental> findTop5ByBookOrderByRentedAtDesc(Book book); // People also rented
    List<Rental> findTop5ByUserOrderByRentedAtDesc(User user); // Recently rented by user
}
