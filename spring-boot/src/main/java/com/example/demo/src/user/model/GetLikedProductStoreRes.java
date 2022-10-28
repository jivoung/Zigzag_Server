package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikedProductStoreRes {
    private int storeIdx;
    private String storeName;
    private int productCount;
}