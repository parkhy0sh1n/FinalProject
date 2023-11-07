package com.gdu.halbae.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.halbae.domain.ClassListDTO;

public interface ClassListService {
	
	public void getMainList(HttpServletRequest request, Model model);
	
	public void getClassList(HttpServletRequest request, Model model);		// 전체/카테고리 클래스
	public void getClassListNew(HttpServletRequest request, Model model);	// 최신 클래스
	public ClassListDTO getClassByNo(HttpServletRequest request, Model model);			// 디테일 클래스
	
	public int addClass(MultipartHttpServletRequest multipartHttpServletRequest);	// 클래스 등록(파일)
	public ResponseEntity<byte[]> display(int classNo);								// 클래스 등록한 이미지 뽑는 메소드(메인)
	public ResponseEntity<byte[]> displayDetail(int classNo);						// 클래스 등록한 이미지 뽑는 메소드(상세) 
	public void getClassUploadList(HttpServletRequest request, Model model);		// 클래스 등록 목록
	public int getClassUploadRemove(HttpServletRequest request);					// 등록한 클래스 삭제
	public void getClassEdit(HttpServletRequest request, Model model);				// 등록한 클래스 편집화면(값 화면에 뿌리기)
	public int modifyClass(MultipartHttpServletRequest multipartHttpServletRequest);// 등록한 클래스 수정
}
