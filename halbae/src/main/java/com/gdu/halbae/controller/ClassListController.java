package com.gdu.halbae.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.halbae.service.ClassListService;
import com.gdu.halbae.service.EnrollService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/class")
@Controller
public class ClassListController {
   
   // field
   private final ClassListService classListService;
   private final EnrollService enrollService;
   
   // 찜목록 이동 페이지
   @GetMapping("/wish.html")
   public String wish() {
      return "classlist/wish";
   }
   
   // 클래스 등록 페이지
   @GetMapping("/classUpload.html")
   public String classUpload() {
      return "classlist/classupload";
   }
   
   
   // 전체/카테고리 클래스
   @GetMapping("/classList.do")
   public String list(HttpServletRequest request, Model model) {
      classListService.getClassList(request, model);
      return "classlist/classlist";
   }
   
   // 최신 클래스
   @GetMapping("/classListNew.do")
   public String listNew(HttpServletRequest request, Model model) {
      classListService.getClassListNew(request, model);
      return "classlist/classlist";
   }
   
   // 디테일 클래스
   @GetMapping("/classDetail.do")
   public String listDetail(HttpServletRequest request, Model model) {
      model.addAttribute("imgClass", classListService.getClassByNo(request, model));
      model.addAttribute("classNo", request.getParameter("classNo"));
      System.out.println("컨트롤러 디테일 클래스 : " + classListService.getClassByNo(request, model));
      
      model.addAttribute("scheduleList", enrollService.scheduleByClassNo(request));
      model.addAttribute("classList", enrollService.classListByNo(request));
      return "classlist/classdetail";
   }
   
   // 클래스 등록
   @PostMapping("/classAdd.do")
   public String addClass(MultipartHttpServletRequest multipartHttpServletRequest, RedirectAttributes redirectAttributes) {
      int uploadResult = classListService.addClass(multipartHttpServletRequest);
      redirectAttributes.addFlashAttribute("uploadResult", uploadResult);
      return "redirect:/class/classListNew.do";
   }
   
   // 클래스 등록 이미지 뽑기 (메인)
   @GetMapping("/display.do")
   public ResponseEntity<byte[]> display(@RequestParam("classNo") int classNo){
      return classListService.display(classNo);
   }
   
   // 클래스 등록 이미지 뽑기 (상세)
   @GetMapping("/displayDetail.do")
   public ResponseEntity<byte[]> displayDetail(@RequestParam("classNo") int classNo){
      return classListService.displayDetail(classNo);
   }
   
   // 클래스 등록 목록
   @GetMapping("/classUploadList.do")
   public String classUploadList(HttpServletRequest request, Model model){
      classListService.getClassUploadList(request, model);
      return "classlist/classuploadlist";
   }
   
   // 등록한 클래스 삭제
   @GetMapping("/classUploadRemove.do")
   public String classUploadRemove(HttpServletRequest request, RedirectAttributes redirectAttributes) {
      int removeResult = classListService.getClassUploadRemove(request);
      redirectAttributes.addFlashAttribute("removeResult", removeResult);
      return "redirect:/class/classUploadList.do";
   }
   
   // 등록한 클래스 편집화면으로 이동/값 화면에 뿌리기
   @GetMapping("/classEdit.do")
   public String classEdit(HttpServletRequest request, Model model) {
      classListService.getClassEdit(request, model);
      return "classlist/classedit";
   }
   
   // 등록한 클래스 수정
   @PostMapping("/classModify.do")
   public String classModify(MultipartHttpServletRequest multipartHttpServletRequest, RedirectAttributes redirectAttributes) {
	   int modifyResult = classListService.modifyClass(multipartHttpServletRequest);
	   redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
	   return "redirect:/class/classUploadList.do";
   }

}