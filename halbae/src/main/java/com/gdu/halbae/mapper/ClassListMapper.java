package com.gdu.halbae.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.ClassListDTO;

@Mapper
public interface ClassListMapper {
	
	public List<ClassListDTO> selectAllClass();
	
	public int getClassListCount(String classCategory);						// 전체/카테고리 클래스
	public List<ClassListDTO> selectClassList(Map<String, Object> map);		// 전체/카테고리 클래스
	public int getClassListCountAll();										// 전체 클래스
	public int getClassListCountNew();										// 최신 클래스
	public List<ClassListDTO> selectClassListNew(Map<String, Object> map);	// 최신 클래스
	public ClassListDTO selectClassByNo(int classNo);						// 디테일 클래스
	
	public int insertClass(ClassListDTO classListDTO);						// 클래스 등록
	public ClassListDTO getClassByNo(int classNo);							// 이미지 뽑을때 클래스 번호주고 해당 HDD저장되어있는 imgMainPath 가져오는 코드
	public List<Integer> selectClassUploadList(int userNo);					// 클랙스 등록 목록 userNo주고 classNo받아오는 코드
	public List<ClassListDTO> selectUploadList(List<Integer> uploadList);	// 다 뽑은 classNo를 전달해서 classList 테이블에 컬럼 값을 가져오는 코드!
	public int removeClass(int classNo);									// 등록한 클래스 삭제
	public int modifyClass(ClassListDTO classListDTO);						// 클래스 수정
}
