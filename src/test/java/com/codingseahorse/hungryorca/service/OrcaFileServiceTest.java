package com.codingseahorse.hungryorca.service;

import com.codingseahorse.hungryorca.exception.MyFileSaveException;
import com.codingseahorse.hungryorca.exception.NotFoundException;
import com.codingseahorse.hungryorca.model.OrcaFile;
import com.codingseahorse.hungryorca.repository.OrcaFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrcaFileServiceTest {

    @Mock
    OrcaFileRepository orcaFileRepository;
    @InjectMocks
    OrcaFileService orcaFileService;

    MultipartFile multipartFile;
    OrcaFile orcaFile;

    HttpServletResponse httpServletResponse = new MockHttpServletResponse();

    @BeforeEach
    void setup() throws IOException {
        multipartFile = new MockMultipartFile(
                "fileThatDoesNotExists.txt",
                "fileThatDoesNotExists.txt",
                "text/plain",
                "This is a dummy file content".getBytes(StandardCharsets.UTF_8));
        orcaFile = new OrcaFile(
                multipartFile.getName(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                new Date());

        orcaFile.setOrcaFileId(1L);
    }

    @Test
    void should_upload_a_orcaFile_to_database(){
        orcaFileService.saveFile(multipartFile);

        verify(orcaFileRepository, times(1))
                .save(any(OrcaFile.class));
    }

    @Test
    void should_throw_exception_if_OrcaFile_already_exists() {
        given(orcaFileRepository
                .existsOrcaFileByOrcaFileName(anyString()))
                .willReturn(true);

        assertThatThrownBy(
                () -> orcaFileService.saveFile(multipartFile))
                .isInstanceOf(MyFileSaveException.class)
                .hasMessageContaining(String.format(
                        "File with the name %s already exists",
                        multipartFile.getName()));
    }

    @Test
    void should_throw_exception_if_OrcaFile_contains_special_signs() {
        MultipartFile wrongMultipartFile = new MockMultipartFile(
                "fileThatDoesNotExists.txt",
                "wrongMulti_%&>!?^name.txt",
                "text/plain",
                "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(
                () -> orcaFileService.saveFile(wrongMultipartFile))
                .isInstanceOf(MyFileSaveException.class)
                .hasMessageContaining("Filename contains special characters. " +
                        "Please enter a valid fileName (a-z,A-Z,0-9)");
    }

    @Test
    void should_throw_exception_if_a_error_has_occurred() {
        MultipartFile wrongMultipartFile2 = new MockMultipartFile(
                "null",
                null,
                null,
                "".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(
                () -> orcaFileService.saveFile(wrongMultipartFile2))
                .isInstanceOf(MyFileSaveException.class)
                .hasMessageContaining("A error has occurred. Failed saving file.");
    }

    @Test
    void should_retrieveAllOrcaFiles_from_in_database() {
        List<OrcaFile> orcaFileList = new ArrayList<>();
        orcaFileList.add(orcaFile);

        given(orcaFileRepository.findAll())
                .willReturn(orcaFileList);

        List<OrcaFile> resultList =
                orcaFileService.retrieveAllOrcaFiles();

        assertThat(resultList)
                .isEqualTo(orcaFileList);
    }

    @Test
    void should_throw_exception_if_no_OrcaFiles_found() {
        List<OrcaFile> emptyOrcaFileList = new ArrayList<>();

        given(orcaFileRepository.findAll())
                .willReturn(emptyOrcaFileList);

        assertThatThrownBy(
                () -> orcaFileService.retrieveAllOrcaFiles())
                .isNotNull()
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No OrcaFiles found in database. Please upload a file");
    }

    // TODO:EXTEND DOWNLOAD TEST
    @Test
    void should_download_OrcaFile() throws IOException {
        given(orcaFileRepository.findByOrcaFileName(anyString()))
                .willReturn(orcaFile);

        orcaFileService.download(orcaFile.getOrcaFileName(),httpServletResponse);
    }

    @Test
    void should_throw_exception_if_orcaFile_not_found() {
        given(orcaFileRepository.findByOrcaFileName(anyString()))
                .willReturn(null);

        assertThatThrownBy(
                ()->orcaFileService.download(orcaFile.getOrcaFileName(),httpServletResponse))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format("file %s not found.",orcaFile.getOrcaFileName()));
    }
}