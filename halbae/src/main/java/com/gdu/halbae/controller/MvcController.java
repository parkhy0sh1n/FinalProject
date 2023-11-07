package com.gdu.halbae.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.halbae.service.ClassListService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MvcController {
	
	private final ClassListService classListService;

	@GetMapping("/")
	public String welcome(HttpServletRequest request, Model model) {
		classListService.getMainList(request, model);
		return "main";
	}
	
}