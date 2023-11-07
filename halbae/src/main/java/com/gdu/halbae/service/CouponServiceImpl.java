package com.gdu.halbae.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.CouponUserDTO;
import com.gdu.halbae.mapper.CouponMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
	
    private final CouponMapper couponMapper;
    
    // 회원이 보유한 쿠폰 목록 조회
    @Override
    public List<CouponDTO> getAllCoupons(int userNo) {
        return couponMapper.getAllCoupons(userNo);
    }
    
    // 회원의 보유 쿠폰 수 조회
    @Override
    public int getAvailableCouponCount(int userNo) {
        return couponMapper.getAvailableCouponCount(userNo);
    }

    // 쿠폰 등록
    @Override
    public int issueCouponToUser(String couponName, int userNo) {
        CouponDTO dto = couponMapper.getCouponNoByCouponName(couponName);
        if(dto == null) {
        	return 0;
        }
        CouponUserDTO couponUserDTO = new CouponUserDTO();
        couponUserDTO.setUserNo(userNo);
        couponUserDTO.setCouponNo(dto.getCouponNo());
        couponUserDTO.setCouponStatus(1);
        return couponMapper.insertCouponUser(couponUserDTO);
    }

    // 쿠폰명을 기준으로 쿠폰 번호를 조회
    @Override
    public int getCouponNoByCouponName(String couponName) {
    	CouponDTO dto = couponMapper.getCouponNoByCouponName(couponName);
    	return dto.getCouponNo();
    };
    
    // 쿠폰 사용
    @Override
    public void useCoupon(CouponUserDTO couponUserDTO) {
        couponUserDTO.setCouponStatus(1);
        couponMapper.useCoupon(couponUserDTO);
    }
    
}