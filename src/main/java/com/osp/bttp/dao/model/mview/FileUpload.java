package com.osp.bttp.dao.model.mview;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class FileUpload {

    @Id
    @Column(name = "FILE_ID")
    private Long idFile;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "LINK_FILE")
    private String linkFile;

    public FileUpload() {
    }

    public Long getIdFile() {
        return idFile;
    }

    public void setIdFile(Long idFile) {
        this.idFile = idFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }

    
    
}
