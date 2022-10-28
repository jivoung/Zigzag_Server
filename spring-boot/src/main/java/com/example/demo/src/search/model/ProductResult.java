package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResult {
    private int productIdx;
    private String productName;
    private String storeName;
    private int price;
    private int shippingCost;
    private String isNew;
    private String isQuick;
    private String isBrand;
    private String isZdiscount;
    private int discountRate;
}
