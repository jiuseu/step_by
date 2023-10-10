package com.example.test.service;

import com.example.test.domain.Board;
import com.example.test.dto.BoardDTO;
import com.example.test.dto.BoardListReplyCountDTO;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(BoardDTO boardDTO){

        Board board = modelMapper.map(boardDTO, Board.class);

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno){

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO){

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();
        board.Change(boardDTO.getTitle(), boardDTO.getContent());

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno){

        try{
            Optional<Board> result = boardRepository.findById(bno);
            Board board = result.orElseThrow();
            boardRepository.delete(board);
        }catch (NoSuchElementException e){
            log.info("이미 삭제 되었거나 없는 데이터입니다.");
        }

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        List<BoardDTO> list = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(list)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listReply(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListReplyCountDTO> result = boardRepository.boardOfReply(types,keyword,pageable);

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .total((int)result.getTotalElements())
                .dtoList(result.getContent())
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
