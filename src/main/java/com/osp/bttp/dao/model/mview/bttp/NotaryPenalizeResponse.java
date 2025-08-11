package com.osp.bttp.dao.model.mview.bttp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryPenalizeResponse {
    private Long id;
    private String dispatchCode; // số văn bản
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;

    private String singer;

    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực

    // penlize
    private Long administrationIdPenalty;

    private Long notaryInfoId;

    private Long orgNotaryId;

    private Long typePenalize;

    private String reason;

    private Long leverPenalize;

    private Long additionalPenalty;

    private Long moneyPenalty;
    private Long documentId;
}
