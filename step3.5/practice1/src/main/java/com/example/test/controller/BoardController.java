package com.example.test.controller;

import com.example.test.dto.BoardDTO;
import com.example.test.dto.BoardOfReplyDTO;
import com.example.test.dto.PageRequestDTO;
import com.example.test.dto.PageResponseDTO;
import com.example.test.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void listGet(PageRequestDTO pageRequestDTO, Model model){

        PageResponseDTO<BoardOfReplyDTO> responseDTO = boardService.listOfReply(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGet(){
    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){

        log.info("Register Post...");

        if(bindingResult.hasErrors()){
            log.info("Register Post Error...");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());

            return "redirect:/board/register";
        }
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result",bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read","/modify"})
    public void read(Long bno,Model model,PageRequestDTO pageRequestDTO){

        log.info("read/modify Get....");
        BoardDTO boardDTO = boardService.readOne(bno);
        model.addAttribute("dto",boardDTO);
    }

    @PostMapping("/modify")
    public String modifyPost(@Valid BoardDTO boardDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           PageRequestDTO pageRequestDTO){

        String link = pageRequestDTO.getLink();

        log.info("modify POST Error....");
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno",boardDTO.getBno());

            return "redirect:/board/modify?"+link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result","modifed");
        redirectAttributes.addAttribute("bno",boardDTO.getBno());

        return "redirect:/board/read?"+link;
    }

    @PostMapping("/remove")
    public String remove(Long bno,
                         RedirectAttributes redirectAttributes){

        log.info("Remove POST....");

        boardService.remove(bno);
        redirectAttributes.addFlashAttribute("result","removed");
        redirectAttributes.addAttribute("bno",bno);

        return "redirect:/board/list";
    }
}
