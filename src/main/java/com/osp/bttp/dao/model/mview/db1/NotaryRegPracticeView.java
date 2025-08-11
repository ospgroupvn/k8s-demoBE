package com.osp.bttp.dao.model.mview.db1;

import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class NotaryRegPracticeView {

    private Long notaryRegPracticeId;
    private String reason;
    private Long active;
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private Long documentId;
    private Long orgNotaryInfoId;
    private Long notaryInfoId;
    private String dispatchCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    private String signer;
    private String unitSign;
    private String notaryInfoName;
    private String numberCad;
    private String orgNotaryInfoName;
    private Long notaryStatus;
    private String notaryStatusStr;
    private String orgNotaryInfoAddress;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    private String effectiveDateStr;
    private String fileName;
    private String linkFile;
    private Long idWork;

    private String pracStatusStr;
    private Long pracStatus;

    private String activeStr;

    public NotaryRegPracticeView() {
    }

    public String getNotaryStatusStr() {
        notaryStatusStr = ConstantsTccc.NOTARY_STATUS.getStr(notaryStatus,notaryStatusStr);
        return notaryStatusStr;
    }

    public String getEffectiveDateStr() {
        effectiveDateStr = UtilsDate.date2str(effectiveDate, "dd-MM-yyyy");
        return effectiveDateStr;
    }

    public String getActiveStr() {
        activeStr = ConstantsTccc.ACTIVE.getStrNot2(active,activeStr);
        return activeStr;
    }

    public String getPracStatusStr() {
        pracStatusStr = ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.getStr(pracStatus,pracStatusStr);
        return pracStatusStr;
    }





    public void setPracStatusStr(String pracStatusStr) {
        this.pracStatusStr = pracStatusStr;
    }

    public Long getPracStatus() {
        return pracStatus;
    }

    public void setPracStatus(Long pracStatus) {
        this.pracStatus = pracStatus;
    }

    public void setActiveStr(String activeStr) {
        this.activeStr = activeStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setNotaryStatusStr(String notaryStatusStr) {
        this.notaryStatusStr = notaryStatusStr;
    }

    public Long getNotaryStatus() {
        return notaryStatus;
    }

    public void setNotaryStatus(Long notaryStatus) {
        this.notaryStatus = notaryStatus;
    }

    public String getOrgNotaryInfoName() {
        return orgNotaryInfoName;
    }

    public void setOrgNotaryInfoName(String orgNotaryInfoName) {
        this.orgNotaryInfoName = orgNotaryInfoName;
    }

    public String getNotaryInfoName() {
        return notaryInfoName;
    }

    public void setNotaryInfoName(String notaryInfoName) {
        this.notaryInfoName = notaryInfoName;
    }

    public Long getNotaryRegPracticeId() {
        return notaryRegPracticeId;
    }

    public void setNotaryRegPracticeId(Long notaryRegPracticeId) {
        this.notaryRegPracticeId = notaryRegPracticeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
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

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getUnitSign() {
        return unitSign;
    }

    public void setUnitSign(String unitSign) {
        this.unitSign = unitSign;
    }

    public String getNumberCad() {
        return numberCad;
    }

    public void setNumberCad(String numberCad) {
        this.numberCad = numberCad;
    }

    public String getOrgNotaryInfoAddress() {
        return orgNotaryInfoAddress;
    }

    public void setOrgNotaryInfoAddress(String orgNotaryInfoAddress) {
        this.orgNotaryInfoAddress = orgNotaryInfoAddress;
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

    public Long getIdWork() {
        return idWork;
    }

    public void setIdWork(Long idWork) {
        this.idWork = idWork;
    }
}
