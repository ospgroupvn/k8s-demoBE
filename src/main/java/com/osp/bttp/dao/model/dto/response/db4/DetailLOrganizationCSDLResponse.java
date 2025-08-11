package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.entity.db4.LOrganizationBranch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author sangnk
 * @Created 15/03/2025 - 09:10 AM
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class DetailLOrganizationCSDLResponse extends LOrganization {
    @Schema(description = "Thông tin người đại diện")
    private GetLawyerLegalRep lawyerLegalRep;

    private String lawyerAssName;

    private String addressName;

    private String provinceName;

    private String wardName;

    @Schema(description = "Thông tin thay đổi DKHD/GPTL")
    private List<GetListRegisOfOrgChange> getListRegisOrgChange;

    @Schema(description = "danh sách chi nhánh con")
    private List<GetListOrgBranchResponse> branchOrgList;
    public DetailLOrganizationCSDLResponse(LOrganization organization) {
        BeanUtils.copyProperties(organization, this);
    }
}
