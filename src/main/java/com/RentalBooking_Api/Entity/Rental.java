package com.RentalBooking_Api.Entity;



import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;



@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime rentedAt = LocalDateTime.now();

    private LocalDateTime returnedAt; // null if not returned

    @Column(nullable = false, length = 20)
    private String status = "RENTED"; // RENTED, RETURNED, OVERDUE
    
    @Column(nullable = false)
    private LocalDate rentalDate = LocalDate.now();

    public Rental() {}
    public Rental(User user, Book book, LocalDateTime rentedAt, String status) {
        this.user = user;
        this.book = book;
        this.rentedAt = rentedAt;
        this.status = status;
    }

    // getters & setters...

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
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public LocalDateTime getRentedAt() {
		return rentedAt;
	}
	public void setRentedAt(LocalDateTime rentedAt) {
		this.rentedAt = rentedAt;
	}
	public LocalDate getRentalDate() {
		return rentalDate;
	}
	public void setRentalDate(LocalDate rentalDate) {
		this.rentalDate = rentalDate;
	}
	public LocalDateTime getReturnedAt() {
		return returnedAt;
	}
	public void setReturnedAt(LocalDateTime returnedAt) {
		this.returnedAt = returnedAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rental)) return false;
        Rental rental = (Rental) o;
        return Objects.equals(id, rental.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Rental [id=" + id + ", user=" + user + ", book=" + book +
                ", rentedAt=" + rentedAt + ", returnedAt=" + returnedAt +
                ", status=" + status + "]";
    }
}