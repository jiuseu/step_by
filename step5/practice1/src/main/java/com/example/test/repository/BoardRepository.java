package com.example.test.repository;

import com.example.test.domain.Board;
import com.example.test.repository.search.BoardSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

//    @Query("select b from board b where b.bno=:bno")
//    Page<Board>
}
