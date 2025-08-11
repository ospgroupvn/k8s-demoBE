/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NOTARY_REG_PRACTICE")
public class NotaryRegPractice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_REG_PRACTICE_SEQ", sequenceName="NOTARY_REG_PRACTICE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_REG_PRACTICE_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "REASON")
    private String reason;
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
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "NUMBER_CAD")
    private String numberCad;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "NOTARY_REQ")
    private Long notaryReq;
    @Column(name = "DATE_REQ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReq;
    @Column(name = "TYPE_NOTARY_INFO")
    private Long typeNotaryInfo;

    public NotaryRegPractice() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getNotaryReq() {
        return notaryReq;
    }

    public void setNotaryReq(Long notaryReq) {
        this.notaryReq = notaryReq;
    }

    public Date getDateReq() {
        return dateReq;
    }

    public void setDateReq(Date dateReq) {
        this.dateReq = dateReq;
    }
    
    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public String getNumberCad() {
        return numberCad;
    }

    public void setNumberCad(String numberCad) {
        this.numberCad = numberCad;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Long getTypeNotaryInfo() {
        return typeNotaryInfo;
    }

    public void setTypeNotaryInfo(Long typeNotaryInfo) {
        this.typeNotaryInfo = typeNotaryInfo;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaryRegPractice)) {
            return false;
        }
        NotaryRegPractice other = (NotaryRegPractice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
