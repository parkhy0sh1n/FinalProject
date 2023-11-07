package com.gdu.halbae.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

  // 로그인 여부를 확인해서
  // 로그인이 되어 있지 않으면 로그인 페이지로 이동시키는 인터셉터
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
  	HttpSession session = request.getSession();
  	
  	if(session != null && session.getAttribute("loginId") == null) {
  		
  		response.setContentType("text/html; charset=UTF-8");
  		
  		PrintWriter out = response.getWriter();
  		
  		out.println("<script>");
  		out.println("if(confirm('로그인이 필요한 기능입니다. 로그인하시겠습니까?')) {");
  		out.println("location.href='/user/login.html';");
  		out.println("}");
  		out.println("else {");
  		out.println("history.back();");
  		out.println("}");
  		out.println("</script>");
  		out.flush();
  		out.close();
  		
  		return false;
  	}
  	return true;
  }
}	