package com.example.test.controller;

import com.example.test.dto.UploadFileDTO;
import com.example.test.dto.UploadResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${com.example.upload.path}")
    private String uploadPath;

    @Operation(summary = "POST Upload", description = "POST방식으로 첨부파일 등록")
    @PostMapping(value ="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> uploadPost(@RequestBody UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null){

            final List<UploadResultDTO> result = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(file ->{
                boolean img = false;
                String uuid = UUID.randomUUID().toString();
                String fileName = file.getOriginalFilename();
                Path savePath = Paths.get(uploadPath,uuid+"_"+fileName);

                try{
                    file.transferTo(savePath);

                    if(Files.probeContentType(savePath).startsWith("image")){
                        img = true;
                        File thumbnailFile = new File(uploadPath,"s_"+uuid+"_"+fileName);
                        Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,200,200);
                    }

                    result.add(UploadResultDTO.builder()
                            .uuid(uuid)
                            .fileName(fileName)
                            .img(img)
                            .build());


                }catch (IOException e){
                    e.printStackTrace();
                }
            });
            return result;
        }

        return null;
    }

    @Operation(summary = "Upload Get", description = "GET방식으로 첨부파일 조회")
    @GetMapping(value = "/view/{fileName}")
    public ResponseEntity<Resource> uploadGet(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @Operation(summary = "Upload Delete", description = "Delete 방식으로 첨부파일 삭제")
    @DeleteMapping(value = "/remove/{fileName}")
    public Map<String,Boolean> uploadDelete(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        Map<String,Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
           String contentType = Files.probeContentType(resource.getFile().toPath());
           removed = resource.getFile().delete();

           if(contentType.startsWith("image")){
             File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
             thumbnailFile.delete();
           }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        resultMap.put("result",removed);

        return resultMap;
    }
}
