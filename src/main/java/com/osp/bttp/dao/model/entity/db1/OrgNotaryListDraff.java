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
 * @author sangnk
 */
@Entity
@Table(name = "ORG_NOTARY_LIST_DRAFF")
public class OrgNotaryListDraff implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="ORG_NOTARY_LIST_DRAFF_SEQ", sequenceName="ORG_NOTARY_LIST_DRAFF_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_LIST_DRAFF_SEQ")
    private Long id;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "GEN_DAE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDae;
    @Column(name = "LASTUPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastupdate;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "ORG_NOTARY_TRANSFER_DETAIL_ID")
    private Long orgNotaryTransferDetailId;
    @Column(name = "TYPE_NOTARY_INFO")
    private Long typeNotaryInfo;
    
    public OrgNotaryListDraff() {
    }

    public Long getTypeNotaryInfo() {
        return typeNotaryInfo;
    }

    public void setTypeNotaryInfo(Long typeNotaryInfo) {
        this.typeNotaryInfo = typeNotaryInfo;
    }
    
    public OrgNotaryListDraff(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }
    
    public Date getGenDae() {
        return genDae;
    }

    public void setGenDae(Date genDae) {
        this.genDae = genDae;
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
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

    public Long getOrgNotaryTransferDetailId() {
        return orgNotaryTransferDetailId;
    }

    public void setOrgNotaryTransferDetailId(Long orgNotaryTransferDetailId) {
        this.orgNotaryTransferDetailId = orgNotaryTransferDetailId;
    }
    
}
