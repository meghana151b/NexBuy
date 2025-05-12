package com.nexbuy.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexbuy.entity.User;
import com.nexbuy.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
	
	private final String SECRET_KEY="dewdygcewfgyjghudhiewfwruqrhdiuhfihf93r9hriu1hiure18ry9urheiuh128e1gfw123";
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
	public AuthService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User authenticate(String username,String password) {
	User user=userRepository.findByUsername(username)
			.orElseThrow(() -> new RuntimeException("Invalid username or password"));
	
	if(!passwordEncoder.matches(password,user.getPassword())) {
		throw new RuntimeException("Invlaid username or password");
	}
	return user;
	}
	
	public String generateToken(User user) {
		Key key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("role",user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 3600000))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
	}
	public String extractUsername(String token) {
	    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody()
	            .getSubject();  // This is the username stored as subject
	}
	public boolean validateToken(String token) {
	    try {
	        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	        Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token); // If invalid, this will throw an exception

	        return true;
	    } catch (JwtException | IllegalArgumentException e) {
	        return false;
	    }
	}

}
