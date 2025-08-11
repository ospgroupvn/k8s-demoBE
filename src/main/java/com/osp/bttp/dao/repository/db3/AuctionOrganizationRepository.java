package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.AuctionOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

public interface AuctionOrganizationRepository extends JpaRepository<AuctionOrganization, String>, ListQuerydslPredicateExecutor<AuctionOrganization>, AuctionOrganizationCustomRepository {
}
