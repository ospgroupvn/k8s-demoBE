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
@Table(name = "AIMS_ORG_COMPLEX")
public class AuOrgConPlex {
     @Id
    @Column(name = "ID")
    @SequenceGenerator(name="AIMS_ORGANIZATION_SEQ", sequenceName="AIMS_ORGANIZATION_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIMS_ORGANIZATION_SEQ")
    private Long id;
     @Column(name="ORG_ID_DEST")
    private Long orgDest;
     @Column(name="ORG_ID_SRC")
    private Long orgSrc;
     @Column(name="GEN_DATE")
    private Timestamp genDate;

    public AuOrgConPlex() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgDest() {
        return orgDest;
    }

    public void setOrgDest(Long orgDest) {
        this.orgDest = orgDest;
    }

    public Long getOrgSrc() {
        return orgSrc;
    }

    public void setOrgSrc(Long orgSrc) {
        this.orgSrc = orgSrc;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }
    
    
    
}
