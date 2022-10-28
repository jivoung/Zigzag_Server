package com.example.demo.src.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetShoppingBagRes {
    private int userIdx;
    private int productIdx;
    private String storeImg;
    private String storeName;
    private String productImgUrl;
    private String productName;
    private String colorName;
    private String sizeName;
    private int count;
    private int price;
    private int shippingCost;
}