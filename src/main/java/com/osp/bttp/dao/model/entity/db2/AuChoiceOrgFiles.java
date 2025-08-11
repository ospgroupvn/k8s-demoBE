package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_CHOICE_ORG_FILES")
public class AuChoiceOrgFiles {
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_CHOICE_ORG_FILES_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Column(name = "ID")
    private Long  id;
    @Column(name = "FILE_ID")
    private Long  fileId;
    @Column(name = "CHOICE_ORG_ID")
    private Long choiceOrgId;
    @Column(name = "GEN_DATE")
    private Date genDate;

    public AuChoiceOrgFiles(Long fileId, Long choiceOrgId) {
        this.fileId = fileId;
        this.choiceOrgId = choiceOrgId;
    }

    public AuChoiceOrgFiles() {
    }

    
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getChoiceOrgId() {
        return choiceOrgId;
    }

    public void setChoiceOrgId(Long choiceOrgId) {
        this.choiceOrgId = choiceOrgId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
