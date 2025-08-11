package com.osp.bttp.dao.model.dto.db3;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryPenalizeCreateDto {
    // document
    @NotNull
    private String dispatchCode; // số văn bản
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;

    private Date decisionDate; // ngày quyết định

    private Date effectiveDate; // ngày hiệu lực

    // penlize
    private Long administrationIdPenalty;

    private Long notaryInfoId;
    @Max(3)
    @Min(1)
    private Long orgNotaryId;

    @Max(2)
    @Min(1)
    private Long typePenalize;

    private String reason;

    private Long leverPenalize;

    private Long additionalPenalty;

    private Long moneyPenalty;

}
