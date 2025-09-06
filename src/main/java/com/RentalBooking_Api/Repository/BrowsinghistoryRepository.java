package com.RentalBooking_Api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.Entity.Browsinghistory;
import com.RentalBooking_Api.Entity.User;


import java.util.List;

public interface BrowsinghistoryRepository extends JpaRepository<Browsinghistory, Long> {

    // Find user’s history
    List<Browsinghistory> findByUserOrderByViewedAtDesc(User user);

    // Optionally: filter only by action type
    List<Browsinghistory> findByUserAndActionType(User user, String actionType);
    
    List<Browsinghistory> findTop10ByUserIdOrderByViewedAtDesc(Long userId);
    
    List<Browsinghistory> findTop5ByUserOrderByViewedAtDesc(User user);
    
    @Query("SELECT h.book FROM Browsinghistory h WHERE h.user.id = :userId ORDER BY h.viewedAt DESC")
    List<Book> findRecentBooksByUserId(@Param("userId") Long userId);

    

    // ✅ Correct way: JPA will generate query automatically
    @Query("SELECT CASE WHEN COUNT(h) > 0 THEN TRUE ELSE FALSE END " +
    	       "FROM Browsinghistory h WHERE h.user.id = :userId AND h.book.isbn = :isbn")
    	boolean existsByUserIdAndBookIsbn(@Param("userId") Long userId, @Param("isbn") String isbn);

 // Recent browsing history by userId
    @Query("SELECT h FROM Browsinghistory h WHERE h.user.id = :userId ORDER BY h.viewedAt DESC")
    List<Browsinghistory> findRecentByUserId(@Param("userId") Long userId);


}

