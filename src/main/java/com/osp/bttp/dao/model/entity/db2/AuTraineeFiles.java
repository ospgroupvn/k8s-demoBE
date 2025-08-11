package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_TRAINEE_FILES")
public class AuTraineeFiles {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_TRAINEE_FILES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID")
    private Long id;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "TRAINEE_HIS_ID")
    private Long traineeHisID;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    public AuTraineeFiles() {
    }

    public AuTraineeFiles(Long fileId, Long traineeHisID) {
        this.fileId = fileId;
        this.traineeHisID = traineeHisID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getTraineeHisID() {
        return traineeHisID;
    }

    public void setTraineeHisID(Long traineeHisID) {
        this.traineeHisID = traineeHisID;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

}
