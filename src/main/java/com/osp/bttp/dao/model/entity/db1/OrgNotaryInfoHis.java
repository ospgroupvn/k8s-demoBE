/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "ORG_NOTARY_INFO_HIS")
public class OrgNotaryInfoHis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="ORG_NOTARY_INFO_HIS_SEQ", sequenceName="ORG_NOTARY_INFO_HIS_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_INFO_HIS_SEQ")
    @Column(name = "ID_HIS")
    private Long idHis;
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "NOTARY_ID_OFFICE_CHIEF")
    private Long notaryIdOfficeChief;
    @Basic(optional = false)
    @Column(name = "ACTIVE")
    private Long active;
    @Basic(optional = false)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Basic(optional = false)
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "ADDRESS_ID")
    private Long addressId;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;
    @Column(name = "TYPE_ACTION")
    private String typeAction;
    @Column(name = "OBJ_ID")
    private Long objId;

    public OrgNotaryInfoHis() {
    }

    public Long getIdHis() {
        return idHis;
    }

    public void setIdHis(Long idHis) {
        this.idHis = idHis;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getNotaryIdOfficeChief() {
        return notaryIdOfficeChief;
    }

    public void setNotaryIdOfficeChief(Long notaryIdOfficeChief) {
        this.notaryIdOfficeChief = notaryIdOfficeChief;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
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

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getAdministrationId() {
        return administrationId;
    }

    public void setAdministrationId(Long administrationId) {
        this.administrationId = administrationId;
    }

    public String getStatusBefor() {
        return statusBefor;
    }

    public void setStatusBefor(String statusBefor) {
        this.statusBefor = statusBefor;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }
    
}
