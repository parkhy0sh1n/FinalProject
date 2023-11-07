package com.gdu.halbae.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;
import com.gdu.halbae.domain.PaymentDTO;

@Mapper
public interface PaymentMapper {
	
	/* 조회 */
	
	// 해당 쿠폰 조회
	public CouponDTO selectCouponByNo(int couponNo);
	// 수강신청 조회
	public EnrollDTO selectEnrollNo (int userNo);
	
	// 클래스 & 스케줄 조회
	public int selectEnroll(int enrollNo);
	public int selectClassNoBySchNo(int schNo); 
	
	// 결제 추가
	public int insertPayment(PaymentDTO paymentDTO);
	
	// 포인트 update
	public int updateUserPoint(Map<String, Object> map);
	
	// 쿠폰 delete
	public int deleteCoupon(Map<String, Object> map);
	
	// 결제취소 (Enroll 삭제)
	public int deleteEnroll(int enrollNo);
	
}

