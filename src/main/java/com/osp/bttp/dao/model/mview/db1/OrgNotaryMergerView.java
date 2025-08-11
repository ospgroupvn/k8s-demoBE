
package com.osp.bttp.dao.model.mview.db1;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Tuan
 */
public class OrgNotaryMergerView {
    
    /*Org Notary Recive*/
    private String nameOrgRecive;
    private String addressOrgRecive;
    private Long idOrgRecive;
    /*Org Notary Recive*/
    private Long idOrgNotaryTransfer;
    
    /*Dm Document*/
    private Long documentId;
    private String dispatchCode;
    private String signer;
    private Date dateSign;
    private String dateSignStr;
    private String fileName;
    private String linkFile;
    private Date effectiveDate;
    private String effectiveDateStr;
    private Long documentType;
    private String createdBy;
    private Date genDate;
    private String updatedBy;
    private Date lastUpdate;
    /*Dm Document*/

    /*list bị sáp nhập*/
    private List<OrgNotaryInfoView> listOrgNotary;

    public Long getIdOrgNotaryTransfer() {
        return idOrgNotaryTransfer;
    }

    public void setIdOrgNotaryTransfer(Long idOrgNotaryTransfer) {
        this.idOrgNotaryTransfer = idOrgNotaryTransfer;
    }
    
    public Long getIdOrgRecive() {
        return idOrgRecive;
    }

    public void setIdOrgRecive(Long idOrgRecive) {
        this.idOrgRecive = idOrgRecive;
    }
    
    public String getNameOrgRecive() {
        return nameOrgRecive;
    }

    public void setNameOrgRecive(String nameOrgRecive) {
        this.nameOrgRecive = nameOrgRecive;
    }

    public String getAddressOrgRecive() {
        return addressOrgRecive;
    }

    public void setAddressOrgRecive(String addressOrgRecive) {
        this.addressOrgRecive = addressOrgRecive;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDispatchCode() {
        return dispatchCode;
    }

    public void setDispatchCode(String dispatchCode) {
        this.dispatchCode = dispatchCode;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Date getDateSign() {
        return dateSign;
    }

    public void setDateSign(Date dateSign) {
        this.dateSign = dateSign;
    }

    public String getDateSignStr() {
        return dateSignStr;
    }

    public void setDateSignStr(String dateSignStr) {
        this.dateSignStr = dateSignStr;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    public List<OrgNotaryInfoView> getListOrgNotary() {
        return listOrgNotary;
    }

    public void setListOrgNotary(List<OrgNotaryInfoView> listOrgNotary) {
        this.listOrgNotary = listOrgNotary;
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
    
    
}
