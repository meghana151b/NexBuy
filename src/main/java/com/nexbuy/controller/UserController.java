package com.nexbuy.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexbuy.entity.User;
import com.nexbuy.service.UserService;

@RestController
//@CrossOrigin(origins ="http://localhost:5176/")

@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;
	
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.ok(Map.of("message","User registered successfully","user",registeredUser));
		}catch(RuntimeException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
	

}
