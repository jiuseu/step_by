package com.example.test.controller;

import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.dto.ReplyDTO;
import com.example.test.repository.ReplyRepository;
import com.example.test.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Replies POST",description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> registerPost(@Valid @RequestBody ReplyDTO replyDTO,
                                                         BindingResult bindingResult)
            throws BindException{

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String,Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);
        resultMap.put("rno",rno);

        return resultMap;
    }

    @Operation(summary = "Replies Of Board", description = "GET 방식으로 특정 게시물 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> GetList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> responseDTO = replyService.BoardOfReply(bno, pageRequestDTO);

        return responseDTO;
    }

    @Operation(summary = "Read Reply", description = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

        ReplyDTO replyDTO = replyService.readOne(rno);

        return replyDTO;
    }

    @Operation(summary = "Replies Modify", description = "PUT 방식으로 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> modify(@PathVariable("rno")Long rno,
                                   @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno);
        replyService.modify(replyDTO);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);

        return resultMap;
    }

    @Operation(summary = "Replies Delete", description = "DELETE 방식으로 댓글 삭제")
    @DeleteMapping(value = "/{rno}")
    public Map<String,Long> remove(@PathVariable("rno")Long rno){

        replyService.remove(rno);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);

        return resultMap;
    }
}
