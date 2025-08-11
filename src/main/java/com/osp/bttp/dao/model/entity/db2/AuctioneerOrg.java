package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_AUCTIONEER_ORG")
public class AuctioneerOrg {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_AUCTIONEER_ORG_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long auctioneerOrgId;
    @Column(name = "AUCTIONEER_ID")
    private Long auctioneerId;
    @Column(name = "ORG_ID")
    private Long orgId;
    @Column(name = "GEN_BY")
    private String genBy;
    @Column(name = "GEN_DATE")
    private Date genDate;


    public Long getAuctioneerOrgId() {
        return auctioneerOrgId;
    }

    public void setAuctioneerOrgId(Long auctioneerOrgId) {
        this.auctioneerOrgId = auctioneerOrgId;
    }

 
   

    public Long getAuctioneerId() {
        return auctioneerId;
    }

    public void setAuctioneerId(Long auctioneerId) {
        this.auctioneerId = auctioneerId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getGenBy() {
        return genBy;
    }

    public void setGenBy(String genBy) {
        this.genBy = genBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public AuctioneerOrg() {
    }


}
