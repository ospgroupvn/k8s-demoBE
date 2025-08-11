package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.ProbationaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryProbationaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProbationaryInfoRepository extends JpaRepository<ProbationaryInfo, Long> {

    @Query("SELECT new com.osp.bttp.dao.model.mview.bttp.NotaryProbationaryResponse(" +
            "org.id, org.name, org.address, " +
            "dm.id, dm.dispatchCode,dm.signer, dm.dateSign, dm.fileName, dm.linkFile, " +
            "pro.id, pro.dateStart, pro.dateEnd, " +
            "pro.status, pro.active, pro.dateNumber, pro.typeCertificate, pro.note) " +
            "FROM ProbationaryInfo pro " +
            "JOIN OrgNotaryInfo org ON pro.orgNotaryInfoId = org.id " +
            "JOIN DmDocument dm ON pro.documentCertificateId = dm.id " +
            "WHERE org.active = 0 AND pro.active = 0 AND dm.active = 0 " +
            "AND pro.notaryInfoId = :idNotary")
    List<NotaryProbationaryResponse> getNotaryProbationaryResponse(@Param("idNotary") Long idNotary);

    List<ProbationaryInfo> findByOrgNotaryInfoId(Long orgNotaryInfoId);

    List<ProbationaryInfo> findAllByNotaryInfoId(Long notaryInfoId);



}
