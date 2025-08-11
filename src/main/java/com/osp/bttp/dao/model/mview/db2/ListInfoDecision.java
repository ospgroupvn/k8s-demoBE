/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

/**
 *
 * @author admin
 */
@Entity
@Data
public class ListInfoDecision {

    @Id  
    private Long id;

    @Column(name = "AUCTIONEER_ID", nullable = false)
    private Long auctioneerID; 

    @Column(name = "AUCTIONEER_TYPE")
    private Long auctioneerType;
    
    @Column(name = "ORG_ID", nullable = false)
    private Long orgID;
   
    @Column(name = "CER_CODE")
    private String cerCode;

    @Column(name = "CARD_CODE")
    private String cardCode;
    
    @Column(name = "ACT_TYPE")    
    private Long actType;
     
    @Column(name = "NUMBER_OF_DECISION")
    private String numberOfDecision;
    
    @Column(name = "DATE_OF_DECISION")
    private Timestamp dateOfDecision;
    
    @Column(name = "EFFECTIVE_DATE")
    private Timestamp effectiveDate;
    
    @Column(name="ORG_NAME")
    private String orgName;
    

    public ListInfoDecision() {
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

   
}
