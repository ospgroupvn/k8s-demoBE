package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AuctioneerWithCardLatestHasEffectiveDto {

    private String uuid;

    private String fullName;

    private LocalDate dob;

    private String certCode;
    private LocalDate dateOfDecisionCert;
    private AuCardStatus cardStatus;

    private String cardCode;
    private LocalDate dateOfDecisionCard;
    private AuCertStatus certStatus;

    public AuctioneerWithCardLatestHasEffectiveDto(String uuid,
                                                   String fullName,
                                                   LocalDate dob,
                                                   String certCode,
                                                   LocalDate dateOfDecisionCert,
                                                   AuCardStatus cardStatus,
                                                   String cardCode,
                                                   LocalDate dateOfDecisionCard,
                                                   AuCertStatus certStatus) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.dob = dob;
        this.certCode = certCode;
        this.dateOfDecisionCert = dateOfDecisionCert;
        this.cardStatus = cardStatus;
        this.cardCode = cardCode;
        this.dateOfDecisionCard = dateOfDecisionCard;
        this.certStatus = certStatus;
    }
}
