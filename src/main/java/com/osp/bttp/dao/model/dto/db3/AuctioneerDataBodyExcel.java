package com.osp.bttp.dao.model.dto.db3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctioneerDataBodyExcel {

    private String uuid;

    private String fullName;

    private String dob;

    private String idCode;

    private String certStatus;

    private String cardStatus;

    private String certCode;

    private String dateOfDecisionCert;

    private String cardCode;

    private String issueDate;

    private String createdBy;

    private String createdDate;

    private String updatedBy;

    private String lastModifiedDate;
}
