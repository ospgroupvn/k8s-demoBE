package com.osp.bttp.dao.repository.db3.predicate;

import com.osp.bttp.dao.model.entity.db3.QAuctionCardInfo;
import com.osp.bttp.dao.model.entity.db3.QAuctionCertificateInfo;
import com.osp.bttp.dao.model.entity.db3.QAuctioneer;
import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class AuctioneerPredicate extends BasePredicate {

    private static final QAuctioneer qAuctioneer = QAuctioneer.auctioneer;

    public AuctioneerPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qAuctioneer.uuid.eq(id));
        }
        return this;
    }

    public AuctioneerPredicate withIdCode(String idCode) {
        if (StringUtils.hasText(idCode)) {
            criteria.and(qAuctioneer.idCode.eq(idCode));
        }
        return this;
    }

    public AuctioneerPredicate withOrgId(String orgId) {
        if (StringUtils.hasText(orgId)) {
            criteria.and(qAuctioneer.auctionCardInfos.any().organization.uuid.eq(orgId));
        }
        return this;
    }

    public AuctioneerPredicate withCertCode(String certCode) {
        if (StringUtils.hasText(certCode)) {
            criteria.and(qAuctioneer.auctionCertificateInfos.any().certCode.eq(certCode));
        }
        return this;
    }

    public AuctioneerPredicate withText(String text) {
        if (StringUtils.hasText(text)) {
            criteria.andAnyOf(
                    qAuctioneer.idCode.containsIgnoreCase(text),
                    qAuctioneer.fullName.containsIgnoreCase(text),
                    qAuctioneer.auctionCardInfos.any().cardCode.containsIgnoreCase(text),
                    qAuctioneer.auctionCertificateInfos.any().certCode.containsIgnoreCase(text)
            );
        }
        return this;
    }

    public AuctioneerPredicate withDepartmentCode(String departmentCode) {
        if (StringUtils.hasText(departmentCode)) {
            criteria.and(qAuctioneer.auctionCardInfos.any().departmentCode.eq(departmentCode));
        }
        return this;
    }

    public AuctioneerPredicate withCardStatus(AuCardStatus cardStatus) {
        if (cardStatus != null) {
            criteria.and(qAuctioneer.auctionCardInfos.any().status.eq(cardStatus));
        }
        return this;
    }

    public AuctioneerPredicate withCardLatestHasEffective(AuCardStatus status) {
        if (status != null) {
            QAuctionCardInfo qAuctionCardInfo = QAuctionCardInfo.auctionCardInfo;
            QAuctionCardInfo subCard = new QAuctionCardInfo("subCard");

            JPQLQuery<LocalDate> subMaxEffDate = JPAExpressions
                    .select(subCard.effectiveDate.max())
                    .from(subCard)
                    .where(
                            subCard.auctioneer.eq(qAuctioneer),
                            subCard.effectiveDate.loe(LocalDate.now())
                    );
            criteria.and(JPAExpressions
                    .selectOne().from(qAuctionCardInfo)
                    .where(
                            qAuctionCardInfo.auctioneer.eq(qAuctioneer),
                            qAuctionCardInfo.effectiveDate.eq(subMaxEffDate),
                            qAuctionCardInfo.status.eq(status)
                    ).exists());
        }
        return this;
    }

    public AuctioneerPredicate withCertStatus(AuCertStatus certStatus) {
        if (certStatus != null) {
            criteria.and(qAuctioneer.auctionCertificateInfos.any().status.eq(certStatus));
        }
        return this;
    }

    public AuctioneerPredicate withCertLatestHasEffective(AuCertStatus status) {
        if (status != null) {
            QAuctionCertificateInfo qAuctionCertInfo = QAuctionCertificateInfo.auctionCertificateInfo;
            QAuctionCertificateInfo subCert = new QAuctionCertificateInfo("subCert");

            JPQLQuery<LocalDate> subMaxEffDate = JPAExpressions
                    .select(subCert.effectiveDate.max())
                    .from(subCert)
                    .where(
                            subCert.auctioneer.eq(qAuctioneer),
                            subCert.effectiveDate.loe(LocalDate.now())
                    );
            criteria.and(JPAExpressions
                    .selectOne().from(qAuctionCertInfo)
                    .where(
                            qAuctionCertInfo.auctioneer.eq(qAuctioneer),
                            qAuctionCertInfo.effectiveDate.eq(subMaxEffDate),
                            qAuctionCertInfo.status.eq(status)
                    ).exists());
        }
        return this;
    }

    public AuctioneerPredicate withAuctioneerOrOrgName(String auctioneerOrOrgName) {
        if (StringUtils.hasText(auctioneerOrOrgName)) {

            QAuctionCardInfo qAuctionCardInfo = QAuctionCardInfo.auctionCardInfo;
            QAuctionCardInfo subCard = new QAuctionCardInfo("subCard");

            JPQLQuery<LocalDate> subMaxEffDate = JPAExpressions
                    .select(subCard.effectiveDate.max())
                    .from(subCard)
                    .where(
                            subCard.auctioneer.eq(qAuctioneer),
                            subCard.effectiveDate.loe(LocalDate.now())
                    );
            criteria.andAnyOf(
                    qAuctioneer.fullName.containsIgnoreCase(auctioneerOrOrgName)
//                    JPAExpressions
//                            .selectOne().from(qAuctionCardInfo)
//                            .where(
//                                    qAuctionCardInfo.auctioneer.eq(qAuctioneer),
//                                    qAuctionCardInfo.effectiveDate.eq(subMaxEffDate),
//                                    qAuctionCardInfo.organization.fullName.containsIgnoreCase(auctioneerOrOrgName)
//                            ).exists()
            );
        }
        return this;
    }

    public AuctioneerPredicate withProvinceCode(String provinceCode) {
        if (StringUtils.hasText(provinceCode)) {
            criteria.and(qAuctioneer.provinceCode.eq(provinceCode));
        }
        return this;
    }
}
