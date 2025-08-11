/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;


public class NotaryReApppointedView {
    private Long orgNotaryId;
    private String reason;
    private Long active;
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private Long documentId;
    private Long notaryInfoId;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    private String dispatchCode;
    private String fullName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    private String idNo;
    private String signer;
    private Long statusNotaryInfo;
    private String statusNotaryInfoStr;
    private String birthDayStr;
    private String dateSignStr;
    private String nameOrgNotaryInfo;

    public String getNameOrgNotaryInfo() {
        return nameOrgNotaryInfo;
    }

    public void setNameOrgNotaryInfo(String nameOrgNotaryInfo) {
        this.nameOrgNotaryInfo = nameOrgNotaryInfo;
    }
    
    
    public Long getOrgNotaryId() {
        return orgNotaryId;
    }

    public void setOrgNotaryId(Long orgNotaryId) {
        this.orgNotaryId = orgNotaryId;
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

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Long getStatusNotaryInfo() {
        return statusNotaryInfo;
    }

    public void setStatusNotaryInfo(Long statusNotaryInfo) {
        this.statusNotaryInfo = statusNotaryInfo;
    }

    public String getStatusNotaryInfoStr() {
        statusNotaryInfoStr = ConstantsTccc.NOTARY_STATUS.getStr(statusNotaryInfo,statusNotaryInfoStr);
        return statusNotaryInfoStr;
    }

    public void setStatusNotaryInfoStr(String statusNotaryInfoStr) {
        this.statusNotaryInfoStr = statusNotaryInfoStr;
    }

    public String getBirthDayStr() {
        birthDayStr = UtilsDate.date2str(birthDay, "yyyy");
        return birthDayStr;
    }

    public void setBirthDayStr(String birthDayStr) {
        this.birthDayStr = birthDayStr;
    }

    public String getDateSignStr() {
        dateSignStr = UtilsDate.date2str(dateSign, "dd/MM/yyyy");
        return dateSignStr;
    }

    public void setDateSignStr(String dateSignStr) {
        this.dateSignStr = dateSignStr;
    }
    
    
}
