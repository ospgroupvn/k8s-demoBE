package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Entity
    @Table(name = "AIMS_ORG_HIS")
    public class OrganizationHis {
  @Id
        @SequenceGenerator(name="AIMS_ORG_HIS_SEQ", sequenceName="AIMS_ORG_HIS_SEQ",allocationSize=1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIMS_ORG_HIS_SEQ")
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
    @Column(name = "GEN_DATE")
    private Timestamp genDate;


    @Column(name = "ORG_INFO")
    private String orgInfo;

    @Column(name = "LICENSE_EXPDATE")
    private Date licenseExpDate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CONTACT_INFO")
    private String contacInfor;
    @Column(name = "ACT_TYPE" )
    private Long actType;
    @Column(name = "ACT_DESC" )
    private String des;
    @Column(name = "ORG_ID",nullable = false)
    private Long orgId;

    @Column(name = "FONE_NUMBER")
    private String foneNumber;

    @Column(name = "FAX_NUMBER")
    private String faxNumber;

    @Column(name = "ADDR", nullable = false)
    private String address;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADDR_DISTRICT_ID")
private Long districtId;
    @Column(name = "ADDR_CITY_ID")
    private Long cityId;
    @Column(name="EFF_DATE")
    private Date effDate;
    @Column(name="SCAN_ORG")
    private Long scanOrg;
     @Column(name="MANAGER_OTHER")
     private String managerOther;
    @Column(name="ORTHER_CITY")
    private Long ortherCity;
    @Column(name="USER_CREATED")
    private Long userCreate;
    @Column(name="USER_UPDATED")
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

    public String getManagerOther() {
        return managerOther;
    }

    public void setManagerOther(String managerOther) {
        this.managerOther = managerOther;
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
    public OrganizationHis() {
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

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public String getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(String orgInfo) {
        this.orgInfo = orgInfo;
    }

    public Date getLicenseExpDate() {
        return licenseExpDate;
    }

    public void setLicenseExpDate(Date licenseExpDate) {
        this.licenseExpDate = licenseExpDate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getContacInfor() {
        return contacInfor;
    }

    public void setContacInfor(String contacInfor) {
        this.contacInfor = contacInfor;
    }

    public Long getActType() {
        return actType;
    }

    public void setActType(Long actType) {
        this.actType = actType;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public OrganizationHis(Long orgType, Long orgRoot, String fullname, String licenseNo, Date licenseDate, Timestamp genDate, String orgInfo, Date licenseExpDate, Long status, String contacInfor, Long actType, String des, Long orgId) {
        this.orgType = orgType;
        this.orgRoot = orgRoot;
        this.fullname = fullname;
        this.licenseNo = licenseNo;
        this.licenseDate = licenseDate;
        this.genDate = genDate;
        this.orgInfo = orgInfo;
        this.licenseExpDate = licenseExpDate;
        this.status = status;
        this.contacInfor = contacInfor;
        this.actType = actType;
        this.des = des;
        this.orgId = orgId;
    }

    public Long getScanOrg() {
        return scanOrg;
    }
    
    public void setScanOrg(Long scanOrg) {
        this.scanOrg = scanOrg;
    }
}
