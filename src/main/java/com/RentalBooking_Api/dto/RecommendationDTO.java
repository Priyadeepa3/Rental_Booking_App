package com.RentalBooking_Api.dto;

// BUG FIX: Was empty class body — getters/setters were missing, making it useless.
// RecommendationController was returning raw Recommendation entities instead of this DTO.
// Now fixed: proper fields + getters/setters so controller can safely return this.
public class RecommendationDTO {

    private Long id;
    private String bookTitle;
    private String bookIsbn;
    private String coverImageUrl;
    private String source;       // content-based / collaborative / fallback
    private Double score;
    private String reason;

    public RecommendationDTO() {}

    public RecommendationDTO(Long id, String bookTitle, String bookIsbn,
                              String coverImageUrl, String source, Double score, String reason) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.coverImageUrl = coverImageUrl;
        this.source = source;
        this.score = score;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
