package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class StoreResult {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private String storeType;
    private int likeNum;
}
