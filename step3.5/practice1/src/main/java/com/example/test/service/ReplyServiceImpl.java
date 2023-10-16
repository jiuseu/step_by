package com.example.test.service;

import com.example.test.domain.Reply;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.dto.ReplyDTO;
import com.example.test.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    public void GetMapping(){
        modelMapper.typeMap(ReplyDTO.class, Reply.class).addMapping(
                src -> src.getBno(), (dest,v) -> dest.getBoard().MappingBno((Long)v)
        );
        modelMapper.typeMap(Reply.class, ReplyDTO.class).addMapping(
                src -> src.getBoard().getBno(), (dest,v) -> dest.setBno((Long)v)
        );
    }

    @Override
    public Long register(ReplyDTO replyDTO){

        GetMapping();
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO readOne(Long rno){

        GetMapping();
        Optional<Reply> result = replyRepository.findById(rno);
        Reply reply = result.orElseThrow();
        ReplyDTO replyDTO = modelMapper.map(reply, ReplyDTO.class);

        return replyDTO;
    }

    @Override
    public void modify(ReplyDTO replyDTO){

        Optional<Reply> result = replyRepository.findById(replyDTO.getRno());
        Reply reply = result.orElseThrow();
        reply.changeReplyText(replyDTO.getReplyText());

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno){

        Optional<Reply> result = replyRepository.findById(rno);
        replyRepository.delete(result.orElseThrow());
    }

    @Override
    public PageResponseDTO<ReplyDTO> replyList(Long bno, PageRequestDTO pageRequestDTO){

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0: pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(), Sort.by("rno").ascending());

        Page<Reply> result = replyRepository.BoardOfReply(bno, pageable);
        List<ReplyDTO> list = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .dtoList(list)
                .pageRequestDTO(pageRequestDTO)
                .total((int)result.getTotalElements())
                .build();
    }
}
