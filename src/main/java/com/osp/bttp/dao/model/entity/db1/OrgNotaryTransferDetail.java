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
@Table(name = "ORG_NOTARY_TRANSFER_DETAIL")
public class OrgNotaryTransferDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="ORG_NOTARY_TRANSFER_DETAIL_SEQ", sequenceName="ORG_NOTARY_TRANSFER_DETAIL_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_TRANSFER_DETAIL_SEQ")
    private Long id;
    @Column(name = "ORG_NOTARY_TYPE")
    private Long orgNotaryType;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "ORG_NOTARY_TRANSFER_ID")
    private Long orgNotaryTransferId;

    public OrgNotaryTransferDetail() {
    }

    public OrgNotaryTransferDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgNotaryType() {
        return orgNotaryType;
    }

    public void setOrgNotaryType(Long orgNotaryType) {
        this.orgNotaryType = orgNotaryType;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Long getOrgNotaryTransferId() {
        return orgNotaryTransferId;
    }

    public void setOrgNotaryTransferId(Long orgNotaryTransferId) {
        this.orgNotaryTransferId = orgNotaryTransferId;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgNotaryTransferDetail)) {
            return false;
        }
        OrgNotaryTransferDetail other = (OrgNotaryTransferDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
