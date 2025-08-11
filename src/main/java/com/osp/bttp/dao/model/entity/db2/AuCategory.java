package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_CATEGORY")
public class AuCategory {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_CATEGORY_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;
    @Column(name = "CAT_TYPE")
    private String catType;
    @Column(name = "CAT_LEVEL")
    private Long level;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "CREATED_BY")
    private Long createBy;
    @Column(name = "MODIFIED_BY")
    private Long modifiedBy;
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;
    @Column(name = "MODIFY_DATE")
    private Timestamp modifyDate;
    @Column (name="STATUS")
    private Long status;
    @Column(name="DES")
    private String des;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public AuCategory() {
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    

    public AuCategory(String name, String code, String catType, Long level, Long parentId, Long createBy, Long modifiedBy, Timestamp createDate, Timestamp modifyDate) {
        this.name = name;
        this.code = code;
        this.level = level;
        this.parentId = parentId;
        this.createBy = createBy;
        this.modifiedBy = modifiedBy;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

}
