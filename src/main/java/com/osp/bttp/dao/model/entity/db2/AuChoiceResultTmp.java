package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_CHOICE_RESULT_TMP")
public class AuChoiceResultTmp {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_RESULT_TMP_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ORG_ID", nullable = false)
    private Long orgID;
    @Column(name = "CHOICE_ORG_ID", nullable = false)
    private Long choiceOrgID;
    @Column(name = "PUBLISH_INFO")
    private String publishInfo;
    @Column(name = "REQ_CONFIRM_TIME")
    private Timestamp reqConfirmTime;
    @Column(name = "REQ_CONFIRM_SOURCE")
    private Long reqConfirmSource;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;
    @Column(name = "USER_ID_REQ")
    private Long userIdReq;
    @Column(name = "FILE_ID")
    private Long fileID;

    public AuChoiceResultTmp() {
    }

    public AuChoiceResultTmp(Long orgID, Long choiceOrgID, Timestamp genDate, Timestamp lastUpdated, Long userIdReq) {
        this.orgID = orgID;
        this.choiceOrgID = choiceOrgID;
        this.genDate = genDate;
        this.lastUpdated = lastUpdated;
        this.userIdReq = userIdReq;
    }   

    public AuChoiceResultTmp(Long orgID, Long choiceOrgID, String publishInfo, Timestamp reqConfirmTime, Long reqConfirmSource, Timestamp genDate, Timestamp lastUpdated, Long userIdReq) {
        this.orgID = orgID;
        this.choiceOrgID = choiceOrgID;
        this.publishInfo = publishInfo;
        this.reqConfirmTime = reqConfirmTime;
        this.reqConfirmSource = reqConfirmSource;
        this.genDate = genDate;
        this.lastUpdated = lastUpdated;
        this.userIdReq = userIdReq;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public Long getChoiceOrgID() {
        return choiceOrgID;
    }

    public void setChoiceOrgID(Long choiceOrgID) {
        this.choiceOrgID = choiceOrgID;
    }

    public String getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(String publishInfo) {
        this.publishInfo = publishInfo;
    }

    public Timestamp getReqConfirmTime() {
        return reqConfirmTime;
    }

    public void setReqConfirmTime(Timestamp reqConfirmTime) {
        this.reqConfirmTime = reqConfirmTime;
    }

    public Long getReqConfirmSource() {
        return reqConfirmSource;
    }

    public void setReqConfirmSource(Long reqConfirmSource) {
        this.reqConfirmSource = reqConfirmSource;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getUserIdReq() {
        return userIdReq;
    }

    public void setUserIdReq(Long userIdReq) {
        this.userIdReq = userIdReq;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }
    
    

}
