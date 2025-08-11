package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCertDto;
import com.osp.bttp.dao.model.entity.db3.QAuctionCertificateInfo;
import com.osp.bttp.dao.model.entity.db3.QAuctioneer;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AuctionCertInfoCustomRepositoryImpl implements AuctionCertInfoCustomRepository {

    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Override
    public List<AuctioneerWithCertDto> getAllCert() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAuctionCertificateInfo qCert = QAuctionCertificateInfo.auctionCertificateInfo;
        QAuctionCertificateInfo qCertSub = new QAuctionCertificateInfo("qCertSub");

        // Subquery: lấy effectiveDate lớn nhất từng auctioneer
        JPQLQuery<LocalDate> maxEffectiveDateSubquery = JPAExpressions
                .select(qCertSub.effectiveDate.max())
                .from(qCertSub)
                .where(qCertSub.auctioneer.eq(qCert.auctioneer)
                        .and(qCertSub.effectiveDate.loe(LocalDate.now()))); // so sánh với qCert.auctioneer trong query chính

        // Query chính: Lấy auctioneerId, certCode với certificate có effectiveDate lớn nhất
        return queryFactory
                .select(Projections.constructor(
                        AuctioneerWithCertDto.class,
                        qCert.auctioneer.uuid,
                        qCert.certCode
                ))
                .from(qCert)
                .where(qCert.effectiveDate.eq(maxEffectiveDateSubquery))
                .fetch();
    }
}
