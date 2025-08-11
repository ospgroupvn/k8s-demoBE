package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.type.AuCardStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AUCTION_CARD_INFO")
public class AuctionCardInfo extends Auditor {

    @Id
    @UuidGenerator
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "CARD_CODE", length = 100)
    private String cardCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuCardStatus status;

    @Column(name = "NUMBER_OF_DECISION", length = 100)
    private String numberOfDecision;

    @Column(name = "DATE_OF_DECISION")
    private LocalDate dateOfDecision;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "ISSUE_DATE")
    private LocalDate issueDate;

    @Column(name = "DEPARTMENT_CODE")
    private String departmentCode;

    @Column(name = "FILE_OBJECT", columnDefinition = "CLOB")
    private String fileObj;

    @ManyToOne
    @JoinColumn(name = "auctioneer_id", nullable = false)
    private Auctioneer auctioneer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private AuctionOrganization organization;
}
