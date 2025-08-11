package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.dao.model.type.AuCardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionCardInfoDto {

    private String uuid;

    @Require
    private String cardCode;

    @Require
    private AuCardStatus status;

    @Require
    private String numberOfDecision;

    @Require
    private LocalDate dateOfDecision;

    @Require
    private LocalDate effectiveDate;

    @Require
    private LocalDate issueDate;

    @Require
    private String departmentCode;

    private String departmentText;

    @Require
    private String orgId;
    private String orgName;

    private List<AuctionAttachFileDto> attachFile;
}
