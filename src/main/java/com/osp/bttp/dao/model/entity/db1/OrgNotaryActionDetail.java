/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "ORG_NOTARY_ACTION_DETAIL")
public class OrgNotaryActionDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @SequenceGenerator(name="ORG_NOTARY_ACTION_DETAIL_SEQ", sequenceName="ORG_NOTARY_ACTION_DETAIL_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_ACTION_DETAIL_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "ORG_NOTARY_ACTION_ID")
    private Long orgNotaryActionId;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "TYPE_NOTARY_INFO")
    private Long typeNotaryInfo;

    public OrgNotaryActionDetail() {
    }

    public OrgNotaryActionDetail(Long id) {
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

    public Long getOrgNotaryActionId() {
        return orgNotaryActionId;
    }

    public void setOrgNotaryActionId(Long orgNotaryActionId) {
        this.orgNotaryActionId = orgNotaryActionId;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getTypeNotaryInfo() {
        return typeNotaryInfo;
    }

    public void setTypeNotaryInfo(Long typeNotaryInfo) {
        this.typeNotaryInfo = typeNotaryInfo;
    }
}
