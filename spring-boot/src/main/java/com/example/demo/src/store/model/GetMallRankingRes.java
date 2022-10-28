package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMallRankingRes {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private List<String> ageGroup;
    private List<String> style;
    private int likeNum;
    private boolean freeDelivery;
    private int maxCouponPrice;
}
