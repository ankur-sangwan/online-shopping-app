package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.resource.LoginRequestDto;
import com.ankur.OnlineShoppingApp.resource.UserRequestDto;
import com.ankur.OnlineShoppingApp.response.UserResponseDto;
import com.ankur.OnlineShoppingApp.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto user) {
        System.out.println("hlo, trying to rin register api");
        UserResponseDto userResponse = userService.register(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody @Valid LoginRequestDto user, HttpServletResponse  response) {
        // Call the service method to authenticate user and generate JWT token
       // return userService.login1(user, response);

        return new ResponseEntity<>(userService.login(user,response),HttpStatus.OK);
    }
}
