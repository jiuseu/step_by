package com.example.test.repository;

import com.example.test.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select r from Reply r where r.board.bno =:bno")
    Page<Reply> BoardOfReply(Long bno, Pageable pageable);
}
