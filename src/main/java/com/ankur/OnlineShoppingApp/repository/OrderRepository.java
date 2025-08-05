package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Cart;
import com.ankur.OnlineShoppingApp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByUserId(int userId);
}
