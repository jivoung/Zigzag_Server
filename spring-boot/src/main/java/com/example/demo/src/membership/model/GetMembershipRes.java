package com.example.demo.src.membership.model;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMembershipRes {
    private String membershipType;
    private int minimumPrice;
    @Null
    private int maximumPrice;
    private float rewardRate;
}
