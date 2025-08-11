package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.dao.model.type.OrganizationStatus;
import com.osp.bttp.dao.model.type.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionOrganizationRequest {

    @Require
    private String licenseNo;

    @Require
    private LocalDate licenseDate;

    @Require
    private OrganizationType type;

    @Require
    private String fullName;

    @Require
    private String provinceCode;

    @Require
    private String wardCode;

    @Require
    private String address;

    private String telNumber;

    private String email;

    @Require
    private OrganizationStatus status;

    private String orgRoot;

    private String managerUuid;

    private List<MemberPartnerRequest> memberPartners;
}
