package com.codingseahorse.hungryorca.controller;

import com.codingseahorse.hungryorca.service.OrcaFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrcaFileController.class)
class OrcaFileControllerTest {
    @MockBean
    OrcaFileService orcaFileService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    MockMultipartFile mockMultipartFile;

    @BeforeEach
    void setup(){
        mockMultipartFile = new MockMultipartFile(
                "fileThatDoesNotExists",
                "fileThatDoesNotExists.txt",
                "text/plain",
                "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void check_if_endpoint_upload_works_correctly() throws Exception {
        orcaFileService.saveFile(mockMultipartFile);

        mockMvc.perform(multipart("/api/orca/upload")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        then(orcaFileService)
                .should()
                .saveFile(mockMultipartFile);
    }

    @Test
    void check_if_endpoint_getAllOrcaFiles_works_correctly() throws Exception {
        mockMvc.perform(get("/api/orca")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        then(orcaFileService)
                .should()
                .retrieveAllOrcaFiles();
    }
}