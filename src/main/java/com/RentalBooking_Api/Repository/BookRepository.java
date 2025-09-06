package com.RentalBooking_Api.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.RentalBooking_Api.Entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Custom finder: search by title
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findByAuthorNameLike(@Param("author") String author);
    
    
    List<Book> findByGenresContainingIgnoreCase(String genre);

    Optional<Book> findByIsbn(String isbn);
    
    
    List<Book> findByCategoryContainingIgnoreCase(String category);
    
    // 📌 Popular books (for recommendations)
    List<Book> findTop10ByOrderByRentalCountDesc();
    
    
    // Fallback random books (for recommendations)
    @Query(value = "SELECT * FROM books ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Book> findRandomBooks();

    
    
}
