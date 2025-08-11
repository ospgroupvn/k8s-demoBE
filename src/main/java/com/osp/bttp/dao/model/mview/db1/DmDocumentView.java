package com.osp.bttp.dao.model.mview.db1;


import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.dao.model.entity.db1.DmDocument;

import java.util.Date;

public class DmDocumentView {

    private Long id;
    private String dispatchCode;
    private Date dateSign;
    private String signer;
    private String unitSign;
    private String linkFile;
    private String fileName;
    private String note;
    private Long type;
    private Date effectiveDate;
    private Long active;
    private String createdBy;
    private Date genDate;
    private String updatedBy;
    private Date lastUpdate;

    private String typeDataStr;

    public DmDocumentView() {
    }

    public DmDocumentView boToFrom(DmDocument form) {
        DmDocumentView bo = new DmDocumentView();
        if (form.getDispatchCode() != null && !"".equals(form.getDispatchCode())) {
            bo.setDispatchCode(form.getDispatchCode());
        }
        if (form.getDateSign() != null) {
            bo.setDateSign(form.getDateSign());
        }
        if (form.getSigner() != null && !"".equals(form.getSigner())) {
            bo.setSigner(form.getSigner());
        }
        if (form.getUnitSign() != null && !"".equals(form.getUnitSign())) {
            bo.setUnitSign(form.getUnitSign());
        }
        if (form.getLinkFile() != null && !"".equals(form.getLinkFile())) {
            bo.setLinkFile(form.getLinkFile());
        }
        if (form.getFileName() != null && !"".equals(form.getFileName())) {
            bo.setFileName(form.getFileName());
        }
        if (form.getNote() != null && !"".equals(form.getNote())) {
            bo.setNote(form.getNote());
        }
        if (form.getType() != null && form.getType() != -1L) {
            bo.setType(form.getType());
        }
        if (form.getEffectiveDate() != null) {
            bo.setEffectiveDate(form.getEffectiveDate());
        }
        if (form.getActive() != null && form.getActive() != -1L) {
            bo.setActive(form.getActive());
        }
        if (form.getCreatedBy() != null && !"".equals(form.getCreatedBy())) {
            bo.setCreatedBy(form.getCreatedBy());
        }
        if (form.getGenDate() != null) {
            bo.setGenDate(form.getGenDate());
        }
        if (form.getUpdatedBy() != null && !"".equals(form.getUpdatedBy())) {
            bo.setUpdatedBy(form.getUpdatedBy());
        }
        if (form.getLastUpdate() != null) {
            bo.setLastUpdate(form.getLastUpdate());
        }
        bo.getTypeDataStr();
        return bo;

    }

    public String getTypeDataStr() {
        typeDataStr = ConstantsTccc.LOAI_CONG_VAN.getSelectDocument(type, typeDataStr);
        return typeDataStr;
    }

    public void setTypeDataStr(String typeDataStr) {
        this.typeDataStr = typeDataStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
