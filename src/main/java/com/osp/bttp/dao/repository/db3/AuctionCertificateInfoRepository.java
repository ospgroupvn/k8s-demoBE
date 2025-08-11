package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.AuctionCertificateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

import java.util.List;

public interface AuctionCertificateInfoRepository extends JpaRepository<AuctionCertificateInfo, String>, ListQuerydslPredicateExecutor<AuctionCertificateInfo>, AuctionCertInfoCustomRepository {

    List<AuctionCertificateInfo> findAllByCertCode(String certCode);
}