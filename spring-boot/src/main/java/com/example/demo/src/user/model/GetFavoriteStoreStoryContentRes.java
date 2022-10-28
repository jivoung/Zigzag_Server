package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteStoreStoryContentRes {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private java.sql.Timestamp createdAt;
    private int productIdx;
    private String storyImg;
    private String productName;
}
