package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.AuctionCardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

public interface AuctionCardInfoRepository extends JpaRepository<AuctionCardInfo, String>, ListQuerydslPredicateExecutor<AuctionCardInfo>, AuctionCardInfoCustomRepository {
}