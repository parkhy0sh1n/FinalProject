package com.gdu.halbae.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.halbae.service.WishService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/wish")
@Controller
public class WishController {
	
	// field
	private final WishService wishService;
	
	// 안녕
	
	// 찜목록 이동 페이지
	@GetMapping("/wish.html")
	public String wish() {
		return "classlist/wish";
	}
	
	// 클래스리스트에 찜목록 삽입/삭제
	@ResponseBody
	@GetMapping(value="/wish.do", produces="application/json")
	public Map<String, Object> addWish(HttpServletRequest request) {
		return wishService.getWishByNo(request);
	}
	
	// 찜목록 페이지 삭제
	@GetMapping("/removeWish.do")
	public String removeWish(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		int removeResult = wishService.getWishRemoveByNo(request);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/wish/listWish.do";
	}
	
	// 찜목록 리스트
	@GetMapping("/listWish.do")
	public String listWish(HttpServletRequest request, Model model) {
		wishService.getWishList(request, model);
		return "classlist/wish";
	}
	
	
	
	
	

}