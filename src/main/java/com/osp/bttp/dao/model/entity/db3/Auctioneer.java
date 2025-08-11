package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.type.GenderType;
import jakarta.persistence.CascadeType;
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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AUCTIONEER")
public class Auctioneer extends Auditor {

    @Id
    @UuidGenerator
    @Column(name = "UUID")
    private String uuid;

    @Column(name = "FULL_NAME", nullable = false, length = 150)
    private String fullName;

    @Column(name = "GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "DOB")
    private LocalDate dob;

    @Column(name = "TEL_NUMBER", length = 50)
    private String telNumber;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ID_CODE", nullable = false, unique = true, length = 15)
    private String idCode;

    @Column(name = "ID_DOI")
    private LocalDate idDoi;

    @Column(name = "ID_POI")
    private String idPoi;

    @Column(name = "ADDR_PERMANENT")
    private String addPermanent;

    @Column(name = "PROVINCE_CODE")
    private String provinceCode;

    @Column(name = "WARD_CODE")
    private String wardCode;

    @OneToMany(mappedBy = "auctioneer", cascade = CascadeType.REMOVE)
    private List<AuctionCardInfo> auctionCardInfos;

    @OneToMany(mappedBy = "auctioneer", cascade = CascadeType.REMOVE)
    private List<AuctionCertificateInfo> auctionCertificateInfos;
}
