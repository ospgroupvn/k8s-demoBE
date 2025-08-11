/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DM_DOCUMENT")
public class DmDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="DOCUMENT_SEQ", sequenceName="DOCUMENT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
    private Long id;
    @Column(name = "DISPATCH_CODE")
    private String dispatchCode;
    @Column(name = "DATE_SIGN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    @Column(name = "SIGNER")
    private String signer;
    @Column(name = "UNIT_SIGN")
    private String unitSign;
    @Column(name = "LINK_FILE")
    private String linkFile;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Column(name = "ACTIVE")
    private Long active;
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
    @Column(name = "DATE_GRANT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateGrant;

    public DmDocument() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public String getUnitSign() {
        return unitSign;
    }

    public void setUnitSign(String unitSign) {
        this.unitSign = unitSign;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDateGrant() {
        return dateGrant;
    }

    public void setDateGrant(Date dateGrant) {
        this.dateGrant = dateGrant;
    }
}
