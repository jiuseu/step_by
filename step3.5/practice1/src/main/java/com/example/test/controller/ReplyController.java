package com.example.test.controller;

import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.dto.ReplyDTO;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "/Replies POST", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> registerPost(@Valid @RequestBody ReplyDTO replyDTO,
                                                         BindingResult bindingResult)throws BindException {

        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        Map<String, Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

    @Operation(summary = "Replies Of Board", description = "특정 게시물 댓글 목록 조회")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> boardOfReply(@PathVariable("bno") Long bno,
                                                  PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> result = replyService.replyList(bno, pageRequestDTO);

        return result;
    }

    @Operation(summary = "Replies Get", description = "특정 댓글 조회")
    @GetMapping(value = "/{rno}")
    public ReplyDTO GetReply(@PathVariable("rno") Long rno){

        ReplyDTO replyDTO = replyService.readOne(rno);

        return replyDTO;
    }

    @Operation(summary = "Replies Modify", description = "특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> replyModify(@PathVariable("rno") Long rno,
                                           @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("상태:","modify success");

        return resultMap;
    }

    @Operation(summary = "Replies Remove", description = "특정 댓글 삭제")
    @DeleteMapping(value = "/{rno}")
    public Map<String, String> replyRemove(@PathVariable("rno")Long rno){

        replyService.remove(rno);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("상태:", "remove success");

        return resultMap;
    }
}
