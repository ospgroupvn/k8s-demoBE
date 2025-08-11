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
@Table(name = "AIMS_AUC_TRAINEE")

public class AuTrainee {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_AUC_TRAINEE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;

    @Column(name = "FULLNAME")
    private String fullname;

    @Column(name = "SEX")
    private Long sex;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "ID_CODE")
    private String idCode;

    @Column(name = "ADDR_PERMANENT")
    private String addrPermanent;

    @Column(name = "ADDR_CURRENT")
    private String addrCurrent;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TEL_NUMBER")
    private String telNumber;

    @Column(name = "ADDR_CITY_ID")
    private Long cityID;

    @Column(name = "ADDR_DISTRISCT_ID")
    private Long districtID;

    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ID_ORG")
    private Long idOrg;
    @Column(name = "ID_AUCTIONEER")
    private Long idAuctioneer;

    @Column(name = "NUMBER_CERTIFICATE")
    private String numberCertificate;
    @Column(name = "GRADUATE_DATE")
    private String graduateDate;
    @Column(name = "DATE_START")
    private Timestamp dateStart;

    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "ID_DOI")
    private String idDOI;

    @Column(name = "ID_POI")
    private String idPOI;

    public AuTrainee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getAddrPermanent() {
        return addrPermanent;
    }

    public void setAddrPermanent(String addrPermanent) {
        this.addrPermanent = addrPermanent;
    }

    public String getAddrCurrent() {
        return addrCurrent;
    }

    public void setAddrCurrent(String addrCurrent) {
        this.addrCurrent = addrCurrent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Long getCityID() {
        return cityID;
    }

    public void setCityID(Long cityID) {
        this.cityID = cityID;
    }

    public Long getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Long districtID) {
        this.districtID = districtID;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getIdOrg() {
        return idOrg;
    }

    public void setIdOrg(Long idOrg) {
        this.idOrg = idOrg;
    }

    public Long getIdAuctioneer() {
        return idAuctioneer;
    }

    public void setIdAuctioneer(Long idAuctioneer) {
        this.idAuctioneer = idAuctioneer;
    }

    public String getNumberCertificate() {
        return numberCertificate;
    }

    public void setNumberCertificate(String numberCertificate) {
        this.numberCertificate = numberCertificate;
    }

    public String getGraduateDate() {
        return graduateDate;
    }

    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getIdDOI() {
        return idDOI;
    }

    public void setIdDOI(String idDOI) {
        this.idDOI = idDOI;
    }

    public String getIdPOI() {
        return idPOI;
    }

    public void setIdPOI(String idPOI) {
        this.idPOI = idPOI;
    }

    
    
}
