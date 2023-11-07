package com.gdu.halbae.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.halbae.domain.ReviewAttachDTO;
import com.gdu.halbae.domain.ReviewDTO;
import com.gdu.halbae.domain.ReviewLikeDTO;
import com.gdu.halbae.service.ReviewService;
import com.gdu.halbae.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 목록 페이지 요청
    @GetMapping("/review/list")
    public String getReviewList(int classNo, PageUtil pageUtil, Model model) {
        // 총 리뷰 수 조회
        int totalReviewCount = reviewService.getTotalReviewCount(classNo);
        // 클래스 리뷰 평점 조회
        double averageRating = reviewService.getAverageRating(classNo);
        // 페이지에 해당하는 리뷰 목록 조회
        List<ReviewDTO> reviewList = reviewService.getReviewList(classNo, pageUtil);
        // 모델에 데이터 추가
        model.addAttribute("classNo", classNo);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("pageMaker", pageUtil.getPagination("/review/list?classNo=" + classNo));
        model.addAttribute("totalReviewCount", totalReviewCount); 
        model.addAttribute("averageRating", averageRating); 
        return "review/reviewList"; // review/reviewList.html 페이지로 이동
        
    }

    // 리뷰 작성 폼 페이지로 이동
    @GetMapping("/review/form")
    public String showReviewForm(Model model, HttpSession session, @RequestParam("classNo") int classNo) {
        int userNo = (int) session.getAttribute("userNo");
        model.addAttribute("classNo", classNo);
        model.addAttribute("userNo", userNo);
        return "review/reviewForm";
    }

    // 리뷰 작성 처리
    @PostMapping("/review/write")
    public String writeReview(HttpSession session, MultipartHttpServletRequest multipartRequest) throws IOException {
        reviewService.writeReview(session, multipartRequest);
        int classNo = Integer.parseInt(multipartRequest.getParameter("classNo"));
        return "redirect:/review/list?classNo=" + classNo;
    }
    
    // @ResponseBody
    @GetMapping("/review/display.do")
	public ResponseEntity<byte[]> display(String path, String fileName) {
		return reviewService.displayProfile(path, fileName);
	}

    // 리뷰 첨부 파일 목록 가져오기
    @ResponseBody
    @GetMapping(value = "/review/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReviewAttachDTO> getReviewAttachList(int reviewNo) {
        return reviewService.getReviewAttachList(reviewNo); // 리뷰 첨부 파일 목록 반환
    }
    
    // 리뷰 좋아요
    @PostMapping("/review/likeReview")
    @ResponseBody
    public String likeReview(@RequestBody ReviewLikeDTO reviewLikeDTO) {
        boolean alreadyLiked = reviewService.checkIfAlreadyLiked(reviewLikeDTO);
        if (alreadyLiked) {
            return "alreadyLiked"; // 이미 좋아요를 눌렀을 경우
        }
        boolean liked = reviewService.likeReview(reviewLikeDTO);
        if (liked) {
            return "liked"; // 좋아요 누름
        } else {
            return "unliked"; // 좋아요 취소
        }
    }
    
}