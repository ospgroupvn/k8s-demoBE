package com.osp.bttp.dao.repository.db3.predicate;

import com.osp.bttp.dao.model.entity.db3.QAuctionOrganization;
import com.osp.bttp.dao.model.type.OrganizationStatus;
import org.springframework.util.StringUtils;

import java.util.List;

public class AuctionOrganizationPredicate extends BasePredicate {

    private final static QAuctionOrganization qAuctionOrganization = QAuctionOrganization.auctionOrganization;

    public AuctionOrganizationPredicate withTypes(List<Integer> typeCodes) {
        if (typeCodes != null && !typeCodes.isEmpty()) {
            criteria.and(qAuctionOrganization.orgType.in(typeCodes));
        }
        return this;
    }

    public AuctionOrganizationPredicate withStatus(OrganizationStatus status) {
        if (status != null) {
            criteria.and(qAuctionOrganization.status.eq(status));
        }
        return this;
    }

    public AuctionOrganizationPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qAuctionOrganization.uuid.eq(id));
        }
        return this;
    }

    public AuctionOrganizationPredicate withProvinceCode(String provinceCode) {
        if (StringUtils.hasText(provinceCode)) {
            criteria.and(qAuctionOrganization.provinceCode.eq(provinceCode));
        }
        return this;
    }

    public AuctionOrganizationPredicate withText(String text) {
        if (StringUtils.hasText(text)) {
            criteria.andAnyOf(
                    qAuctionOrganization.fullName.containsIgnoreCase(text)
            );
        }
        return this;
    }

    public AuctionOrganizationPredicate withOrgName(String orgName) {
        if (StringUtils.hasText(orgName)) {
            criteria.and(qAuctionOrganization.fullName.containsIgnoreCase(orgName));
        }
        return this;
    }
}
