package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.type.AuCertStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "AUCTION_CERTIFICATE_INFO")
public class AuctionCertificateInfo extends Auditor {

    @Id
    @UuidGenerator
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "CERT_CODE", length = 100)
    private String certCode;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuCertStatus status;

    @Column(name = "NUMBER_OF_DECISION", length = 100)
    private String numberOfDecision;

    @Column(name = "DATE_OF_DECISION")
    private LocalDate dateOfDecision;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "FILE_OBJECT", columnDefinition = "CLOB")
    private String fileObj;

    @ManyToOne
    @JoinColumn(name = "auctioneer_id", nullable = false)
    private Auctioneer auctioneer;
}
