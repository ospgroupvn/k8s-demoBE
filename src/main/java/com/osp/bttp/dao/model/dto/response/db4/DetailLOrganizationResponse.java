package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author sangnk
 * @Created 15/03/2025 - 09:10 AM
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class DetailLOrganizationResponse extends LOrganization {
    @Schema(description = "Thông tin người đại diện")
    private DetailLLawyerResponse lawyerLegalRepresentative;

    @Schema(description = "Thông tin đoàn luật sư")
    private LLawyerAssociation lawyerAssociation;

    public DetailLOrganizationResponse(LOrganization organization) {
        BeanUtils.copyProperties(organization, this);
    }
}
