package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreRes {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private List<String> ageGroup;
    private List<String> style;
    private int likeNum;
    private int maxCouponPrice;
    private int productCount;
}
