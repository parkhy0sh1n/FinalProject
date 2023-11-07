package com.gdu.halbae.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private int couponNo;
    private String couponName;
    private int couponDiscount;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponEndDate;
}