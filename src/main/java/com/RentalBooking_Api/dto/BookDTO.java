package com.RentalBooking_Api.dto;

import java.util.List;

public class BookDTO {

    private Long id;
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private String googleBookId;
    private String isbn;
    private List<String> genres;
    private Integer copies;
    private String coverImageUrl;
    private String category;
    private Double averageRating;
    private Integer ratingsCount;

    public BookDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getPublishedDate() { return publishedDate; }
    public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGoogleBookId() { return googleBookId; }
    public void setGoogleBookId(String googleBookId) { this.googleBookId = googleBookId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }
    public Integer getCopies() { return copies; }
    public void setCopies(Integer copies) { this.copies = copies; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
    public Integer getRatingsCount() { return ratingsCount; }
    public void setRatingsCount(Integer ratingsCount) { this.ratingsCount = ratingsCount; }
}
