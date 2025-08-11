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
@Table(name = "NOTARY_DISMISSED")
public class NotaryDismissed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_DISMISSED_SEQ", sequenceName="NOTARY_DISMISSED_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_DISMISSED_SEQ")
    private Long id;
    @Column(name = "ORG_NOTARY_ID")
    private Long orgNotaryId;
    @Size(max = 500)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "TYPE_DISMISSED")
    private Long typeDismissed;
    @Column(name = "STATUS_DISMISSED")
    private Long statusDismissed;
    @Column(name = "REASON_ID")
    private String reasonId;
    
    public NotaryDismissed() {
    }

    public Long getTypeDismissed() {
        return typeDismissed;
    }

    public void setTypeDismissed(Long typeDismissed) {
        this.typeDismissed = typeDismissed;
    }

    public Long getStatusDismissed() {
        return statusDismissed;
    }

    public void setStatusDismissed(Long statusDismissed) {
        this.statusDismissed = statusDismissed;
    }

    public NotaryDismissed(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgNotaryId() {
        return orgNotaryId;
    }

    public void setOrgNotaryId(Long orgNotaryId) {
        this.orgNotaryId = orgNotaryId;
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

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaryDismissed)) {
            return false;
        }
        NotaryDismissed other = (NotaryDismissed) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.osp.model.NotaryDismissed[ id=" + id + " ]";
    }
    
}
