/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DM_AREA")
public class DmArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="DM_AREA_SEQ", sequenceName="DM_AREA_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_AREA_SEQ")
    private Long id;
    @Size(max = 200)
    @Column(name = "PROVINCE_NAME")
    private String provinceName;
    @Size(max = 10)
    @Column(name = "PROVINCE_CODE")
    private String provinceCode;
    @Size(max = 200)
    @Column(name = "DISTRICT_NAME")
    private String districtName;
    @Size(max = 10)
    @Column(name = "DISTRICT_CODE")
    private String districtCode;
    @Size(max = 200)
    @Column(name = "COMMUNE_NAME")
    private String communeName;
    @Size(max = 10)
    @Column(name = "COMMUNE_CODE")
    private String communeCode;
    @Size(max = 50)
    @Column(name = "UNITS_AREA")
    private String unitsArea;
    @Size(max = 50)
    @Column(name = "CREATE_BY")
    private String createBy;
    @Size(max = 50)
    @Column(name = "UPDATE_BY")
    private String updateBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "ACTIVE")
    private Long active;
    public DmArea() {
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    public String getCommuneCode() {
        return communeCode;
    }

    public void setCommuneCode(String communeCode) {
        this.communeCode = communeCode;
    }

    public String getUnitsArea() {
        return unitsArea;
    }

    public void setUnitsArea(String unitsArea) {
        this.unitsArea = unitsArea;
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
