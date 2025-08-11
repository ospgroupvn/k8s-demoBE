package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 12/27/2017.
 */
@Entity
@Table(name = "ADM_LOG_ACCESS")
public class LogAccess implements Serializable{
    private static final long serialVersionUID = 1451508189162183268L;

    @Id
    @SequenceGenerator(name="ADM_LOG_ACCESS_SEQ", sequenceName="ADM_LOG_ACCESS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_LOG_ACCESS_SEQ")
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "USER_ID",nullable = false)
    private Long userId;

    @Column(name = "MODULE_ID",length = 100)
    private String module;

    @Column(name = "IP",length = 100)
    private String ip;

    @Column(name = "ACTIONS",length = 200)
    private String actions;

    @Column(name = "GEN_DATE",nullable = false)
    private Date genDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
