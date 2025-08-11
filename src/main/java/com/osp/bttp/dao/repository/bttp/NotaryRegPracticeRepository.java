package com.osp.bttp.dao.repository.bttp;

import com.osp.bttp.dao.model.entity.db3.NotaryRegPractice;
import com.osp.bttp.dao.model.mview.bttp.NotaryRegAndAuctionCardResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NotaryRegPracticeRepository extends JpaRepository<NotaryRegPractice, Long> {

    @Query("SELECT new com.osp.bttp.dao.model.mview.bttp.NotaryRegAndAuctionCardResponse( " +
            "nrp.id,dd.dispatchCode, nrp.reason, dd.linkFile, dd.fileName, dd.active, dd.signer, dd.dateSign, dd.effectiveDate, " +
            "nrp.orgNotaryInfoId, nrp.notaryInfoId, nrp.numberCad, nrp.status, nrp.notaryReq, nrp.dateReq, nrp.typeNotaryInfo ,dd.id) " +
            "FROM NotaryRegPractice nrp " +
            "JOIN DmDocument dd ON dd.id = nrp.documentId " +
            "WHERE nrp.notaryInfoId = :idNotary")
    List<NotaryRegAndAuctionCardResponse> getRegPracticeByNotary(@Param("idNotary") Long idNotary);


    Optional<NotaryRegPractice> findByNumberCad(String numberCad);

    List<NotaryRegPractice> findAllByNotaryInfoId(Long notaryInfoId);
}
