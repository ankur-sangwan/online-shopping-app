package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {
    WishList findByUserId(int userId);
}
