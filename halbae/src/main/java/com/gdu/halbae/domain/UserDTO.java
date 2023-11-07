package com.gdu.halbae.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String userTel;
	private Date userJoinDate;
	private String userAutoLoginId;
	private Date userAutoLoginExpired;
	private int userPoint;
	private String userImgPath;
	private String userImgOriginName;
	private String userImgFileName;
	private int userHasImg;
}
