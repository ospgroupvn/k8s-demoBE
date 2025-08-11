/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "DM_ADMINISTRATION")
public class DmAdministration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="DM_ADMINISTRATION_SEQ", sequenceName="DM_ADMINISTRATION_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_ADMINISTRATION_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "IS_CENTER")
    private Long isCenter;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "TYPE")
    private Long type;
    @Size(max = 1000)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 20)
    @Column(name = "PHONE")
    private String phone;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 50)
    @Column(name = "WEBSITE")
    private String website;
    @Size(max = 20)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "UPDATED_BY")
    private Long updatedBy;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_BY")
    private Long createBy;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @Transient
    private String createByStr;
    @Transient
    private String updatedByStr;
    
    public DmAdministration() {
    }

    public String getCreateByStr() {
        return createByStr;
    }

    public void setCreateByStr(String createByStr) {
        this.createByStr = createByStr;
    }

    public String getUpdatedByStr() {
        return updatedByStr;
    }

    public void setUpdatedByStr(String updatedByStr) {
        this.updatedByStr = updatedByStr;
    }

    public DmAdministration(Long id) {
        this.id = id;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getIsCenter() {
        return isCenter;
    }

    public void setIsCenter(Long isCenter) {
        this.isCenter = isCenter;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
}
