package com.example.test.repository;

import com.example.test.domain.Board;
import com.example.test.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

}
