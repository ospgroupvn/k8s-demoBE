package com.osp.bttp.dao.model.mview.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class OrgNotaryView implements Serializable {

    @Id
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "NOTARY_ID_OFFICE_CHIEF")
    private Long notaryIdOfficeChief;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "ADDRESS_ID")
    private Long addressId;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getNotaryIdOfficeChief() {
        return notaryIdOfficeChief;
    }

    public void setNotaryIdOfficeChief(Long notaryIdOfficeChief) {
        this.notaryIdOfficeChief = notaryIdOfficeChief;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getAdministrationId() {
        return administrationId;
    }

    public void setAdministrationId(Long administrationId) {
        this.administrationId = administrationId;
    }

    public String getStatusBefor() {
        return statusBefor;
    }

    public void setStatusBefor(String statusBefor) {
        this.statusBefor = statusBefor;
    }

}
