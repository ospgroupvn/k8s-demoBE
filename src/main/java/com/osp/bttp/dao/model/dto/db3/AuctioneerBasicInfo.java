package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctioneerBasicInfo extends AuditorDto {

    private String uuid;

    private String fullName;

    private LocalDate dob;

    private String idCode;

    private String organizationName;

    private String organizationAddress;

    private String orgProvinceCode;

    private String orgProvinceName;

    private String orgWardCode;

    private String orgWardName;

    private AuCertStatus certStatus;

    private AuCardStatus cardStatus;

    // Additional info for list auctioneer in organization
    private String certCode;
    private LocalDate dateOfDecisionCert;
    private String cardCode;
    private LocalDate issueDate;
}
