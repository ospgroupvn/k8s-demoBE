package com.osp.bttp.dao.model.dto.db3;

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
public class MemberPartnerRequest {

    private String uuid;

    private String fullName;

    private String dob;

    private String certCode;

    private LocalDate dateOfDecision;
}
