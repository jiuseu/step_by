package com.example.test.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardOfReplyDTO {

    private Long bno;

    private String title;

    private String user;

    private Long replyCount;

    private LocalDateTime regDate;
}
