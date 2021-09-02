package com.codingseahorse.hungryorca.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orca_file")
@Getter
@Setter
@NoArgsConstructor
public class OrcaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orcaFileId;

    @Column(
            length = 500,
            nullable = false,
            unique = true)
    private String orcaFileName;

    private String orcaFileType;
    @Lob
    private byte[] orcaFileData;

    private Date createdAt;

    public OrcaFile(String orcaFileName, String orcaFileType) {
        this.orcaFileName = orcaFileName;
        this.orcaFileType = orcaFileType;
    }

    public OrcaFile(String orcaFileName, String orcaFileType, byte[] orcaFileData, Date createdAt) {
        this.orcaFileName = orcaFileName;
        this.orcaFileType = orcaFileType;
        this.orcaFileData = orcaFileData;
        this.createdAt = createdAt;
    }
}
