package com.gdu.halbae.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/enrollList.do")
	public String enrollList(HttpServletRequest request, Model model) {
		userService.getEnrollList(request, model);
		return "user/enrollList";
	}
	
	@GetMapping("/login.html")
	public String login(HttpServletRequest request, Model model) {
		model.addAttribute("naverApiURL", userService.getNaverLoginApiURL(request));
		model.addAttribute("kakaoApiURL", userService.getKakaoLoginApiURL(request));
		return "user/login";
	}
	
	@GetMapping("/naver/login.do")
	public String naverLogin(HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response, Model model) {
		String accessToken = userService.getNaverLoginToken(request);
		
		UserDTO profile = userService.getNaverLoginProfile(accessToken);
		
		UserDTO naverUser = userService.getUserById(profile.getUserId());;
		
		if(naverUser == null) {
			userService.insertUser(profile);
			redirectAttributes.addFlashAttribute("alertPw", "초기 비밀번호는 연락처 8자리입니다.");
		}
		naverUser = userService.getUserById(profile.getUserId());;
		userService.naverLogin(request, response, naverUser);
		return "redirect:/";
	}
	
	@GetMapping("/kakao/login.do")
	public String kakaoLogin(HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response, Model model) {
		String accessToken = userService.getKakaoLoginToken(request);
		
		UserDTO profile = userService.getKakaoLoginProfile(accessToken);
		
		UserDTO kakaoUser = userService.getUserById(profile.getUserId());
		
		if(kakaoUser == null) {
			userService.insertUser(profile);
			redirectAttributes.addFlashAttribute("alertPw", "초기 비밀번호는 아이디와 동일합니다. (@ 이후 제외)");
		}
		kakaoUser = userService.getUserById(profile.getUserId());
		userService.naverLogin(request, response, kakaoUser);
		
		
		return "redirect:/";
	}
	
	// 회원 가입
	@GetMapping("/join.html")
	public String join() {
		return "user/join";
	}
	
	@PostMapping("/agree.html")
	public String agree(UserDTO userDTO, Model model, RedirectAttributes redirectAttributes) {
		String unqMsg = "";
					 unqMsg += userService.checkUniqueId(userDTO);
					 unqMsg += userService.checkIdCountByTel(userDTO);
		if(unqMsg.isEmpty()==false) {
			redirectAttributes.addFlashAttribute("unqMsg", unqMsg);
			return "redirect:/user/join.html";
		}
		model.addAttribute("userDTO", userDTO);
		return "user/agree";
	}
	
	@PostMapping("/successJoin.do")
	public String successJoin(UserDTO userDTO) {
		userService.insertUser(userDTO);
		return "user/successjoin";
	}
	
	// 로그인
	@PostMapping("/login.do")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		userService.login(request, response);
	}
	
	// 로그아웃
	@PostMapping("/logout.do")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
			userService.logout(request, response);
	}
	
	// 아이디 찾기
	@GetMapping("/findId.html")
	public String toFindId() {
		return "user/findId";
	}
	// 가입한 아이디 조회 후 내역 보여주기
	@PostMapping("/findId.do")
	public String findId(String userTel, Model model) {
		model.addAttribute("userDTOList",userService.selectUserIdByTel(userTel));
		return "user/findIdResult";
	}
	// 비밀번호 찾기
	@GetMapping("/findPw.html")
	public String toFindPw() {
		return "user/findPw";
	}
	// 인증코드 전송하기
	@ResponseBody
	@GetMapping(value="/sendCode.do", produces="application/json")
	public Map<String, Object> sendCode(String email) {
		return userService.sendCode(email);
	}
	
	// 임시 비밀번호 전송하기
	@ResponseBody
	@GetMapping(value="/sendTempPw.do", produces="application/json")
	public Map<String, Object> sendTempPw(String email) {
		return userService.sendTempPw(email);
	}
	
	// 전송된 임시 비번으로 유저 비번 바꾸기
	@PostMapping("/tempPw.do")
	public void updateTempPw(UserDTO userDTO, HttpServletResponse response) {
		userService.updateTempPw(userDTO, response);
	}
	// 마이 프로필로 이동
	@GetMapping("/myProfile.html")
	public String myProfile(String loginId, Model model) {
		model.addAttribute("userDTO", userService.selectUserProfile(loginId));
		return "user/myprofile";
	}
	// 프로필 사진 바꾸기
	@ResponseBody
	@PostMapping(value="/updateProfile.do", produces="application/json")
	public Map<String, Object> updateProfile(MultipartHttpServletRequest multipartRequest) {
		return userService.updateProfileImg(multipartRequest);
	}
	@GetMapping("/display.do")
	public ResponseEntity<byte[]> display(String userImgPath) {
		return userService.displayProfile(userImgPath);
	}
	
	// 변경사항 적용하기
	@PostMapping("/editProfile.do")
	public void editProfile(UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
		userService.editProfile(userDTO, request, response);
	}
	
	// 비번 바꾸기로 이동
	@GetMapping("/modifyPw.html")
	public String ToModifyPw(String userId, Model model)  {
		model.addAttribute("userId", userId);
		return "user/modifyPw";
	}
	
	// 현재 비밀번호 확인
	@ResponseBody
	@PostMapping(value="/confirmPw.do", produces="application/json")
	public Map<String, Object> confirmPw(UserDTO userDTO) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("confirmResult", userService.confirmPw(userDTO));
		
		return map;
	}
	
	// 비밀번호 변경하기
	@PostMapping("/modifyPw.do")
	public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
		userService.updateUserPwById(request, response);
	}
	
	// 회원 탈퇴
	@PostMapping("/deleteUser.do")
	public void deleteAccount(HttpServletRequest request, HttpServletResponse response) {
		userService.deleteUser(request, response);
	}
	
}