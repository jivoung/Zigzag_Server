package com.example.demo.src.store.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewBrandRes {
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private String storeCategory;
    private int likeNum;
    private boolean freeDelivery;
}
