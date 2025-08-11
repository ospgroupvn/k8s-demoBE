package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "MEMBER_PARTER")
public class MemberPartner extends InitializationInfo {

    @Id
    @UuidGenerator
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "FULL_NAME", length = 150)
    private String fullName;

    @Column(name = "DOB", length = 15)
    private String dob;

    @Column(name = "CERT_CODE", length = 50)
    private String certCode;

    @Column(name = "DATE_OF_DECISION")
    private LocalDate dateOfDecision;

    @ManyToOne
    @JoinColumn(name = "ORG_UUID", nullable = false)
    private AuctionOrganization organization;
}
