package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.OrgNotaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryPenalizeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface OrgNotaryInfoRepository extends JpaRepository<OrgNotaryInfo, Long> {
    Optional<OrgNotaryInfo> findByIdAndActive(Long id, Long active);

    List<OrgNotaryInfo> findByNameIgnoreCaseAndAdministrationId(String name,Long administrationId);

    Page<OrgNotaryInfo> findAllByAdministrationIdAndNameContainingIgnoreCase(Pageable pageable, Long admId, String name);

    Page<OrgNotaryInfo> findAllByAdministrationId(Pageable pageable, Long admId);

    Page<OrgNotaryInfo> findAllByNameContainingIgnoreCase(Pageable pageable, String name);

    List<OrgNotaryInfo> findAllByNotaryIdOfficeChief(Long idChief);


}
