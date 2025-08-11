/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "AIMS_CHOICE_ORG_NOTICE")
public class ChoiceOrgNotice {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_ORG_NOTICE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "RECEIVE_TIME_START")
    private Date receiveTimeStart;
    @Column(name = "RECEIVE_TIME_END")
    private Date receiveTimeEnd;
    @Column(name = "RECEIVE_ADDR")
    private String receiveAddr;
    @Column(name = "CONTACT_INFO")
    private String contactInfo;
    @Column(name = "OTHER_INFO")
    private String otherInfo;
    @Column(name = "HAS_FILE")
    private Long hasFile;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "LAST_UPDATED")
    private Timestamp lastUpdated;
    @Column(name = "IS_CONFIRMED")
    private Long isConfirmed;
    @Column(name = "PUBLISH_STATUS")
    private Long publishStatus;

    public ChoiceOrgNotice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getReceiveTimeStart() {
        return receiveTimeStart;
    }

    public void setReceiveTimeStart(Date receiveTimeStart) {
        this.receiveTimeStart = receiveTimeStart;
    }

    public Date getReceiveTimeEnd() {
        return receiveTimeEnd;
    }

    public void setReceiveTimeEnd(Date receiveTimeEnd) {
        this.receiveTimeEnd = receiveTimeEnd;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Long getHasFile() {
        return hasFile;
    }

    public void setHasFile(Long hasFile) {
        this.hasFile = hasFile;
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

    public Long getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Long isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Long getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Long publishStatus) {
        this.publishStatus = publishStatus;
    }
    
    
}
