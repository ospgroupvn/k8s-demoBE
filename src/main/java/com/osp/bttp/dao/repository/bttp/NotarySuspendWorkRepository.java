package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.NotarySuspendWork;
import com.osp.bttp.dao.model.mview.bttp.NotarySuspendWorkResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotarySuspendWorkRepository extends CrudRepository<NotarySuspendWork, Long> {
    @Query("SELECT new com.osp.bttp.dao.model.mview.bttp.NotarySuspendWorkResponse( " +
            "nsw.id, dd.dispatchCode, dd.note, dd.linkFile, dd.fileName, dd.active,dd.signer, " +
            "dd.dateSign, dd.effectiveDate, " +
            "nsw.notaryInfoId, nsw.orgNotaryId, nsw.reason, nsw.typeSupend,nsw.dateNumber,dd.id) " +
            "FROM NotarySuspendWork nsw " +
            "JOIN DmDocument dd ON nsw.documentId = dd.id " +
            "WHERE nsw.notaryInfoId = :idNotary")
    List<NotarySuspendWorkResponse> getSuspendWorkByNotary(@Param("idNotary") Long idNotary);

    List<NotarySuspendWork> findByNotaryInfoId(Long idNotary);


}
