package com.example.test.service;

import com.example.test.domain.Board;
import com.example.test.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listReply(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .user(board.getUser())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted()
                .map(fileName -> fileName.getUuid()+"_"+fileName.getFileName())
                .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

    default Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .user(boardDTO.getUser())
                .build();

        if(boardDTO.getFileNames() != null){
          boardDTO.getFileNames().forEach(file ->{
              String[] arr = file.split("_");
              String destArr = "";

              for(int i = 1; i < arr.length; i++){
                  destArr += arr[i];
              }
              board.addImage(arr[0],destArr);
          });
        }

        return board;
    }
}
