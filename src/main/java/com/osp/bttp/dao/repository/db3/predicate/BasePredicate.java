package com.osp.bttp.dao.repository.db3.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public abstract class BasePredicate {

    protected final BooleanBuilder criteria = new BooleanBuilder();

    public static final String DEFAULT_ORDER_PROPERTY = "createdDate:desc";

    /**
     * Get the combination of query criteria
     *
     * @return
     */
    public Predicate getCriteria() {
        return criteria;
    }

}
