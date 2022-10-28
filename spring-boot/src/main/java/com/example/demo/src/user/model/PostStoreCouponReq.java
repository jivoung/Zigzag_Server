package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
public class PostStoreCouponReq {
    private int storeIdx;
    private int couponIdx;
}
