package com.gdu.halbae.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;
import com.gdu.halbae.domain.PaymentDTO;
import com.gdu.halbae.domain.ScheduleDTO;
import com.gdu.halbae.mapper.EnrollMapper;
import com.gdu.halbae.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
	
	public final PaymentMapper paymentMapper;
	public final EnrollMapper enrollMapper;
	
	/* 조회 */
	
	// 해당 쿠폰 조회
	@Override
	public CouponDTO selectCouponByNo(int couponNo) {
		return paymentMapper.selectCouponByNo(couponNo);
	}
	
	// 수강신청 조회
	@Override
	public EnrollDTO selectEnrollNo(int userNo) {
		return paymentMapper.selectEnrollNo(userNo);
	}
	
	// 클래스 & 스케줄 조회
	@Override
	public Map<String, Object> selectClassAndSch(int enrollNo) {
		
		// enrollNo 통해서 스케줄 가져오기
		int schNo = paymentMapper.selectEnroll(enrollNo);
		
		ScheduleDTO scheduleDTO = enrollMapper.selectScheduleByNo(schNo);
		
		// schNo 통해서 스케줄 가져오기
		int classNo = paymentMapper.selectClassNoBySchNo(schNo); 
		ClassListDTO classListDTO = enrollMapper.selectClassByNo(classNo);

		// Map에 담기
		Map<String, Object> map = new HashMap<>();
		map.put("scheduleDTO", scheduleDTO);
		map.put("classListDTO", classListDTO);
		return map;
	}
	
	/* 결제 수행 */
	@Transactional
	@Override
	public void insertPayment(HttpServletRequest request) {
		
		// 결제 INSERT
		int couponNo = Integer.parseInt(request.getParameter("couponNo"));
		int point = Integer.parseInt(request.getParameter("point"));
		int enrollNo = Integer.parseInt(request.getParameter("enrollNo"));
		int totalCP = Integer.parseInt(request.getParameter("totalCP"));			
		int paySale = totalCP + point;														// 총 할인금액
		int payMethod = Integer.parseInt(request.getParameter("payMethod"));				// 결제수단
		int payAmount = Integer.parseInt(request.getParameter("amount"));					// 총 결제금액
		int userNo = Integer.parseInt(request.getSession().getAttribute("userNo")+"");		// 회원번호
		int schNo = Integer.parseInt(request.getParameter("schNo"));						// 스케줄 번호
		
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setEnrollNo(enrollNo);
		paymentDTO.setPayAmount(payAmount);
		paymentDTO.setPaySale(paySale);
		paymentDTO.setPayMethod(payMethod);
		
		int resultInsert = paymentMapper.insertPayment(paymentDTO);
		
		// 스케줄 인원 UPDATE
		EnrollDTO enrollDTO = enrollMapper.selectEnroll(enrollNo);
		ScheduleDTO schduleDTO = enrollMapper.selectScheduleByNo(schNo);
		enrollDTO.setScheduleDTO(schduleDTO);
		
		if(resultInsert == 1) {
			enrollMapper.updateSchNowNum(enrollDTO);
			enrollMapper.updateSchState(enrollDTO);
		}
		
		// 사용한 포인트 UPDATE
		Map<String, Object> pointUser = new HashMap<>();
		pointUser.put("point", point);
		pointUser.put("userNo", userNo);
		paymentMapper.updateUserPoint(pointUser);
		
		// 사용한 쿠폰 DELETE
		if(couponNo != 0) {
			Map<String, Object> couponUser = new HashMap<>();
			couponUser.put("couponNo", couponNo);
			couponUser.put("userNo", userNo);
			paymentMapper.deleteCoupon(couponUser);			
		}
		
	}
	
	// 결제실패 (Enroll 삭제)
	@Override
	public void deleteEnroll(HttpServletRequest request) {
		int enrollNo = Integer.parseInt(request.getParameter("enrollNo"));
		paymentMapper.deleteEnroll(enrollNo);
		
	}
	
}
