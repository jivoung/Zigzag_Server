package com.example.demo.src.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostTagReq {
    private int userIdx;
    private int storeIdx;
    private String keyword;
}
