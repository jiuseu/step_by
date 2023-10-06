package com.example.test.repositoryTest;

import com.example.test.domain.Board;
import com.example.test.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void CreateTest(){

        IntStream.rangeClosed(1,100).forEach(i ->{
            Board board = Board.builder()
                    .title("Title "+i)
                    .content("Content..."+i)
                    .user("User"+i)
                    .build();

            boardRepository.save(board);
        });
    }

    @Test
    public void ReadTest(){
        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void UpdateTest(){
        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.orElseThrow();
        board.Change("Update Title 1", "Update Content..1");

        boardRepository.save(board);
        ReadTest();
    }

    @Test
    public void DeleteTest(){
        Optional<Board> result = boardRepository.findById(1L);
        Board board = result.orElseThrow();
        boardRepository.delete(board);
    }
}
