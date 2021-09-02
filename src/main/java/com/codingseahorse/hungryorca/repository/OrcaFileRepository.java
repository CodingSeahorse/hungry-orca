package com.codingseahorse.hungryorca.repository;

import com.codingseahorse.hungryorca.model.OrcaFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrcaFileRepository extends JpaRepository<OrcaFile,Long> {
    OrcaFile findByOrcaFileName(String orcaFileName);
    boolean existsOrcaFileByOrcaFileName(String orcaFileName);
}
