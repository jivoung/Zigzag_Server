package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreProductRes {
    private int productIdx;
    private String productName;
    private List<String> productImg;
    private int price;
    private int shippingCost;
    private String isNew;
    private String isQuick;
    private String isZdiscount;
    private int discountRate;
    private int reviewAverage;
    private int reviewCount;
}
