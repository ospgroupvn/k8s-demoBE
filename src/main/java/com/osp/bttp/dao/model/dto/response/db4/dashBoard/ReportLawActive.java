package com.osp.bttp.dao.model.dto.response.db4.dashBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportLawActive {
    private Long provinceId;

    private String provinceName;

    private Integer totalActive;

    private Integer totalRevoke;

    private Integer totalCard;

    private Integer totalNotCard;

}
