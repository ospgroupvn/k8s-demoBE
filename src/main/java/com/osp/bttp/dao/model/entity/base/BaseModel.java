package com.osp.bttp.dao.model.entity.base;

import jakarta.persistence.*;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel implements Serializable, Creatable, Updatable {

	private static final long serialVersionUID = 1L;


	@Column(name = "CREATE_BY")
    @CreatedBy
	private String createBy;

	@Column(name = "UPDATE_BY")
    @LastModifiedBy
	private String updateBy;

	@Column(name = "GEN_DATE")
    @CreatedDate
	private Date genDate;

	@Column(name = "LAST_UPDATED")
    @LastModifiedDate
	private Date lastUpdated;

    @Column(name = "UPDATE_BY_ID")
    private Long updateById;

    @Column(name = "CREATE_BY_ID")
    private Long createById;

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

    public Long getUpdateById() {
        return updateById;
    }

    @Override
    public void setUpdateById(Long updateById) {
        this.updateById = updateById;
    }

    public Long getCreateById() {
        return createById;
    }

    @Override
    public void setCreateById(Long createById) {
        this.createById = createById;
    }
}
