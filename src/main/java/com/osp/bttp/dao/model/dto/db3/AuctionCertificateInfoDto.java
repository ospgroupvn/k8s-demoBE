package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.dao.model.type.AuCertStatus;
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
public class AuctionCertificateInfoDto {

    private String uuid;

    @Require
    private String certCode;

    @Require
    private AuCertStatus status;

    @Require
    private String numberOfDecision;

    @Require
    private LocalDate dateOfDecision;

    @Require
    private LocalDate effectiveDate;

    private List<AuctionAttachFileDto> attachFile;
}
