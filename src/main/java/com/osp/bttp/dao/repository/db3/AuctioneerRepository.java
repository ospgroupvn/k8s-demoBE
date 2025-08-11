package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.Auctioneer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

import java.util.List;

public interface AuctioneerRepository extends JpaRepository<Auctioneer, String>, ListQuerydslPredicateExecutor<Auctioneer> {

    List<Auctioneer> findAllByIdCode(String idCode);
}