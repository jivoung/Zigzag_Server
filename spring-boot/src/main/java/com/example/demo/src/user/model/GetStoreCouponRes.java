package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreCouponRes {
    private int couponIdx;
    private int price;
    private String couponName;
    private int minimumCost;
    private String couponType;
}
