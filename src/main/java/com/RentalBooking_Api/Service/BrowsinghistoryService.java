package com.RentalBooking_Api.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Browsinghistory;
import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Repository.BrowsinghistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BrowsinghistoryService {

	private final BrowsinghistoryRepository browsinghistoryRepository;

	public BrowsinghistoryService(BrowsinghistoryRepository browsinghistoryRepository) {
		this.browsinghistoryRepository = browsinghistoryRepository;
	}

	// Save a new browsing action
	public void saveHistory(User user, Book book, String actionType) {
		 boolean alreadyViewed = browsinghistoryRepository.existsByUserIdAndBookIsbn(user.getId(), book.getIsbn());

	        if (!alreadyViewed) {
	            Browsinghistory history = new Browsinghistory();
	            history.setUser(user);
	            history.setBook(book);
	            history.setActionType(actionType);
	            history.setViewedAt(LocalDateTime.now());
	            browsinghistoryRepository.save(history);
	        } else {
	            // Optional: update the timestamp if user already viewed this book
	            Browsinghistory history = new Browsinghistory();
	            history.setUser(user);
	            history.setBook(book);
	            history.setActionType(actionType);
	            history.setViewedAt(LocalDateTime.now());
	            browsinghistoryRepository.save(history);
	        }
	    }

	    /**
	     * Get full browsing history for a user, newest first.
	     */
	    public List<Browsinghistory> getHistoryByUser(User user) {
	        return browsinghistoryRepository.findByUserOrderByViewedAtDesc(user);
	    }

	    /**
	     * Get only browsing actions (ignoring RENT/RETURN).
	     */
	    public List<Browsinghistory> getOnlyBrowsing(User user) {
	        return browsinghistoryRepository.findByUserAndActionType(user, "BROWSE");
	    }

	    /**
	     * Get only books the user rented.
	     */
	    public List<Browsinghistory> getOnlyRented(User user) {
	        return browsinghistoryRepository.findByUserAndActionType(user, "RENT");
	    }

	    /**
	     * Get only books the user returned.
	     */
	    public List<Browsinghistory> getOnlyReturned(User user) {
	        return browsinghistoryRepository.findByUserAndActionType(user, "RETURN");
	    }
	    
	    public List<Browsinghistory> recentHistory(User user) {
	        return browsinghistoryRepository.findTop5ByUserOrderByViewedAtDesc(user);
	    }
	
}
