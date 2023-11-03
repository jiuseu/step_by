package com.example.test.dto;

import com.example.test.domain.BoardImage;
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
public class BoardAllDTO {

    private Long bno;

    private String title;

    private String user;

    private LocalDateTime regDate;

    private Long replyCount;

    private List<BoardImageDTO> boardImages;
}
