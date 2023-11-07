package com.gdu.halbae.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.ScheduleDTO;
import com.gdu.halbae.domain.UserDTO;

@Mapper
public interface UserMapper {
	// 회원가입
	public UserDTO checkUniqueId(String userId);
	public int checkIdCountByTel(String userId);
	public int insertUser(UserDTO userDTO);
	//로그인
	public UserDTO selectLoginInfo(Map<String, Object> map);
	public int insertAutoLogin(UserDTO userDTO);
	public int deleteAutoLogin(String userId);
	// 자동로그인
	public UserDTO selectUserDTOByAutoLoginId(String userAutoLoginId);
	// 전화번호로 아이디 찾기
	public List<UserDTO> selectUserByTel(String userTel);
	// 임시 비번으로 바꾸기
	public int updateTempPw(UserDTO userDTO);
	
	// 프로필 정보 가져오기
	public UserDTO selectUserProfile(String loginId);
	// 프로필 변경하기
	public int updateProfile(UserDTO userDTO);
	
	// 비번 변경 전 현재 비번 가져오기
	public String selectUserPwById(String userId);
	// 비번 변경하기
	public int updateUserPwById(UserDTO userDTO);
	// 회원 탈퇴하기
	public int deleteUser(String userId);
	
	// 사용중인 프로필 목록 가져오기 
	public List<String> selectUsedProfile();
	
	// 수강 목록 가져오기
	public List<Integer> selectSchNoByUserNo(int userNo);
	public int selectClassNoBySchNo(int schNo);
	public ClassListDTO selectClassByClassNo(int classNo);
	public ScheduleDTO selectSchBySchNo(int schNo);
}