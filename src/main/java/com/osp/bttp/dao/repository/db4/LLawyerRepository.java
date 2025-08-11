package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.dto.response.db4.GetLawyerLegalRep;
import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerInOrg;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LLawyerUpdateInfoHis;
import com.osp.bttp.dao.repository.BaseRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerRepository extends BaseRepository<LLawyer>, JpaSpecificationExecutor<LLawyer> {

    LLawyer getByLawyerIdAndActivityStatus(Long lawyerId,Integer activityStatus);

    List<LLawyer> getAllByCertificateNumberAndActivityStatus(String number,Integer status);

    List<LLawyer> getAllByRevokeDecisionNumberAndActivityStatus(String numberRevoke,Integer status);

    List<LLawyer> findAllByCertificateNumber(String cerNum);

    List<LLawyer> findAllByLawyerIdAndOrganizationId(Long idlaw,Long idOrg);

    @Query("SELECT new com.osp.bttp.dao.model.dto.response.db4.GetLawyerLegalRep" +
            "(l.lawyerId,l.fullName,l.dateOfBirth,l.certificateNumber,li.licenseNumber) " +
            "from LLawyer l \n" +
            "join  LLicense li on l.lawyerId = li.ownerId \n" +
            "AND li.licenseId = (" +
            "    SELECT MAX(li2.licenseId) " +
            "    FROM LLicense li2 " +
            "    WHERE li2.ownerId = l.lawyerId " +
            "    AND li2.ownerType = 1 " +
            "    AND li2.licenseType = 2" +
            ") " +
            "WHERE l.lawyerId = ?1 \n" +
            "and li.ownerType=1 \n" +
            "and li.licenseType= 2")
    GetLawyerLegalRep getLawyerLegalRep(Long orgId);

    @Query("SELECT new com.osp.bttp.dao.model.dto.response.db4.GetLawyerLegalRep" +
            "(l.lawyerId, l.fullName, l.dateOfBirth, l.certificateNumber, li.licenseNumber) " +
            "FROM LLawyer l " +
            " left JOIN LLicense li ON l.lawyerId = li.ownerId " +
            "AND li.licenseId = (" +
            "    SELECT MAX(li2.licenseId) " +
            "    FROM LLicense li2 " +
            "    WHERE li2.ownerId = l.lawyerId " +
            "    AND li2.ownerType = 1 " +
            "    AND li2.licenseType in (2,3)" +
            ") " +
            "WHERE l.organizationId is null and l.lawyerId  NOT IN (" +
            "    SELECT o.lawyerLegalRepresentativeId " +
            "    FROM LOrganization o " +
            "    JOIN LLawyer l2 ON o.lawyerLegalRepresentativeId = l2.lawyerId" +
            ")"
    )
    List<GetLawyerLegalRep> getLawyerLegalRepUnique();

    @Transactional
    @Modifying
    @Query("UPDATE LLawyer l SET l.organizationId = NULL WHERE l.organizationId  = ?1")
    void removeLaw(Long idOrg);


    List<LLawyer> findAllByIdentityCardNumber(String identityCardNumber);

    List<LLawyer> findAllByIdentityCardNumberAndLawyerIdNot( String identityCardNumber, Long lawyerId);
}
