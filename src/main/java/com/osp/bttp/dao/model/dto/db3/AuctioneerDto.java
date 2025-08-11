package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.GenderType;
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
public class AuctioneerDto {

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

    private List<AuctionCertificateInfoDto> auctionCertificateInfos;

    private List<AuctionCardInfoDto> auctionCardInfos;
}
