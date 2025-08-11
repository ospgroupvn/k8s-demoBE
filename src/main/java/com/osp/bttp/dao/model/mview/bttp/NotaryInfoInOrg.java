package com.osp.bttp.dao.model.mview.bttp;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.UtilsDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class NotaryInfoInOrg {

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
    // Req
    private String numberCad;

}
