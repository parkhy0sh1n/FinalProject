package com.gdu.halbae.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.halbae.domain.ReviewAttachDTO;
import com.gdu.halbae.domain.ReviewDTO;
import com.gdu.halbae.domain.ReviewLikeDTO;
import com.gdu.halbae.domain.UserDTO;
import com.gdu.halbae.mapper.ReviewMapper;
import com.gdu.halbae.util.MyFileUtil;
import com.gdu.halbae.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {
	
    private final ReviewMapper reviewMapper;
    private final MyFileUtil myFileUtil;

    // 리뷰 작성
    @Transactional
    @Override
    public void writeReview(HttpSession session, MultipartHttpServletRequest multipartRequest) throws IOException {
        ReviewDTO reviewDTO = new ReviewDTO();
        double reviewGrade = Double.parseDouble(multipartRequest.getParameter("reviewGrade"));
        String reviewContent = multipartRequest.getParameter("reviewContent");
        int userNo = (int) session.getAttribute("userNo");
        int classNo = Integer.parseInt(multipartRequest.getParameter("classNo"));
        reviewDTO.setReviewGrade(reviewGrade);
        reviewDTO.setReviewContent(reviewContent);
        UserDTO userDTO = new UserDTO();
    	userDTO.setUserNo(userNo);
    	reviewDTO.setUserDTO(userDTO);
        reviewDTO.setClassNo(classNo);
        reviewMapper.insertReview(reviewDTO);
        List<MultipartFile> reviewAttachList = multipartRequest.getFiles("reviewAttachList");
        int reviewNo = reviewDTO.getReviewNo();
        // 파일 업로드 경로 설정
        String path = myFileUtil.getPath();
        
        // 리뷰 첨부 파일 처리
        if (reviewAttachList != null && !reviewAttachList.isEmpty()) {
            for (MultipartFile file : reviewAttachList) {
                ReviewAttachDTO reviewAttachDTO = new ReviewAttachDTO();
                reviewAttachDTO.setReviewNo(reviewNo);
                String originName = file.getOriginalFilename();
                String fileName = myFileUtil.getFilesystemName(originName);
                reviewAttachDTO.setPath(path);
                reviewAttachDTO.setOriginName(originName);
                reviewAttachDTO.setFileName(fileName);
                reviewAttachDTO.setThumbnail(1);
                // 파일 업로드
                MyFileUtil.uploadFile(path, fileName, file.getBytes());
                // REVIEW_ATTACH 테이블에 INSERT
                reviewMapper.insertReviewAttach(reviewAttachDTO);
            }
        }
        // 사진 첨부 여부에 따른 포인트 적립
        int userPoint = 0;
        if (reviewAttachList != null && !reviewAttachList.isEmpty()) {
            userPoint = 2000;
        } else {
            userPoint = 500;
        }
        // 회원 포인트 업데이트
        userDTO.setUserNo(userNo);
        userDTO.setUserPoint(userPoint);
        reviewMapper.updateUserPoint(userDTO);
    }
    
    @Override
	public ResponseEntity<byte[]> displayProfile(String path, String fileName) {
		ResponseEntity<byte[]> img = null;
		File profile = new File(path, fileName);
		try {
			img = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(profile), HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
    // 리뷰 첨부 파일 목록 가져오기
    @Override
    public List<ReviewAttachDTO> getReviewAttachList(int reviewNo) {
        return reviewMapper.getReviewAttachList(reviewNo);
    }
    
    // 총 리뷰 수 가져오기
    @Override
    public int getTotalReviewCount(int classNo) {
    	return reviewMapper.getTotalReviewCount(classNo);
    }
    
    // 리뷰 목록 조회
    @Override
    public List<ReviewDTO> getReviewList(int classNo, PageUtil pageUtil) {
    	int page = pageUtil.getPage() == 0 ? 1 : pageUtil.getPage();
    	int recordPerPage = 5;
    	int totalReviewCount = reviewMapper.getTotalReviewCount(classNo);
    	pageUtil.setPageUtil(page, totalReviewCount, recordPerPage);
    	Map<String, Object> map = new HashMap<>();
    	map.put("begin", pageUtil.getBegin());
    	map.put("recordPerPage", recordPerPage);
    	map.put("classNo", classNo);
        return reviewMapper.getReviewList(map);
    }

    // 리뷰 평균 평점 가져오기
    @Override
    public double getAverageRating(int classNo) {
        return reviewMapper.getAverageRating(classNo);
    }
    
    // 리뷰 좋아요 정보 저장
    @Override
    public boolean checkIfAlreadyLiked(ReviewLikeDTO reviewLikeDTO) {
        int count = reviewMapper.countReviewLike(reviewLikeDTO);
        return count > 0;
    }

    @Override
    public boolean likeReview(ReviewLikeDTO reviewLikeDTO) {
        boolean liked = false;
        if (!checkIfAlreadyLiked(reviewLikeDTO)) {
            // 좋아요 등록
            reviewMapper.insertReviewLike(reviewLikeDTO);
            reviewMapper.updateReviewLikeCount(reviewLikeDTO.getReviewNo(), 1);
            liked = true;
        } else {
            // 좋아요 취소
            reviewMapper.deleteReviewLike(reviewLikeDTO);
            reviewMapper.updateReviewLikeCount(reviewLikeDTO.getReviewNo(), -1);
        }
        return liked;
    }

    // 닉네임으로 사용자 번호 조회
    @Override
    public int getUserNoByUsername(String username) {
        return reviewMapper.getUserNoByUsername(username);
    }

}