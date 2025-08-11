package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_DEPT_OF_JUSTICE")
public class AuDepOfJustice {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_DEP_OF_JUSTICE_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "FULLNAME")
    private String fullName;
    @Column(name = "LICENSE_NO")
    private String licenseNo;
    @Column(name = "LICENSE_DATE")
    private Date licenseDate;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "FAX_NUMBER")
    private String faxNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "WEB_SITE")
    private String webSite;
    @Column(name = "DIRECTOR")
    private String director;
    @Column(name = "status")
    private Long status;
    @Column(name = "ROLE_INFO")
    private String roleInfo;
    @Column(name = "OTHER_INFO")
    private String otherInfo;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "LAST_UPDATED")
    private Date lastUpdate;
    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;
    @Column(name = "ADDR_CITY_ID")
    private Long cityId;
    @Column(name = "USER_CREATE")
    private Long userCreate;
    @Column(name = "LAST_USER_UPDATE")
    private Long lastUserUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(String roleInfo) {
        this.roleInfo = roleInfo;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    public Long getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(Long userCreate) {
        this.userCreate = userCreate;
    }

    public Long getLastUserUpdate() {
        return lastUserUpdate;
    }

    public void setLastUserUpdate(Long lastUserUpdate) {
        this.lastUserUpdate = lastUserUpdate;
    }
}
