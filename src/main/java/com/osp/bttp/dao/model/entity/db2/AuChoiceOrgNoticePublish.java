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
@Table(name = "AIMS_CHOICE_ORG_NOTICE_PUB")
public class AuChoiceOrgNoticePublish {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_ORG_NOTICE_PUB_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_ORG_ID")
    private Long choiceOrgID;
    @Column(name = "USER_ID")
    private Long userID;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "ACT_STATUS")
    private Long actStatus;
    @Column(name = "ACT_DESC")
    private String actDesc;

    public AuChoiceOrgNoticePublish() {
    }

    public AuChoiceOrgNoticePublish(Long choiceOrgID, Long userID, Timestamp genDate, Long actStatus, String actDesc) {
        this.choiceOrgID = choiceOrgID;
        this.userID = userID;
        this.genDate = genDate;
        this.actStatus = actStatus;
        this.actDesc = actDesc;
    }       

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChoiceOrgID() {
        return choiceOrgID;
    }

    public void setChoiceOrgID(Long choiceOrgID) {
        this.choiceOrgID = choiceOrgID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
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

}
