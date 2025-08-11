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
@Table(name = "AIMS_SENSITIVE_KEYWORDS")       
public class AuSenSitiveKeyWord {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_CATEGORY_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "KEY_NAME")
    private String keyName;
    @Column(name = "GEN_DATE")
    private Timestamp gendate;
    @Column(name = "CREATE_BY")
    private Long createBy;
    @Column(name = "LAST_UPDATE")
    private Timestamp lastUpdate;
    @Column(name = "CREATE_UPDATE")
    private Long  createUpdate;
    @Column(name = "STATUS")
    private Long status;

    public AuSenSitiveKeyWord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public Timestamp getGendate() {
        return gendate;
    }

    public void setGendate(Timestamp gendate) {
        this.gendate = gendate;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getCreateUpdate() {
        return createUpdate;
    }

    public void setCreateUpdate(Long createUpdate) {
        this.createUpdate = createUpdate;
    }
    
    
    
}
