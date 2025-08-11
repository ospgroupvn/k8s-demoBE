package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationBasicInfo;
import com.osp.bttp.dao.repository.db3.predicate.AuctionOrganizationPredicate;

import java.util.List;

public interface AuctionOrganizationCustomRepository {

    List<AuctionOrganizationBasicInfo> getAll(AuctionOrganizationPredicate predicate);
}
