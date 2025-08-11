package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCardLatestHasEffectiveDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerWithCardLatestHasEffectiveSearchReq;

import java.util.List;
import java.util.Map;

public interface AuctionCardInfoCustomRepository {

    List<AuctioneerWithCardLatestHasEffectiveDto> getAllAuctioneerWithCardLatestHasEffective(
            AuctioneerWithCardLatestHasEffectiveSearchReq request
    );

    Map<String, Long> countCardsByOrganization(List<String> organizationIds);
}
