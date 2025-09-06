package com.RentalBooking_Api.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RentalBooking_Api.Entity.Rental;
import com.RentalBooking_Api.Service.RentalService;
import com.RentalBooking_Api.dto.RentalDTO;

@RestController
@RequestMapping("/rentals")
public class RentalController {

	private final RentalService rentalservice;

	@Autowired
	public RentalController(RentalService rentalservice) {

		this.rentalservice = rentalservice;
	}

	@GetMapping("rentall")
	public List<Rental> GetALLRental() {
		return rentalservice.getAllRentals();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Rental> GetbyRentalId(@PathVariable Long id) {
		Optional<Rental> Rent = rentalservice.getRentalById(id);
		return Rent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

	}

	@PostMapping("/rent")
	public ResponseEntity<Rental> rentBook(@RequestBody RentalDTO rented) {
	    Rental saved = rentalservice.rentBook(rented.getUserId(), rented.getBookId());
	    return ResponseEntity.ok(saved);
	}

	@PostMapping("/return/{id}")
	public ResponseEntity<Rental> returnBook(@PathVariable Long id) {
	    Rental returned = rentalservice.returnBook(id);
	    return ResponseEntity.ok(returned);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Rental> UpdateRental(@PathVariable Long id, @RequestBody Rental rental) {
	    return rentalservice.getRentalById(id).map(updaterental -> {
	        updaterental.setUser(rental.getUser());
	        updaterental.setBook(rental.getBook());
	        updaterental.setRentedAt(rental.getRentedAt() != null ? rental.getRentedAt() : updaterental.getRentedAt());
	        updaterental.setReturnedAt(rental.getReturnedAt());
	        updaterental.setStatus(rental.getStatus());

	        Rental updated = rentalservice.saveRental(updaterental);
	        return ResponseEntity.ok(updated);
	    }).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void>deleteRental(@PathVariable Long id) {
		rentalservice.deleteRental(id);
		return ResponseEntity.noContent().build();

	}
}
