package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    private int productIdx;
    private String productImgUrl;
    private String productName;
    private String color;
    private String size;
}
