package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.model.entity.db3.QAuctionOrganization;
import com.osp.bttp.dao.repository.db3.predicate.AuctionOrganizationPredicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuctionOrganizationCustomRepositoryImpl implements AuctionOrganizationCustomRepository {

    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Override
    public List<AuctionOrganizationBasicInfo> getAll(AuctionOrganizationPredicate predicate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAuctionOrganization qAuctionOrganization = QAuctionOrganization.auctionOrganization;
        return queryFactory
                .select(Projections.constructor(
                        AuctionOrganizationBasicInfo.class,
                        qAuctionOrganization.uuid,
                        qAuctionOrganization.fullName
                ))
                .from(qAuctionOrganization)
                .where(predicate.getCriteria())
                .fetch();
    }
}
