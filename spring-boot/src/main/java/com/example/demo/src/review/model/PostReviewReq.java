package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {
    private int productIdx;
    private int starRate;
    private int reviewSize;
    private int reviewColor;
    private int reviewQuality;
    private String contents;
}
