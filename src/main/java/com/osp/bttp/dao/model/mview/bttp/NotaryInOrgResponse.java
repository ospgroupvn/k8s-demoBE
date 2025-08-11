package com.osp.bttp.dao.model.mview.bttp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryInOrgResponse {
    //notary
    private String name;
    private String idNo;  // CMND/CCCD
    private Date birthDate;
    private String addressResident;
    private Long addressResidentId;
    private String phoneNumber;
    //document
    private Long documentId;
    private String dispatchCode; // số văn bản quyết định
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;
    private Date decisionDate; // ngày quyết định , bổ nhiệm

    // reqPractice
    private String numberCad; // số thẻ CCV
    ;
}
