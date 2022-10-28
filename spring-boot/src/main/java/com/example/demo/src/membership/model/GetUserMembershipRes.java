package com.example.demo.src.membership.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserMembershipRes {
    private String membershipType;
    private int additionalPrice;
    private String nextMembershipType;
}
