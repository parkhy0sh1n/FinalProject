package com.gdu.halbae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImgClassDTO {
	
	private int imgNo;
	private String imgName;
	private String imgMainPath;
	private String imgDetailPath;
	private ClassListDTO classListDTO;

}
