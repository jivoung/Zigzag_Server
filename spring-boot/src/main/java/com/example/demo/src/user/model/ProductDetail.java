package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDetail {
    private int productIdx;
    private int storeIdx;
    private String storeName;
    private String productName;
    private String productCode;
    private int price;
    private int discountRate;
    private int shippingCost;
    private String contents;
    private String colorName;
    private String sizeName;
    private int shippingPossible;
}