package com.gdu.halbae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
	
	private int enrollNo; 	// 수강신청 식별자
	private int payAmount;	// 총 결제금액
	private int paySale;	// 총 할인금액
	private int payMethod;	// 결제수단
	
}