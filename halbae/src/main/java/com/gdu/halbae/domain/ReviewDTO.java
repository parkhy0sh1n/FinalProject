package com.gdu.halbae.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewNo;
    private int classNo;
    private String reviewContent;
    private double reviewGrade;
    private Date writeDate;
    private UserDTO userDTO;
    private int likes;
}