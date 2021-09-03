package com.codingseahorse.hungryorca.controller;

import com.codingseahorse.hungryorca.service.OrcaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/api/orca")
public class OrcaFileController {

    @Autowired
    OrcaFileService orcaFileService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/upload")
    public void uploadOrcaFile(
            @RequestBody MultipartFile multipartFile,
            RedirectAttributes ra){
        orcaFileService.saveFile(multipartFile);

        ra.addFlashAttribute(
                "message",
                "The file has been successfully uploaded");
    }
}
