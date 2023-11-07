package com.example.test.repository.search;

import com.example.test.domain.Board;
import com.example.test.dto.BoardAllDTO;
import com.example.test.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> boardOfReply(String[] types,String keyword, Pageable pageable);

    Page<BoardAllDTO>  searchWithAll(String[] types, String keyword,Pageable pageable);
}
