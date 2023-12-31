package com.example.test.service;

import com.example.test.domain.Board;
import com.example.test.dto.BoardDTO;
import com.example.test.dto.BoardImageDTO;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void RegisterTest(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Project Service Title")
                .content("Project Service Content")
                .user("ServiceUser")
                .build();

        List<String> fileNames = Arrays.asList(
                UUID.randomUUID()+"_"+"111.jpg",
                UUID.randomUUID()+"_"+"222.jpg",
                UUID.randomUUID()+"_"+"333.jpg"
        );

        boardDTO.setFileNames(fileNames);

        boardService.register(boardDTO);
    }

    @Test
    public void ReadOneTest(){
       Long bno = 134L;
       BoardDTO boardDTO = boardService.readOne(bno);

       log.info(boardDTO);
    }

    @Test
    public void ModifyTest(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(134L)
                .title("Updated Title")
                .content("Updated Content")
                .user("freiokjrtoihnyi")
                .build();

        List<String> fileNames = Arrays.asList(
                UUID.randomUUID()+"_444.jpg"
        );
        boardDTO.setFileNames(fileNames);
        boardService.modify(boardDTO);
    }

    @Test
    public void DeleteTest(){
        Long bno = 134L;
        boardService.remove(bno);
    }

    @Test
    public void ListTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("")
                .keyword("")
                .build();

        PageResponseDTO pageResponseDTO = boardService.listWithAll(pageRequestDTO);
        log.info(pageResponseDTO);
    }
}
