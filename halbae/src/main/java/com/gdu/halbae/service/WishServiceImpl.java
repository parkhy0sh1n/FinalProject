package com.gdu.halbae.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.WishDTO;
import com.gdu.halbae.mapper.WishMapper;

import lombok.RequiredArgsConstructor;

		// 내가 찜하려는 클래스넘버와 현재 찜목록 DB에 들어있는 클래스 넘버를 비교해서 중복 찜을 막으려는 if문 (미완성)
//		List<Integer> wishList = wishMapper.selectClassNoInWish(userNo);
//		int wishResult = 0;
//		for (Integer classNoList : wishList) {
//			
//		    // classNo를 사용하여 작업을 수행합니다
//		    System.out.println("수강 번호 리스트 들 : " + classNoList);
//		    
//		    // 원하는 로직을 여기에 추가하세요
//		    if(wishDTO.getClassNo() == classNoList) {
//		    	
//		    	System.out.println("내가 추가하는 클래스 넘버 " + wishDTO.getClassNo());
//		    	System.out.println("추가되어 있는 클래스 넘버 " + classNoList);
//		    	System.out.println("이미 등록된 클래스입니다.");
//		    	return wishResult;
//		    }
//		    System.out.println("서비스 임플에 있는 wishResult 제대로 실행되지 않은 값 : " + wishResult);
//		}

@RequiredArgsConstructor
@Service
public class WishServiceImpl implements WishService {
	
	// field
	private final WishMapper wishMapper;
	
	// 클래스리스트 찜목록 삽입/삭제
	@Override
	public Map<String, Object> getWishByNo(HttpServletRequest request) {
		
		String strClassNo = request.getParameter("classNo");
		int classNo = 0;
		if(strClassNo != null && strClassNo.isEmpty() == false) {
			classNo = Integer.parseInt(strClassNo);
		}
		
		HttpSession session = request.getSession();
		int userNo = Integer.parseInt(session.getAttribute("userNo").toString());
		
		WishDTO wishDTO = new WishDTO();
		wishDTO.setClassNo(classNo);
		wishDTO.setUserNo(userNo);
		
		List<Integer> wishList = wishMapper.selectClassNoInWish(userNo);
		int insertResult = 0;
		int removeResult = 0;
		
		if (wishList.isEmpty() || !wishList.contains(classNo)) {
	        insertResult = wishMapper.insertWishByNo(wishDTO);
	    } else {
	        removeResult = wishMapper.removeWishByNo(wishDTO);
	    }
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("insertResult", insertResult);
		resultMap.put("removeResult", removeResult);
		System.out.println(resultMap);
		return resultMap;
		
		
	}
	
	// 찜목록 안에 찜 삭제
	@Override
	public int getWishRemoveByNo(HttpServletRequest request) {
		
		String strClassNo = request.getParameter("classNo");
		int classNo = 0;
		if(strClassNo != null && strClassNo.isEmpty() == false) {
			classNo = Integer.parseInt(strClassNo);
		}
		
		HttpSession session = request.getSession();
		int userNo = Integer.parseInt(session.getAttribute("userNo").toString());
		
		WishDTO wishDTO = new WishDTO();
		wishDTO.setClassNo(classNo);
		wishDTO.setUserNo(userNo);
		
		int removeResult = wishMapper.removeWishByNo(wishDTO);
		
		return removeResult;
	}
	
	
	// 찜목록 리스트
	@Override
	public void getWishList(HttpServletRequest request, Model model) {
		
		HttpSession session = request.getSession();
		int userNo = Integer.parseInt(session.getAttribute("userNo").toString());
		
		int wishCount = wishMapper.getWishListCount(userNo);
		
		
		// userNo를 mapper에 주고 classNo를 전부 뽑아오는 코드
		List<Integer> wishList = wishMapper.selectClassNoInWish(userNo);
		List<ClassListDTO> classList = null;
		if(wishList.size() == 0) {
			model.addAttribute("classList", classList);			
		}else {
			classList = wishMapper.selectWishList(wishList);			
			model.addAttribute("classList", classList);
		}

		// select -> option 태그에 카테고리 별 개수 구하는 코드
		model.addAttribute("wishCount", wishCount);
		
	}
	
}
