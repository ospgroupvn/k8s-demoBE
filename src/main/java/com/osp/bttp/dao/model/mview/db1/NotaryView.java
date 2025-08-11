/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
public class NotaryView implements Serializable {

    @Id
    private Long id;
    @Size(max = 200)
    @Column(name = "NAME")
    private String name;
    @Column(name = "SEX")
    private Long sex;
    @Column(name = "BIRTH_DAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    @Size(max = 15)
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "ID_NO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    @Size(max = 500)
    @Column(name = "ADDRESS_ID_NO")
    private String addressIdNo;
    @Size(max = 500)
    @Column(name = "ADDRESS_RESIDENT")
    private String addressResident;
    @Column(name = "ADDRESS_RESIDENT_ID")
    private Long addressResidentId;
    @Size(max = 500)
    @Column(name = "ADDRESS_NOW")
    private String addressNow;
    @Column(name = "ADDRESS_NOW_ID")
    private Long addressNowId;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;

    @Transient
    private String numberCad;
    @Transient
    private String typeNotaryInfo;

    @Transient
    private String nameAddressResident;
    @Transient
    private String nameAddressNow;

    public NotaryView() {
    }

    public String getNameAddressResident() {
        return nameAddressResident;
    }

    public void setNameAddressResident(String nameAddressResident) {
        this.nameAddressResident = nameAddressResident;
    }

    public String getNameAddressNow() {
        return nameAddressNow;
    }

    public void setNameAddressNow(String nameAddressNow) {
        this.nameAddressNow = nameAddressNow;
    }

    public NotaryView(Long id) {
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

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddressIdNo() {
        return addressIdNo;
    }

    public void setAddressIdNo(String addressIdNo) {
        this.addressIdNo = addressIdNo;
    }

    public String getAddressResident() {
        return addressResident;
    }

    public void setAddressResident(String addressResident) {
        this.addressResident = addressResident;
    }

    public Long getAddressResidentId() {
        return addressResidentId;
    }

    public void setAddressResidentId(Long addressResidentId) {
        this.addressResidentId = addressResidentId;
    }

    public String getAddressNow() {
        return addressNow;
    }

    public void setAddressNow(String addressNow) {
        this.addressNow = addressNow;
    }

    public Long getAddressNowId() {
        return addressNowId;
    }

    public void setAddressNowId(Long addressNowId) {
        this.addressNowId = addressNowId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public String getNumberCad() {
        return numberCad;
    }

    public void setNumberCad(String numberCad) {
        this.numberCad = numberCad;
    }

    public Date getIdNoDate() {
        return idNoDate;
    }

    public void setIdNoDate(Date idNoDate) {
        this.idNoDate = idNoDate;
    }

    public String getTypeNotaryInfo() {
        return typeNotaryInfo;
    }

    public void setTypeNotaryInfo(String typeNotaryInfo) {
        this.typeNotaryInfo = typeNotaryInfo;
    }

    public String getStatusBefor() {
        return statusBefor;
    }

    public void setStatusBefor(String statusBefor) {
        this.statusBefor = statusBefor;
    }
}
