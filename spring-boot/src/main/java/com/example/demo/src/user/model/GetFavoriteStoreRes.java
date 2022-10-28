package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteStoreRes {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private int maxCouponPrice;
    private int newProductCount;
}
