package com.gdu.halbae.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface WishService {
	
	public Map<String, Object> getWishByNo(HttpServletRequest request);	// 클래스 리스트에 찜목록 삽입/삭제
	public int getWishRemoveByNo(HttpServletRequest request);			// 찜목록안에 삭제
	public void getWishList(HttpServletRequest request, Model model);	// 찜목록 리스트

}
