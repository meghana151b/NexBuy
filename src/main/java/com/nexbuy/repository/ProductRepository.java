package com.nexbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexbuy.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>{
	List<Product> findByCategory_CategoryId(Integer categoryId);

	@Query("SELECT p.category.name FROM Product p WHERE p.id = :productId")
    String findCategoryNameByProductId(@Param("productId") int productId);
}
