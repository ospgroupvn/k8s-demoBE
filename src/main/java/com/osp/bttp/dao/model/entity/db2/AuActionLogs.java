package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_ACTION_LOG")
public class AuActionLogs {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_ACTION_LOG_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "USER_ID")
    private Long userId;
    @Column(name = "INFO")
    private String info;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "TABLE_NAME")
    private String tableName;
    @Column(name = "FIELDS_NAME")
    private String fieldsName;
    @Column(name = "IP")
    private String ip;
    @Column(name = "ORG_ID ")
    private Long orgId;
    @Column(name = "OBJECT_ID")
    private Long objectId;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "TYPE_USER_LOGS")
    private Long typeUserLogs;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public void setFieldsName(String fieldsName) {
        this.fieldsName = fieldsName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getTypeUserLogs() {
        return typeUserLogs;
    }

    public void setTypeUserLogs(Long typeUserLogs) {
        this.typeUserLogs = typeUserLogs;
    }
}
