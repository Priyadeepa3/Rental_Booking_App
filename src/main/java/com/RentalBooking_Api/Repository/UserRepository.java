package com.RentalBooking_Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RentalBooking_Api.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}