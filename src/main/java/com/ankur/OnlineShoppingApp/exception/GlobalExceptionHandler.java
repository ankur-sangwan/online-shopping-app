package com.ankur.OnlineShoppingApp.exception;

import com.ankur.OnlineShoppingApp.model.ProductCategory;
import com.ankur.OnlineShoppingApp.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFoundException(ProductNotFoundException e) {

        return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(new ApiResponse<>("validation failed", errorMap, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<String>> handleInvalidInputExceptions(Exception ex) {
        StringBuilder allowedValues = new StringBuilder();
        for (ProductCategory category : ProductCategory.values()) {
            allowedValues.append(category).append(", ");
        }
        // Remove trailing comma and space
        String string = allowedValues.substring(0, allowedValues.length() - 1);
        String userMessage = "Invalid input provided. Please make sure the product category is one of: " + string;
        return new ResponseEntity<>(new ApiResponse<>(userMessage, null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomValidationException(CustomValidationException e) {

        return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(),null,HttpStatus.UNAUTHORIZED),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<String>> InsufficientStockException(InsufficientStockException  ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(),null,HttpStatus.CONFLICT),HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(),null,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(RuntimeException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(),null,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("❌ " + ex.getMessage());
    }

    @ExceptionHandler(PaymentAlreadyDoneException.class)
    public ResponseEntity<ApiResponse<String>> handlePaymentAlreadyDoneException(PaymentAlreadyDoneException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InsufficientWalletBalanceException.class)
    public ResponseEntity<ApiResponse<String>> handleInsufficientWalletBalanceException(InsufficientWalletBalanceException ex) {
        return new ResponseEntity<>(new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidOrderStateException(InvalidOrderStateException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(OrderAlreadyReturnedException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderAlreadyReturnedException(OrderAlreadyReturnedException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(InvalidReturnQuantityException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidReturnQuantityException(InvalidReturnQuantityException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(ProductNotInOrderException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotInOrderException(ProductNotInOrderException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleOrderNotFoundException(OrderNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse response = new ApiResponse("Access Denied", null, HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }



}
