package com.osp.bttp.dao.model.mview.bttp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryRegAndAuctionCardResponse {
    private Long id;
    private String dispatchCode; // số văn bản
    private String reason;
    private String linkFile;
    private String fileName;
    private Long active;

    private String singer;

    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực
    // reqPractice

    private Long orgNotaryInfoId;

    private Long notaryInfoId;

    private String numberCad;

    private Long status;

    private Long notaryReq;


    private Date dateReq;

    private Long typeNotaryInfo;
    private Long documentId;
}
