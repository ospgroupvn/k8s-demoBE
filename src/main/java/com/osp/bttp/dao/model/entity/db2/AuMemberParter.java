package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_MEMBER_PARTER")
public class AuMemberParter {
    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_MEMBER_PARTER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "CER_CODE")
    private String cerCode;
    @Column(name="DOB")
    private String dob;
    @Column(name="FULL_NAME")
    private String fullname;
    @Column(name="AUCTIONEER_ID")
    private Long AuctioneerId;
    @Column(name="ORG_ID")
    private Long orgId;
    @Column(name="CER_DOI")
    private Date cerDOI;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCerCode() {
        return cerCode;
    }

    public void setCerCode(String cerCode) {
        this.cerCode = cerCode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Long getAuctioneerId() {
        return AuctioneerId;
    }

    public void setAuctioneerId(Long auctioneerId) {
        AuctioneerId = auctioneerId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Date getCerDOI() {
        return cerDOI;
    }

    public void setCerDOI(Date cerDOI) {
        this.cerDOI = cerDOI;
    }
}
