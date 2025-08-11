package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.MemberPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;

public interface MemberPartnerRepository extends JpaRepository<MemberPartner, String>, ListQuerydslPredicateExecutor<MemberPartner> {
}
