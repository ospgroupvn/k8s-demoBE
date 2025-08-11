
package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.utils.UtilsDate;

import java.util.Date;

/**
 *
 * @author Tuan
 */
public class OrgNotaryTransferView {
   
    /*Org Notary From*/
    private String nameOrgFrom;
    private Long idOrgFrom;
    /*Org Notary From*/

    /*Org Notary To*/
    private String nameOrgTo;
    private Long idOrgTo;
    private String addressOrgTo;
    /*Org Notary From*/
    
    
    /*Dm Document*/
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

    /*OrgNotaryTransfer*/
    private Long idOrgNotaryTransfer;
    /*OrgNotaryTransfer*/

    public Long getIdOrgNotaryTransfer() {
        return idOrgNotaryTransfer;
    }

    public void setIdOrgNotaryTransfer(Long idOrgNotaryTransfer) {
        this.idOrgNotaryTransfer = idOrgNotaryTransfer;
    }
  
    public String getNameOrgFrom() {
        return nameOrgFrom;
    }

    public void setNameOrgFrom(String nameOrgFrom) {
        this.nameOrgFrom = nameOrgFrom;
    }

    public Long getIdOrgFrom() {
        return idOrgFrom;
    }

    public void setIdOrgFrom(Long idOrgFrom) {
        this.idOrgFrom = idOrgFrom;
    }

    public String getNameOrgTo() {
        return nameOrgTo;
    }

    public void setNameOrgTo(String nameOrgTo) {
        this.nameOrgTo = nameOrgTo;
    }

    public Long getIdOrgTo() {
        return idOrgTo;
    }

    public void setIdOrgTo(Long idOrgTo) {
        this.idOrgTo = idOrgTo;
    }

    public String getAddressOrgTo() {
        return addressOrgTo;
    }

    public void setAddressOrgTo(String addressOrgTo) {
        this.addressOrgTo = addressOrgTo;
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
        dateSignStr = UtilsDate.date2str(dateSign, "dd/MM/yyyy");
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
        effectiveDateStr = UtilsDate.date2str(effectiveDate, "dd/MM/yyyy");
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
