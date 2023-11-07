package com.gdu.halbae.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.CouponUserDTO;

@Mapper
public interface CouponMapper {
    // 회원이 보유한 쿠폰 목록 조회
    List<CouponDTO> getAllCoupons(int userNo);
    // 회원의 보유 쿠폰 수 조회
    int getAvailableCouponCount(int userNo);
    // 쿠폰 등록
    int insertCouponUser(CouponUserDTO couponUserDTO);
    // 쿠폰명을 기준으로 쿠폰 번호를 조회
    CouponDTO getCouponNoByCouponName(String couponName);
    // 쿠폰 사용
    void useCoupon(CouponUserDTO couponUserDTO);
}