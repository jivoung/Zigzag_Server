package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetStoreResultExtendRes {
    private List<String> relatedKeywords;
    private List<GetStoreResultRes> getStoreResultRes;
}
