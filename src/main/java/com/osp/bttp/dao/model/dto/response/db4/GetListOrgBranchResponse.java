package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.LOrganizationBranch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListOrgBranchResponse extends LOrganizationBranch {
    private Long orgBranchId;

    private Long orgId;


    private String orgName;


    private String address;

    private String phone;


    private String nameRep;

    private Long lawyerLegalRepresentativeId;


    private String businessLicenseNumber;


    private Date businessLicenseIssueDate;

}
