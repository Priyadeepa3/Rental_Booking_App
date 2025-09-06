package com.RentalBooking_Api.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.RentalBooking_Api.Entity.Recommendation;
import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Repository.BookRepository;
import com.RentalBooking_Api.Repository.UserRepository;
import com.RentalBooking_Api.Service.RecommendationService;
import com.RentalBooking_Api.dto.RecommendationDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public RecommendationController(RecommendationService recommendationService, UserRepository userRepository,BookRepository bookRepository) {
        this.recommendationService = recommendationService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }
    
    
    
    
    /**
     * 🔹 General personalized feed for a user
     * Example: GET /api/recommendations/3/feed?limit=5
     */
    @GetMapping("/{userId}/feed")
    public ResponseEntity<?> getGeneralRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<Recommendation> recommendations = recommendationService.recommendForUser(userOpt.get(), limit);
        if (recommendations.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(recommendations);
    }

    /**
     * 🔹 Book-specific recommendations ("Because you read X")
     * Example: GET /api/recommendations/3/similar/10?limit=5
     */
    @GetMapping("/{userId}/similar/{bookId}")
    public ResponseEntity<?> getSimilarRecommendations(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "5") int limit) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        return bookRepository.findById(bookId)
                .map(book -> ResponseEntity.ok(
                        recommendationService.recommendAfterRental(userOpt.get(), book, limit)
                ))
                .orElse(ResponseEntity.notFound().build());
    }


 
//
//    /**
//     * Get dynamic book recommendations for a user
//     * Example: GET /api/recommendations/5?limit=5
//     */
//    @GetMapping("/{userId}/similar/{bookId}")
//    public ResponseEntity<?> getRecommendationsForUser(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "5") int limit) {
//
//        Optional<User> userOpt = userRepository.findById(userId);
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<Recommendation> recommendations = recommendationService.recommendForUser(userOpt.get(), limit);
//
//        if (recommendations.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(recommendations);
//    }
    
 // 1. Similar books after rental
    @GetMapping("/{userId}/after-rental/{bookId}")
    public ResponseEntity<?> recommendAfterRental(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "5") int limit) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        return bookRepository.findById(bookId)
                .map(book -> ResponseEntity.ok(
                        recommendationService.recommendAfterRental(userOpt.get(), book, limit)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Similar books after return
    @GetMapping("/{userId}/after-return/{bookId}")
    public ResponseEntity<?> recommendAfterReturn(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "5") int limit) {

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        return bookRepository.findById(bookId)
                .map(book -> ResponseEntity.ok(
                        recommendationService.recommendAfterReturn(userOpt.get(), book, limit)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Browsing-based recommendations
    @GetMapping("/{userId}/browsing")
    public ResponseEntity<?> recommendFromBrowsing(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        List<Recommendation> recs = recommendationService.recommendFromBrowsing(userId, limit);
        if (recs.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(recs);
    }

    
}
