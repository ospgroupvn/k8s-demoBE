package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.entity.db4.LLicenseChange;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLicenseChangeRepository extends BaseRepository<LLicenseChange>, JpaSpecificationExecutor<LLicenseChange> {
    List<LLicenseChange> getAllByLicenseIdAndOwnerTypeAndLicenseType(Long idLic,Integer ownerType,Integer licType);

    List<LLicenseChange> getAllByLicenseId(Long idLicPresent);

    @Transactional
    void deleteAllByLicenseId(Long idLic);
}
