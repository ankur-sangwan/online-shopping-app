package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Cart;
import com.ankur.OnlineShoppingApp.model.ReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem,Integer> {

    List<ReturnItem> findByReturnEntityOrderId(int orderId);
}
