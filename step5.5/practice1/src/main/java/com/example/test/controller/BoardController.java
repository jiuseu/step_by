package com.example.test.controller;

import com.example.test.dto.*;
import com.example.test.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @GetMapping("/list")
    public void listGet(PageRequestDTO pageRequestDTO, Model model){

        //PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listReply(pageRequestDTO);
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
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
    public String remove(BoardDTO boardDTO,
                         RedirectAttributes redirectAttributes){


        log.info("Remove POST....");

        Long bno = boardDTO.getBno();
        boardService.remove(bno);
        List<String> fileNames = boardDTO.getFileNames();

        if(fileNames != null && fileNames.size() > 0){
            fileNames.forEach(fileName ->{
                Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
                String resourceName = resource.getFilename();
                try{
                 String ContentType = Files.probeContentType(resource.getFile().toPath());
                 resource.getFile().delete();

                 if(ContentType.startsWith("image")){
                  File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                  thumbnailFile.delete();
                 }
                }catch (Exception e){
                    log.error(e.getMessage());
                }

            });

        }

        redirectAttributes.addFlashAttribute("result","removed");
        redirectAttributes.addAttribute("bno",bno);

        return "redirect:/board/list";
    }
}
