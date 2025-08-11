package com.osp.bttp.dao.model.mview.db1;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class MutiView {

    /*NotaryInfo*/
    private Long id_notary;
    private String name_notary;
    private Long sex_notary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay_notary;
    private String idNo_notary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate_notary;
    private String addressIdNo_notary;
    private String addressResident_notary;
    private Long addressResidentId_notary;
    private String addressNow_notary;
    private Long addressNowId_notary;
    private Long status_notary;
    private String phoneNumber_notary;
    private String email_notary;
    private String note_notary;
    private Long active_notary;
    /*end*/

    /*OrgNotaryInfo*/
    private Long id_org;
    private String name_org;
    private String address_org;
    private String tel_org;
    private String fax_org;
    private String email_org;
    private String website_org;
    private Long notaryIdOfficeChief_org;
    private Long active_org;
    private Long status_org;
    private Long type_org;
    private Long administrationId_org;
    /*end*/

    /*DmDocument*/
    private Long id_doc;
    private String dispatchCode_doc;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign_doc;
    private String signer_doc;
    private String unitSign_doc;
    private String linkFile_doc;
    private String fileName_doc;
    private String note_doc;
    private Long type_doc;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate_doc;
    private Long active_doc;
    /*end*/

    private String createdBy;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;





    public MutiView() {
    }




    public Long getId_notary() {
        return id_notary;
    }

    public void setId_notary(Long id_notary) {
        this.id_notary = id_notary;
    }

    public String getName_notary() {
        return name_notary;
    }

    public void setName_notary(String name_notary) {
        this.name_notary = name_notary;
    }

    public Long getSex_notary() {
        return sex_notary;
    }

    public void setSex_notary(Long sex_notary) {
        this.sex_notary = sex_notary;
    }

    public Date getBirthDay_notary() {
        return birthDay_notary;
    }

    public void setBirthDay_notary(Date birthDay_notary) {
        this.birthDay_notary = birthDay_notary;
    }

    public String getIdNo_notary() {
        return idNo_notary;
    }

    public void setIdNo_notary(String idNo_notary) {
        this.idNo_notary = idNo_notary;
    }

    public Date getIdNoDate_notary() {
        return idNoDate_notary;
    }

    public void setIdNoDate_notary(Date idNoDate_notary) {
        this.idNoDate_notary = idNoDate_notary;
    }

    public String getAddressIdNo_notary() {
        return addressIdNo_notary;
    }

    public void setAddressIdNo_notary(String addressIdNo_notary) {
        this.addressIdNo_notary = addressIdNo_notary;
    }

    public String getAddressResident_notary() {
        return addressResident_notary;
    }

    public void setAddressResident_notary(String addressResident_notary) {
        this.addressResident_notary = addressResident_notary;
    }

    public Long getAddressResidentId_notary() {
        return addressResidentId_notary;
    }

    public void setAddressResidentId_notary(Long addressResidentId_notary) {
        this.addressResidentId_notary = addressResidentId_notary;
    }

    public String getAddressNow_notary() {
        return addressNow_notary;
    }

    public void setAddressNow_notary(String addressNow_notary) {
        this.addressNow_notary = addressNow_notary;
    }

    public Long getAddressNowId_notary() {
        return addressNowId_notary;
    }

    public void setAddressNowId_notary(Long addressNowId_notary) {
        this.addressNowId_notary = addressNowId_notary;
    }

    public Long getStatus_notary() {
        return status_notary;
    }

    public void setStatus_notary(Long status_notary) {
        this.status_notary = status_notary;
    }

    public String getPhoneNumber_notary() {
        return phoneNumber_notary;
    }

    public void setPhoneNumber_notary(String phoneNumber_notary) {
        this.phoneNumber_notary = phoneNumber_notary;
    }

    public String getEmail_notary() {
        return email_notary;
    }

    public void setEmail_notary(String email_notary) {
        this.email_notary = email_notary;
    }

    public String getNote_notary() {
        return note_notary;
    }

    public void setNote_notary(String note_notary) {
        this.note_notary = note_notary;
    }

    public Long getActive_notary() {
        return active_notary;
    }

    public void setActive_notary(Long active_notary) {
        this.active_notary = active_notary;
    }

    public Long getId_org() {
        return id_org;
    }

    public void setId_org(Long id_org) {
        this.id_org = id_org;
    }

    public String getName_org() {
        return name_org;
    }

    public void setName_org(String name_org) {
        this.name_org = name_org;
    }

    public String getAddress_org() {
        return address_org;
    }

    public void setAddress_org(String address_org) {
        this.address_org = address_org;
    }

    public String getTel_org() {
        return tel_org;
    }

    public void setTel_org(String tel_org) {
        this.tel_org = tel_org;
    }

    public String getFax_org() {
        return fax_org;
    }

    public void setFax_org(String fax_org) {
        this.fax_org = fax_org;
    }

    public String getEmail_org() {
        return email_org;
    }

    public void setEmail_org(String email_org) {
        this.email_org = email_org;
    }

    public String getWebsite_org() {
        return website_org;
    }

    public void setWebsite_org(String website_org) {
        this.website_org = website_org;
    }

    public Long getNotaryIdOfficeChief_org() {
        return notaryIdOfficeChief_org;
    }

    public void setNotaryIdOfficeChief_org(Long notaryIdOfficeChief_org) {
        this.notaryIdOfficeChief_org = notaryIdOfficeChief_org;
    }

    public Long getActive_org() {
        return active_org;
    }

    public void setActive_org(Long active_org) {
        this.active_org = active_org;
    }

    public Long getStatus_org() {
        return status_org;
    }

    public void setStatus_org(Long status_org) {
        this.status_org = status_org;
    }

    public Long getType_org() {
        return type_org;
    }

    public void setType_org(Long type_org) {
        this.type_org = type_org;
    }

    public Long getAdministrationId_org() {
        return administrationId_org;
    }

    public void setAdministrationId_org(Long administrationId_org) {
        this.administrationId_org = administrationId_org;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId_doc() {
        return id_doc;
    }

    public void setId_doc(Long id_doc) {
        this.id_doc = id_doc;
    }

    public String getDispatchCode_doc() {
        return dispatchCode_doc;
    }

    public void setDispatchCode_doc(String dispatchCode_doc) {
        this.dispatchCode_doc = dispatchCode_doc;
    }

    public Date getDateSign_doc() {
        return dateSign_doc;
    }

    public void setDateSign_doc(Date dateSign_doc) {
        this.dateSign_doc = dateSign_doc;
    }

    public String getSigner_doc() {
        return signer_doc;
    }

    public void setSigner_doc(String signer_doc) {
        this.signer_doc = signer_doc;
    }

    public String getUnitSign_doc() {
        return unitSign_doc;
    }

    public void setUnitSign_doc(String unitSign_doc) {
        this.unitSign_doc = unitSign_doc;
    }

    public String getLinkFile_doc() {
        return linkFile_doc;
    }

    public void setLinkFile_doc(String linkFile_doc) {
        this.linkFile_doc = linkFile_doc;
    }

    public String getFileName_doc() {
        return fileName_doc;
    }

    public void setFileName_doc(String fileName_doc) {
        this.fileName_doc = fileName_doc;
    }

    public String getNote_doc() {
        return note_doc;
    }

    public void setNote_doc(String note_doc) {
        this.note_doc = note_doc;
    }

    public Long getType_doc() {
        return type_doc;
    }

    public void setType_doc(Long type_doc) {
        this.type_doc = type_doc;
    }

    public Date getEffectiveDate_doc() {
        return effectiveDate_doc;
    }

    public void setEffectiveDate_doc(Date effectiveDate_doc) {
        this.effectiveDate_doc = effectiveDate_doc;
    }

    public Long getActive_doc() {
        return active_doc;
    }

    public void setActive_doc(Long active_doc) {
        this.active_doc = active_doc;
    }
}
