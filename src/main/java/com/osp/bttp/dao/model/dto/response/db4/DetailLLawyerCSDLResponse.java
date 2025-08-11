package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.model.entity.db4.LLicenseChange;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author sangnk
 * @Created 15/03/2025 - 9:12 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Data
@NoArgsConstructor
public class DetailLLawyerCSDLResponse extends LLawyer {

    private String nationalityName;

    private String provinceName;

    private String wardName;

    private String addressName;
//    @Schema(description = "Danh sách tổ chức của luật sư")
//    private List<GetListLawyerOrg> orgList;

    private String orgName;

    private String assocName;
//
//    @Schema(description = "Danh sách giấy phép thẻ luật sư của luật sư")
//    private List<LLicense> lsCardLicenses;

    public DetailLLawyerCSDLResponse(LLawyer lawyer) {
        BeanUtils.copyProperties(lawyer, this);
    }
}
