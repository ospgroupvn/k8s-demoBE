package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.NotaryRegPracticeCreateDto;
import com.osp.bttp.dao.model.entity.db3.NotaryRegPractice;
import com.osp.bttp.dao.model.mview.bttp.NotaryChiefResponse;
import com.osp.bttp.dao.model.mview.bttp.NotaryRegAndAuctionCardResponse;
import com.osp.bttp.dao.model.mview.bttp.OrgCategoryInfo;

import java.util.List;

public interface NotaryRegPracticeService {
    NotaryRegPractice add(NotaryRegPracticeCreateDto notaryRegPracticeCreateDto);
    List<NotaryRegAndAuctionCardResponse> getNotaryRegAndAuctionCard(Long id);
    NotaryRegPractice edit(Long id, NotaryRegPracticeCreateDto dto);
    void delete(Long id);
    void deleteByNotaryId(Long notaryId);
}
