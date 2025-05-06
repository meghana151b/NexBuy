package com.registrationservice.service;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.registrationservice.entities.User;
import com.registrationservice.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	

	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
		this.passwordEncoder=new BCryptPasswordEncoder();
		
	}
	public User registerUser(User user) {
		//Check if username or email already exists
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username is already taken");
		}
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already taken");
		}
		
		//Encode password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		//Save the user
		return userRepository.save(user);
	}

}
