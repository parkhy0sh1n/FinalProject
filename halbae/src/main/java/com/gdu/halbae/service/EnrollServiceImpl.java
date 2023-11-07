package com.gdu.halbae.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;
import com.gdu.halbae.domain.ScheduleDTO;
import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.mapper.EnrollMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollServiceImpl implements EnrollService {

	public final EnrollMapper enrollMapper;

	/* 조  회 */
	// 강의 조회
	@Override
	public ClassListDTO classListByNo(HttpServletRequest request) {
		String strclassNo = request.getParameter("classNo");
		int classNo = 0;
		if (strclassNo != null && strclassNo.isEmpty() == false) {
			classNo = Integer.parseInt(strclassNo);
		}
		return enrollMapper.selectClassByNo(classNo);
	}

	// 스케줄 조회
	@Override
	public List<ScheduleDTO> scheduleByClassNo(HttpServletRequest request) {
		String strclassNo = request.getParameter("classNo");
		int classNo = 0;
		if (strclassNo != null && strclassNo.isEmpty() == false) {
			classNo = Integer.parseInt(strclassNo);
		}
		return enrollMapper.scheduleByClassNo(classNo);
	}
	
	// 사용자 조회 -1
	@Override
	public UserDTO selectUserByNo(HttpServletRequest request) {
		int userNo =  Integer.parseInt(request.getParameter("userNo"));
		return enrollMapper.selectUserByNo(userNo);
	}
	
	// 사용자 조회 -2
	@Override
	public UserDTO getUser(int userNo) {
		return enrollMapper.selectUserByNo(userNo);	
	}
	
	// 쿠폰 조회
	@Override
	public List<CouponDTO> selectCoupon(HttpServletRequest request) {
		int userNo =  Integer.parseInt(request.getParameter("userNo"));
		List<CouponDTO> couponList = enrollMapper.couponListByNo(userNo);
		System.out.println("하이~~~~~~~" + couponList);
		return couponList;
	}

	/* 수 강 신 청 */
	@Transactional
	@Override
	public void applyClass(HttpServletRequest request) {
		
		// 파라미터1: userID 통해서 UserDTO 가져오기
		int userNo =  Integer.parseInt(request.getParameter("userNo"));
		UserDTO userDTO = enrollMapper.selectUserByNo(userNo);
		
		// 파라미터2: schNo 통해서 ScheduleDTO 가져오기
		int schNo = Integer.parseInt(request.getParameter("schNo"));
		
		// 파라미터3: 신청인원 enrollPerson
		int enrollPerson = Integer.parseInt(request.getParameter("enrollPerson"));
		
		// 파라미터4: 요청사항 enrollRequest
		String enrollRequest = request.getParameter("enrollRequest");
		
	// 작가님과 협의 Schedule 선택 시
		ScheduleDTO schduleDTO = new ScheduleDTO();
		
		// 파라미터 : 클래스번호 classNo
		int classNo = Integer.parseInt(request.getParameter("classNo"));
		ClassListDTO classListDTO = enrollMapper.selectClassByNo(classNo);
		Date date = new Date();
		
		if(schNo == 0) {
			schduleDTO.setSchNo(schNo);
			schduleDTO.setClassListDTO(classListDTO);
			schduleDTO.setSchStart(date);
			schduleDTO.setSchNowNum(0);
			schduleDTO.setSchMaxNum(0);
			schduleDTO.setSchState(0);	
			
			// EnrollDTO에 담기
			EnrollDTO enrollDTO = new EnrollDTO();
			enrollDTO.setUserDTO(userDTO);
			enrollDTO.setScheduleDTO(schduleDTO);
			enrollDTO.setEnrollPerson(enrollPerson);
			enrollDTO.setEnrollRequest(enrollRequest);
			
			enrollMapper.applyClass(enrollDTO);

		} else {
	
	// 일반적인 Schedule 선택 시
			schduleDTO = enrollMapper.selectScheduleByNo(schNo);
			
			// EnrollDTO에 담기
			EnrollDTO enrollDTO = new EnrollDTO();
			enrollDTO.setUserDTO(userDTO);
			enrollDTO.setScheduleDTO(schduleDTO);
			enrollDTO.setEnrollPerson(enrollPerson);
			enrollDTO.setEnrollRequest(enrollRequest);
			
			enrollMapper.applyClass(enrollDTO);	
		}
		
	}
	
}
