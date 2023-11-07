package com.gdu.halbae.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.halbae.domain.ClassListDTO;
import com.gdu.halbae.domain.WishDTO;

@Mapper
public interface WishMapper {
	
	public int insertWishByNo(WishDTO wishDTO);
	public int removeWishByNo(WishDTO wishDTO);
	public List<Integer> selectClassNoInWish(int userNo);
	public List<ClassListDTO> selectWishList(List<Integer> wishList);
	public int getWishListCount(int userNo);
}
