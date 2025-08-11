package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.NotaryPenalize;
import com.osp.bttp.dao.model.mview.bttp.NotaryPenalizeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotaryPenalizeRepository extends CrudRepository<NotaryPenalize, Long> {
    @Query("SELECT new com.osp.bttp.dao.model.mview.bttp.NotaryPenalizeResponse(" +
            " np.id, doc.dispatchCode, doc.note, doc.linkFile, doc.fileName, doc.active, " +
            " doc.signer ,doc.dateSign, doc.effectiveDate, " +
            " np.administrationIdPenalty, np.notaryInfoId, np.orgNotaryId, " +
            " np.typePenalize, np.reason, np.leverPenalize, np.additionalPenalty, np.moneyPenalty,doc.id) " +
            "FROM NotaryPenalize np " +
            "JOIN DmDocument doc ON np.documentId = doc.id " +
            "WHERE np.notaryInfoId = :notaryInfoId")
    List<NotaryPenalizeResponse> getPenaltiesByNotary(@Param("notaryInfoId") Long notaryInfoId);
    List<NotaryPenalize> findByNotaryInfoId(Long notaryInfoId);

}
