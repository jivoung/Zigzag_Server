package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikedProductRes {
    private int productIdx;
    private int storeIdx;
    private String storeName;
    private String productImgUrl;
    private String productName;
    private int price;
    private int discountRate;
    private int shippingCost;
    private String isNew;
    private String isQuick;
    private String isBrand;
    private String isZdiscount;
    private int folderIdx;
    private String folderName;
}