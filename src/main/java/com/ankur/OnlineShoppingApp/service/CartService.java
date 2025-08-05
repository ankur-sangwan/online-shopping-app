package com.ankur.OnlineShoppingApp.service;


import com.ankur.OnlineShoppingApp.exception.ProductNotFoundException;
import com.ankur.OnlineShoppingApp.model.Cart;
import com.ankur.OnlineShoppingApp.model.CartItem;
import com.ankur.OnlineShoppingApp.model.Product;
import com.ankur.OnlineShoppingApp.model.Users;
import com.ankur.OnlineShoppingApp.repository.CartItemRepository;
import com.ankur.OnlineShoppingApp.repository.CartRepository;
import com.ankur.OnlineShoppingApp.repository.ProductRepository;
import com.ankur.OnlineShoppingApp.repository.UserRepository;
import com.ankur.OnlineShoppingApp.resource.AddToCartRequestDto;
import com.ankur.OnlineShoppingApp.resource.CartItemDto;
import com.ankur.OnlineShoppingApp.response.CartResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartResponseDto addToCart(int userId, AddToCartRequestDto requestDto) {
      //  Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
        Product product = productRepository.findById(requestDto.getProductId()).orElseThrow(() -> new ProductNotFoundException("product not exists"));

        Cart cart = cartRepository.findByUserId(userId);
        int availableQuantity = product.getQuantity();
        int requestedQuantity = requestDto.getQuantity();
        if (requestedQuantity > availableQuantity) {
            throw new RuntimeException("Requested quantity exceeds available stock.");
        }

//        check if the product is present in the cart
        CartItem cartItem;
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(requestedQuantity);
            cartItem.setProduct(product);
            cartItem.setCart(cart);

            cart.getItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);


        int total = 0;
        List<CartItemDto> itemList = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setQuantity(item.getQuantity());
            cartItemDto.setProductName(item.getProduct().getName());
            cartItemDto.setPricePerUnit(item.getProduct().getPrice());
            itemList.add(cartItemDto);
            total += item.getProduct().getPrice() * item.getQuantity();


        }
        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setTotalAmount(total);
        responseDto.setUserId(userId);
        responseDto.setItemList(itemList);
        return responseDto;
    }

    public CartResponseDto viewCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId);
        List<CartItemDto> dtoList = new ArrayList<>();
        int total = 0;

        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setPricePerUnit(item.getProduct().getPrice());
            cartItemDto.setQuantity(item.getQuantity());
            cartItemDto.setProductName(item.getProduct().getName());
            dtoList.add(cartItemDto);
        }
        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setUserId(userId);
        responseDto.setTotalAmount(total);
        responseDto.setItemList(dtoList);
        return responseDto;


    }

    public CartResponseDto deleteCartItems(int userId, int productId, int quantityToRemove) {
        Cart cart=cartRepository.findByUserId(userId);
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("product not found"));
        CartItem existingItem=cartItemRepository.findByCartAndProduct(cart,product).orElseThrow(()->new ProductNotFoundException("product not exists in the cart"));
        if(quantityToRemove>=existingItem.getQuantity()){
            cartItemRepository.delete(existingItem);

        }
        else{
            int quantity=existingItem.getQuantity();
            existingItem.setQuantity(existingItem.getQuantity()-quantityToRemove);
            cartItemRepository.save(existingItem);
        }
        int total=0;
        List<CartItemDto> dtoList=new ArrayList<>();
        for(CartItem item: cart.getItems()){
            total += item.getProduct().getPrice() * item.getQuantity();
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setPricePerUnit(item.getProduct().getPrice());
            cartItemDto.setQuantity(item.getQuantity());
            cartItemDto.setProductName(item.getProduct().getName());
            dtoList.add(cartItemDto);

        }
        CartResponseDto responseDto=new CartResponseDto();
        responseDto.setUserId(userId);
        responseDto.setTotalAmount(total);
        responseDto.setItemList(dtoList);
        return responseDto;

    }

    public CartResponseDto clearCart(int userId) {
        Cart cart=cartRepository.findByUserId(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setUserId(userId);
        responseDto.setTotalAmount(0);
        responseDto.setItemList(new ArrayList<>()); // Empty list

        return responseDto;

    }
}