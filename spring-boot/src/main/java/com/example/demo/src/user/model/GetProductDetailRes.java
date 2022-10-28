package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductDetailRes {
    private int productIdx;
    private int storeIdx;
    private String storeName;
    private List<String> productImg;
    private String productName;
    private String productCode;
    private int price;
    private int discountRate;
    private int shippingCost;
    private String contents;
    private String color;
    private String size;
    private int shippingPossible;
}
