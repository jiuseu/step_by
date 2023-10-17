package com.example.test.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;

    @NotEmpty
    @Size(min = 3,max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String user;

    private LocalDateTime regDate;

    private LocalDateTime modDate;
}
