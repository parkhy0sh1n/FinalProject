package com.gdu.halbae.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.ScheduleDTO;
import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.mapper.UserMapper;
import com.gdu.halbae.util.JavaMailUtil;
import com.gdu.halbae.util.ProfileUtil;
import com.gdu.halbae.util.SecurityUtil;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserMapper userMapper;
	private final SecurityUtil securityUtil;
	private final JavaMailUtil javaMailUtil;
	private final ProfileUtil profileUtil;
	
	// 수강목록 가져오기
	@Override
	public void getEnrollList(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		int userNo = (Integer)session.getAttribute("userNo");

		List<Integer> schNos = userMapper.selectSchNoByUserNo(userNo);
		List<Integer> classNos = new ArrayList<>();
		List<ClassListDTO> enrollList = new ArrayList<>();
		for(int schNo : schNos) {
			classNos.add(userMapper.selectClassNoBySchNo(schNo));
		}
		for(int classNo : classNos) {
			enrollList.add(userMapper.selectClassByClassNo(classNo));
		}
		model.addAttribute("enrollList", enrollList);
		
		List<ScheduleDTO> scheduleList = new ArrayList<>();
		for(int schNo : schNos) {
			scheduleList.add(userMapper.selectSchBySchNo(schNo));
		}
		model.addAttribute("schduleList", scheduleList);
		
		
	}
	
	// 회원가입
	@Override 
	public String checkUniqueId(UserDTO userDTO) {
		String msg = "";
		if(userMapper.checkUniqueId(userDTO.getUserId()) != null) {
			msg="이미 사용중인 이메일입니다.";
			return msg;
		}
		return msg;
	}
	@Override
	public String checkIdCountByTel(UserDTO userDTO) {
		String msg = "";
		if(userMapper.checkIdCountByTel(userDTO.getUserTel()) >= 3) {
			msg="입력하신 연락처로 더 이상 계정 생성이 불가능합니다. (최대 3개)";
			return msg;
		}
		return msg;
	}
	
	@Override
	public int insertUser(UserDTO userDTO) {
		String userName = userDTO.getUserName();
		String userTel = userDTO.getUserTel();
		String userId = userDTO.getUserId();
		String userPw = userDTO.getUserPw();
		
		// 스크립트 방지
		userName = securityUtil.preventXSS(userName);
		userTel = securityUtil.preventXSS(userTel);
		userId = securityUtil.preventXSS(userId);
		userPw = securityUtil.preventXSS(userPw);
		// 비번 암호화
		userPw = securityUtil.getSha256(userPw);
		// 보안 처리 완료한 정보로 바꾸기
		userDTO.setUserName(userName);
		userDTO.setUserTel(userTel);
		userDTO.setUserId(userId);
		userDTO.setUserPw(userPw);
		if(userDTO.getUserImgPath() == null) {
			userDTO.setUserImgPath("/images/user/default_profile.jpeg");
		}
		
		return userMapper.insertUser(userDTO);
	}
	
	// 로그인
	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		
		String userId = request.getParameter("userId");
		String userPw = request.getParameter("userPw");
		
		userPw = securityUtil.getSha256(userPw);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("userId", userId);
		map.put("userPw", userPw);
		
		UserDTO userDTO = userMapper.selectLoginInfo(map);

		if(userDTO != null) {
			// 자동 로그인 처리하기
			autoLogin(request, response);
			HttpSession session = request.getSession();
			// 세션에 정보 저장
			// 추천 : session.setAttribute("loginUser", userDTO);   ${session.loginUser.userNo}
			session.setAttribute("loginId", userId);
			session.setAttribute("userNo", userDTO.getUserNo());
			session.setAttribute("userName", userDTO.getUserName());
			session.setAttribute("userPoint", userDTO.getUserPoint());
			session.setAttribute("userImgPath", userDTO.getUserImgPath());

			// 로그인 후 메인으로 이동
			try {
				response.sendRedirect("/");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			response.setContentType("text/html; charset=UTF-8");
			try {
		    PrintWriter out = response.getWriter();
		    out.println("<script>");
		    out.println("alert('존재하지 않는 계정입니다.');");
		    out.println("location.href='" + request.getContextPath() + "/user/login.html'");
		    out.println("</script>");
		    out.flush();
		    out.close();
				} catch (IOException e) {
				    e.printStackTrace();
				}
		}
		
	}
	
	// 자동 로그인
	@Override
	public void autoLogin(HttpServletRequest request, HttpServletResponse response) {
		
		String userId = request.getParameter("userId");
		String chkAutoLogin = request.getParameter("chkAutoLogin");
		
		if(chkAutoLogin != null) {
			HttpSession session = request.getSession();
			String sessionId = session.getId();
			
			UserDTO userDTO = new UserDTO();
			userDTO.setUserId(userId);
			userDTO.setUserAutoLoginId(sessionId);
			userDTO.setUserAutoLoginExpired(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15))); // 15일
			
			userMapper.insertAutoLogin(userDTO);
			
			Cookie cookie = new Cookie("autoLoginId", sessionId);
			cookie.setMaxAge(60 * 60 * 24 * 15); // 15일
			cookie.setPath("/");
			response.addCookie(cookie);
			
		}else {
			userMapper.deleteAutoLogin(userId);
			Cookie cookie = new Cookie("autoLoginId", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
	
	// 로그아웃
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String loginId = request.getParameter("loginId");
		userMapper.deleteAutoLogin(loginId);
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		Cookie cookie = new Cookie("autoLoginId", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 인증코드 보내기
	@Override
	public Map<String, Object> sendCode(String email) {
		String authCode = securityUtil.getRandomString(5, true, true);
		
		String content = "";
					content += "<h1>하루배움에서 인증코드를 발송했습니다</h1>";
					content += "<div>인증코드</div>";
					content += "<input readonly value='" + authCode + "'>";
		
		javaMailUtil.sendJavaMail(email, "[하루배움] 인증코드 발송해드립니다.", content);
		
		Map<String, Object> map = new HashMap<>();
		map.put("authCode", authCode);
		return map;
	}
	// 임시 비번 보내기
	@Override
	public Map<String, Object> sendTempPw(String email) {
		String tempPw = securityUtil.getRandomString(10, true, true);
		
		String content = "";
		content += "<h1>하루배움에서 임시 비밀번호를 발송했습니다</h1>";
		content += "<div>임시 비밀번호</div>";
		content += "<input readonly value='" + tempPw + "'>";

		javaMailUtil.sendJavaMail(email, "[하루배움] 임시 비밀번호 발송해드립니다.", content);
		
		System.out.println("비밀번호 전송 완료");
		
		Map<String, Object> map = new HashMap<>();
		map.put("tempPw", tempPw);
		return map;
	}
	// 임시 비번으로 비번 바꾸기
	@Override
	public void updateTempPw(UserDTO userDTO, HttpServletResponse response) {
		String tempPw = securityUtil.getSha256(userDTO.getUserPw());
		userDTO.setUserPw(tempPw);
		int updateResult = userMapper.updateTempPw(userDTO);
		response.setContentType("text/html; charset=UTF-8");
		
		if(updateResult == 1) {
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("if(confirm('임시 비밀번호가 발급되었습니다. 로그인하러 가시겠습니까?')) {");
				out.println("location.href='/user/login.html';");
				out.println("} else {");
				out.println(" location.href='/';");
				out.println("}");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('임시 비밀번호 발급이 실패했습니다. 아이디를 확인해주세요.');");
				out.println("location.href='/user/findPw.html';");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	// 전화번호로 가입 아이디 조회
	@Override
		public List<UserDTO> selectUserIdByTel(String userTel) {
		return userMapper.selectUserByTel(userTel);
		}	
	// 유저 프로필 가져오기
	@Override
	public UserDTO selectUserProfile(String loginId) {
		return userMapper.selectUserProfile(loginId);
	}
	
	// 프로필 변경사항 적용하기
	@Override
	public void editProfile(UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
		
		int result = userMapper.updateProfile(userDTO);
		
		HttpSession session = request.getSession();
		session.setAttribute("userName", userDTO.getUserName());
		session.setAttribute("userImgPath", userDTO.getUserImgPath());
		
		response.setContentType("text/html; charset=UTF-8");
		if(result == 1) {
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('변경 사항이 적용되었습니다.');");
				out.println("location.href='/'");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('프로필 변경이 실패했습니다.');");
				out.println("location.href='/'");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	// 비밀번호 변경하기
	@Override
	public boolean confirmPw(UserDTO userDTO) {
		String userId = userDTO.getUserId();
		String userPw = securityUtil.getSha256(userDTO.getUserPw());

		if(userPw.equals(userMapper.selectUserPwById(userId))) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void updateUserPwById(HttpServletRequest request, HttpServletResponse response) {
		String userId = request.getParameter("userId");
		String userPw = securityUtil.getSha256(request.getParameter("userPw"));
		
		UserDTO userDTO = new UserDTO();
		userDTO.setUserId(userId);
		userDTO.setUserPw(userPw);
		
		if(userMapper.updateUserPwById(userDTO) == 1) {
			
			userMapper.deleteAutoLogin(userDTO.getUserId());
			HttpSession session = request.getSession();
			
			session.invalidate();
			
			Cookie cookie = new Cookie("autoLoginId", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			response.setContentType("text/html; charset=UTF-8");
			
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호가 변경되었습니다. 다시 로그인 해주세요.');");
				out.println("location.href='/'");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호 변경이 실패했습니다. 다시 시도해주세요.');");
				out.println("location.href='/'");
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	@Override
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) {

		String userId = request.getParameter("userId");
		
		if(userMapper.deleteUser(userId) == 1) {
			userMapper.deleteAutoLogin(userId);
			HttpSession session = request.getSession();
			
			session.invalidate();
			
			Cookie cookie = new Cookie("autoLoginId", "");
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			response.setContentType("text/html; charset=UTF-8");
			
			try {
				PrintWriter out = response.getWriter();
				
				out.println("<script>");
				out.println("alert('하루배움을 이용해주셔서 감사합니다.');");
				out.println("location.href='/';");
				out.println("</script>");
				out.flush();
				out.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Map<String, Object> updateProfileImg(MultipartHttpServletRequest multipartRequest) {
		
		String sep = Matcher.quoteReplacement(File.separator);
		
		Map<String, Object> map = new HashMap<>();
		
		MultipartFile profile = multipartRequest.getFile("profile");
		
		try {
			
			if(profile != null && profile.isEmpty() == false) {
				
				String path = profileUtil.getPath();
				
				File dir = new File(path);
				if(dir.exists() == false) {
					dir.mkdirs();
				}
				
				String imgOriginName = profile.getOriginalFilename();
				
				imgOriginName = imgOriginName.substring(imgOriginName.lastIndexOf("\\") + 1);
				
				String userImgFileName = profileUtil.getFilesystemName(imgOriginName);
				
				File file = new File(dir, userImgFileName);
				
				profile.transferTo(file);
				
				String userImgPath = path + sep + userImgFileName;
				
				map.put("display", "/user/display.do?userImgPath=" + userImgPath);
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public ResponseEntity<byte[]> displayProfile(String userImgPath) {
		ResponseEntity<byte[]> img = null;
		
		File profile = new File(userImgPath);
		try {
			img = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(profile), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	// 미사용 프로필 지우기
	@Override
	public void removeUnusedProfile() {
		List<String> profiles = userMapper.selectUsedProfile();		
		String path = profileUtil.getPath();
		
		File dir = new File(path);
		
		// 프로필 저장 경로의 모든 파일 배열
		File[] files = dir.listFiles();
		
		if(files != null) {
			
			for(File file : files) {
				
				String fileName = file.getName();
				boolean keepFile = false;
				
				for(String profile : profiles) {
					profile = profile.substring(profile.lastIndexOf("/") + 1);
					System.out.println("사용 프로필 " + profile);
					if(profile.equals(fileName)) {
						keepFile = true;
						break;
					}
				}
				
				if(keepFile == false) {
					boolean delete = file.delete();
					if(delete) {
						System.out.println("파일 삭제 :" + fileName);
					}else {
						System.out.println("삭제 실패 :" + fileName);
					}
				}
				
			}
		}
	}
	
	/**********************************************/
	/**********************************************/
	/************ 네이버 로그인 API **************/
	/**********************************************/
	/**********************************************/
	
  private static final String CLIENT_ID = "D9tF3QxxsI_fgIeTvWuG";
  private static final String CLIENT_SECRET = "VL5ywVPQTM";
	
	@Override
	public String getNaverLoginApiURL(HttpServletRequest request) {

		String apiURL = "";
		
		try {
			String redirectURL = URLEncoder.encode("http://localhost:8080/user/naver/login.do", "UTF-8");
			SecureRandom secureRandom = new SecureRandom();
			String state = new BigInteger(130, secureRandom).toString();
			
			apiURL += "https://nid.naver.com/oauth2.0/authorize?response_type=code";
			apiURL += "&client_id=" + CLIENT_ID;
			apiURL += "&redirect_uri=" + redirectURL;
			apiURL += "&state=" + state;
			
			HttpSession session = request.getSession();
			session.setAttribute("state", state);
		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return apiURL;
	}
	
	@Override
	public String getNaverLoginToken(HttpServletRequest request) {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String redirectURI = null;
		try {
			redirectURI = URLEncoder.encode("http://localhost:8080/", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		StringBuffer res = new StringBuffer();
		try {
			String apiURL;
	    apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + CLIENT_ID;
	    apiURL += "&client_secret=" + CLIENT_SECRET;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
	    apiURL += "&state=" + state;
	    
	    URL url = new URL(apiURL);
	    HttpURLConnection con = (HttpURLConnection)url.openConnection();
	    con.setRequestMethod("GET");
	    int responseCode = con.getResponseCode();
	    BufferedReader br;
	    
	    if(responseCode == 200)	 {
	    	br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    }else {
	    	br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	    }
	    
	    String inputLine;
	    while((inputLine = br.readLine()) != null) {
	    	res.append(inputLine);
	    }
	    br.close();
	    con.disconnect();
	    
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(res.toString());
		String accessToken = obj.getString("access_token");
		
		return accessToken;
	}
	
	@Override
	public UserDTO getNaverLoginProfile(String accessToken) {

		String header = "Bearer " + accessToken;
		StringBuffer sb = new StringBuffer();

		try {
			String apiURL = "https://openapi.naver.com/v1/nid/me";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", header);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			while((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			con.disconnect();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		UserDTO userDTO = new UserDTO();
		try {
			JSONObject profile = new JSONObject(sb.toString()).getJSONObject("response");
			/* 가져온 데이터
			  {"id":"2wVtKX5WoKnsPtPEueEykYS-WKW8PzggkGnMUzgbOho",
			  "nickname":"\uc870\uc6b0\ubbfc",
			  "profile_image":"https:\/\/phinf.pstatic.net\/contact\/20220518_74\/1652883673112JElr8_JPEG\/%C0%AF%C6%A9%BA%EA%C7%C1%B7%CE%C7%CA.jpg",
			  "email":"umin5056@naver.com",
				"mobile":"010-5056-5439"
			*/
			String userImgPath = profile.getString("profile_image").replace("\\", "");
			String userTel = profile.getString("mobile").replace("-", "");
			String userPw = userTel.substring(3); 
			
			userDTO.setUserName(profile.getString("nickname"));
			userDTO.setUserTel(userTel);
			userDTO.setUserId(profile.getString("email"));
			userDTO.setUserPw(userPw);
			userDTO.setUserImgPath(userImgPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDTO;
	}
	
	@Override
	public UserDTO getUserById(String userId) {
		return userMapper.checkUniqueId(userId);
	}
	
	@Override
	public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDTO naverUser) {
		HttpSession session = request.getSession();
		String userId = naverUser.getUserId();
		String userName = naverUser.getUserName();
		session.setAttribute("loginId", userId);
		session.setAttribute("userName", userName);
		session.setAttribute("userNo", naverUser.getUserNo());
		session.setAttribute("userPoint", naverUser.getUserPoint());
		session.setAttribute("userImgPath", naverUser.getUserImgPath());
		
	}
	
	/**********************************************/
	/**********************************************/
	/************ 카카오 로그인 API ***************/
	/**********************************************/
	/**********************************************/
	private static final String KAKAO_ID = "e3983a7e391da60579eea19765c3228d";
	
	@Override
	public String getKakaoLoginApiURL(HttpServletRequest request) {
		String apiURL = "";
		try {
			String redirectURL = URLEncoder.encode("http://localhost:8080/user/kakao/login.do", "UTF-8");
			apiURL += "https://kauth.kakao.com/oauth/authorize?response_type=code";
			apiURL += "&client_id=" + KAKAO_ID;
			apiURL += "&redirect_uri=" + redirectURL;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return apiURL;
	}
	
	@Override
	public String getKakaoLoginToken(HttpServletRequest request) {
		
		String redirectURI = "";
		try {
			redirectURI = URLEncoder.encode("http://localhost:8080/user/kakao/login.do", "UTF-8");
		}catch(Exception e) {
			e.printStackTrace();
		}
		String code = request.getParameter("code");

		StringBuilder sb = new StringBuilder();
		try {
			String apiURL = "";
			apiURL += "https://kauth.kakao.com/oauth/token?grant_type=authorization_code";
			apiURL += "&client_id=" + KAKAO_ID;
			apiURL += "&redirect_uri=" + redirectURI;
			apiURL += "&code=" + code;
			
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200)	 {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}

			sb = new StringBuilder();
			String inputLine;
			while((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			
			br.close();
			con.disconnect();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	
		JSONObject obj = new JSONObject(sb.toString());
		String accessToken = obj.getString("access_token");
		
	return accessToken;
	}
	
	@Override
	public UserDTO getKakaoLoginProfile(String accessToken) {
		
		String authorization = "Bearer " + accessToken;
		StringBuilder sb = new StringBuilder();
		
		try {
			String apiURL = "https://kapi.kakao.com/v2/user/me";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", authorization);
			
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200)	 {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}

			sb = new StringBuilder();
			String inputLine;
			while((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			br.close();
			con.disconnect();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("객체 " + sb.toString());
		
		UserDTO userDTO = new UserDTO();
		
		try {
			JSONObject obj = new JSONObject(sb.toString()).getJSONObject("properties");
			JSONObject obj2 = new JSONObject(sb.toString()).getJSONObject("kakao_account");
			
			String userName = obj.getString("nickname");
			String userImgPath = obj.getString("profile_image");
			String userId = obj2.getString("email");
			String userPw = userId.substring(0, userId.indexOf("@"));
			String userTel = "010" + (int)(Math.random() * 100000000);
			
			userDTO.setUserId(userId);
			userDTO.setUserPw(userPw);
			userDTO.setUserName(userName);
			userDTO.setUserImgPath(userImgPath);
			userDTO.setUserTel(userTel);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return userDTO;
	}
	
	
}// 클래스 종료
	
	
	
	














