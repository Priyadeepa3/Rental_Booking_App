package com.RentalBooking_Api.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @ElementCollection
    @CollectionTable(name = "authors", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author", length = 100)
    private List<String> authors = new ArrayList<>();

    @Column(length = 100)
    private String publisher;

    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    @ElementCollection
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre", length = 100)
    private List<String> genres = new ArrayList<>();
   

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "google_book_id", length = 50)
    private String googleBookId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_date", length = 20)
    private String publishedDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;
    
 // ✅ NEW optional fields
    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "ratings_count")
    private Integer ratingsCount;
    
    @Column(name = "copies", nullable = false, columnDefinition = "int default 1")
    private Integer copies = 1;

    @Column(name = "rental_count", nullable = false, columnDefinition = "int default 0")
    private Integer rentalCount = 0;
 

    public Integer getRentalCount() {
		return rentalCount;
	}

	public void setRentalCount(Integer rentalCount) {
		this.rentalCount = rentalCount;
	}

	public Book() {}

    public Book(String title, List<String> authors, String publisher, String isbn, List<String> genres, String category,
                String googleBookId, Integer copies, String publishedDate, String description, String coverImageUrl) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.isbn = isbn;
        this.genres = genres;
        this.category = category;
        this.googleBookId = googleBookId;
        this.copies = copies;
        this.publishedDate = publishedDate;
        this.description = description;
        this.coverImageUrl = coverImageUrl;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    // ...

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGoogleBookId() {
		return googleBookId;
	}

	public void setGoogleBookId(String googleBookId) {
		this.googleBookId = googleBookId;
	}

	public Integer getCopies() {
		return copies;
	}

	public void setCopies(Integer copies) {
		this.copies = copies;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverImageUrl() {
		return coverImageUrl;
	}

	public void setCoverImageUrl(String coverImageUrl) {
		this.coverImageUrl = coverImageUrl;
	}
	
	// ✅ Getters & setters for new fields
    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }


	// equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", authors=" + authors + ", isbn=" + isbn + "]";
    }

}
