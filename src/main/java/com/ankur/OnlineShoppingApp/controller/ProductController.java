package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.model.ProductCategory;
import com.ankur.OnlineShoppingApp.resource.ProductRequestDto;
import com.ankur.OnlineShoppingApp.response.ApiResponse;
import com.ankur.OnlineShoppingApp.response.ProductResponseDto;
import com.ankur.OnlineShoppingApp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    public ProductService productService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> create( @RequestBody  @Valid ProductRequestDto requestDto) {

        ProductResponseDto productResponseDto=productService.create(requestDto);
        return new ResponseEntity<>(new ApiResponse<>("Product created",productResponseDto,HttpStatus.CREATED),HttpStatus.CREATED);
    }
   // UserDetails
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable int id) {
        productService.delete(id);
        return new ResponseEntity<>(new ApiResponse<>("product deleted", null,HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> update(@PathVariable int id, @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto productResponseDto=productService.update(id, productRequestDto);
        return new ResponseEntity<>(new ApiResponse<>("updated successfully",productResponseDto,HttpStatus.OK),HttpStatus.OK);
    }
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAll(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(defaultValue="id") List<String> sortBy,
                                            @RequestParam(defaultValue="asc") String sortDir){

       Page<ProductResponseDto> productResponseDto=productService.getAll(page,size,sortBy,sortDir);

        return new ResponseEntity<>(new ApiResponse<>("product retrived",productResponseDto,HttpStatus.OK),HttpStatus.OK);
    }
    @GetMapping("/filterByCategory")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getByCategory(@RequestParam List<ProductCategory> categories,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size){

        Page<ProductResponseDto> productResponseDto=productService.getByCategory(categories,page,size);
        return new ResponseEntity<>(new ApiResponse<>("Product retrived",productResponseDto,HttpStatus.OK),HttpStatus.OK);
    }


}
