package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetProductResultRes {
    private int productIdx;
    private List<String> productImgUrl;
    private String storeName;
    private String productName;
    private int price;
    private int shippingCost;
    private String isNew;
    private String isQuick;
    private String isBrand;
    private String isZdiscount;
    private int discountRate;
}
