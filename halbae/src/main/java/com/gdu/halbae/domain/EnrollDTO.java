package com.gdu.halbae.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollDTO {
	
	private int enrollNo;                // 수강신청식별자
	private UserDTO userDTO;             // 회원식별자
	private ScheduleDTO scheduleDTO;     // 일정식별변호
	private Date enrollDate;             // 수강신청일
	private int enrollPerson;            // 신청인원
	private String enrollRequest;        // 요청사항

}