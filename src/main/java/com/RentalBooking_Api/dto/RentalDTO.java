package com.RentalBooking_Api.dto;

public class RentalDTO {
	private Long userId;
    private Long bookId;

    public RentalDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

}
