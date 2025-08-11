/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "NOTARY_INFO_HIS")
public class NotaryInfoHis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_HIS")
    @SequenceGenerator(name="NOTARY_INFO_HIS_SEQ", sequenceName="NOTARY_INFO_HIS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_INFO_HIS_SEQ")
    private Long idHis;
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SEX")
    private Long sex;
    @Column(name = "BIRTH_DAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "ADDRESS_ID_NO")
    private String addressIdNo;
    @Column(name = "ADDRESS_RESIDENT")
    private String addressResident;
    @Column(name = "ADDRESS_RESIDENT_ID")
    private Long addressResidentId;
    @Column(name = "ADDRESS_NOW")
    private String addressNow;
    @Column(name = "ADDRESS_NOW_ID")
    private Long addressNowId;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "ID_NO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    @Column(name = "TYPE_ACTION")
    private String typeAction;
    @Column(name = "OBJ_ID")
    private Long objId;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    public NotaryInfoHis() {
    }

    public Long getIdHis() {
        return idHis;
    }

    public void setIdHis(Long idHis) {
        this.idHis = idHis;
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

    public Date getIdNoDate() {
        return idNoDate;
    }

    public void setIdNoDate(Date idNoDate) {
        this.idNoDate = idNoDate;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
}
