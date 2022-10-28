package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetProductResultExtendRes {
    private List<String> relatedKeywords;
    private List<GetProductResultRes> getProductResultRes;
}
