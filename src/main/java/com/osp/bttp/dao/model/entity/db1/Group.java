package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 12/27/2017.
 */
@Entity
@Table(name = "ADM_GROUP")
public class Group implements Serializable {
    private static final long serialVersionUID = -166399391710801760L;

    @Id
    @SequenceGenerator(name="ADM_GROUP_SEQ", sequenceName="ADM_GROUP_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_GROUP_SEQ")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "GROUP_NAME",nullable = false)
    private String groupName;

    @Column(name = "STATUS",nullable = false)
    private boolean status;

    @Column(name = "AUTHORITY",length = 1024)
    private String authority;

    @Column(name = "description")
    private String description;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "GEN_DATE",nullable = false)
    private Date genDate;

    @Column(name = "LAST_UPDATED",nullable = false)
    private Date lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
