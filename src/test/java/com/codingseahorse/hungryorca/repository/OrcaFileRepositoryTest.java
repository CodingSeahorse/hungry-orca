package com.codingseahorse.hungryorca.repository;

import com.codingseahorse.hungryorca.model.OrcaFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrcaFileRepositoryTest {

    @Autowired
    OrcaFileRepository orcaFileRepository;

    MultipartFile multipartFile;
    OrcaFile orcaFile;

    @BeforeEach
    void setUp() throws IOException {
        multipartFile = new MockMultipartFile("fileThatDoesNotExists.txt",
                "mockFile.txt",
                "text/plain",
                "This is a dummy file".getBytes(StandardCharsets.UTF_8));
        orcaFile = new OrcaFile(
                multipartFile.getName(),
                multipartFile.getContentType(),
                multipartFile.getBytes(),
                new Date());

        orcaFile.setOrcaFileId(1L);
    }

    @Test
    void should_save_a_MultipartFile_to_Database(){
        orcaFileRepository.save(orcaFile);

        OrcaFile findOrcaFile = orcaFileRepository.findByOrcaFileName(orcaFile.getOrcaFileName());

        assertThat(findOrcaFile.getOrcaFileName())
                .isEqualTo(orcaFile.getOrcaFileName());
    }

    @Test
    void should_check_if_MultipartFile_already_exists() {
        orcaFileRepository.save(orcaFile);

        boolean existsOrcaFile = orcaFileRepository.existsOrcaFileByOrcaFileName(orcaFile.getOrcaFileName());

        assertThat(existsOrcaFile)
                .isTrue();
    }

    @Test
    void should_get_file_by_Id() {
        OrcaFile findOrcaFile = orcaFileRepository.getById(1L);

        assertThat(findOrcaFile.getOrcaFileId())
                .isEqualTo(orcaFile.getOrcaFileId());
    }
}