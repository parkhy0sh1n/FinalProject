package com.gdu.halbae.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAttachDTO {
	private int reaNo;
    private int reviewNo;
    private String path;
    private String originName;
    private String fileName;
    private int download;
    private int thumbnail;
}