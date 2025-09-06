package com.RentalBooking_Api.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Browsinghistory;
import com.RentalBooking_Api.Entity.Rental;
import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Entity.Recommendation;
import com.RentalBooking_Api.Repository.BookRepository;
import com.RentalBooking_Api.Repository.BrowsinghistoryRepository;
import com.RentalBooking_Api.Repository.RentalRepository;
import com.RentalBooking_Api.Repository.RecommendationRepository;

@Service
public class RecommendationService {

	private final BrowsinghistoryRepository browsinghistoryRepository;
	private final BookRepository bookRepository;
	private final RentalRepository rentalRepo;
	private final RecommendationRepository recommendationRepo;

	private final int DEFAULT_LIMIT = 5;

	
	public RecommendationService(BrowsinghistoryRepository browsinghistoryRepository, BookRepository bookRepository,
			RentalRepository rentalRepo, RecommendationRepository recommendationRepo) {
		this.browsinghistoryRepository = browsinghistoryRepository;
		this.bookRepository = bookRepository;
		this.rentalRepo = rentalRepo;
		this.recommendationRepo = recommendationRepo;
	}
	
	public List<Recommendation> recommendForUser(User user, int limit) {
        int wanted = Math.max(limit, DEFAULT_LIMIT);

        // Clear old recommendations
      //  recommendationRepo.deleteAll(recommendationRepo.findByUser(user));

        List<Rental> history = rentalRepo.findByUser(user);
        Set<Book> recommendedBooks = new LinkedHashSet<>();
        List<Recommendation> recommendations = new ArrayList<>();

        if (history.isEmpty()) {
            return fallbackRecommendations(user, wanted, recommendedBooks, recommendations);
        }

        // Seed: most recent rental
        history.sort(Comparator.comparing(Rental::getRentedAt).reversed());
        Book seed = history.get(0).getBook();

        contentBasedRecommendations(user, seed, recommendedBooks, recommendations);
        collaborativeRecommendations(user, seed, recommendedBooks, recommendations);

        // Fill up with random fallback if needed
        if (recommendations.size() < wanted) {
            fallbackRecommendations(user, wanted, recommendedBooks, recommendations);
        }

        return recommendationRepo.saveAll(recommendations.stream().limit(wanted).toList());
    }
	
	private void contentBasedRecommendations(User user, Book seed, Set<Book> recommendedBooks, List<Recommendation> recommendations) {
        // Similar authors
        if (seed.getAuthors() != null) {
            for (String author : seed.getAuthors()) {
            	bookRepository.findByAuthorNameLike(author).forEach(b -> {
                    if (!b.getId().equals(seed.getId()) && recommendedBooks.add(b)) {
                        recommendations.add(buildRecommendation(user, b, "content-based", 0.9, "Similar author: " + author));
                    }
                });
            }
        }

        // Similar category
        if (seed.getCategory() != null) {
        	bookRepository.findByCategoryContainingIgnoreCase(seed.getCategory()).forEach(b -> {
                if (!b.getId().equals(seed.getId()) && recommendedBooks.add(b)) {
                    recommendations.add(buildRecommendation(user, b, "content-based", 0.8, "Similar category: " + seed.getCategory()));
                }
            });
        }
    }
	
	
	private void collaborativeRecommendations(User user, Book seed, Set<Book> recommendedBooks, List<Recommendation> recommendations) {
        List<Rental> seedRentals = rentalRepo.findByBook(seed);
        Map<Book, Integer> coRentCounts = new HashMap<>();

        for (Rental r : seedRentals) {
            for (Rental tr : rentalRepo.findByUser(r.getUser())) {
                Book b = tr.getBook();
                if (!b.getId().equals(seed.getId())) {
                    coRentCounts.put(b, coRentCounts.getOrDefault(b, 0) + 1);
                }
            }
        }

        coRentCounts.entrySet().stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    Book b = entry.getKey();
                    if (recommendedBooks.add(b)) {
                        recommendations.add(buildRecommendation(user, b, "collaborative",
                                0.7 + (entry.getValue() * 0.05),
                                "Users who rented '" + seed.getTitle() + "' also rented this"));
                    }
                });
    }
	
	
	
	private List<Recommendation> fallbackRecommendations(User user, int limit, Set<Book> recommendedBooks, List<Recommendation> recommendations) {
        List<Book> allBooks = bookRepository.findAll();
        Collections.shuffle(allBooks);
        for (Book b : allBooks) {
            if (recommendations.size() >= limit) break;
            if (recommendedBooks.add(b)) {
                recommendations.add(buildRecommendation(user, b, "fallback", 0.2, "Random suggestion"));
            }
        }
        return recommendations;
    }

    private Recommendation buildRecommendation(User user, Book book, String type, double score, String reason) {
        Recommendation rec = new Recommendation();
        rec.setUser(user);
        rec.setRecommendedBook(book);
        rec.setSource(type);
        rec.setScore(score);
        rec.setReason(reason);
        return rec;
    }
    
    
 // 1. After a RENTAL, recommend similar books
    public List<Recommendation> recommendAfterRental(User user, Book rentedBook, int limit) {
        Set<Book> recommendedBooks = new LinkedHashSet<>();
        List<Recommendation> recommendations = new ArrayList<>();

        contentBasedRecommendations(user, rentedBook, recommendedBooks, recommendations);
        collaborativeRecommendations(user, rentedBook, recommendedBooks, recommendations);

        return recommendationRepo.saveAll(recommendations.stream().limit(limit).toList());
    }

    // 2. After a RETURN, recommend similar books
    public List<Recommendation> recommendAfterReturn(User user, Book returnedBook, int limit) {
        Set<Book> recommendedBooks = new LinkedHashSet<>();
        List<Recommendation> recommendations = new ArrayList<>();

        contentBasedRecommendations(user, returnedBook, recommendedBooks, recommendations);
        collaborativeRecommendations(user, returnedBook, recommendedBooks, recommendations);

        // Mark the reason differently
        recommendations.forEach(r -> r.setReason(r.getReason() + " (since you returned)"));

        return recommendationRepo.saveAll(recommendations.stream().limit(limit).toList());
    }

 // 3. Browsing history recommendations
    public List<Recommendation> recommendFromBrowsing(Long userId, int limit) {
        // Step 1: Fetch browsing history entries
        List<Browsinghistory> history = browsinghistoryRepository.findRecentByUserId(userId);

        if (history.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 2: Extract distinct books from browsing history
        List<Book> browsedBooks = history.stream()
                .map(Browsinghistory::getBook)
                .distinct()
                .toList();

        Set<Book> recommendedBooks = new LinkedHashSet<>();
        List<Recommendation> recommendations = new ArrayList<>();

        // Step 3: Generate recommendations using browsed books as seeds
        for (Book seed : browsedBooks) {
            if (seed == null) continue; // skip invalid/null entries
            contentBasedRecommendations(null, seed, recommendedBooks, recommendations);
        }


        // Step 4: Save and return limited results
        return recommendationRepo.saveAll(
                recommendations.stream().limit(limit).toList()
        );
    }

   // List<Book> browsedBooks = BrowsinghistoryRepository.findRecentBooksByUserId(userId);
	
}