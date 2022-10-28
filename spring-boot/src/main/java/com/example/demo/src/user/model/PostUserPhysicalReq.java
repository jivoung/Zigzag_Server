package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserPhysicalReq {
    private int userIdx;
    private int height;
    private int weight;
    private int topSize;
    private int bottomSize;
    private int shoesSize;
}
