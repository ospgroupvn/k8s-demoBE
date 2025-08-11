package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.GenderType;
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
public class AuctionOrganizationDto {

    private String licenseNo;

    private LocalDate licenseDate;

    private OrganizationType type;

    private String fullName;

    private String provinceCode;

    private String provinceName;

    private String wardCode;

    private String wardName;

    private String address;

    private String telNumber;

    private String email;

    private OrganizationStatus status;

    private String orgRoot;
    private String orgRootName;
    private String orgRootDepartmentCode;
    private String orgRootDepartmentName;

    private String managerUuid;

    private Manager manager;

    private List<MemberPartnerDto> memberPartners;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class Manager {

        private String uuid;

        private String fullName;

        private GenderType gender;

        private LocalDate dob;

        private String telNumber;

        private String email;

        private String idCode;

        private LocalDate idDoi;

        private String idPoi;

        private String textPoi;

        private String addPermanent;

        private String provinceCode;

        private String textProvince;

        private String wardCode;

        private String textWard;

        private String certCode;
        private LocalDate dateOfDecisionCert;

        private String cardCode;
        private LocalDate dateOfDecisionCard;
    }
}
