package com.gdu.halbae.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationDTO {
    private int conId;
    private int classNo;
    private int userNo;
    private LocalDateTime conCreate;
    private int conState;
}