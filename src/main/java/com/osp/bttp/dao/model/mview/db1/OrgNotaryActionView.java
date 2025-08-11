
package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class OrgNotaryActionView {

    private Long idOrgNotary;
    private String orgNotaryName;
    private String notaryNameOfficeChief;
    private String orgNotaryAddRess;
    private Long orgNotaryStatus;
    private String orgNotaryStatusStr;

    /*document*/
    private String dispatchCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    private String effectiveDateStr;
    private String signer;
    private Date dateSign;
    private String dateSignStr;

    /*OrgNotary*/
    private String tel_org;
    private String fax_org;
    private String email_org;
    private String website_org;
    private Long active_org;
    private Long type_org;
    private String typeStr_org;

    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private String genDateStr;
    private String lastUpdateStr;

    /*OrgAction*/
    private Long id_action;
    private Long type_action;
    private String note_action;
    private Long active_action;
    private Long documentId_action;
    private Long orgNotaryInfoId_action;
    private Long notaryIdOfficeChiefOld_action;
    private Long typeAction;
    private String typeActionStr;

    /*Notaryinfo*/
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
    private String sexStr_notary;
    
    //OrgNotaryTransfer
    private Long idOrgNotaryTransfer;
    private Long typeTransfer;
    private String typeTransferStr;
    
    //OrgNotaryTransferDetail
    private Long orgNotaryType;
    private Long orgNotaryInfoIdNew;
    private Long orgNotaryInfoIdOld;

    //OrgNotaryInfo
    private String orgNameNew;
    private String orgNameOld;
    private String address;
    public OrgNotaryActionView() {
    }

    public Long getIdOrgNotaryTransfer() {
        return idOrgNotaryTransfer;
    }

    public void setIdOrgNotaryTransfer(Long idOrgNotaryTransfer) {
        this.idOrgNotaryTransfer = idOrgNotaryTransfer;
    }
    
    public Long getOrgNotaryType() {
        return orgNotaryType;
    }

    public void setOrgNotaryType(Long orgNotaryType) {
        this.orgNotaryType = orgNotaryType;
    }

    public Long getOrgNotaryInfoIdNew() {
        return orgNotaryInfoIdNew;
    }

    public void setOrgNotaryInfoIdNew(Long orgNotaryInfoIdNew) {
        this.orgNotaryInfoIdNew = orgNotaryInfoIdNew;
    }

    public Long getOrgNotaryInfoIdOld() {
        return orgNotaryInfoIdOld;
    }

    public void setOrgNotaryInfoIdOld(Long orgNotaryInfoIdOld) {
        this.orgNotaryInfoIdOld = orgNotaryInfoIdOld;
    }

    public String getOrgNameNew() {
        return orgNameNew;
    }

    public void setOrgNameNew(String orgNameNew) {
        this.orgNameNew = orgNameNew;
    }

    public String getOrgNameOld() {
        return orgNameOld;
    }

    public void setOrgNameOld(String orgNameOld) {
        this.orgNameOld = orgNameOld;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
    public String getDateSignStr() {
        dateSignStr = UtilsDate.date2str(dateSign, "dd-MM-yyyy");
        return dateSignStr;
    }

    public String getGenDateStr() {
        genDateStr = UtilsDate.date2str(genDate, "dd-MM-yyyy");
        return genDateStr;
    }

    public String getLastUpdateStr() {
        lastUpdateStr = UtilsDate.date2str(lastUpdate, "dd-MM-yyyy");
        return lastUpdateStr;
    }

    public Long getTypeTransfer() {
        return typeTransfer;
    }

    public void setTypeTransfer(Long typeTransfer) {
        this.typeTransfer = typeTransfer;
    }

    public String getTypeTransferStr() {
        typeTransferStr = ConstantsTccc.LOAI_DU_LIEU.getType(typeTransfer,typeTransferStr);
        return typeTransferStr;
    }

    public void setTypeTransferStr(String typeTransferStr) {
        this.typeTransferStr = typeTransferStr;
    }
    
    public String getTypeActionStr() {
        typeActionStr = ConstantsTccc.TYPE_ORG_ACTION.getStr(typeAction,typeActionStr);
        return typeActionStr;
    }

    public String getSexStr_notary() {
        sexStr_notary = ConstantsTccc.SEX.getStr(sex_notary,sexStr_notary);
        return sexStr_notary;
    }

    public String getOrgNotaryStatusStr() {
        orgNotaryStatusStr = ConstantsTccc.STATUS_ORG_NOTARY.getStr(orgNotaryStatus,orgNotaryStatusStr);
        return orgNotaryStatusStr;
    }

    public String getTypeStr_org() {
        typeStr_org = ConstantsTccc.TYPE_ORG_NOTARY.getStr(type_org,typeStr_org);
        return typeStr_org;
    }

    public void setTypeStr_org(String typeStr_org) {
        this.typeStr_org = typeStr_org;
    }

    public Long getType_org() {
        return type_org;
    }

    public void setType_org(Long type_org) {
        this.type_org = type_org;
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

    public Long getActive_org() {
        return active_org;
    }

    public void setActive_org(Long active_org) {
        this.active_org = active_org;
    }

    public void setSexStr_notary(String sexStr_notary) {
        this.sexStr_notary = sexStr_notary;
    }

    public void setTypeActionStr(String typeActionStr) {
        this.typeActionStr = typeActionStr;
    }

    public Long getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(Long typeAction) {
        this.typeActionStr = ConstantsTccc.TYPE_ORG_ACTION.getStr(typeAction, typeActionStr);
        this.typeAction = typeAction;
    }

    public void setDateSignStr(String dateSignStr) {
        this.dateSignStr = dateSignStr;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getGenDate() {
        return genDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setGenDateStr(String genDateStr) {
        this.genDateStr = genDateStr;
    }

    public void setLastUpdateStr(String lastUpdateStr) {
        this.lastUpdateStr = lastUpdateStr;
    }

    public void setOrgNotaryStatusStr(String orgNotaryStatusStr) {
        this.orgNotaryStatusStr = orgNotaryStatusStr;
    }

    public Long getOrgNotaryStatus() {
        return orgNotaryStatus;
    }

    public void setOrgNotaryStatus(Long orgNotaryStatus) {
        this.orgNotaryStatus = orgNotaryStatus;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public String getOrgNotaryName() {
        return orgNotaryName;
    }

    public void setOrgNotaryName(String orgNotaryName) {
        this.orgNotaryName = orgNotaryName;
    }

    public String getNotaryNameOfficeChief() {
        return notaryNameOfficeChief;
    }

    public void setNotaryNameOfficeChief(String notaryNameOfficeChief) {
        this.notaryNameOfficeChief = notaryNameOfficeChief;
    }

    public String getOrgNotaryAddRess() {
        return orgNotaryAddRess;
    }

    public void setOrgNotaryAddRess(String orgNotaryAddRess) {
        this.orgNotaryAddRess = orgNotaryAddRess;
    }

    public Long getIdOrgNotary() {
        return idOrgNotary;
    }

    public void setIdOrgNotary(Long idOrgNotary) {
        this.idOrgNotary = idOrgNotary;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Long getId_action() {
        return id_action;
    }

    public void setId_action(Long id_action) {
        this.id_action = id_action;
    }

    public Long getType_action() {
        return type_action;
    }

    public void setType_action(Long type_action) {
        this.type_action = type_action;
    }

    public String getNote_action() {
        return note_action;
    }

    public void setNote_action(String note_action) {
        this.note_action = note_action;
    }

    public Long getActive_action() {
        return active_action;
    }

    public void setActive_action(Long active_action) {
        this.active_action = active_action;
    }

    public Long getDocumentId_action() {
        return documentId_action;
    }

    public void setDocumentId_action(Long documentId_action) {
        this.documentId_action = documentId_action;
    }

    public Long getOrgNotaryInfoId_action() {
        return orgNotaryInfoId_action;
    }

    public void setOrgNotaryInfoId_action(Long orgNotaryInfoId_action) {
        this.orgNotaryInfoId_action = orgNotaryInfoId_action;
    }

    public Long getNotaryIdOfficeChiefOld_action() {
        return notaryIdOfficeChiefOld_action;
    }

    public void setNotaryIdOfficeChiefOld_action(Long notaryIdOfficeChiefOld_action) {
        this.notaryIdOfficeChiefOld_action = notaryIdOfficeChiefOld_action;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDateStr() {
        if(effectiveDate!=null)
        effectiveDateStr = UtilsDate.date2str(effectiveDate, "dd-MM-yyyy");
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }
    
    
}
