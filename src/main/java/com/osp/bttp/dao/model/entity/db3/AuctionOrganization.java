package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.type.OrganizationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AUCTION_ORGANIZATION")
public class AuctionOrganization extends Auditor {

    @Id
    @UuidGenerator
    @Column(name = "UUID", length = 36)
    private String uuid;

    @Column(name = "ORG_TYPE", nullable = false)
    private Integer orgType;

    @Column(name = "ORG_ROOT", length = 36)
    private String orgRoot;

    @Column(name = "FULLNAME", nullable = false, length = 200)
    private String fullName;

    @Column(name = "LICENSE_NO", nullable = false, length = 100)
    private String licenseNo;

    @Column(name = "LICENSE_DATE", nullable = false)
    private LocalDate licenseDate;

    @Column(name = "TEL_NUMBER", length = 50)
    private String telNumber;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private OrganizationStatus status;

    @Column(name = "PROVINCE_CODE", length = 10)
    private String provinceCode;

    @Column(name = "WARD_CODE", length = 10)
    private String wardCode;

    @Column(name = "MANAGER_UUID", length = 36)
    private String managerUuid;

    @OneToMany(mappedBy = "organization")
    private List<MemberPartner> memberPartners;
}
