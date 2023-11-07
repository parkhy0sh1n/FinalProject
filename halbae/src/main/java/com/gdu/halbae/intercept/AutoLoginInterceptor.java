package com.gdu.halbae.intercept;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AutoLoginInterceptor implements HandlerInterceptor{
	
	private final UserMapper userMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		HttpSession session = request.getSession();
		
		if(session != null && session.getAttribute("userId") == null) {
			
			Cookie cookie = WebUtils.getCookie(request, "autoLoginId");
			
			if(cookie != null) {
				
				String autoLoginId = cookie.getValue();		
				UserDTO userDTO = userMapper.selectUserDTOByAutoLoginId(autoLoginId);
				
				if(userDTO != null) {
					
					session.setAttribute("loginId", userDTO.getUserId());
					session.setAttribute("userNo", userDTO.getUserNo());
					session.setAttribute("userName", userDTO.getUserName());
					session.setAttribute("userImgPath", userDTO.getUserImgPath());
				
				}
			}
		}
		return true;
	}

}
