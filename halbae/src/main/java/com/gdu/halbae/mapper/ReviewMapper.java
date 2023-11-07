package com.gdu.halbae.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gdu.halbae.domain.ReviewAttachDTO;
import com.gdu.halbae.domain.ReviewDTO;
import com.gdu.halbae.domain.ReviewLikeDTO;
import com.gdu.halbae.domain.UserDTO;

@Mapper
public interface ReviewMapper {
    // 리뷰 등록
	int insertReview(ReviewDTO reviewDTO);
    void insertReviewAttach(ReviewAttachDTO reviewAttachDTO);
    void updateUserPoint(UserDTO userDTO);
    // 리뷰 첨부파일 목록 조회
    List<ReviewAttachDTO> getReviewAttachList(int reviewNo);
    // 전체 리뷰 개수 조회
    int getTotalReviewCount(int classNo);
    // 리뷰 목록 조회
    List<ReviewDTO> getReviewList(Map<String, Object> map);
    // 전체 리뷰 평균 평점 조회
    double getAverageRating(int classNo);
    // 리뷰 좋아요
    int countReviewLike(ReviewLikeDTO reviewLikeDTO);
    void insertReviewLike(ReviewLikeDTO reviewLikeDTO);
    void deleteReviewLike(ReviewLikeDTO reviewLikeDTO);
    void updateReviewLikeCount(@Param("reviewNo") int reviewNo, @Param("count") int count);
    // 사용자명으로 사용자 번호 조회
    int getUserNoByUsername(String username);
}