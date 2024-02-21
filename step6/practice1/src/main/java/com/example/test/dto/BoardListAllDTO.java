package com.example.test.dto;

import com.example.test.domain.Board;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListAllDTO {

    private Long bno;

    private String title;

    private String user;

    private LocalDateTime regDate;

    private Long replyCount;

    private List<BoardImageDTO> boardImage;

}
