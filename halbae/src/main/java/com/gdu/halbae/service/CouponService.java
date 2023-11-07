package com.gdu.halbae.service;

import java.util.List;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.CouponUserDTO;

public interface CouponService {
    // 회원이 보유한 쿠폰 목록 조회
	List<CouponDTO> getAllCoupons(int userNo);
    // 회원의 보유 쿠폰 수 조회
	int getAvailableCouponCount(int userNo);
    // 쿠폰 등록
    int issueCouponToUser(String couponName, int userNo);
    // 쿠폰명을 기준으로 쿠폰 번호를 조회
    int getCouponNoByCouponName(String couponName);
    // 쿠폰 사용
    void useCoupon(CouponUserDTO couponUserDTO);
}