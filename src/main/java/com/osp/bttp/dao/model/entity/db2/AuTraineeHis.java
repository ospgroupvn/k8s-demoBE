/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "AIMS_TRAINEE_HIS")
public class AuTraineeHis {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_TRAINEE_HIS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;

    @Column(name = "TRAINEE_ID")
    private Long traineeID;
    @Column(name = "ORG_ID")
    private Long orgID;

    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;

    @Column(name = "S_DATE")
    private Timestamp sDate;

    @Column(name = "E_DATE")
    private Timestamp eDate;

    @Column(name = "INFO")
    private String info;

    @Column(name = "ORG_ID_FROM")
    private Long orgIDFrom;

    @Column(name = "ORG_ID_TO")
    private Long orgIDTo;

    @Column(name = "NEW_S_DATE")
    private Timestamp newSDate;

    @Column(name = "OTHER")
    private String other;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "STATUS")
    private Long status;

    public AuTraineeHis() {
    }

    public AuTraineeHis(Long traineeID, Long orgID, Timestamp sDate, Timestamp eDate, String info, Long orgIDFrom, Long orgIDTo, Timestamp newSDate, Long status,String other) {
        this.traineeID = traineeID;
        this.orgID = orgID;
        this.sDate = sDate;
        this.eDate = eDate;
        this.info = info;
        this.orgIDFrom = orgIDFrom;
        this.orgIDTo = orgIDTo;
        this.newSDate = newSDate;
        this.status = status;
        this.other = other;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(Long traineeID) {
        this.traineeID = traineeID;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Timestamp getsDate() {
        return sDate;
    }

    public void setsDate(Timestamp sDate) {
        this.sDate = sDate;
    }

    public Timestamp geteDate() {
        return eDate;
    }

    public void seteDate(Timestamp eDate) {
        this.eDate = eDate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getOrgIDFrom() {
        return orgIDFrom;
    }

    public void setOrgIDFrom(Long orgIDFrom) {
        this.orgIDFrom = orgIDFrom;
    }

    public Long getOrgIDTo() {
        return orgIDTo;
    }

    public void setOrgIDTo(Long orgIDTo) {
        this.orgIDTo = orgIDTo;
    }

    public Timestamp getNewSDate() {
        return newSDate;
    }

    public void setNewSDate(Timestamp newSDate) {
        this.newSDate = newSDate;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
     
    

}
