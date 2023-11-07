package com.gdu.halbae.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;
import com.gdu.halbae.domain.ScheduleDTO;
import com.gdu.halbae.domain.UserDTO;

@Mapper
public interface EnrollMapper {
	
	/* 조 회 */
	// 강의 조회
	public ClassListDTO selectClassByNo(int classNo);
	
	// 해당 강의 스케줄 조회
	public List<ScheduleDTO> scheduleByClassNo(int classNo);
	
	// 스케줄 조회
	public ScheduleDTO selectScheduleByNo(int schNo);
	
	// 회원 조회
	public UserDTO selectUserByNo(int userNo);
	
	// 수강신청 조회
	public EnrollDTO selectEnroll(int enrollNo);
	
	// 쿠폰 조회
	public List<CouponDTO> couponListByNo(int userNo);
	
	/* 수 강 신 청 */
	// 수강 신청
	public int applyClass(EnrollDTO enrollDTO);
	// 스케줄 인원 반영
	public int updateSchNowNum(EnrollDTO enrollDTO);
	// 스케줄 상태 변경
	public int updateSchState(EnrollDTO enrollDTO);
	
}
