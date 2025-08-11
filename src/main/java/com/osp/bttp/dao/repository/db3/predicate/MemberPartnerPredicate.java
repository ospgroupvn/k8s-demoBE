package com.osp.bttp.dao.repository.db3.predicate;

import com.osp.bttp.dao.model.entity.db3.QMemberPartner;
import org.springframework.util.StringUtils;

import java.util.List;

public class MemberPartnerPredicate extends BasePredicate {

    private static final QMemberPartner qMemberPartner = QMemberPartner.memberPartner;

    public MemberPartnerPredicate withIds(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            criteria.and(qMemberPartner.uuid.in(ids));
        }
        return this;
    }

    public MemberPartnerPredicate withOrgId(String orgId) {
        if (StringUtils.hasText(orgId)) {
            criteria.and(qMemberPartner.organization.uuid.eq(orgId));
        }
        return this;
    }

    public MemberPartnerPredicate withId(String id) {
        if (StringUtils.hasText(id)) {
            criteria.and(qMemberPartner.uuid.eq(id));
        }
        return this;
    }
}
