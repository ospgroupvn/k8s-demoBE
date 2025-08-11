package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_FILES")
public class AuFiles {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_FILES_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "FILE_TITLE")
    private String fileTitle;
    @Column(name = "FILE_TYPE")
    private Long fileType;
    @Column(name = "PATH")
    private String path;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "NAME_IN_SERVER")
    private String nameInServer;

    public AuFiles() {
    }

    public AuFiles(String fileTitle, Long fileType, String path, Date genDate, String nameInServer) {
        this.fileTitle = fileTitle;
        this.fileType = fileType;
        this.path = path;
        this.genDate = genDate;
        this.nameInServer = nameInServer;
    }    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public Long getFileType() {
        return fileType;
    }

    public void setFileType(Long fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getNameInServer() {
        return nameInServer;
    }

    public void setNameInServer(String nameInServer) {
        this.nameInServer = nameInServer;
    }
    
    
}
