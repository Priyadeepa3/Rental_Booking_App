package com.RentalBooking_Api.Entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "recommended_book_id", nullable = false)
	private Book recommendedBook;

	@Column(nullable = false)
	private String source;

	private Double score;

	private String reason;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public Recommendation() {
	}

	public Recommendation(User user, Book recommendedBook, String source, Double score, String reason,
			LocalDateTime createdAt) {
		this.user = user;
		this.recommendedBook = recommendedBook;
		this.source = source;
		this.score = score;
		this.reason = reason;
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getRecommendedBook() {
		return recommendedBook;
	}

	public void setRecommendedBook(Book recommendedBook) {
		this.recommendedBook = recommendedBook;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Recommendation [id=" + id + ", source=" + source + ", score=" + score + ", reason=" + reason
				+ ", createdAt=" + createdAt + "]";
	}

}
