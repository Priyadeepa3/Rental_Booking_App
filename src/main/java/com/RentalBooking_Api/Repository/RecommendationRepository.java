package com.RentalBooking_Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RentalBooking_Api.Entity.Recommendation;
import com.RentalBooking_Api.Entity.User;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
	 List<Recommendation> findByUser(User user);
}
