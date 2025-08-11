package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_CHOICE_RESULT_FILES")
public class ChoiceResultFiles {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_RESULT_FILES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_RESULT_ID")
    private Long choiceResultID;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    public ChoiceResultFiles() {
    }

    public ChoiceResultFiles(Long choiceResultID, Long fileId, Timestamp genDate) {
        this.choiceResultID = choiceResultID;
        this.fileId = fileId;
        this.genDate = genDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChoiceResultID() {
        return choiceResultID;
    }

    public void setChoiceResultID(Long choiceResultID) {
        this.choiceResultID = choiceResultID;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

}
