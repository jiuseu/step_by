package com.example.test.service;

import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.dto.ReplyDTO;
import org.springframework.data.domain.Pageable;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO readOne(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> replyList(Long bno, PageRequestDTO pageRequestDTO);
}
