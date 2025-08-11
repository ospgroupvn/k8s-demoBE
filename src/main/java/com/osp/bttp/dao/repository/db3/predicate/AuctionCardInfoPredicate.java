package com.osp.bttp.dao.repository.db3.predicate;

import com.osp.bttp.dao.model.entity.db3.QAuctionCardInfo;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class AuctionCardInfoPredicate extends BasePredicate {

    public static final String EFFECTIVE_DATE_ORDER_PROPERTY = "effectiveDate:desc";

    private final static QAuctionCardInfo qAuctionCardInfo = QAuctionCardInfo.auctionCardInfo;

    public AuctionCardInfoPredicate withUuid(String uuid) {
        if (StringUtils.hasText(uuid)) {
            criteria.and(qAuctionCardInfo.uuid.eq(uuid));
        }
        return this;
    }

    public AuctionCardInfoPredicate withUuids(List<String> uuids) {
        if (uuids != null && !uuids.isEmpty()) {
            criteria.and(qAuctionCardInfo.uuid.in(uuids));
        }
        return this;
    }

    public AuctionCardInfoPredicate withAuctioneerId(String auctioneerId) {
        if (StringUtils.hasText(auctioneerId)) {
            criteria.and(qAuctionCardInfo.auctioneer.uuid.eq(auctioneerId));
        }
        return this;
    }

    public AuctionCardInfoPredicate withAuctioneerIds(List<String> auctioneerIds) {
        if (auctioneerIds != null && !auctioneerIds.isEmpty()) {
            criteria.and(qAuctionCardInfo.auctioneer.uuid.in(auctioneerIds));
        }
        return this;
    }

    public AuctionCardInfoPredicate withOrgId(String orgId) {
        if (StringUtils.hasText(orgId)) {
            criteria.and(qAuctionCardInfo.organization.uuid.eq(orgId));
        }
        return this;
    }

    public AuctionCardInfoPredicate withCardCode(String cardCode) {
        if (StringUtils.hasText(cardCode)) {
            criteria.and(qAuctionCardInfo.cardCode.eq(cardCode));
        }
        return this;
    }

    public AuctionCardInfoPredicate withNotInIds(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            criteria.and(qAuctionCardInfo.uuid.notIn(ids));
        }
        return this;
    }

    public AuctionCardInfoPredicate effectiveToDate(LocalDate fromDate) {
        if (fromDate != null) {
            criteria.and(qAuctionCardInfo.effectiveDate.loe(fromDate));
        }
        return this;
    }
}
