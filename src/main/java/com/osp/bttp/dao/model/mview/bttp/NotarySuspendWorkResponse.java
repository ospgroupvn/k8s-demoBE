package com.osp.bttp.dao.model.mview.bttp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotarySuspendWorkResponse {
    private Long id;
    @NotNull
    private String dispatchCode; // số văn bản
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;

    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực

    // suspendWork
    private Long notaryInfoId;

    private Long orgNotaryId;

    private String reason;

    private Long typeSupend;
    private Long dateNumber;
    private Long documentId;
}
