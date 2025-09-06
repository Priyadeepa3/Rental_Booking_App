package com.RentalBooking_Api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.RentalBooking_Api.Entity.Book;
import com.RentalBooking_Api.dto.BookDTO;
import com.fasterxml.jackson.databind.JsonNode;

public class MapperUtils {

	// Map Google Books JSON -> BookDTO
	public static BookDTO mapGoogleBookToDTO(JsonNode item) {
		JsonNode volumeInfo = item.path("volumeInfo");

		String title = volumeInfo.path("title").asText();

		// Authors
		List<String> authors = new ArrayList<>();
		if (volumeInfo.has("authors")) {
			for (JsonNode author : volumeInfo.get("authors")) {
				authors.add(author.asText());
			}
		}

		String publisher = volumeInfo.path("publisher").asText(null);
		String publishedDate = volumeInfo.path("publishedDate").asText(null);
		String description = volumeInfo.path("description").asText(null);
		String thumbnail = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
		String googleBookId = item.path("id").asText();

		// ISBN
		String isbn = null;
		if (volumeInfo.has("industryIdentifiers")) {
			JsonNode identifiers = volumeInfo.get("industryIdentifiers");
			if (identifiers.isArray() && identifiers.size() > 0) {
				isbn = identifiers.get(0).path("identifier").asText();
			}
		}

		// Genres (categories)
		List<String> genres = new ArrayList<>();
		if (volumeInfo.has("categories")) {
			for (JsonNode category : volumeInfo.get("categories")) {
				genres.add(category.asText());
			}
		}

		// Ratings
		Double averageRating = volumeInfo.has("averageRating") ? volumeInfo.get("averageRating").asDouble() : null;
		Integer ratingsCount = volumeInfo.has("ratingsCount") ? volumeInfo.get("ratingsCount").asInt() : null;

		// Defaults
		Integer copies = 1; // assume 1 copy
		String coverImageUrl = thumbnail;
		String category = genres.isEmpty() ? null : genres.get(0);

		// Build DTO
		BookDTO dto = new BookDTO();
		dto.setTitle(title);
		dto.setAuthors(authors);
		dto.setPublisher(publisher);
		dto.setPublishedDate(publishedDate);
		dto.setDescription(description);

		dto.setGoogleBookId(googleBookId);
		dto.setIsbn(isbn);
		dto.setGenres(genres);

		dto.setCopies(copies);
		dto.setCoverImageUrl(coverImageUrl);
		dto.setCategory(category);

		dto.setAverageRating(averageRating);
		dto.setRatingsCount(ratingsCount);

		return dto;
	}

	public static List<BookDTO> mapList(List<Book> localBooks, Class<BookDTO> target) {
		return localBooks.stream().map(book -> map(book, BookDTO.class)).collect(Collectors.toList());
	}

	// Map BookDTO -> Book (Entity)
	public static Book map(BookDTO dto, Class<Book> target) {
		Book book = new Book();
		book.setId(dto.getId());
		book.setTitle(dto.getTitle());
		book.setAuthors(dto.getAuthors());
		book.setPublisher(dto.getPublisher());
		book.setPublishedDate(dto.getPublishedDate());
		book.setDescription(dto.getDescription());
		book.setCoverImageUrl(dto.getCoverImageUrl());
		book.setGoogleBookId(dto.getGoogleBookId());
		book.setIsbn(dto.getIsbn());
		book.setGenres(dto.getGenres());
		book.setCopies(dto.getCopies());
		book.setCategory(dto.getCategory());
		book.setAverageRating(dto.getAverageRating());
		book.setRatingsCount(dto.getRatingsCount());
		return book;
	}

	// Map Book (Entity) -> BookDTO
	public static BookDTO map(Book book, Class<BookDTO> target) {
		BookDTO dto = new BookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setAuthors(book.getAuthors());
		dto.setPublisher(book.getPublisher());
		dto.setPublishedDate(book.getPublishedDate());
		dto.setDescription(book.getDescription());
		dto.setCoverImageUrl(book.getCoverImageUrl());
		dto.setGoogleBookId(book.getGoogleBookId());
		dto.setIsbn(book.getIsbn());
		dto.setGenres(book.getGenres());
		dto.setCopies(book.getCopies());
		dto.setCategory(book.getCategory());
		dto.setAverageRating(book.getAverageRating());
		dto.setRatingsCount(book.getRatingsCount());
		return dto;
	}
}
