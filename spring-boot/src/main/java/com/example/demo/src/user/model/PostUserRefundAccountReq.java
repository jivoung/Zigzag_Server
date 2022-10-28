package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRefundAccountReq {
    private String bankName;
    private String accountNumber;
    private String accountHolder;
}