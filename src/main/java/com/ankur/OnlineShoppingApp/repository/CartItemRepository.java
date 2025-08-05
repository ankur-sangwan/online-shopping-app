package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Cart;
import com.ankur.OnlineShoppingApp.model.CartItem;
import com.ankur.OnlineShoppingApp.model.Product;
import com.ankur.OnlineShoppingApp.resource.CartOrderItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.user.id = :userId AND ci.product.id IN :productIds")
    List<CartItem> findSelectedCartItems(@Param("userId") int userId, @Param("productIds") List<Integer> productIds);
    @Query("Select ci FROM CartItem ci WHERE ci.cart.user.id=:userId")
    List<CartItem> findCartItems(@Param("userId") int userId);
}
