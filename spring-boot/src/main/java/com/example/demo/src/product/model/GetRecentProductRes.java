package com.example.demo.src.product.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRecentProductRes {
    private int userIdx;
    private int productIdx;
    private String productImgUrl;
    private String storeName;
    private String ProductName;
    private int discountRate;
    private int price;
    private String likeStatus;
}