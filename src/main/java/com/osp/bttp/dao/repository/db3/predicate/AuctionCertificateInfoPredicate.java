package com.osp.bttp.dao.repository.db3.predicate;

import com.osp.bttp.dao.model.entity.db3.QAuctionCertificateInfo;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

public class AuctionCertificateInfoPredicate extends BasePredicate {

    public static final String EFFECTIVE_DATE_ORDER_PROPERTY = "effectiveDate:desc";

    private final static QAuctionCertificateInfo qAuctionCertificateInfo = QAuctionCertificateInfo.auctionCertificateInfo;

    public AuctionCertificateInfoPredicate withId(String uuid) {
        if (StringUtils.hasText(uuid)) {
            criteria.and(qAuctionCertificateInfo.uuid.eq(uuid));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate withIds(List<String> uuids) {
        if (uuids != null && !uuids.isEmpty()) {
            criteria.and(qAuctionCertificateInfo.uuid.in(uuids));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate withNotInIds(List<String> uuids) {
        if (uuids != null && !uuids.isEmpty()) {
            criteria.and(qAuctionCertificateInfo.uuid.notIn(uuids));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate withCertCode(String certCode) {
        if (StringUtils.hasText(certCode)) {
            criteria.and(qAuctionCertificateInfo.certCode.eq(certCode));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate withAuctioneerId(String auctioneerId) {
        if (StringUtils.hasText(auctioneerId)) {
            criteria.and(qAuctionCertificateInfo.auctioneer.uuid.eq(auctioneerId));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate withAuctioneerIds(List<String> auctioneerIds) {
        if (auctioneerIds != null && !auctioneerIds.isEmpty()) {
            criteria.and(qAuctionCertificateInfo.auctioneer.uuid.in(auctioneerIds));
        }
        return this;
    }

    public AuctionCertificateInfoPredicate effectiveToDate(LocalDate fromDate) {
        if (fromDate != null) {
            criteria.and(qAuctionCertificateInfo.effectiveDate.loe(fromDate));
        }
        return this;
    }
}
