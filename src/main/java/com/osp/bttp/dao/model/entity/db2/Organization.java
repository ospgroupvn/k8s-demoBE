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
@Table(name = "AIMS_ORGANIZATION")
public class Organization {


    @Id
    @SequenceGenerator(name="AIMS_ORGANIZATION_SEQ", sequenceName="AIMS_ORGANIZATION_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIMS_ORGANIZATION_SEQ")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "ORG_TYPE", nullable = false)
    private Long orgType;

    @Column(name = "ORG_ROOT")
    private Long orgRoot;

    @Column(name = "FULLNAME", nullable = false)
    private String fullname;

    @Column(name = "LICENSE_NO", nullable = false)
    private String licenseNo;

    @Column(name = "LICENSE_DATE", nullable = false)
    private Date licenseDate;

    @Column(name = "FONE_NUMBER")
    private String foneNumber;

    @Column(name = "FAX_NUMBER")
    private String faxNumber;

    @Column(name = "ADDR", nullable = false)
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GEN_DATE", nullable = false)
    private Timestamp genDate;

    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;

    @Column(name = "ORG_INFO")
    private String orgInfo;

    @Column(name = "LICENSE_EXPDATE")
    private Date licenseExpDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "COMPLEX_STATUS")
    private Long complexStatus;
    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;
    @Column(name = "ADDR_CITY_ID")
    private Long cityId;
    @Column(name="EFF_DATE")
    private Date effDate;
     @Column(name="IS_PUBLISH")
    private Long isPublish;
     @Column(name="MANAGER_OTHER")
     private String managerOther;

    @Column(name="ORTHER_CITY")
    private Long ortherCity;
    @Column(name="DES")
    private String des;
    @Column(name="USER_CREATE")
    private Long userCreate;
    @Column(name="USER_UPDATE")
    private Long userUpdated;

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Long getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(Long userUpdated) {
        this.userUpdated = userUpdated;
    }

    public Long getOrtherCity() {
        return ortherCity;
    }

    public void setOrtherCity(Long ortherCity) {
        this.ortherCity = ortherCity;
    }

    public Organization() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgType() {
        return orgType;
    }

    public void setOrgType(Long orgType) {
        this.orgType = orgType;
    }

    public Long getOrgRoot() {
        return orgRoot;
    }

    public void setOrgRoot(Long orgRoot) {
        this.orgRoot = orgRoot;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public Date getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(Date licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getFoneNumber() {
        return foneNumber;
    }

    public void setFoneNumber(String foneNumber) {
        this.foneNumber = foneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getLicenseExpDate() {
        return licenseExpDate;
    }

    public void setLicenseExpDate(Date licenseExpDate) {
        this.licenseExpDate = licenseExpDate;
    }

    public String getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(String orgInfo) {
        this.orgInfo = orgInfo;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getComplexStatus() {
        return complexStatus;
    }

    public void setComplexStatus(Long complexStatus) {
        this.complexStatus = complexStatus;
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

    public Date getEffDate() {
        return effDate;
    }

    public void setEffDate(Date effDate) {
        this.effDate = effDate;
    }

    public Long getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Long isPublish) {
        this.isPublish = isPublish;
    }

    public String getManagerOther() {
        return managerOther;
    }

    public void setManagerOther(String managerOther) {
        this.managerOther = managerOther;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
