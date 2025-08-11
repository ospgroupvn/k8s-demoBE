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
@Table(name = "NOTARY_PENALIZE")
public class NotaryPenalize implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_PENALIZE_SEQ", sequenceName="NOTARY_PENALIZE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_PENALIZE_SEQ")
    private Long id;
    @Column(name = "ADMINISTRATION_ID_PENALTY")
    private Long administrationIdPenalty;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "ORG_NOTARY_ID")
    private Long orgNotaryId;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "TYPE_PENALIZE")
    private Long typePenalize;
    @Size(max = 2000)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "LEVER_PENALIZE")
    private Long leverPenalize;
    @Column(name = "ADDITIONAL_PENALTY")
    private Long additionalPenalty;
    @Column(name = "MONEY_PENALTY")
    private Long moneyPenalty;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public NotaryPenalize() {
    }

    public NotaryPenalize(Long id) {
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

    public Long getOrgNotaryId() {
        return orgNotaryId;
    }

    public void setOrgNotaryId(Long orgNotaryId) {
        this.orgNotaryId = orgNotaryId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getTypePenalize() {
        return typePenalize;
    }

    public void setTypePenalize(Long typePenalize) {
        this.typePenalize = typePenalize;
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

    public Long getLeverPenalize() {
        return leverPenalize;
    }

    public void setLeverPenalize(Long leverPenalize) {
        this.leverPenalize = leverPenalize;
    }

    public Long getAdditionalPenalty() {
        return additionalPenalty;
    }

    public void setAdditionalPenalty(Long additionalPenalty) {
        this.additionalPenalty = additionalPenalty;
    }

    public Long getMoneyPenalty() {
        return moneyPenalty;
    }

    public void setMoneyPenalty(Long moneyPenalty) {
        this.moneyPenalty = moneyPenalty;
    }

    public Long getAdministrationIdPenalty() {
        return administrationIdPenalty;
    }

    public void setAdministrationIdPenalty(Long administrationIdPenalty) {
        this.administrationIdPenalty = administrationIdPenalty;
    }
}
