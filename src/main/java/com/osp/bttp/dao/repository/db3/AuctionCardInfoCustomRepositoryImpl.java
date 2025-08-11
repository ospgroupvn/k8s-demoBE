package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCardLatestHasEffectiveDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCardLatestHasEffectiveSearchReq;
import com.osp.bttp.dao.model.entity.db3.QAuctionCardInfo;
import com.osp.bttp.dao.model.entity.db3.QAuctioneer;
import com.osp.bttp.dao.model.type.AuCardStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuctionCardInfoCustomRepositoryImpl implements AuctionCardInfoCustomRepository {

    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Override
    public List<AuctioneerWithCardLatestHasEffectiveDto> getAllAuctioneerWithCardLatestHasEffective(AuctioneerWithCardLatestHasEffectiveSearchReq request) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QAuctioneer qAuctioneer = QAuctioneer.auctioneer;
        QAuctionCardInfo qAuctionCardInfo = QAuctionCardInfo.auctionCardInfo;

        BooleanBuilder criteria = new BooleanBuilder();
        criteria.and(qAuctionCardInfo.effectiveDate.isNotNull());
        if (StringUtils.hasText(request.getOrganizationId())) {
            criteria.and(qAuctionCardInfo.organization.uuid.eq(request.getOrganizationId()));
        }

        // Subquery: Lấy effectiveDate max cho mỗi auctioneer
        var subMaxEffDate = JPAExpressions
                .select(qAuctionCardInfo.effectiveDate.max())
                .from(qAuctionCardInfo)
                .where(qAuctionCardInfo.auctioneer.uuid.eq(qAuctioneer.uuid)
                        .and(qAuctionCardInfo.effectiveDate.isNotNull())
                        .and(qAuctionCardInfo.effectiveDate.loe(LocalDate.now())));

        // Main query: Join và filter
        return queryFactory
                .select(Projections.constructor(
                        AuctioneerWithCardLatestHasEffectiveDto.class,
                        qAuctioneer.uuid,
                        qAuctioneer.fullName,
                        qAuctioneer.dob,
                        qAuctionCardInfo.cardCode,
                        qAuctionCardInfo.dateOfDecision,
                        qAuctionCardInfo.status
                ))
                .from(qAuctioneer)
                .join(qAuctioneer.auctionCardInfos, qAuctionCardInfo)
                .on(qAuctionCardInfo.effectiveDate.eq(subMaxEffDate))
                .where(criteria)
                .fetch();
    }

    @Override
    public Map<String, Long> countCardsByOrganization(List<String> organizationIds) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QAuctioneer qAuctioneer = QAuctioneer.auctioneer;
        QAuctionCardInfo qAuctionCardInfo = QAuctionCardInfo.auctionCardInfo;

        BooleanBuilder criteria = new BooleanBuilder();
        criteria.and(qAuctionCardInfo.effectiveDate.isNotNull());
        criteria.and(qAuctionCardInfo.status.in(AuCardStatus.NEW_CARD, AuCardStatus.RENEW_CARD));

        if (organizationIds != null && !organizationIds.isEmpty()) {
            criteria.and(qAuctionCardInfo.organization.uuid.in(organizationIds));
        }

        var subMaxEffDate = JPAExpressions
                .select(qAuctionCardInfo.effectiveDate.max())
                .from(qAuctionCardInfo)
                .where(qAuctionCardInfo.auctioneer.uuid.eq(qAuctioneer.uuid)
                        .and(qAuctionCardInfo.effectiveDate.isNotNull())
                        .and(qAuctionCardInfo.effectiveDate.loe(LocalDate.now())));

        List<Tuple> tuples = queryFactory
                .select(qAuctionCardInfo.organization.uuid, qAuctionCardInfo.count())
                .from(qAuctioneer)
                .join(qAuctioneer.auctionCardInfos, qAuctionCardInfo)
                .on(qAuctionCardInfo.effectiveDate.eq(subMaxEffDate))
                .where(criteria)
                .groupBy(qAuctionCardInfo.organization.uuid)
                .fetch();

        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(qAuctionCardInfo.organization.uuid),
                        tuple -> {
                            Long count = tuple.get(qAuctionCardInfo.count());
                            return count != null ? count : 0L;
                        }
                ));
    }
}
