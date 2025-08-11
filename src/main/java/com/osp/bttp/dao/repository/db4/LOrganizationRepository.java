package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.dto.response.db4.GetListBranchOrg;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
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
public interface LOrganizationRepository extends BaseRepository<LOrganization>, JpaSpecificationExecutor<LOrganization> {
    @Query("SELECT new com.osp.bttp.dao.model.dto.response.db4.GetListBranchOrg" +
            "(o.orgId,o.orgName,o.address,o.phone,la.fullName,o.registrationLicenseNumber,o.registrationLicenseIssueDate) " +
            "from LOrganization o " +
            " join LLawyer la on o.lawyerLegalRepresentativeId = la.lawyerId \n" +
            "WHERE o.parentOrgId = ?1 ")
    List<GetListBranchOrg> getListBranchOrg(Long orgParent); //4 - GPTL", " 5 - ĐKHĐ"

    List<LOrganization> findAllByOrgIdAndParentOrgId(Long idBranch, Long idOrg);

    List<LOrganization> findAllByLawyerLegalRepresentativeId(Long idLaw);

}
