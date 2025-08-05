package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findByUserId(int userId);
}
