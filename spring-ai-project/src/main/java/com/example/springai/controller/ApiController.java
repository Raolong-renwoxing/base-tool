package com.example.springai.controller;

import com.example.springai.entity.User;
import com.example.springai.entity.secondary.Product;
import com.example.springai.repository.UserRepository;
import com.example.springai.repository.secondary.ProductRepository;
import com.example.springai.service.AiService;
import com.example.springai.service.HiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final AiService aiService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HiveService hiveService;

    @Autowired
    public ApiController(
            AiService aiService, 
            UserRepository userRepository,
            ProductRepository productRepository,
            HiveService hiveService) {
        this.aiService = aiService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.hiveService = hiveService;
    }

    @PostMapping("/ai/generate")
    public ResponseEntity<String> generateAiResponse(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        if (input == null || input.isEmpty()) {
            return ResponseEntity.badRequest().body("Input is required");
        }
        
        String response = aiService.generateResponse(input);
        return ResponseEntity.ok(response);
    }

    // Primary MySQL database endpoints
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
    
    // Secondary MySQL database endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Primary Hive database endpoints
    @PostMapping("/hive/primary/query")
    public ResponseEntity<List<Map<String, Object>>> executePrimaryHiveQuery(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        
        List<Map<String, Object>> result = hiveService.executePrimaryQuery(query);
        return ResponseEntity.ok(result);
    }
    
    // Secondary Hive database endpoints
    @PostMapping("/hive/secondary/query")
    public ResponseEntity<List<Map<String, Object>>> executeSecondaryHiveQuery(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        
        List<Map<String, Object>> result = hiveService.executeSecondaryQuery(query);
        return ResponseEntity.ok(result);
    }
    
    // Get database connection status
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // Check MySQL connections
        try {
            long userCount = userRepository.count();
            status.put("primaryMySQLConnection", "Connected");
            status.put("primaryMySQLUserCount", userCount);
        } catch (Exception e) {
            status.put("primaryMySQLConnection", "Failed: " + e.getMessage());
        }
        
        try {
            long productCount = productRepository.count();
            status.put("secondaryMySQLConnection", "Connected");
            status.put("secondaryMySQLProductCount", productCount);
        } catch (Exception e) {
            status.put("secondaryMySQLConnection", "Failed: " + e.getMessage());
        }
        
        // Check Hive connections
        try {
            hiveService.executePrimaryQuery("SHOW DATABASES LIMIT 1");
            status.put("primaryHiveConnection", "Connected");
        } catch (Exception e) {
            status.put("primaryHiveConnection", "Failed: " + e.getMessage());
        }
        
        try {
            hiveService.executeSecondaryQuery("SHOW DATABASES LIMIT 1");
            status.put("secondaryHiveConnection", "Connected");
        } catch (Exception e) {
            status.put("secondaryHiveConnection", "Failed: " + e.getMessage());
        }
        
        return ResponseEntity.ok(status);
    }
}