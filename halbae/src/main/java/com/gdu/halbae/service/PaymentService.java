package com.gdu.halbae.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;

public interface PaymentService {
	
	// 해당 쿠폰 조회
	public CouponDTO selectCouponByNo(int couponNo);
	
	// 수강신청 조회
	public EnrollDTO selectEnrollNo(int userNo);
	
	// 결제 실행
	public void insertPayment(HttpServletRequest request);
	
	// 클래스, 스케줄 조회
	public Map<String, Object> selectClassAndSch(int enrollNo);
	
	// Enroll 삭제
	public void deleteEnroll(HttpServletRequest request);
}
