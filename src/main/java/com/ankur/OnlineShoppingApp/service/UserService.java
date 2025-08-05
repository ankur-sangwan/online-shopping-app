package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.CustomValidationException;
import com.ankur.OnlineShoppingApp.model.Cart;
import com.ankur.OnlineShoppingApp.model.Users;
import com.ankur.OnlineShoppingApp.model.WishList;
import com.ankur.OnlineShoppingApp.repository.CartRepository;
import com.ankur.OnlineShoppingApp.repository.UserRepository;
import com.ankur.OnlineShoppingApp.repository.WishListRepository;
import com.ankur.OnlineShoppingApp.resource.LoginRequestDto;
import com.ankur.OnlineShoppingApp.resource.UserRequestDto;
import com.ankur.OnlineShoppingApp.response.UserResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private WishListRepository wishListRepository;

    public UserResponseDto register(@Valid UserRequestDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomValidationException("user already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomValidationException("email address already exists");
        }
        Users userModel = new Users();
        userModel.setName(user.getName());
        userModel.setEmail(user.getEmail());
        userModel.setUsername(user.getUsername());
        userModel.setPassword(encoder.encode(user.getPassword())); // 👉 Encrypt in real apps
        userModel.setRole(user.getRole());
        userModel.setWalletBalance(0);

        Users savedUser = userRepository.save(userModel);
        // Create an empty cart for the new user
        Cart newCart = new Cart();
        newCart.setUser(savedUser);
        cartRepository.save(newCart);


        // Create an wishlist for the new user

        WishList wishList=new WishList();
        wishList.setUser(savedUser);
        wishListRepository.save(wishList);



        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setUsername(savedUser.getUsername());
        response.setRole(savedUser.getRole());

        return response;

    }


    public Map<String, Object> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // HttpServletResponse :- to set jwt token,
        // Authenticate the loginRequestDto with username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        // Generate JWT token after successful authentication
        String jwtToken = jwtService.generateToken(loginRequestDto.getUsername());

        // Set JWT token in response header
        response.setHeader("Authorization", "Bearer " + jwtToken);  // JWT token in Authorization header

        Users user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("loginRequestDto not found"));
        Cart cart = cartRepository.findByUserId(user.getId());
        int cartItemCount = 0;
        if (cart != null) {

            cartItemCount = cart.getItems().size();
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful, welcome back " + loginRequestDto.getUsername() + "!");
        responseBody.put("username", loginRequestDto.getUsername());
        responseBody.put("cartItemCount", cartItemCount);

        return responseBody;
    }
}
