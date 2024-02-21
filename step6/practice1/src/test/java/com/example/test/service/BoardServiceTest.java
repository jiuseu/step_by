package com.example.test.service;

import com.example.test.domain.Board;
import com.example.test.dto.BoardDTO;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void RegisterTest(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("Service Title")
                .content("Service Content")
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
       Long bno = 113L;
       BoardDTO boardDTO = boardService.readOne(bno);

       log.info(boardDTO);
       boardDTO.getFileNames().forEach(file ->{
           log.info(file);
       });
    }

    @Test
    public void ModifyTest(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(113L)
                .title("Updated Title")
                .content("Updated Content")
                .user("freiokjrtoihnyi")
                .build();

        List<String> fileNames = Arrays.asList(
                UUID.randomUUID()+"_"+"999.jpg"
        );
        boardDTO.setFileNames(fileNames);
        boardService.modify(boardDTO);
    }

    @Test
    public void DeleteTest(){
        Long bno = 2L;
        boardService.remove(bno);
    }

    @Test
    public void ListTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tc")
                .keyword("Title")
                .build();

        PageResponseDTO pageResponseDTO = boardService.listWithAll(pageRequestDTO);
        log.info(pageResponseDTO);
    }
}
