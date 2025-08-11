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
@Table(name = "ORG_NOTARY_INFO")
public class OrgNotaryInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="ORG_NOTARY_INFO_SEQ", sequenceName="ORG_NOTARY_INFO_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_INFO_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @Column(name = "TEL")
    private String tel;
    @Size(max = 15)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 50)
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "NOTARY_ID_OFFICE_CHIEF")
    private Long notaryIdOfficeChief;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private Long type;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "ADDRESS_ID")
    private Long addressId;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;

    public OrgNotaryInfo() {
    }

    public OrgNotaryInfo(Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgNotaryInfo)) {
            return false;
        }
        OrgNotaryInfo other = (OrgNotaryInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
