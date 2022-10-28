package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserExtendRes {
    private int userIdx;
    private String name;
    private String email;
    private String membershipType;
    private int orderDeliveryCount;
    private int reviewCount;
    private int couponCount;
    private int point;
}
