package com.osp.bttp.dao.model.dto.db3;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotarySuspendWorkCreateDto {
    // document
    @NotNull
    private String dispatchCode; // số văn bản
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;
    private Long dateNumber;
    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực

    // suspendWork
    private Long notaryInfoId;

    private Long orgNotaryId;

    private String reason;

    private Long typeSupend;
}
