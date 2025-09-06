package com.RentalBooking_Api.Service;

import com.RentalBooking_Api.dto.BookDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleBooksService {

    private static final String GOOGLE_BOOKS_API = "https://www.googleapis.com/books/v1/volumes?q=";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleBooksService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Call Google Books API
    public List<BookDTO> searchBooks(String query) {
        String url = GOOGLE_BOOKS_API + query;
        String response = restTemplate.getForObject(url, String.class);
        List<BookDTO> books = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                JsonNode volumeInfo = item.path("volumeInfo");

                String title = volumeInfo.hasNonNull("title") ? volumeInfo.get("title").asText() : null;
                
             // ✅ Skip if no proper title
                if (title == null || title.isBlank()) {
                    continue;
                }

                //Authors
                List<String> authors = new ArrayList<>();
                if (volumeInfo.has("authors")) {
                    for (JsonNode author : volumeInfo.get("authors")) {
                        authors.add(author.asText());
                    }
                }
                String author = String.join(", ", authors);
                String publisher = volumeInfo.path("publisher").asText(null);
                String publishedDate = volumeInfo.path("publishedDate").asText(null);
                String description = volumeInfo.path("description").asText(null);
                //String thumbnail = volumeInfo.path("imageLinks").path("thumbnail").asText(null);
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
                String genre = String.join(", ", genres);

                // Ratings
                Double averageRating = volumeInfo.has("averageRating") ?
                        volumeInfo.get("averageRating").asDouble() : null;
                Integer ratingsCount = volumeInfo.has("ratingsCount") ?
                        volumeInfo.get("ratingsCount").asInt() : null;
                
             // Cover image (directly map to coverImageUrl now)
                String coverImageUrl = null;
                if (volumeInfo.has("imageLinks") && volumeInfo.get("imageLinks").has("thumbnail")) {
                    coverImageUrl = volumeInfo.get("imageLinks").get("thumbnail").asText();
                }

                // Defaults (since Google API may not provide them)
                Integer copies = 1; // default single copy
               
                String category = genres.isEmpty() ? null : genres.get(0);

                // Build DTO
                BookDTO dto = new BookDTO();

                dto.setTitle(title);
                dto.setAuthors(authors);
                dto.setPublisher(publisher);
                dto.setPublishedDate(publishedDate);
                dto.setDescription(description);
            //    dto.setThumbnail(thumbnail);

                dto.setGoogleBookId(googleBookId);
                dto.setIsbn(isbn);
                dto.setGenres(genres);

                dto.setCopies(copies);
                dto.setCoverImageUrl(coverImageUrl);
                dto.setCategory(category);

                dto.setAverageRating(averageRating);
                dto.setRatingsCount(ratingsCount);

                books.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}
