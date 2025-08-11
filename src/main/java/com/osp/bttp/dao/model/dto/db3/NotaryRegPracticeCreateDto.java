package com.osp.bttp.dao.model.dto.db3;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryRegPracticeCreateDto {
    // document
    @NotNull
    private String dispatchCode; // số văn bản
    private String reason;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;
    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực
    // reqPractice

    private Long orgNotaryInfoId;

    private Long notaryInfoId;
    @NotNull(message = "Chứng chỉ hành nghề k được null")
    private String numberCad;

    private Long status;

    private Long notaryReq;
    private Long administrationId;

    private Date dateReq;

    private Long typeNotaryInfo;
}
