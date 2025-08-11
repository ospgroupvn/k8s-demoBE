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
@Table(name = "AIMS_PUBLISH_HISTORY")
public class AuPublishHistory {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_PUBLISH_HISTORY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "OBJECT_ID")
    private Long objectID;
    @Column(name = "OBJECT_TYPE")
    private Long objectType;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "REQ_PUBLISH_SOURCE")
    private Long reqPublishSource;
    @Column(name = "ACT_STATUS")
    private Long actStatus;
    @Column(name = "ACT_DESC")
    private String actDesc;
    @Column(name = "USER_ID")
    private Long userID;
    @Column(name = "IS_CHECKED")
    private Long isChecked;
    @Column(name = "SEEN_DATE")
    private Timestamp seenDate;

    public AuPublishHistory() {
    }

    public AuPublishHistory(Long objectID, Long objectType, Timestamp genDate, Long actStatus, String actDesc, Long userID) {
        this.objectID = objectID;
        this.objectType = objectType;
        this.genDate = genDate;
        this.actStatus = actStatus;
        this.actDesc = actDesc;
        this.userID = userID;
    }

    public AuPublishHistory(Long objectID, Long objectType, Timestamp genDate, Long reqPublishSource, Long actStatus, String actDesc, Long userID) {
        this.objectID = objectID;
        this.objectType = objectType;
        this.genDate = genDate;
        this.reqPublishSource = reqPublishSource;
        this.actStatus = actStatus;
        this.actDesc = actDesc;
        this.userID = userID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getObjectID() {
        return objectID;
    }

    public void setObjectID(Long objectID) {
        this.objectID = objectID;
    }

    public Long getObjectType() {
        return objectType;
    }

    public void setObjectType(Long objectType) {
        this.objectType = objectType;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Long getReqPublishSource() {
        return reqPublishSource;
    }

    public void setReqPublishSource(Long reqPublishSource) {
        this.reqPublishSource = reqPublishSource;
    }

    public Long getActStatus() {
        return actStatus;
    }

    public void setActStatus(Long actStatus) {
        this.actStatus = actStatus;
    }

    public String getActDesc() {
        return actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Long isChecked) {
        this.isChecked = isChecked;
    }

    public Timestamp getSeenDate() {
        return seenDate;
    }

    public void setSeenDate(Timestamp seenDate) {
        this.seenDate = seenDate;
    }
    
    


}
