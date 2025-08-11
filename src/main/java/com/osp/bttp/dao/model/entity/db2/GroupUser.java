package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 12/27/2017.
 */
@Entity
@Table(name = "ADM_GROUP_USER")
public class GroupUser implements Serializable {
    private static final long serialVersionUID = -6175963269564113777L;

    @Id
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "CREATE_BY",nullable = false)
    private String createBy;

    @Column(name = "GEN_DATE",nullable = false)
    private Date genDate;

    public GroupUser() {
    }

    public GroupUser(Long groupId, Long userId, String createBy, Date genDate) {
        this.groupId = groupId;
        this.userId = userId;
        this.createBy = createBy;
        this.genDate = genDate;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
