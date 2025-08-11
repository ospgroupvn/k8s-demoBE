/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;

import com.osp.bttp.common.contants.ConstantsDGTS;
import com.osp.bttp.dao.model.mview.FileUpload;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author admin
 */
@Data
public class ViewDetailAuctioneerHistory {
    private Long id;
    private Long auctioneerID;    
    private Long auctioneerType;
    private Long orgID;
    private String cerCode;
    private String cardCode;  
    private Long actType;
    private String actTypeStr;
    private String numberOfDecision;
    private Timestamp dateOfDecision;
    private Timestamp effectiveDate;
    private String orgName;
    private String info;
    private String otherInfo;
    public List<FileUpload> listFileUpload;

    public ViewDetailAuctioneerHistory() {
    }

    public ViewDetailAuctioneerHistory(Long id, Long auctioneerID, Long auctioneerType, Long orgID, String cerCode,
                                       String cardCode, Long actType, String numberOfDecision, Timestamp dateOfDecision, Timestamp effectiveDate, String orgName) {
        this.id = id;
        this.auctioneerID = auctioneerID;
        this.auctioneerType = auctioneerType;
        this.orgID = orgID;
        this.cerCode = cerCode;
        this.cardCode = cardCode;
        this.actType = actType;
        this.actTypeStr = ConstantsDGTS.getStrActType(actType);
        this.numberOfDecision = numberOfDecision;
        this.dateOfDecision = dateOfDecision;
        this.effectiveDate = effectiveDate;
        this.orgName = orgName;
    }

    public ViewDetailAuctioneerHistory(Long id, Long auctioneerID, Long auctioneerType, Long orgID, String cerCode, String cardCode, Long actType, String numberOfDecision, Timestamp dateOfDecision, Timestamp effectiveDate, String orgName, String info, String otherInfo) {
        this.id = id;
        this.auctioneerID = auctioneerID;
        this.auctioneerType = auctioneerType;
        this.orgID = orgID;
        this.cerCode = cerCode;
        this.cardCode = cardCode;
        this.actType = actType;
        this.numberOfDecision = numberOfDecision;
        this.dateOfDecision = dateOfDecision;
        this.effectiveDate = effectiveDate;
        this.orgName = orgName;
        this.info = info;
        this.otherInfo = otherInfo;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<FileUpload> getListFileUpload() {
        return listFileUpload;
    }

    public void setListFileUpload(List<FileUpload> listFileUpload) {
        this.listFileUpload = listFileUpload;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

  
    
}
