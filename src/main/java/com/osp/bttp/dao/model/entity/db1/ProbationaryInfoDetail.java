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
@Table(name = "PROBATIONARY_INFO_DETAIL")
public class ProbationaryInfoDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="PROBATIONARY_INFO_DETAIL_SEQ", sequenceName="PROBATIONARY_INFO_DETAIL_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROBATIONARY_INFO_DETAIL_SEQ")
    private Long id;
    @Column(name = "PROBATIONARY_INFO_ID")
    private Long probationaryInfoId;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "ORG_NOTARY_INFO_TO")
    private Long orgNotaryInfoTo;
    @Column(name = "DATE_START")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "DATE_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "NOTE")
    private String note;
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
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "NOTARY_TUTORIAL_ID")
    private Long notaryTutorialId;

    public ProbationaryInfoDetail() {
    }

    public Long getOrgNotaryInfoTo() {
        return orgNotaryInfoTo;
    }

    public void setOrgNotaryInfoTo(Long orgNotaryInfoTo) {
        this.orgNotaryInfoTo = orgNotaryInfoTo;
    }

    public ProbationaryInfoDetail(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getProbationaryInfoId() {
        return probationaryInfoId;
    }

    public void setProbationaryInfoId(Long probationaryInfoId) {
        this.probationaryInfoId = probationaryInfoId;
    }

    public Long getNotaryTutorialId() {
        return notaryTutorialId;
    }

    public void setNotaryTutorialId(Long notaryTutorialId) {
        this.notaryTutorialId = notaryTutorialId;
    }
}
