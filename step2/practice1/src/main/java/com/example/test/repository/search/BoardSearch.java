package com.example.test.repository.search;

import com.example.test.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
