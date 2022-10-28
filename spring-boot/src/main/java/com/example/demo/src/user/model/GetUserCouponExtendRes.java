package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GetUserCouponExtendRes {
    private int count;
    private int zigzagCount;
    private int storeCount;
    private int deliveryCount;
    private List<GetUserCouponRes> getUserCouponResList;
}
