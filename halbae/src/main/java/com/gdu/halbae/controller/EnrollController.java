package com.gdu.halbae.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.service.EnrollService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/enroll")
@RequiredArgsConstructor
@Controller
public class EnrollController {

	private final EnrollService enrollService;

	/* 신청 페이지 */
	// 클래스 정보와, 스케줄 정보 뿌림
	@RequestMapping("/apply.page")
	public String applyPage(HttpServletRequest request, Model model) {
		model.addAttribute("classList", enrollService.classListByNo(request));
		model.addAttribute("scheduleList", enrollService.scheduleByClassNo(request));
		return "enroll/apply";
	}
	
	// 회원 전화번호
    @GetMapping("/user.do")
    @ResponseBody
    public UserDTO getUserTel(@RequestParam("userNo") Integer userNo) {
        UserDTO userDTO = enrollService.getUser(userNo);
        return userDTO;
    }
	
    
    // 수강 신청
    @PostMapping("/apply.do")
    public String applyClass(HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes) {
    	enrollService.applyClass(request);
    	redirectAttributes.addAttribute("schNo", request.getParameter("schNo"));
    	session.setAttribute("classList", enrollService.classListByNo(request));
    	session.setAttribute("enrollPerson", request.getParameter("enrollPerson"));
    	session.setAttribute("user", enrollService.selectUserByNo(request));
    	session.setAttribute("couponList", enrollService.selectCoupon(request));
    	return "redirect:/enroll/payment.page";
    }
    
	/* 결제 페이지 */
	@RequestMapping("/payment.page")
	public String payPage(HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("schNo", request.getParameter("schNo"));
		model.addAttribute("classList", session.getAttribute("classList"));
		model.addAttribute("enrollPerson", session.getAttribute("enrollPerson"));
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("couponList", session.getAttribute("couponList"));
		model.addAttribute("payCoupon", request.getParameter("payCoupon"));
		model.addAttribute("payPoint", request.getParameter("payPoint"));
		return "enroll/payment";
	}
	
}
	
