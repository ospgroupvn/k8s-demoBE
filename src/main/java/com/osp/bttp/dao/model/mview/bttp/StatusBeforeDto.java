package com.osp.bttp.dao.model.mview.bttp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusBeforeDto {
    private Long kind;
    private String kindStr;
    private Long type;
    private String typeStr;
    private Long statusNotary;
    private String statusNotaryStr;
}
