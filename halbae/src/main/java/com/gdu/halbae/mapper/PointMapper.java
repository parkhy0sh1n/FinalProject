package com.gdu.halbae.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PointMapper {
	// 회원의 보유 포인트 조회
	int getUserPoint(int userNo);
}