
package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sang
 */

@Data
public class OrgNotaryInfoView {
    
    /*Org Notary*/
    private String name;
    private String adminName;
    private String address;
    private Long addressId;
    private String tel;
    private String website;
    private String email;
    private String fax;
    private Long idOrgNotaryInfo;
    private List<Long> notaryIds;
    private Long orgNotaryStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayActive;
    private String dayActiveStr;
    private Long statusOrg;
    @Schema(description = "Trạng thái tổ chức. 0 đang hoạt động, 1 chờ thành lập 2 giải thể, ...", example = "1")
    private String statusOrgStr;
    private Long administrationId;
    private List<NotaryInfoView> listNotary;
    /*Org Notary*/
    
    /*NotaryOfficeChief*/
    private Long notaryIdTrans;
    private String notaryNameTrans;
    private String addressResidentTrans;
    private String officeChiefName;
    private String addressResident;
    private String addressNow;
    private Long sex;
    private Long notaryIdOfficeChief;
    private String phoneNumber;
    private String emailNotary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    private String idNo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    private String addressIdNo;
    private String numberCad;
    /*NotaryOfficeChief*/
    
    /*Dm Document*/
    private Long documentId;
    private String establishment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEstablishment;
    private String dateEstablishmentStr;
    private String paperRegistration;
    @Schema(description = "Số quyết định cấp thẻ CCV/ Số văn bản thông báo")
    private String dispatchCode;
    @Schema(description = "Người ký")
    private String signer;
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Ngày ký")
    private Date dateSign;
    @Schema(description = "Ngày ký")
    private String dateSignStr;
    private String fileName;
    private String linkFile;
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Ngày hiệu lực")
    private Date effectiveDate;
    @Schema(description = "Ngày hiệu lực")
    private String effectiveDateStr;
    private Long documentType;
    /*Dm Document*/
    
    /*Org Notary Action*/
    private String note;
    private String parameter;
    private Long onaType;
    private String onaTypeStr;
    private Long idOrgNotaryAction;
    private String createOnaBy;
    private String updateOnaBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDateOna;
    private String genDateOnaStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateOna;
    private String lastUpdateOnaStr;
    /*Org Notary Action*/
    
    /*Org Notary Penalize*/
    private Long leverPenalize;
    private String leverPenalizeStr;
    private Long typePenalize;
    private String typePenalizeStr;
    private String penalizeReason;
    private Long penalizeId;
    private Long administrationIdPenalty;
    private String administrationIdPenaltyStr;
    private Long additionalPenalty;
    private String additionalPenaltyStr;
    private Long moneyPenalty;
    
    /*Org Notary Penalize*/
    /*Org History*/
    private String actionName;
    /*Org History*/


    private Long countNotary;


    public Long getMoneyPenalty() {
        return moneyPenalty;
    }

    public void setMoneyPenalty(Long moneyPenalty) {
        this.moneyPenalty = moneyPenalty;
    }
    
    public String getAdministrationIdPenaltyStr() {
        return administrationIdPenaltyStr;
    }

    public void setAdministrationIdPenaltyStr(String administrationIdPenaltyStr) {
        this.administrationIdPenaltyStr = administrationIdPenaltyStr;
    }
    
    public Long getAdditionalPenalty() {
        return additionalPenalty;
    }

    public void setAdditionalPenalty(Long additionalPenalty) {
        this.additionalPenalty = additionalPenalty;
    }

    public String getAdditionalPenaltyStr() {
        
        additionalPenaltyStr = ConstantsTccc.ADDITIONAL_PENALTY.getStr(additionalPenalty, additionalPenaltyStr);
        return additionalPenaltyStr;
    }

    public void setAdditionalPenaltyStr(String additionalPenaltyStr) {
        this.additionalPenaltyStr = additionalPenaltyStr;
    }

    
    public Long getAdministrationIdPenalty() {
        return administrationIdPenalty;
    }

    public void setAdministrationIdPenalty(Long administrationIdPenalty) {
        this.administrationIdPenalty = administrationIdPenalty;
    }
    
    public String getParameter() {
        return parameter;
    }
    public void setParameter(String parameter) {    
        this.parameter = parameter;
    }

    public String getLeverPenalizeStr() {
        leverPenalizeStr = ConstantsTccc.LEVER_PENALIZE.getStr(leverPenalize, leverPenalizeStr);
        return leverPenalizeStr;
    }

