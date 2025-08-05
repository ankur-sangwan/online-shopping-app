package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Product;
import com.ankur.OnlineShoppingApp.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p")
    Page<Product> fetchAll(Pageable pageable);
   @Query("SELECT p FROM Product p WHERE p.category IN :categories ORDER BY p.id ASC")
    Page<Product> findByCategory(List<ProductCategory> categories,Pageable pageable);
}
