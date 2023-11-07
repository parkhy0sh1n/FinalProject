package com.gdu.halbae.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gdu.halbae.intercept.AutoLoginInterceptor;
import com.gdu.halbae.intercept.LoginCheckInterceptor;
import com.gdu.halbae.intercept.PreventLoginInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class UserMvcConfig implements WebMvcConfigurer {

	private final AutoLoginInterceptor autoLoginInterceptor;
	private final LoginCheckInterceptor loginCheckInterceptor;
	private final PreventLoginInterceptor preventLoginInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> preventLogin = new ArrayList<>();
		preventLogin.add("/user/join.html");
		preventLogin.add("/user/agree.html");
		preventLogin.add("/user/successJoin.do");
		preventLogin.add("/user/login.html");
		preventLogin.add("/user/login.do");
		preventLogin.add("/user/findId.html");
		preventLogin.add("/user/findId.do");
		preventLogin.add("/user/findPw.html");
		registry.addInterceptor(autoLoginInterceptor).addPathPatterns("/**");
		registry.addInterceptor(preventLoginInterceptor).addPathPatterns(preventLogin);
		// 로그인 체크 인터셉터 추가하기
		registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/review/**")
													  .addPathPatterns("/chat/**")
													  .addPathPatterns("/wish/**")
													  .addPathPatterns("/enroll/**");
	}
	
}
