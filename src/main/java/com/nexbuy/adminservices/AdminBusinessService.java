package com.nexbuy.adminservices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nexbuy.entity.Order;
import com.nexbuy.entity.OrderItem;
import com.nexbuy.entity.OrderStatus;
import com.nexbuy.repository.OrderItemRepository;
import com.nexbuy.repository.OrderRepository;
import com.nexbuy.repository.ProductRepository;

@Service
public class AdminBusinessService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;

	public AdminBusinessService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
	}


	public Map<String, Object> calculateMonthlyBusiness(int month, int year) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (year < 2000 || year > 2100) { // Adjust range as needed
			throw new IllegalArgumentException("Invalid year: " + year);
		}

		// Fetch successful orders
		List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByMonthAndYear(month, year);

		// Calculate total business
		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				// Fetch category name based on productId
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		// Prepare the response
		Map<String, Object> businessReport = new HashMap<>();
		businessReport.put("totalBusiness", totalBusiness);
		businessReport.put("categorySales", categorySales);

		return businessReport;
	}




	public Map<String, Object> calculateDailyBusiness(LocalDate date) {
		if (date == null) {
			throw new IllegalArgumentException("Invalid date: Date cannot be null");
		}

		// Fetch successful orders for the date
		List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByDate(date);

		// Calculate total business
		double totalBusiness = 0.0;
		Map<String, Integer> categorySales = new HashMap<>();

		for (Order order : successfulOrders) {
			totalBusiness += order.getTotalAmount().doubleValue();

			List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
			for (OrderItem item : orderItems) {
				// Fetch category name based on productId
				String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
				categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
			}
		}

		// Prepare the response
		Map<String, Object> businessReport = new HashMap<>();
		businessReport.put("totalBusiness", totalBusiness);
		businessReport.put("categorySales", categorySales);

		return businessReport;
	}





	public Map<String, Object> calculateYearlyBusiness(int year) {
        if (year < 2000 || year > 2100) { // Adjust range as needed
            throw new IllegalArgumentException("Invalid year: " + year);
        }

        // Fetch successful orders for the year
        List<Order> successfulOrders = orderRepository.findSuccessfulOrdersByYear(year);

        // Calculate total business
        double totalBusiness = 0.0;
        Map<String, Integer> categorySales = new HashMap<>();

        for (Order order : successfulOrders) {
            totalBusiness += order.getTotalAmount().doubleValue();

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
            for (OrderItem item : orderItems) {
                // Fetch category name based on productId
                String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
                categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
            }
        }

        // Prepare the response
        Map<String, Object> businessReport = new HashMap<>();
        businessReport.put("totalBusiness", totalBusiness);
        businessReport.put("categorySales", categorySales);

        return businessReport;
    }
	
	
	
	public Map<String, Object> calculateOverallBusiness() {
	    BigDecimal totalBusinessAmount = orderRepository.calculateOverallBusiness();
	    List<Order> successfulOrders = orderRepository.findAllByStatus(OrderStatus.SUCCESS);

	    Map<String, Integer> categorySales = new HashMap<>();
	    for (Order order : successfulOrders) {
	        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
	        for (OrderItem item : orderItems) {
	            String categoryName = productRepository.findCategoryNameByProductId(item.getProductId());
	            categorySales.put(categoryName, categorySales.getOrDefault(categoryName, 0) + item.getQuantity());
	        }
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("totalBusiness", totalBusinessAmount.doubleValue());
	    response.put("categorySales", categorySales);
	    return response;
	}

	
	
}