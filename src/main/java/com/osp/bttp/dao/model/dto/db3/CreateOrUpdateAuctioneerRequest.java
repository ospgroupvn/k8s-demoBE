package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.dao.model.type.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateAuctioneerRequest {

    @Require
    private String fullName;

    @Require
    private GenderType gender;

    @Require
    private LocalDate dob;

    @Require
    private String telNumber;

    private String email;

    @Require
    private String idCode;

    @Require
    private LocalDate idDoi;

    @Require
    private String idPoi;

    @Require
    private String addPermanent;

    @Require
    private String provinceCode;

    @Require
    private String wardCode;
}
