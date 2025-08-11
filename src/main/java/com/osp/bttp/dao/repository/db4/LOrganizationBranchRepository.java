package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.dto.response.db4.GetListOrgBranchResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListRegisOfOrgChange;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.entity.db4.LOrganizationBranch;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LOrganizationBranchRepository extends BaseRepository<LOrganizationBranch>, JpaSpecificationExecutor<LOrganizationBranch> {
    @Query("SELECT new com.osp.bttp.dao.model.dto.response.db4.GetListOrgBranchResponse" +
            "(l.orgBranchId,l.orgId,l.orgName,l.address,l.phone,o.fullName,l.lawyerLegalRepresentativeId,l.businessLicenseNumber,l.businessLicenseIssueDate) " +
            "from LOrganizationBranch l \n" +
            "join LLawyer o on o.lawyerId = l.lawyerLegalRepresentativeId \n" +
            "WHERE l.orgId = ?1 \n" )
    List<GetListOrgBranchResponse> getListOrgBranch(Long orgId);

}
