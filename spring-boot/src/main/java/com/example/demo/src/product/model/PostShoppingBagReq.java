package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShoppingBagReq {
    private int userIdx;
    private int storeIdx;
    private int productIdx;
    private int productDetailIdx;
    private int count;
    private int totalProductPrice;
}
