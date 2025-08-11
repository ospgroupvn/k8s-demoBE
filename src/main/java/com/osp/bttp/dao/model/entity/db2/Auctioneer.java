/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "AIMS_AUCTIONEER")
public class Auctioneer {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_AUCTIONEER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "AUCTIONEER_TYPE", nullable = false)
    private Long auctioneerType;

    @Column(name = "GEN_DATE", nullable = false)
    private Timestamp genDate;

    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;

    @Column(name = "ID_CODE", nullable = false)
    private String idCode;

    @Column(name = "ID_TYPE")
    private String idType;

    @Column(name = "FULLNAME", nullable = false)
    private String fullname;

    @Column(name = "DOB", nullable = false)//date of birth
    private String dob;

    @Column(name = "SEX")
    private Long sex;

    @Column(name = "ADDR_PERMANENT", nullable = false)
    private String addPermanent;

    @Column(name = "ADDR_CURRENT", nullable = false)
    private String addCurrent;

    @Column(name = "TEL_NUMBER")
    private String telNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CER_CODE")
    private String cerCode;

    @Column(name = "CARD_CODE")
    private String cardCode;

    @Column(name = "OTHER_INFO")
    private String otherInfo;

    @Column(name = "ID_DOI")
    private Date idDOI;

    @Column(name = "ID_POI")
    private String idPOI;

    @Column(name = "CER_DOI")
    private Date cerDOI;

    @Column(name = "CARD_POI")
    private String cardPOI;

    @Column(name = "CARD_DOI")
    private Date cardDOI;

    @Column(name = "AUCTIONEER_STATUS")
    private Long auctioneerStatus;

    @Column(name = "IS_PUBLISH")
    private Long isPublish;

    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;

    @Column(name = "ADDR_CITY_ID")
    private Long cityId;
    

    @Column(name = "CER_STATUS")
    private Long cerStatus;

    @Column(name = "CARD_STATUS")
    private Long cardStatus;

    @Column(name = "WARNING")
    private String warning;
    @Column(name = "SCAN_TYPE")
    private Long scanType;
    
    @Column(name = "ORG_ID")
    private Long orgId;
    
    @Column(name = "USER_UPDATE")
    private String userUpdate;

    @Column(name = "IS_PIC")
    private Long isPIC;

    public Auctioneer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctioneerType() {
        return auctioneerType;
    }

    public void setAuctioneerType(Long auctioneerType) {
        this.auctioneerType = auctioneerType;
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

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public String getAddPermanent() {
        return addPermanent;
    }

    public void setAddPermanent(String addPermanent) {
        this.addPermanent = addPermanent;
    }

    public String getAddCurrent() {
        return addCurrent;
    }

    public void setAddCurrent(String addCurrent) {
        this.addCurrent = addCurrent;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Date getIdDOI() {
        return idDOI;
    }

    public void setIdDOI(Date idDOI) {
        this.idDOI = idDOI;
    }

    public String getIdPOI() {
        return idPOI;
    }

    public void setIdPOI(String idPOI) {
        this.idPOI = idPOI;
    }

    public Date getCerDOI() {
        return cerDOI;
    }

    public void setCerDOI(Date cerDOI) {
        this.cerDOI = cerDOI;
    }

    public String getCardPOI() {
        return cardPOI;
    }

    public void setCardPOI(String cardPOI) {
        this.cardPOI = cardPOI;
    }

    public Date getCardDOI() {
        return cardDOI;
    }

    public void setCardDOI(Date cardDOI) {
        this.cardDOI = cardDOI;
    }

    public Long getAuctioneerStatus() {
        return auctioneerStatus;
    }

    public void setAuctioneerStatus(Long auctioneerStatus) {
        this.auctioneerStatus = auctioneerStatus;
    }

    public Long getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Long isPublish) {
        this.isPublish = isPublish;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCerStatus() {
        return cerStatus;
    }

    public void setCerStatus(Long cerStatus) {
        this.cerStatus = cerStatus;
    }

    public Long getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Long cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public Long getScanType() {
        return scanType;
    }

    public void setScanType(Long scanType) {
        this.scanType = scanType;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Long getIsPIC() {
        return isPIC;
    }

    public void setIsPIC(Long isPIC) {
        this.isPIC = isPIC;
    }
}
