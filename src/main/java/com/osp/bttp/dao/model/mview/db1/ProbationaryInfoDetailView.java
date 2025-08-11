package com.osp.bttp.dao.model.mview.db1;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class ProbationaryInfoDetailView {

    private Long id;
    private Long probationaryInfoId;
    private Long orgNotaryInfoId;
    private Long orgNotaryInfoTo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    private Long status;
    private String note;
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private Long active;
    private String orgInfoFromName;
    private String orgInfoToName;

    public ProbationaryInfoDetailView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProbationaryInfoId() {
        return probationaryInfoId;
    }

    public void setProbationaryInfoId(Long probationaryInfoId) {
        this.probationaryInfoId = probationaryInfoId;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Long getOrgNotaryInfoTo() {
        return orgNotaryInfoTo;
    }

    public void setOrgNotaryInfoTo(Long orgNotaryInfoTo) {
        this.orgNotaryInfoTo = orgNotaryInfoTo;
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

    public String getOrgInfoFromName() {
        return orgInfoFromName;
    }

    public void setOrgInfoFromName(String orgInfoFromName) {
        this.orgInfoFromName = orgInfoFromName;
    }

    public String getOrgInfoToName() {
        return orgInfoToName;
    }

    public void setOrgInfoToName(String orgInfoToName) {
        this.orgInfoToName = orgInfoToName;
    }
}
