/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

/**
 *
 * @author admin
 */
@Entity
public class AuctioneerHistoryView {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "AUCTIONEER_ID")
    private Long auctioneerID;

    @Column(name = "S_DATE")
    private String startDate;

    @Column(name = "E_DATE")
    private String endDate;

    @Column(name = "AUCTIONEER_TYPE")
    private Long auctioneerType;

    @Column(name = "ORG_ID")
    private Long orgID;

    @Column(name = "INFO")
    private String info;

    @Column(name = "REWARD_INFO")
    private String rewardInfo;

    @Column(name = "GEN_DATE")
    private Date genDate;

    @Column(name = "SOURCE_LOG")
    private Long sourceLog;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CER_CODE")
    private String cerCode;

    @Column(name = "CARD_CODE")
    private String cardCode;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "ORG_ADDRESS")
    private String orgAddress;   

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }
  
}
