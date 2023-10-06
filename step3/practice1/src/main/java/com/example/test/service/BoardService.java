package com.example.test.service;

import com.example.test.dto.BoardDTO;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
