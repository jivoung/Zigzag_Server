package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewAllRes {
    private String name;
    private int starRate;
    private String confirmedDate;
    private int height;
    private int weight;
    private int topSize;
    private int bottomSize;
    private int shoesSize;
    private String colorName;
    private String sizeName;
    private String reviewSize;
    private String reviewQuality;
    private String reviewColor;
    private String contents;
    private String value;
}
