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
@Table(name = "AIMS_CHOICE_RESULT_HIS")
public class AuChoiceResultHIS {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_RESULT_HIS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_RESULT_ID")
    private Long choiceResultID;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    public AuChoiceResultHIS() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChoiceResultID() {
        return choiceResultID;
    }

    public void setChoiceResultID(Long choiceResultID) {
        this.choiceResultID = choiceResultID;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

}
