package com.codingseahorse.hungryorca.controller;

import com.codingseahorse.hungryorca.model.OrcaFile;
import com.codingseahorse.hungryorca.service.OrcaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/orca")
public class OrcaFileController {

    @Autowired
    OrcaFileService orcaFileService;

    @Operation(summary = "upload a file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "the file has been successfully uploaded",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "invalid payload. Please correct your file*selection or url",
                            content =  @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "wrong url",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "uploaded file was to large",
                            content = @Content)})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/upload")
    public void uploadOrcaFile(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes ra){
        orcaFileService.saveFile(file);

        ra.addFlashAttribute(
                "message",
                "The file has been successfully uploaded");
    }


    @Operation(summary = "retrieve all OrcaFiles from Database")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "found the homepage",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))}),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid variables. Please correct your parameters or url",
                            content =  @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No pages found",
                            content = @Content)})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrcaFile> getAllOrcaFiles(){return orcaFileService.retrieveAllOrcaFiles();}

    @Operation(summary = "download a file")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "downloaded successfully",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid variables. Please correct your parameters or url",
                            content =  @Content),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No page with this url found",
                            content = @Content)})
    @GetMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    public void downloadOrcaFile(
            @RequestParam("fileName") String fileName,
            HttpServletResponse response) throws IOException {
        orcaFileService.download(fileName,response);
    }
}
