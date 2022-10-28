package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewResultRes {
    private int productIdx;
    private double starRate;
    private String reviewSize;
    private int sizeValue;
    private String reviewColor;
    private int colorValue;
    private String reviewQuality;
    private int qualityValue;

}
