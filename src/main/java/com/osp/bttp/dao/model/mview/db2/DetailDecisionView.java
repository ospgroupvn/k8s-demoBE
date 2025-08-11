/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;

import com.osp.bttp.common.contants.ConstantsDGTS;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author admin
 */
@Data
public class DetailDecisionView {

    @Schema(description = "ID sở tư pháp", example = "1")
    private Long orgID;

    @Schema(description = "Tên sở", example = "Sở tư pháp TP HCM")
    private String orgName;

    @Schema(description = "Số quyết định", example = "123/2021/QĐ-STP")
    private String numberOfDecision;

    @Schema(description = "Ngày quyết định")
    private Timestamp dateOfDecision;

    @Schema(description = "Ngày hiệu lực")
    private Timestamp effectiveDate;

    @Schema(description = "actType")
    private Long actType;

    private String strActType;

    public String getStrActType() {
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.CAP_MOI_CCHN)) {
            strActType = "Cấp mới CCHN";
        }
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.CAP_LAI_CCHN)) {
            strActType = "Cấp lại CCHN";
        }
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.THU_HOI_CCHN)) {
            strActType = "Thu hồi CCHN";
        }
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.CAP_MOI_THE_DGV)) {
            strActType = "Cấp mới Thẻ ĐGV";
        }
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.CAP_LAI_THE_DGV)) {
            strActType = "Cấp lại Thẻ ĐGV";
        }
        if (Objects.equals(getActType(), ConstantsDGTS.ACT_TYPE.THU_HOI_THE_DGV)) {
            strActType = "Thu hồi Thẻ ĐGV";
        }
        return strActType;
    }
    
}
