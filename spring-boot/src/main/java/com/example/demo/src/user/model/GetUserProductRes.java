package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserProductRes {
    private int orderProductIdx;
    private java.sql.Timestamp createdAt;
    private String storeName;
    private String productImgUrl;
    private String productName;
    private String status;
}
