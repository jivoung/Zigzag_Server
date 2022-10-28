package com.example.demo.src.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTagRes {
    private int userIdx;
    private int storeTagIdx;
    private String keyword;
    private int count;
}
