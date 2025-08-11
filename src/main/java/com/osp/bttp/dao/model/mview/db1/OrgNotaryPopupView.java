package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.dao.model.entity.db1.OrgNotaryInfo;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class OrgNotaryPopupView {

    private Long id;
    private String name;
    private String address;
    private String tel;
    private String fax;
    private String email;
    private String website;
    private Long notaryIdOfficeChief;
    private Long active;
    private Long status;
    private Long type;
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private Long addressId;
    private Long administrationId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    private String nameAdminis;

    public OrgNotaryPopupView() {
    }

    public OrgNotaryPopupView castToPopup(OrgNotaryInfo bo){
        OrgNotaryPopupView form = new OrgNotaryPopupView();

        if (bo.getId() != null && bo.getId() != -1L){
            form.setId(bo.getId());
        }
        if (bo.getName() != null && !"".equals(bo.getName())){
            form.setName(bo.getName());
        }
        if (bo.getAddress() != null && !"".equals(bo.getAddress())){
            form.setAddress(bo.getAddress());
        }
        if (bo.getTel() != null && !"".equals(bo.getTel())){
            form.setTel(bo.getTel());
        }
        if (bo.getFax() != null && !"".equals(bo.getFax())){
            form.setFax(bo.getFax());
        }
        if (bo.getEmail() != null && !"".equals(bo.getEmail())){
            form.setEmail(bo.getEmail());
        }
        if (bo.getWebsite() != null && !"".equals(bo.getWebsite())){
            form.setWebsite(bo.getWebsite());
        }
        if (bo.getNotaryIdOfficeChief() != null && bo.getNotaryIdOfficeChief() != -1L){
            form.setNotaryIdOfficeChief(bo.getNotaryIdOfficeChief());
        }
        if (bo.getActive() != null && bo.getActive() != -1L){
            form.setActive(bo.getActive());
        }
        if (bo.getStatus() != null && bo.getStatus() != -1L){
            form.setStatus(bo.getStatus());
        }
        if (bo.getType() != null && bo.getType() != -1L){
            form.setType(bo.getType());
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
        if (bo.getAddressId() != null && bo.getAddressId() != -1L){
            form.setAddressId(bo.getAddressId());
        }
        if (bo.getAdministrationId() != null && bo.getAdministrationId() != -1L){
            form.setAdministrationId(bo.getAdministrationId());
        }

        return form;
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

    public String getNameAdminis() {
        return nameAdminis;
    }

    public void setNameAdminis(String nameAdminis) {
        this.nameAdminis = nameAdminis;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }
}
