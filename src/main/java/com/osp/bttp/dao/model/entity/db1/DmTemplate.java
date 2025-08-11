/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "DM_TEMPLATE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DmTemplate.findAll", query = "SELECT d FROM DmTemplate d")
    , @NamedQuery(name = "DmTemplate.findById", query = "SELECT d FROM DmTemplate d WHERE d.id = :id")
    , @NamedQuery(name = "DmTemplate.findByAdministrationId", query = "SELECT d FROM DmTemplate d WHERE d.administrationId = :administrationId")
    , @NamedQuery(name = "DmTemplate.findByAuthKey", query = "SELECT d FROM DmTemplate d WHERE d.authKey = :authKey")
    , @NamedQuery(name = "DmTemplate.findByLinkFile", query = "SELECT d FROM DmTemplate d WHERE d.linkFile = :linkFile")
    , @NamedQuery(name = "DmTemplate.findByFileName", query = "SELECT d FROM DmTemplate d WHERE d.fileName = :fileName")
    , @NamedQuery(name = "DmTemplate.findByActive", query = "SELECT d FROM DmTemplate d WHERE d.active = :active")})
public class DmTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="DM_TEMPLATE_SEQ", sequenceName="DM_TEMPLATE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_TEMPLATE_SEQ")
    private Long id;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Size(max = 200)
    @Column(name = "AUTH_KEY")
    private String authKey;
    @Size(max = 200)
    @Column(name = "LINK_FILE")
    private String linkFile;
    @Size(max = 200)
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "ACTIVE")
    private Long active;
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

    @Transient
    private String authority;
    @Transient
    private String administrationName;
    @Transient
    private String activeStr;
    
    public DmTemplate() {
    }

    public DmTemplate(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdministrationId() {
        return administrationId;
    }

    public void setAdministrationId(Long administrationId) {
        this.administrationId = administrationId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAdministrationName() {
        return administrationName;
    }

    public void setAdministrationName(String administrationName) {
        this.administrationName = administrationName;
    }

    public String getActiveStr() {
        return activeStr;
    }

    public void setActiveStr(String activeStr) {
        this.activeStr = activeStr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DmTemplate)) {
            return false;
        }
        DmTemplate other = (DmTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.osp.model.DmTemplate[ id=" + id + " ]";
    }
    
}
