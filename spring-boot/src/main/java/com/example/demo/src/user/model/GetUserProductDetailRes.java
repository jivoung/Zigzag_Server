package com.example.demo.src.user.model;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserProductDetailRes {
    private int orderProductIdx;
    private String productCode;
    private java.sql.Timestamp createdAt;
    private int storeIdx;
    private String storeImg;
    private String storeName;
    private String storePhoneNum;
    private int productIdx;
    private String productImgUrl;
    private String productName;
    @Null
    private String productColor;
    @Null
    private String productSize;
    private int count;
    private String status;
    private int price;
    private int shippingCost;
    private int totalPrice;
    private String recipientName;
    private String recipientPhoneNum;
    private String recipientAddress;
    @Null
    private String memo;
    private String payment;
}
