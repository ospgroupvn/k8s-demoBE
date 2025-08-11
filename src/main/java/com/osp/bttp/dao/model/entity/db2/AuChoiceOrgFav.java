package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_CHOICE_ORG_FAV")
public class AuChoiceOrgFav {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_CHOICE_ORG_FAV_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ORG_ID", nullable = false)
    private Long orgID;
    @Column(name = "CHOICE_ORG_ID", nullable = false)
    private Long choiceOrgID;
    @Column(name = "USER_ID_CREATE", nullable = false)
    private Long userIdCreate;
    @Column(name = "GEN_DATE", nullable = false)
    private Timestamp genDate;
    @Column(name= "SUBMIT_FILE")
    private Long submitFile;

    public AuChoiceOrgFav() {
    }

    public AuChoiceOrgFav(Long orgID, Long choiceOrgID, Long userIdCreate, Timestamp genDate) {
        this.orgID = orgID;
        this.choiceOrgID = choiceOrgID;
        this.userIdCreate = userIdCreate;
        this.genDate = genDate;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgID() {
        return orgID;
    }

    public void setOrgID(Long orgID) {
        this.orgID = orgID;
    }

    public Long getChoiceOrgID() {
        return choiceOrgID;
    }

    public void setChoiceOrgID(Long choiceOrgID) {
        this.choiceOrgID = choiceOrgID;
    }

    public Long getUserIdCreate() {
        return userIdCreate;
    }

    public void setUserIdCreate(Long userIdCreate) {
        this.userIdCreate = userIdCreate;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Long getSubmitFile() {
        return submitFile;
    }

    public void setSubmitFile(Long submitFile) {
        this.submitFile = submitFile;
    }
    
    

}
