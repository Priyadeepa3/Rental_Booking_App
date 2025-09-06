package com.RentalBooking_Api.Controller;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.RentalBooking_Api.Entity.User;
import com.RentalBooking_Api.Service.UserService;


@RestController("/users")
public class UserController {

	private final UserService userservice;
	
	@Autowired
	public UserController(UserService userservice) {
		this.userservice = userservice;
	}
	//Registeration
	
	@GetMapping("/all")
	public List<User>getAllUser(){
		List<User> users = userservice.getAllUsers(); // fetch all users
        users.sort(Comparator.comparingLong(User::getId)); // sort by id ascending
        return users;		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User>getUserbyId(@PathVariable Long id){
		Optional<User> user = userservice.getUserById(id);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		}
	
	//Create a New User
	@PostMapping("/api/User")
	public ResponseEntity<User>createUser(@RequestBody User user){
		 LocalDateTime sys = LocalDateTime.now();
		 user.setCreatedAt(sys);
		 userservice.saveUser(user);
		return ResponseEntity.ok(user);
	}
	
	//update an Existing user
	
	// Full update (PUT)
    @PutMapping("/major/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, @RequestBody User userDetails) {
        return userservice.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

	
	 // Partial update (PATCH)
    @PatchMapping("/minor/{id}")
    public ResponseEntity<User> patchUser(
            @PathVariable Long id, @RequestBody User userDetails) {
        return userservice.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
	    boolean deleted = userservice.deleteUser(id);
		if (deleted) {
	        return ResponseEntity.noContent().build();
	    }
	    return ResponseEntity.notFound().build();
	}

}
