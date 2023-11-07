package com.gdu.halbae.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.halbae.domain.CouponDTO;
import com.gdu.halbae.service.CouponService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CouponController {
	
    private final CouponService couponService;
    
    // 회원이 보유한 쿠폰 목록과 보유 쿠폰 수 조회
    @GetMapping("/coupon")
    public String getCouponPage(Model model, HttpSession session, @RequestParam(value = "userNo", required = false) Integer userNo) {
        if (userNo == null) {
            userNo = (int) session.getAttribute("userNo");
        }
        List<CouponDTO> userCoupons = couponService.getAllCoupons(userNo);
        int couponCount = couponService.getAvailableCouponCount(userNo);
        model.addAttribute("coupons", userCoupons);
        model.addAttribute("couponCount", couponCount);
        return "coupon/couponForm";
    }
    
    // 쿠폰 등록
    @PostMapping(value="/issueCoupon", produces="application/json")
    @ResponseBody
    public Map<String, Object> issueCoupon(String couponName, HttpSession session) {
        int userNo = (int) session.getAttribute("userNo");
        Map<String, Object> map = new HashMap<>();
        map.put("isSuccess", couponService.issueCouponToUser(couponName, userNo) == 1);
        return map;
    }
    
    // 쿠폰 사용
    @GetMapping("/couponUse")
    public String getCouponUsePage(Model model, HttpSession session, @RequestParam(value = "userNo", required = false) Integer userNo
    															   , @RequestParam(value = "payPoint", required = false) String payPoint
    															   , @RequestParam(value= "schNo", required = false) String schNo) {
        if (userNo == null) {
            userNo = (int) session.getAttribute("userNo");
            }
        List<CouponDTO> userCoupons = couponService.getAllCoupons(userNo);
        int couponCount = couponService.getAvailableCouponCount(userNo);
        model.addAttribute("coupons", userCoupons);
        model.addAttribute("couponCount", couponCount);
        model.addAttribute("payPoint", payPoint);
        model.addAttribute("schNo", schNo);
        return "coupon/couponUse";
    }
}