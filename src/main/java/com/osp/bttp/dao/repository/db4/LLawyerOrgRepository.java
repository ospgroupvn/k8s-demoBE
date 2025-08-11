package com.osp.bttp.dao.repository.db4;
import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerOrg;
import com.osp.bttp.dao.model.entity.db4.LLawyerOrg;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LLawyerOrgRepository extends BaseRepository<LLawyerOrg>, JpaSpecificationExecutor<LLawyerOrg> {
    @Query( "SELECT new com.osp.bttp.dao.model.dto.response.db4.GetListLawyerOrg(lo.lawyerOrgId ,l.lawyerId,lo.orgId ,o.orgName) FROM LLawyer l JOIN LLawyerOrg lo ON lo.lawyerId = l.lawyerId" +
            " JOIN  LOrganization o ON lo.orgId = o.orgId " +
            "where l.lawyerId = ?1")
    List<GetListLawyerOrg> findByIdLawyer( Long idLawyer);
}
