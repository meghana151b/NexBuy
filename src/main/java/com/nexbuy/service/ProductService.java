package com.nexbuy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexbuy.entity.Category;
import com.nexbuy.entity.Product;
import com.nexbuy.entity.ProductImage;
import com.nexbuy.repository.CategoryRepository;
import com.nexbuy.repository.ProductImageRepository;
import com.nexbuy.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;	
	
	 public List<Product> getProductsByCategory(String categoryName) {
	        if (categoryName != null && !categoryName.isEmpty()) {
	            Optional<Category> categoryOpt = categoryRepository.findByCategoryName(categoryName);
	            if (categoryOpt.isPresent()) {
	                Category category = categoryOpt.get();
	                return productRepository.findByCategory_CategoryId(category.getCategoryId());
	            } else {
	                throw new RuntimeException("Category not found");
	            }
	        } else {
	            return productRepository.findAll();
	        }
	    }

	    public List<String> getProductImages(Integer productId) {
	        List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
	        List<String> imageUrls = new ArrayList<>();
	        for (ProductImage image : productImages) {
	            imageUrls.add(image.getImageUrl());
	        }
	        return imageUrls;
	    }
	}
