package com.example.demo.src.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTagStoreReq {
    private int userIdx;
    private String keyword;
}
