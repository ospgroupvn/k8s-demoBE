package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.dao.model.entity.db1.AdmParameter;

import java.util.Date;

public class AdmParameterView {

    private String id;
    private String key;
    private String value;
    private Long typeData;
    private String description;
    private Long userId;
    private Long active;
    private Long lastUserid;
    private String createdBy;
    private Date genDate;
    private String updatedBy;
    private Date lastUpdate;

    private String typeDataStr;

    public AdmParameterView boToFrom(AdmParameter bo){
        AdmParameterView form = new AdmParameterView();
        if (bo.getId() != null && !"".equals(bo.getId())){
            form.setId(bo.getId());
        }
        if (bo.getKey() != null && !"".equals(bo.getKey())){
            form.setKey(bo.getKey());
        }
        if (bo.getValue() != null && !"".equals(bo.getValue())){
            form.setValue(bo.getValue());
        }
        if (bo.getTypeData() != null && bo.getTypeData() != -1L){
            form.setTypeData(bo.getTypeData());
        }
        if (bo.getDescription() != null && !"".equals(bo.getDescription())){
            form.setDescription(bo.getDescription());
        }
        if (bo.getUserId() != null && bo.getUserId() != -1L){
            form.setUserId(bo.getUserId());
        }
        if (bo.getActive() != null && bo.getActive() != -1L){
            form.setActive(bo.getActive());
        }
        if (bo.getLastUserid() != null && bo.getLastUserid() != -1L){
            form.setLastUserid(bo.getLastUserid());
        }
        if (bo.getCreatedBy() != null && !"".equals(bo.getCreatedBy())){
            form.setCreatedBy(bo.getCreatedBy());
        }
        if (bo.getGenDate() != null){
            form.setGenDate(bo.getGenDate());
        }
        if (bo.getUpdatedBy() != null && !"".equals(bo.getUpdatedBy())){
            form.setUpdatedBy(bo.getUpdatedBy());
        }
        if (bo.getLastUpdate() != null){
            form.setLastUpdate(bo.getLastUpdate());
        }
        form.getTypeDataStr();
        return form;
    }

    public String getTypeDataStr() {
        typeDataStr = ConstantsTccc.PARAMETER_TYPE_DATA.getStr(typeData,typeDataStr);
        return typeDataStr;
    }

    public void setTypeDataStr(String typeDataStr) {
        this.typeDataStr = typeDataStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTypeData() {
        return typeData;
    }

    public void setTypeData(Long typeData) {
        this.typeData = typeData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getLastUserid() {
        return lastUserid;
    }

    public void setLastUserid(Long lastUserid) {
        this.lastUserid = lastUserid;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
