package com.osp.bttp.dao.model.mview.bttp;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NotaryInfoView {
    /*NotaryInfo*/
    private Long idNotaryInfo;
    @Schema(description = "Tên công chứng viên")
    private String nameNotaryInfo;
    private Long sex;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    private String yearBirthDay;
    private String idNo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    private String addressIdNo;
    private String addressResident;
    private Long addressResidentId;
    private String addressNow;
    private Long addressNowId;
    private Long statusNotaryInfo;
    private String phoneNumberNotaryInfo;
    private String emailNotaryInfo;
    private Long activeNotaryInfo;
    private String statusNotaryInfoStr;
    private String birthDayStr;
    private String birthDayStr_;
    private String sexStr;
    private String createdBy;
    private String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    private String strGenDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private String strLastUpdate;
    private Long typeNotary;
    
    /*Document*/
    private Long idDocument;
    private String dispatchCode;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    private String signer;
    private String unitSign;
    private String linkFile;
    private Long typeDocument;
    private Long activeDocument;
    private String fileNameDocument;
    private String dateSignStr;
    private String fileName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    private String effectiveDateStr;

    /*OrgNotaryInfo*/
    private Long idOrgNotaryInfo;
    @Schema(description = "Tên tổ chức công chứng")
    private String nameOrgNotaryInfo;
    private String address;
    @Schema(description = "Địa chỉ tổ chức công chứng")
    private String orgNotaryAddress;
    private String telOrgNotaryInfo;
    private String faxOrgNotaryInfo;
    private String emailOrgNotaryInfo;
    private String website;
    private Long notaryIdOfficeChief;
    private Long activeOrgNotaryInfo;
    private Long typeOrg;
    private Long orgAddressId;
    private Long status_org;
    private String status_org_Str;
    
    /*NotaryRegPractice*/
    private Long idNotaryRegPractice;
    private String numberCad;

    /*ProbationaryInfo*/
    private String certificateCode;
    private String name;
    private Long status;
    private String statusStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSignProbationary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    private Long documentCertificateId;
    private String note;
    
    //NotaryAppoint
    private Long idAppoint;
    private String probationaryCode;
    private Long typeAppoint;
    private String typeAppointStr;
    private String reasonDisAppoint;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDateAppoint;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateAppoint;
    private String updatedByAppoint;
    
    //MotaryDismissed
    private Long idDismissed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDateDismissed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDismissed;
    private String updatedByDismissed;
    private Long typeDismissed;
    private String typeDismissedStr;
    
    //notaryReappoint
    private Long idReAppoint;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateReAppoint;
    private String updatedByReAppoint;
    
    /*NotarySuspendWork*/
    private Long idSuspendWork;/*id tạm đình chỉ*/
    private Long typeSupend;/*loại quyết định*/
    private Long supendId;/*id hủy tạm đình chỉ*/
    private String typeSupendStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date termOfSuspension;

    /*NotaryCard*/
    private Long idNotaryCard;
    private Long statusNotaryCard;
    private String statusNotaryCardStr;
    private Long activeCard;
    private String activeCardStr;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReq;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSignApo;
    private String dispatchCodeApo;
    private String reason;
    
    /*NOTARY_PENALIZE*/
    private Long notaryPenalizeId;
    private Long leverPenalize;
    private String leverPenalizeStr;
    private Long activePena;
    private String activePenaStr;
    private Long moneyPenalty;
    private Long typePenalize;
    private Long additionalPenalty;
    private String typePenalizeStr;
    private String additionalPenaltyStr;
    
    /*ProbationaryInfoDetail*/
    private Long idDetail;
    private Long idPro;

    //DM_ADMINISTRATION
    @Schema(description = "Tên cơ quan quản lý")
    private String nameAdmin;
    @Schema(description = "Địa chỉ cơ quan quản lý")
    private String addressAdmin;
    
    //NotaryRequest
    private Long idRequest;
    private Long requestType;
    private String requestTypeStr;
    
    //AdmParameter
    private String value;
    private String userCreate;

    //DM_AREA
    private String provinceName;
    private String dispatchCodeBn;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSignBn;

    /*status NOTARY_REG_PRACTICE*/
    private Long status_prac;
    private String status_prac_Str;

    public NotaryInfoView() {
    }

    
    public String getRequestTypeStr() {
        requestTypeStr = ConstantsTccc.REQUEST_TYPE.getStr(requestType, requestTypeStr);
        return requestTypeStr;
    }

    public String getTypeSupendStr() {
        typeSupendStr = ConstantsTccc.TYPE_SUPEND.getStr(typeSupend, typeSupendStr);
        return typeSupendStr;
    }

    public String getActiveCardStr() {
        activeCardStr = ConstantsTccc.ACTIVE.getStr(activeCard,activeCardStr);
        return activeCardStr;
    }

    public String getActivePenaStr() {
        activePenaStr = ConstantsTccc.ACTIVE.getStr(activePena,activePenaStr);
        return activePenaStr;
    }

    public String getLeverPenalizeStr() {
        leverPenalizeStr = ConstantsTccc.LEVER_PENALIZE.getStr(leverPenalize,leverPenalizeStr);
        return leverPenalizeStr;
    }

    public void setActivePenaStr(String activePenaStr) {
        this.activePenaStr = activePenaStr;
    }

    public String getTypeDismissedStr() {
        typeDismissedStr = ConstantsTccc.REQUEST_TYPE.getStr(typeDismissed,typeDismissedStr);
        return typeDismissedStr;
    }

    public String getStatusNotaryCardStr() {
        statusNotaryCardStr = ConstantsTccc.TYPE_NOTARY_CARD.getStr(statusNotaryCard,statusNotaryCardStr);
        return statusNotaryCardStr;
    }

    public String getTypeAppointStr() {
        typeAppointStr = ConstantsTccc.TYPE_APPOINT.getTypeAppoint(typeAppoint,typeAppointStr);
        return typeAppointStr;
    }

    public String getBirthDayStr_() {
        birthDayStr_ = UtilsDate.date2str(birthDay, "dd/MM/yyyy");
        return birthDayStr_;
    }

    public void setBirthDayStr_(String birthDayStr_) {
        this.birthDayStr_ = birthDayStr_;
    }
    
    public String getBirthDayStr() {
        birthDayStr = UtilsDate.date2str(birthDay, "yyyy");
        return birthDayStr;
    }

    public String getDateSignStr() {
        if(dateSignStr == null) {
            dateSignStr = UtilsDate.date2str(dateSign, "dd/MM/yyyy");
        }
        return dateSignStr;
    }

    public String getEffectiveDateStr() {
        effectiveDateStr = UtilsDate.date2str(effectiveDate, "dd/MM/yyyy");
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }
    
    public String getStatusNotaryInfoStr() {
        statusNotaryInfoStr = ConstantsTccc.NOTARY_STATUS.getStr(statusNotaryInfo,statusNotaryInfoStr);
        return statusNotaryInfoStr;
    }

    public String getStatusStr() {
        statusStr = ConstantsTccc.NOTARY_STATUS.getStr(status,statusStr);
        return statusStr;
    }

    public String getSexStr() {
        sexStr = ConstantsTccc.SEX.getStr(sex,sexStr);
        return sexStr;
    }



    public void setStatusNotaryInfoStr(String statusNotaryInfoStr) {
        this.statusNotaryInfoStr = statusNotaryInfoStr;
    }


    public String getStrGenDate() {
        if(strGenDate == null){
            strGenDate = UtilsDate.date2str(genDate, "dd/MM/yyyy");
        }
        
        return strGenDate;
    }

    public void setStrGenDate(String strGenDate) {
        this.strGenDate = strGenDate;
    }

    public String getStrLastUpdate() {
        if(strLastUpdate == null){
            strLastUpdate = UtilsDate.date2str(lastUpdate, "dd/MM/yyyy");
        }
       
        return strLastUpdate;
    }



    public String getStatus_prac_Str() {
        status_prac_Str = ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.getStr(status_prac,status_prac_Str);
        return status_prac_Str;
    }



    public String getTypePenalizeStr() {
        typePenalizeStr = ConstantsTccc.TYPE_PENALIZE.getStr(typePenalize, typePenalizeStr);
        return typePenalizeStr;
    }

    public void setTypePenalizeStr(String typePenalizeStr) {
        this.typePenalizeStr = typePenalizeStr;
    }

    public String getAdditionalPenaltyStr() {
        additionalPenaltyStr = ConstantsTccc.ADDITIONAL_PENALTY.getStr(additionalPenalty, additionalPenaltyStr);
        return additionalPenaltyStr;
    }

    public void setAdditionalPenaltyStr(String additionalPenaltyStr) {
        this.additionalPenaltyStr = additionalPenaltyStr;
    }

    public Long getStatus_org() {
        return status_org;
    }

    public void setStatus_org(Long status_org) {
        this.status_org = status_org;
    }

    public String getStatus_org_Str() {
        status_org_Str = ConstantsTccc.STATUS_ORG_NOTARY.getStr(status_org, status_org_Str);
        return status_org_Str;
    }

    public void setStatus_org_Str(String status_org_Str) {
        this.status_org_Str = status_org_Str;
    }
}
