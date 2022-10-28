package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPointRes {
    private java.sql.Timestamp createdAt;
    private int point;
    private String pointTypeDesc;
    private java.sql.Timestamp deadline;
}
