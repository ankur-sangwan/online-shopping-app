package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.ProductNotFoundException;
import com.ankur.OnlineShoppingApp.model.Product;
import com.ankur.OnlineShoppingApp.model.ProductCategory;
import com.ankur.OnlineShoppingApp.repository.ProductRepository;
import com.ankur.OnlineShoppingApp.resource.ProductRequestDto;
import com.ankur.OnlineShoppingApp.response.ProductResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    public ProductRepository productRepository;

    public ProductResponseDto create(ProductRequestDto product) {
        Product productModel = new Product();
        productModel.setQuantity(product.getQuantity());
        productModel.setName(product.getName());
        productModel.setDescription(product.getDescription());
        productModel.setPrice(product.getPrice());
        productModel.setCategory(product.getCategory());

        Product saved = productRepository.save(productModel);

        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setName(saved.getName());
        productResponseDto.setQuantity(saved.getQuantity());
        productResponseDto.setDescription(saved.getDescription());
        productResponseDto.setId(saved.getId());
        productResponseDto.setPrice(saved.getPrice());
        productResponseDto.setCategory(saved.getCategory());

        return productResponseDto;
    }

    @CacheEvict(value = "products", key = "#id")

    public void delete(int id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException("product not found with id: " + id);
        }
    }

    @CachePut(value = "products", key = "#product.id")
    public ProductResponseDto update(int id, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        // update the fields
        existingProduct.setName(productRequestDto.getName());
        existingProduct.setDescription(productRequestDto.getDescription());
        existingProduct.setPrice(productRequestDto.getPrice());
        existingProduct.setQuantity(productRequestDto.getQuantity());
        existingProduct.setCategory(productRequestDto.getCategory());


        //save the product
        Product saved = productRepository.save(existingProduct);

        ProductResponseDto responseDto = new ProductResponseDto();
        // update response Dto
        responseDto.setId(saved.getId());
        responseDto.setDescription(saved.getDescription());
        responseDto.setPrice(saved.getPrice());
        responseDto.setQuantity(saved.getQuantity());
        responseDto.setName(saved.getName());
        responseDto.setCategory(saved.getCategory());
        return responseDto;
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponseDto getProductById(int id) {
        System.out.println("Fetching from DB..."); // Proof for cache

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        return new ProductResponseDto(
                product.getId(),
                product.getPrice(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getCategory()
        );
    }

    public Page<ProductResponseDto> getAll(int page, int size, List<String> sortBy, String sortDir) {

        Sort sort;
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy.toArray(new String[0])).ascending();
        } else {
            sort = Sort.by(sortBy.toArray(new String[0])).descending();
        }
        Pageable pageRequest = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.fetchAll(pageRequest);


        List<ProductResponseDto> responseList = new ArrayList<>();
        List<Product> productList = productPage.getContent();


        for (Product product : productList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(product.getId());
            productResponseDto.setName(product.getName());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setDescription(product.getDescription());
            productResponseDto.setQuantity(product.getQuantity());
            productResponseDto.setCategory(product.getCategory());
            responseList.add(productResponseDto);
        }
        return new PageImpl<>(responseList, productPage.getPageable(), productPage.getTotalElements());

    }

    public Page<ProductResponseDto> getByCategory(List<ProductCategory> categories, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByCategory(categories, pageable);
        List<Product> productList = productPage.getContent();
        List<ProductResponseDto> responseList = new ArrayList<>();
        for (Product product : productList) {
            ProductResponseDto productResponseDto = new ProductResponseDto();
            productResponseDto.setId(product.getId());
            productResponseDto.setName(product.getName());
            productResponseDto.setPrice(product.getPrice());
            productResponseDto.setDescription(product.getDescription());
            productResponseDto.setQuantity(product.getQuantity());
            productResponseDto.setCategory(product.getCategory());
            responseList.add(productResponseDto);

        }
        return new PageImpl<>(responseList, productPage.getPageable(), productPage.getTotalElements());
    }
}
