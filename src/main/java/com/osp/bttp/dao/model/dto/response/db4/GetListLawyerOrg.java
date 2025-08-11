package com.osp.bttp.dao.model.dto.response.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLawyerOrg {
    private Long id;

    private Long idLawyer;

    private Long idOrg;

    private String orgName;
}
