package com.gdu.halbae.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.halbae.service.PointService;

@Controller
public class PointController {
	
    @Autowired
    private PointService pointService;

    @GetMapping("/user/point")
    public String getUserPoint(Model model, HttpSession session) {
        int userNo = (int) session.getAttribute("userNo"); // 세션에서 사용자 번호 가져오기
        int userPoint = pointService.getUserPoint(userNo); // 사용자 포인트 가져오기
        model.addAttribute("userPoint", userPoint); // userPoint 속성을 모델에 추가
        return "point/pointForm"; // pointForm.html 템플릿 이름 반환
    }
    
    // 포인트 사용
    @GetMapping("/user/pointUse")
    public String getUserPointUse(HttpServletRequest request, Model model, HttpSession session) {
    	int userNo = (int) session.getAttribute("userNo"); // 세션에서 사용자 번호 가져오기
    	int userPoint = pointService.getUserPoint(userNo); // 사용자 포인트 가져오기
    	model.addAttribute("userPoint", userPoint); // userPoint 속성을 모델에 추가
    	
    	model.addAttribute("payCoupon", request.getParameter("payCoupon")); // 쿠폰 번호
    	model.addAttribute("schNo", request.getParameter("schNo"));			// 스케줄 번호
    
    	return "point/pointUse"; // pointUse.html 템플릿 이름 반환	
    }
    
}