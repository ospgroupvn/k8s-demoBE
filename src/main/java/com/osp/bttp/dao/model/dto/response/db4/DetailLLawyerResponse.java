package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author sangnk
 * @Created 15/03/2025 - 9:12 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class DetailLLawyerResponse extends LLawyer {
//    @Schema(description = "Danh sách giấy phép hanh nghe của luật sư")
//    private List<LLicense> cchnLicenses;
//
//    @Schema(description = "Danh sách giấy phép hanh nghe của luật sư nước ngoài")
//    private List<LLicense> gphnLicenses;

    @Schema(description = "Danh sách giấy phép thẻ luật sư của luật sư")
    private List<LLicense> lsCardLicenses;

    @Schema(description = "Đoàn luật sư của luật sư")
    private LLawyerAssociation lawyerAssociation;

    @Schema(description = "to chuc luat su")
    private LOrganization organization;

    @Schema(description = "Danh sách thông tin cập nhật của luật sư")
    private List<LLawyerUpdateInfoHis> listLawyerUpdateInfoHis;

    public DetailLLawyerResponse(LLawyer lawyer) {
        BeanUtils.copyProperties(lawyer, this);
    }
}
