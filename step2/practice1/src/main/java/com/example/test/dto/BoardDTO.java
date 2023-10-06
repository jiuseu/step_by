package com.example.test.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    private String user;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
