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
@Table(name = "AIMS_CHOICE_ORG_NOTICE_HIS")
public class AuChoiceOrgNoticeHIS {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_ORG_NOTICE_HIS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_ORG_ID")
    private Long choiceOrgID;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    public AuChoiceOrgNoticeHIS() {
    }

    public AuChoiceOrgNoticeHIS(Long choiceOrgID, Timestamp genDate) {
        this.choiceOrgID = choiceOrgID;
        this.genDate = genDate;
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

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

}
