package com.gdu.halbae.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.halbae.domain.ReviewAttachDTO;
import com.gdu.halbae.domain.ReviewDTO;
import com.gdu.halbae.domain.ReviewLikeDTO;
import com.gdu.halbae.util.PageUtil;

public interface ReviewService {
    // 리뷰 작성
	void writeReview(HttpSession session, MultipartHttpServletRequest multipartRequest) throws IOException;
	public ResponseEntity<byte[]> displayProfile(String path, String fileName);
    // 리뷰 첨부파일 목록 조회
    List<ReviewAttachDTO> getReviewAttachList(int reviewNo);
    // 전체 리뷰 개수 조회
    int getTotalReviewCount(int classNo);
    // 리뷰 목록 조회
    public List<ReviewDTO> getReviewList(int classNo, PageUtil pageUtil);
    // 전체 리뷰 평균 평점 조회
    double getAverageRating(int classNo);
    // 리뷰 좋아요
    boolean checkIfAlreadyLiked(ReviewLikeDTO reviewLikeDTO);
    boolean likeReview(ReviewLikeDTO reviewLikeDTO);
    // 사용자명으로 사용자 번호 조회
    int getUserNoByUsername(String username);
}