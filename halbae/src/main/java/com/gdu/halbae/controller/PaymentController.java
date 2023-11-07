package com.gdu.halbae.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.domain.EnrollDTO;
import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.service.EnrollService;
import com.gdu.halbae.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/payment")
@RequiredArgsConstructor
@Controller
public class PaymentController {
	
	private final PaymentService paymentService;
	private final EnrollService enrollService;
	
	
	/* 포인트 사용 */
	@GetMapping("/point.do")
    public String usePoint(HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("classList", session.getAttribute("classList"));
		model.addAttribute("enrollPerson", session.getAttribute("enrollPerson"));
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("couponList", session.getAttribute("couponList"));
		
		return "enroll/payment";
    }
	
	/* 쿠폰 사용 */
	@GetMapping("/useCoupon")
	public String useCoupon(@RequestParam("payCoupon") String payCoupon, HttpSession session, Model model) {
		model.addAttribute("classList", session.getAttribute("classList"));
		model.addAttribute("enrollPerson", session.getAttribute("enrollPerson"));
		model.addAttribute("user", session.getAttribute("user"));
		model.addAttribute("couponList", session.getAttribute("couponList"));
		
	    return "enroll/payment";
	}
	
	/* 쿠폰 번호 조회 */
	@GetMapping("/couponCalculate")
	@ResponseBody
	public CouponDTO getCouponDC(@RequestParam("couponNo") Integer couponNo) {
		CouponDTO couponDTO = paymentService.selectCouponByNo(couponNo);
		return couponDTO;
	}
	
	/* 결제하기 창 (신청번호 & 회원번호 조회) */
	@PostMapping("/pay.do")
	@ResponseBody
	public Map<String, Object> pay(@RequestParam("userNo") Integer userNo){
		EnrollDTO enrollDTO = paymentService.selectEnrollNo(userNo);
		UserDTO userDTO = enrollService.getUser(userNo);
		
		Map<String, Object> map = new HashMap<>();
		map.put("enrollDTO", enrollDTO);
		map.put("userDTO", userDTO);
		
		return map;	 
	}
	
	/* 신용카드 결제 */
	// 결제 실행
	@PostMapping(value="/payEnroll.do", produces="application/json")
	@ResponseBody
	public Map<String, Object> payEnroll(HttpServletRequest request) {
		paymentService.insertPayment(request);
		Map<String, Object> map = new HashMap<>();
		String enrollNo = request.getParameter("enrollNo");
		map.put("enrollNo", enrollNo);
		return map;
	}

	// 결제 완료
	@RequestMapping(value = "/paySuccess.page", method = {RequestMethod.GET, RequestMethod.POST})
	public String paySuccess(@RequestParam("enrollNo") int enrollNo, Model model) {
		
		// 주문번호 만들기 (랜덤번호 + 주문년월일)
		String randomNum = Math.random() + "";
		randomNum += LocalDate.now() + "";
		
		// 소수 부분만 남기고, - 없앤 후, 2023 대신 -23으로
		int dotIndex = randomNum.indexOf(".");  
		String noDotRandomNum = randomNum.substring(dotIndex + 1);
		noDotRandomNum = noDotRandomNum.replace("-", "");
		String changeNum = noDotRandomNum.replace("20", "-");
		model.addAttribute("orderNumber", changeNum);
		
		// enrollNo으로 유저와 스케줄 가져오기
		model.addAttribute("map", paymentService.selectClassAndSch(enrollNo));
		
		return "enroll/paySuccess";	
	}
	
	
	/* 결제 취소 */
	@GetMapping("/payFail.do")
	public String payFail(HttpServletRequest request) {
		paymentService.deleteEnroll(request);
		System.out.println("여기까지 왔다~!");
		return "redirect:/class/classDetail.do?classNo=" + request.getParameter("classNo");
	}
	
}