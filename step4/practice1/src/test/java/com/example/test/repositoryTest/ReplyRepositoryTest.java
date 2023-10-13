package com.example.test.repositoryTest;

import com.example.test.domain.Board;
import com.example.test.domain.Reply;
import com.example.test.dto.BoardListReplyCountDTO;
import com.example.test.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertTest(){

        Board board = Board.builder().bno(100L).build();

        IntStream.rangeClosed(7,107).forEach(i ->{
            Reply reply = Reply.builder()
                    .board(board)
                    .replyer("replyer"+i)
                    .replyText("댓글 테스트.."+i)
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void PageTest(){

        Long bno = 100L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> list = replyRepository.list(bno,pageable);

        list.getContent().forEach(i ->{
            log.info(i);
        });
    }


}
