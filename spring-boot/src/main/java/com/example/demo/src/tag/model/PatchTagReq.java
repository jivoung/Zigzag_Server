package com.example.demo.src.tag.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchTagReq {
    private int userIdx;
    private String keyword;
}
