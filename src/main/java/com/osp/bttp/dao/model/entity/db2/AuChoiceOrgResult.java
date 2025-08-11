package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_CHOICE_RESULT")
public class AuChoiceOrgResult {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_RESULT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_ORG_ID", nullable = false)
    private Long choiceOrgID;
    @Column(name = "ORG_ID", nullable = false)
    private Long orgID;
    @Column(name = "PUBLISH_STATUS")
    private Long publishStatus;
    @Column(name = "PUBLISH_INFO")
    private String publishInfo;
    @Column(name = "PUBLISH_TIME")
    private Timestamp publishTime;
    @Column(name = "CONFIRM_TIME")
    private Timestamp confirmTime;
    @Column(name = "REQ_CONFIRM_SOURCE")
    private Long reqConfirmSource;
    @Column(name = "REQ_CONFIRM_TIME")
    private Timestamp reqConfirmTime;
    @Column(name = "FILE_ID")
    private Long fileID;
    @Column(name = "USER_ID_REQ", nullable = false)
    private Long userIDReq;
    @Column(name = "USER_ID_CONFIRM", nullable = false)
    private Long userIDConfirm;   
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;
    @Column(name = "VERIFIED_STATUS")
    private Long verifiedStatus;
    @Column(name = "ROOT_ID")
    private Long rootId;

    public AuChoiceOrgResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChoiceOrgID() {
        return choiceOrgID;
    }

    public void setChoiceOrgID(Long choiceOrgID) {
        this.choiceOrgID = choiceOrgID;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public Long getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Long publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(String publishInfo) {
        this.publishInfo = publishInfo;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Long getReqConfirmSource() {
        return reqConfirmSource;
    }

    public void setReqConfirmSource(Long reqConfirmSource) {
        this.reqConfirmSource = reqConfirmSource;
    }

    public Timestamp getReqConfirmTime() {
        return reqConfirmTime;
    }

    public void setReqConfirmTime(Timestamp reqConfirmTime) {
        this.reqConfirmTime = reqConfirmTime;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public Long getUserIDReq() {
        return userIDReq;
    }

    public void setUserIDReq(Long userIDReq) {
        this.userIDReq = userIDReq;
    }

    public Long getUserIDConfirm() {
        return userIDConfirm;
    }

    public void setUserIDConfirm(Long userIDConfirm) {
        this.userIDConfirm = userIDConfirm;
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

    public Long getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(Long verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }
}
