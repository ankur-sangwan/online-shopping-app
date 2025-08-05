package com.ankur.OnlineShoppingApp.repository;

import com.ankur.OnlineShoppingApp.model.Product;
import com.ankur.OnlineShoppingApp.model.WishList;
import com.ankur.OnlineShoppingApp.model.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem,Integer> {

    Optional<WishListItem> findByWishlistAndProduct(WishList wishlist, Product product);


    void deleteByWishlistIdAndProductId(int id, int productId);
}
