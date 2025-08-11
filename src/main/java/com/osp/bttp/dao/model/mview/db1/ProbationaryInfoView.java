package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class ProbationaryInfoView {

    private Long id;
    private Long notaryInfoId;
    private Long orgNotaryInfoId;
    private Long documentId;
    private String certificateCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    private String dateStartStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    private String dateEndStr;
    private Long dateNumber;
    private Long documentCertificateId;

    private String fullName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    private String birthDayStr;
    private String idNo;
    private String nameOrgNotaryInfo;
    private Long statusNotaryInfo;
    private String statusNotaryInfoStr;

    private Long probationaryInfoId;
    private Long status;

    private String dispatchCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    private String dateSignStr;
    private String signer;
    private String unitSign;
    private String linkFile;
    private String fileName;
    private String note;
    private Long type;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    private String createdBy;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String genDateStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private String lastUpdateStr;
    private String address;

    //DM_ADMINISTRATION
    private String nameAdmin;
    private String name_ntaryInfo;

    public String getNameAdmin() {
        return nameAdmin;
    }

    public void setNameAdmin(String nameAdmin) {
        this.nameAdmin = nameAdmin;
    }

    public Long getDocumentCertificateId() {
        return documentCertificateId;
    }

    public void setDocumentCertificateId(Long documentCertificateId) {
        this.documentCertificateId = documentCertificateId;
    }

    public ProbationaryInfoView() {
    }

    public String getGenDateStr() {
        genDateStr = UtilsDate.date2str(genDate, "dd/MM/yyyy");
        return genDateStr;
    }

    public void setGenDateStr(String genDateStr) {
        this.genDateStr = genDateStr;
    }

    public String getDateSignStr() {
        dateSignStr = UtilsDate.date2str(dateSign, "dd/MM/yyyy");
        return dateSignStr;
    }

    public void setDateSignStr(String dateSignStr) {
        this.dateSignStr = dateSignStr;
    }
    
    public String getLastUpdateStr() {
        lastUpdateStr = UtilsDate.date2str(lastUpdate, "dd/MM/yyyy");
        return lastUpdateStr;
    }

    public void setLastUpdateStr(String lastUpdateStr) {
        this.lastUpdateStr = lastUpdateStr;
    }

    public Long getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(Long dateNumber) {
        this.dateNumber = dateNumber;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getProbationaryInfoId() {
        return probationaryInfoId;
    }

    public void setProbationaryInfoId(Long probationaryInfoId) {
        this.probationaryInfoId = probationaryInfoId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateStartStr() {
        dateStartStr = UtilsDate.date2str(dateStart, "dd/MM/yyyy");
        return dateStartStr;
    }

    public void setDateStartStr(String dateStartStr) {
        this.dateStartStr = dateStartStr;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateEndStr() {
        dateEndStr = UtilsDate.date2str(dateEnd, "dd/MM/yyyy");
        return dateEndStr;
    }

    public void setDateEndStr(String dateEndStr) {
        this.dateEndStr = dateEndStr;
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

    public String getBirthDayStr() {
        birthDayStr = UtilsDate.date2str(birthDay, "dd/MM/yyyy");
        return birthDayStr;
    }

    public void setBirthDayStr(String birthDayStr) {
        this.birthDayStr = birthDayStr;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getNameOrgNotaryInfo() {
        return nameOrgNotaryInfo;
    }

    public void setNameOrgNotaryInfo(String nameOrgNotaryInfo) {
        this.nameOrgNotaryInfo = nameOrgNotaryInfo;
    }

    public Long getStatusNotaryInfo() {
        return statusNotaryInfo;
    }

    public void setStatusNotaryInfo(Long statusNotaryInfo) {
        this.statusNotaryInfo = statusNotaryInfo;
    }

    public String getStatusNotaryInfoStr() {
        statusNotaryInfoStr = ConstantsTccc.NOTARY_STATUS.getStr(statusNotaryInfo, statusNotaryInfoStr);
        return statusNotaryInfoStr;
    }

    public void setStatusNotaryInfoStr(String statusNotaryInfoStr) {
        this.statusNotaryInfoStr = statusNotaryInfoStr;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName_ntaryInfo() {
        return name_ntaryInfo;
    }

    public void setName_ntaryInfo(String name_ntaryInfo) {
        this.name_ntaryInfo = name_ntaryInfo;
    }
}
