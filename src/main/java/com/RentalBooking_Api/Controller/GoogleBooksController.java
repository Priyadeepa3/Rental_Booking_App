package com.RentalBooking_Api.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.RentalBooking_Api.Service.GoogleBooksService;
import com.RentalBooking_Api.dto.BookDTO;

@RestController
public class GoogleBooksController {
	
	private final GoogleBooksService googlebooksservice;

	//Constructor Injection
	public GoogleBooksController(GoogleBooksService googlebooksservice) {
		
		this.googlebooksservice = googlebooksservice;
	}
	
	 // Example endpoint: /googlebooks/search?query=harry+potter
	//To let users find books that aren’t in your local DB.
	 @GetMapping("/google/search")
	    public List<BookDTO> searchBooks(@RequestParam String q) {
	        return googlebooksservice.searchBooks(q);
	    }

}