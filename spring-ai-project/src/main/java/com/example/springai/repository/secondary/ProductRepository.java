package com.example.springai.repository.secondary;

import com.example.springai.entity.secondary.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be added here
}