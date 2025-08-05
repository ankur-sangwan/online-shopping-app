package com.ankur.OnlineShoppingApp.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProductCategory {
    ELECTRONICS,
    FASHION,
    HOME,
    SPORTS,
    BEAUTY,
    BOOKS;
    @JsonCreator
    public static ProductCategory fromValue(String string){

        for(ProductCategory productCategory:ProductCategory.values()){
            if(string.equalsIgnoreCase(productCategory.name())){
                return productCategory;
            }
        }
        throw new IllegalArgumentException("Invalid ProductCategory: " +string);
    }
}
