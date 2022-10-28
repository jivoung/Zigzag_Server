package com.example.demo.src.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTagStoreRes {
    private int userIdx;
    private int storeIdx;
    private String storeName;
    private String storeImg;
    private String keyword;
    private boolean isFavorite;
}
