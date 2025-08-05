package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.ProductNotFoundException;
import com.ankur.OnlineShoppingApp.model.*;
import com.ankur.OnlineShoppingApp.repository.*;
import com.ankur.OnlineShoppingApp.resource.AddToWishlistRequestDTO;
import com.ankur.OnlineShoppingApp.resource.CartItemDto;
import com.ankur.OnlineShoppingApp.resource.MoveToCartRequestDTO;
import com.ankur.OnlineShoppingApp.resource.WishListItemDto;
import com.ankur.OnlineShoppingApp.response.CartResponseDto;
import com.ankur.OnlineShoppingApp.response.WishListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishListItemRepository wishListItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;


    public WishListResponseDto addToWishlist(int userId, AddToWishlistRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product not exists"));

        WishList wishList = wishListRepository.findByUserId(userId);


        Optional<WishListItem> existingItem = wishListItemRepository.findByWishlistAndProduct(wishList, product);

        if (existingItem.isEmpty()) {
            WishListItem item = new WishListItem();
            item.setWishlist(wishList);
            item.setProduct(product);
            wishList.getItems().add(item);
        }


        // 5. Save updated wishlist
        wishListRepository.save(wishList);
        return buildWishlistResponse(wishList, userId);
    }

    public WishListResponseDto viewWishList(int userId) {
        WishList wishList = wishListRepository.findByUserId(userId);


        return buildWishlistResponse(wishList, userId);

    }

    @Transactional
    public WishListResponseDto deleteWishListItems(int userId, int productId) {
        WishList wishList = wishListRepository.findByUserId(userId);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Optional<WishListItem> existingItems = wishListItemRepository.findByWishlistAndProduct(wishList, product.get());

            if (existingItems.isPresent()) {
                wishListItemRepository.delete(existingItems.get());
                wishList.getItems().remove(existingItems.get());

            }
        } else {
            // maan lete  hai product db se hi delete ho gya ho
          // Product doesn't exist, delete directly by wishlistId and productId
            wishListItemRepository.deleteByWishlistIdAndProductId(wishList.getId(), productId);
        }

        return buildWishlistResponse(wishList, userId);
    }

    public WishListResponseDto clearWishList(int userId) {
        WishList wishList = wishListRepository.findByUserId(userId);
        wishListItemRepository.deleteAll();
        wishList.getItems().clear();
        wishListRepository.save(wishList);
        return buildWishlistResponse(wishList, userId);

    }
    public CartResponseDto moveToCartFromWishlist(int userId,MoveToCartRequestDTO requestDTO) {
        Product product=productRepository.findById(requestDTO.getProductId()).orElseThrow(()->new ProductNotFoundException("product not exists"));
        WishList wishList = wishListRepository.findByUserId(userId);
        Optional<WishListItem> item=wishListItemRepository.findByWishlistAndProduct(wishList,product);
        if(item.isEmpty())
        {
            throw new ProductNotFoundException("product not found ");
        }
        if (product.getQuantity() <= 0) {
            throw new ProductNotFoundException("Product is out of stock");
        }
        Cart cart=cartRepository.findByUserId(userId);
        Optional<CartItem> cartItem=cartItemRepository.findByCartAndProduct(cart,product);
        if(cartItem.isPresent()){
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity()+1);
        }
        else {
            CartItem newItem  = new CartItem();

            newItem.setQuantity(1);
            newItem.setProduct(product);
            newItem.setCart(cart);
            cart.getItems().add(newItem);

        }
        cartRepository.save(cart);

        wishListItemRepository.delete(item.get());
        wishList.getItems().remove(item.get());

        int total=0;
        List<CartItemDto> dtoList=new ArrayList<>();
        for(CartItem cartItems:cart.getItems()){
            CartItemDto cartItemDto=new CartItemDto();
            cartItemDto.setQuantity(cartItems.getQuantity());
            cartItemDto.setPricePerUnit(cartItems.getProduct().getPrice());
            cartItemDto.setProductName(cartItems.getProduct().getName());
            total+=cartItems.getProduct().getPrice()*cartItems.getQuantity();
            dtoList.add(cartItemDto);

        }
        CartResponseDto responseDto=new CartResponseDto();
        responseDto.setItemList(dtoList);
        responseDto.setTotalAmount(total);
        responseDto.setUserId(userId);
        return responseDto;



    }


    private WishListResponseDto buildWishlistResponse(WishList wishList, int userId) {
        int total = 0;
        List<WishListItemDto> itemList = new ArrayList<>();

        for (WishListItem item : wishList.getItems()) {
            WishListItemDto dto = new WishListItemDto();
            dto.setProductName(item.getProduct().getName());
            dto.setPricePerUnit(item.getProduct().getPrice());
            itemList.add(dto);

            total += item.getProduct().getPrice();
        }

        WishListResponseDto responseDto = new WishListResponseDto();
        responseDto.setUserId(userId);
        responseDto.setItemList(itemList);
        responseDto.setPrice(total);

        return responseDto;
    }



}

