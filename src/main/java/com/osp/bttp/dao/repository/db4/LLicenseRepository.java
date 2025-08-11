package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.dto.response.db4.GetListRegisOfOrgChange;
import com.osp.bttp.dao.model.entity.db4.LLicense;
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
public interface LLicenseRepository extends BaseRepository<LLicense>, JpaSpecificationExecutor<LLicense> {
    List<LLicense> findByOwnerIdAndOwnerType(Long ownerId, Integer ownerType);

    List<LLicense> findByOwnerIdAndOwnerTypeAndLicenseType(Long ownerId, Integer typeOwner, Integer licenseType);

    List<LLicense> findByOwnerIdAndOwnerTypeAndLicenseTypeAndStatus(Long ownerId, Integer ownerType, Integer licenseType, Integer status);

    List<LLicense> findByLicenseNumber(String numberLic);

    @Query("SELECT new com.osp.bttp.dao.model.dto.response.db4.GetListRegisOfOrgChange" +
            "(l.licenseId,l.approvalDocument,l.typeChange,l.issueDate,l.changeContent) " +
            "from LLicense l \n" +
            "join LOrganization o on o.orgId = l.ownerId \n" +
            "WHERE o.orgId = ?1 \n" +
            "and l.ownerType=2 \n" +
            "and l.licenseType= ?2")
    List<GetListRegisOfOrgChange> getListRegisOfOrgChange(Long orgId,Integer licenseType);

    void deleteAllByOwnerIdAndOwnerType(Long orgId , Integer typeOwner);
}
