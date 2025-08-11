/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "AIMS_AUCTIONEER_HIS")
public class AuctioneerHistory {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_AUCTIONEER_HIS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "AUCTIONEER_ID", nullable = false)
    private Long auctioneerID;

    @Column(name = "S_DATE", nullable = false)
    private Timestamp startDate;

    @Column(name = "E_DATE", nullable = false)
    private Timestamp endDate;

    @Column(name = "AUCTIONEER_TYPE")
    private Long auctioneerType;

    @Column(name = "ORG_ID", nullable = false)
    private Long orgID;

    @Column(name = "INFO")
    private String info;

    @Column(name = "REWARD_INFO")
    private String rewardInfo;

    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    @Column(name = "SOURCE_LOG")
    private Long sourceLog;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CER_CODE")
    private String cerCode;

    @Column(name = "CARD_CODE")
    private String cardCode;
    @Column(name = "ACT_TYPE")
    private Long actType;

    @Column(name = "FILE_ID")
    private Long fileId;

    @Column(name = "ACT_DESC")
    private String actDesc;
            
    @Column(name = "NUMBER_OF_DECISION")
    private String numberOfDecision;

    @Column(name = "DATE_OF_DECISION")
    private Timestamp dateOfDecision;

    @Column(name = "EFFECTIVE_DATE")
    private Timestamp effectiveDate;

    @Column(name = "OTHER_INFO")
    private String otherInfo;
    
    @Column(name = "SCAN_TYPE")
    private Long scanType;
    
    
    public AuctioneerHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctioneerID() {
        return auctioneerID;
    }

    public void setAuctioneerID(Long auctioneerID) {
        this.auctioneerID = auctioneerID;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Long getAuctioneerType() {
        return auctioneerType;
    }

    public void setAuctioneerType(Long auctioneerType) {
        this.auctioneerType = auctioneerType;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(String rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Long getSourceLog() {
        return sourceLog;
    }

    public void setSourceLog(Long sourceLog) {
        this.sourceLog = sourceLog;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCerCode() {
        return cerCode;
    }

    public void setCerCode(String cerCode) {
        this.cerCode = cerCode;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Long getActType() {
        return actType;
    }

    public void setActType(Long actType) {
        this.actType = actType;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getNumberOfDecision() {
        return numberOfDecision;
    }

    public void setNumberOfDecision(String numberOfDecision) {
        this.numberOfDecision = numberOfDecision;
    }

    public Timestamp getDateOfDecision() {
        return dateOfDecision;
    }

    public void setDateOfDecision(Timestamp dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
    }

    public Timestamp getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Timestamp effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Long getScanType() {
        return scanType;
    }

    public void setScanType(Long scanType) {
        this.scanType = scanType;
    }
    
    
    
}