    public void setLeverPenalizeStr(String leverPenalizeStr) {
        this.leverPenalizeStr = leverPenalizeStr;
    }

    public String getTypePenalizeStr() {
        typePenalizeStr = ConstantsTccc.TYPE_PENALIZE.getStr(typePenalize, typePenalizeStr);
        return typePenalizeStr;
    }

    public void setTypePenalizeStr(String typePenalizeStr) {
        this.typePenalizeStr = typePenalizeStr;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    private Long type;

    public List<NotaryInfoView> getListNotary() {
        return listNotary;
    }

    public void setListNotary(List<NotaryInfoView> listNotary) {
        this.listNotary = listNotary;
    }
    
    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getAdministrationId() {
        return administrationId;
    }

    public void setAdministrationId(Long administrationId) {
        this.administrationId = administrationId;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailNotary() {
        return emailNotary;
    }

    public void setEmailNotary(String emailNotary) {
        this.emailNotary = emailNotary;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Date getIdNoDate() {
        return idNoDate;
    }

    public void setIdNoDate(Date idNoDate) {
        this.idNoDate = idNoDate;
    }

    public String getAddressIdNo() {
        return addressIdNo;
    }

    public void setAddressIdNo(String addressIdNo) {
        this.addressIdNo = addressIdNo;
    }

    public String getNumberCad() {
        return numberCad;
    }

    public void setNumberCad(String numberCad) {
        this.numberCad = numberCad;
    }

    public String getStatusOrgStr() {
        return statusOrgStr;
    }

    public void setStatusOrgStr(String statusOrgStr) {
        this.statusOrgStr = statusOrgStr;
    }
    
    public Long getStatusOrg() {
        return statusOrg;
    }

    public void setStatusOrg(Long statusOrg) {
        this.statusOrgStr = ConstantsTccc.STATUS_ORG_NOTARY.getStr(statusOrg, statusOrgStr);
        this.statusOrg = statusOrg;
    }

    public Long getTypePenalize() {
        return typePenalize;
    }

    public void setTypePenalize(Long typePenalize) {
        this.typePenalize = typePenalize;
    }
    
    public Long getLeverPenalize() {
        return leverPenalize;
    }

    public void setLeverPenalize(Long leverPenalize) {
        
        this.leverPenalize = leverPenalize;
    }

    public String getPenalizeReason() {
        return penalizeReason;
    }

    public void setPenalizeReason(String penalizeReason) {
        this.penalizeReason = penalizeReason;
    }

    public Long getPenalizeId() {
        return penalizeId;
    }

    public void setPenalizeId(Long penalizeId) {
        this.penalizeId = penalizeId;
    }
    
    
    
    public String getAddressResidentTrans() {
        return addressResidentTrans;
    }

    public void setAddressResidentTrans(String addressResidentTrans) {
        this.addressResidentTrans = addressResidentTrans;
    }
    
    public Long getNotaryIdTrans() {
        return notaryIdTrans;
    }

    public void setNotaryIdTrans(Long notaryIdTrans) {
        this.notaryIdTrans = notaryIdTrans;
    }

    public String getNotaryNameTrans() {
        return notaryNameTrans;
    }

    public void setNotaryNameTrans(String notaryNameTrans) {
        this.notaryNameTrans = notaryNameTrans;
    }
    
    public String getAddressNow() {
        return addressNow;
    }

    public void setAddressNow(String addressNow) {
        this.addressNow = addressNow;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    
    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    public Date getDateEstablishment() {
        return dateEstablishment;
    }

    public void setDateEstablishment(Date dateEstablishment) {
        this.dateEstablishment = dateEstablishment;
    }

    public String getDateEstablishmentStr() {
        dateEstablishmentStr = UtilsDate.date2str(dateEstablishment, "dd/MM/yyyy");
        return dateEstablishmentStr;
    }

    public void setDateEstablishmentStr(String dateEstablishmentStr) {
        this.dateEstablishmentStr = dateEstablishmentStr;
    }
    
    public String getDayActiveStr() {
        dayActiveStr = UtilsDate.date2str(dayActive, "dd/MM/yyyy");
        return dayActiveStr;
    }

    public void setDayActiveStr(String dayActiveStr) {
        this.dayActiveStr = dayActiveStr;
    }

    public Date getDayActive() {
        return dayActive;
    }

    public void setDayActive(Date dayActive) {
        this.dayActive = dayActive;
    }
    
    public String getPaperRegistration() {
        return paperRegistration;
    }

    public void setPaperRegistration(String paperRegistration) {
        this.paperRegistration = paperRegistration;
    }

    public String getDateSignStr() {
        dateSignStr = UtilsDate.date2str(dateSign, "dd/MM/yyyy");
        return dateSignStr;
    }

    public void setDateSignStr(String dateSignStr) {
        this.dateSignStr = dateSignStr;
    }

    public String getEffectiveDateStr() {
        effectiveDateStr = UtilsDate.date2str(effectiveDate, "dd/MM/yyyy");
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getGenDateOnaStr() {
        genDateOnaStr = UtilsDate.date2str(genDateOna, "dd/MM/yyyy");
        return genDateOnaStr;
    }

    public void setGenDateOnaStr(String genDateOnaStr) {
        this.genDateOnaStr = genDateOnaStr;
    }

    public String getLastUpdateOnaStr() {
        lastUpdateOnaStr = UtilsDate.date2str(lastUpdateOna, "dd/MM/yyyy");
        return lastUpdateOnaStr;
    }

    public void setLastUpdateOnaStr(String lastUpdateOnaStr) {
        this.lastUpdateOnaStr = lastUpdateOnaStr;
    }

    public String getCreateOnaBy() {
        return createOnaBy;
    }

    public void setCreateOnaBy(String createOnaBy) {
        this.createOnaBy = createOnaBy;
    }

    public String getUpdateOnaBy() {
        return updateOnaBy;
    }

    public void setUpdateOnaBy(String updateOnaBy) {
        this.updateOnaBy = updateOnaBy;
    }

    public Date getGenDateOna() {
        return genDateOna;
    }

    public void setGenDateOna(Date genDateOna) {
        this.genDateOna = genDateOna;
    }

    public Date getLastUpdateOna() {
        return lastUpdateOna;
    }

    public void setLastUpdateOna(Date lastUpdateOna) {
        this.lastUpdateOna = lastUpdateOna;
    }
    
 
    public Long getIdOrgNotaryAction() {
        return idOrgNotaryAction;
    }

    public void setIdOrgNotaryAction(Long idOrgNotaryAction) {
        this.idOrgNotaryAction = idOrgNotaryAction;
    }

    public Long getOrgNotaryStatus() {
        return orgNotaryStatus;
    }

    public void setOrgNotaryStatus(Long orgNotaryStatus) {
        this.orgNotaryStatus = orgNotaryStatus;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    public Long getOnaType() {
        return onaType;
    }

    public void setOnaType(Long onaType) {
        this.onaTypeStr = ConstantsTccc.TYPE_ORG_ACTION.getStr(onaType, onaTypeStr);
        this.onaType = onaType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }
  
    public String getAddressResident() {
        return addressResident;
    }

    public void setAddressResident(String addressResident) {
        this.addressResident = addressResident;
    }

    public String getOfficeChiefName() {
        return officeChiefName;
    }

    public void setOfficeChiefName(String officeChiefName) {
        this.officeChiefName = officeChiefName;
    }
    
    
    public Long getIdOrgNotaryInfo() {
        return idOrgNotaryInfo;
    }

    public void setIdOrgNotaryInfo(Long idOrgNotaryInfo) {
        this.idOrgNotaryInfo = idOrgNotaryInfo;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Long getNotaryIdOfficeChief() {
        return notaryIdOfficeChief;
    }

    public void setNotaryIdOfficeChief(Long notaryIdOfficeChief) {
        this.notaryIdOfficeChief = notaryIdOfficeChief;
    }

    public List<Long> getNotaryIds() {
        return notaryIds;
    }

    public void setNotaryIds(List<Long> notaryIds) {
        this.notaryIds = notaryIds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLinkFile() {
        return linkFile;
    }

    public void setLinkFile(String linkFile) {
        this.linkFile = linkFile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnaTypeStr() {
        return onaTypeStr;
    }

    public void setOnaTypeStr(String onaTypeStr) {
        this.onaTypeStr = onaTypeStr;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    
    
}
