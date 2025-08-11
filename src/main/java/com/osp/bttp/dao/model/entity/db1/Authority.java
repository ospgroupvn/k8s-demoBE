package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 12/27/2017.
 */
@Entity
@Table(name = "ADM_AUTHORITIES")
public class Authority implements Serializable {
    private static final long serialVersionUID = 2894810169009008957L;
    @Id
    @SequenceGenerator(name="ADM_AUTHORITIES_SEQ", sequenceName="ADM_AUTHORITIES_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_AUTHORITIES_SEQ")
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "AUTHORITY",nullable = false)
    private String authority;

    @Column(name = "FID",nullable = false)
    private int fid;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_ID")
    private int orderId;

    @Column(name = "AUTH_KEY")
    private String authKey;
    
    @Column(name = "CREATE_BY")
    private String createBy;
    
    @Column(name = "UPDATE_BY")
    private String updateBy;
    
    @Column(name = "GEN_DATE",nullable = false)
    private Date genDate;

    @Column(name = "LAST_UPDATED",nullable = false)
    private Date lastUpdated;

    @Transient
    private String fAuthority;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getfAuthority() {
        return fAuthority;
    }

    public void setfAuthority(String fAuthority) {
        this.fAuthority = fAuthority;
    }
    
    
}
