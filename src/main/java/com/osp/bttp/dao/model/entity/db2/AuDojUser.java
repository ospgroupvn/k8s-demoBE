package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_DOJ_USER")
public class AuDojUser {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_DOJ_USER_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "DOJ_ID")
    private Long dojId;
    @Column(name = "GEN_BY")
    private String genBy;
    @Column(name = "GEN_DATE")
    private Date genDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDojId() {
        return dojId;
    }

    public void setDojId(Long dojId) {
        this.dojId = dojId;
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
}
