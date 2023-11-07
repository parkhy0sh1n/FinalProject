package com.gdu.halbae.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	private int status; // 응답 상태 코드
    private String message; // 응답 메시지
    private Object data; // 응답 데이터
}