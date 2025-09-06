package com.RentalBooking_Api.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "browsing_history")
public class Browsinghistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Which user browsed?
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// Which book they viewed?
	@ManyToOne
	@JoinColumn(name = "book_id", nullable = true)
	private Book book;

	private LocalDateTime viewedAt; // When they viewed

	private String actionType; // "BROWSE", "RENT", "RETURN" (optional to track intent)

	private String query;
	private String source;

	public Browsinghistory() {
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public LocalDateTime getViewedAt() {
		return viewedAt;
	}

	public void setViewedAt(LocalDateTime viewedAt) {
		this.viewedAt = viewedAt;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Browsinghistory(Long id, User user, Book book, LocalDateTime viewedAt, String actionType, String query,
			String source) {
		this.id = id;
		this.user = user;
		this.book = book;
		this.viewedAt = viewedAt;
		this.actionType = actionType;
		this.query = query;
		this.source = source;
	}

	// Constructors, Getters, Setters
}
