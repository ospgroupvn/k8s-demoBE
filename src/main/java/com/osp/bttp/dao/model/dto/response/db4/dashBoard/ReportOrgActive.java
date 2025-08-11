package com.osp.bttp.dao.model.dto.response.db4.dashBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOrgActive {

    private Long provinceId;

    private String provinceName;

    private Integer totalActive;

    private Integer totalInActive;

    private Integer totalRevoke1;

    private Integer totalRevoke2;

    private Integer totalDisable;

}
