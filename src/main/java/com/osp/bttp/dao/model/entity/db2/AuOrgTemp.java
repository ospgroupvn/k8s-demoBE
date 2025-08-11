package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;

@Entity
@Table(name = "AIMS_ORG_TEMP")
public class AuOrgTemp {
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_ORG_TEMP_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "FULLNAME", nullable = false)
    private String fullname;
    @Column(name = "FONE_NUMBER")
    private String foneNumber;

    @Column(name = "FAX_NUMBER")
    private String faxNumber;

    @Column(name = "ADRR", nullable = false)
    private String address;

    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;
    @Column(name = "ADDR_CITY_ID")
    private Long cityId;

    @Column(name = "ORG_INFO")
    private String orgInfo;
    @Column(name="MANAGER_OTHER")
    private String managerOther;

    @Column(name="ORTHER_CITY")
    private Long ortherCity;
    @Column(name="AUCTIONEER_ID")
    private Long auctioneerId;
    @Column(name="ORG_HIS")
    private Long orgHis;

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

    public String getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(String orgInfo) {
        this.orgInfo = orgInfo;
    }

    public String getManagerOther() {
        return managerOther;
    }

    public void setManagerOther(String managerOther) {
        this.managerOther = managerOther;
    }

    public Long getOrtherCity() {
        return ortherCity;
    }

    public void setOrtherCity(Long ortherCity) {
        this.ortherCity = ortherCity;
    }

    public Long getAuctioneerId() {
        return auctioneerId;
    }

    public void setAuctioneerId(Long auctioneerId) {
        this.auctioneerId = auctioneerId;
    }

    public Long getOrgHis() {
        return orgHis;
    }

    public void setOrgHis(Long orgHis) {
        this.orgHis = orgHis;
    }
}
