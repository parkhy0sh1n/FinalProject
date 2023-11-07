package com.gdu.halbae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUserDTO {
	private int userNo;
	private int couponNo;
	private int couponStatus;
}