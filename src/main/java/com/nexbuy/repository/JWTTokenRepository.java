package com.nexbuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexbuy.entity.JWTToken;

public interface JWTTokenRepository extends JpaRepository<JWTToken,Integer> {
	void deleteByUserId(Integer userId);

}
