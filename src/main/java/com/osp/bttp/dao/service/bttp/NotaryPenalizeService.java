package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.dao.model.dto.db3.NotaryPenalizeCreateDto;
import com.osp.bttp.dao.model.entity.db3.NotaryPenalize;
import com.osp.bttp.dao.model.mview.bttp.NotaryPenalizeResponse;

import java.util.List;

public interface NotaryPenalizeService {
    NotaryPenalize add(NotaryPenalizeCreateDto notaryPenalizeCreateDto);
    List<NotaryPenalizeResponse> getPenaltiesByNotary(Long notaryId);
    NotaryPenalize edit(Long id, NotaryPenalizeCreateDto dto);
    void deleteByNotaryId(Long notaryId);
    void delete(Long id);
}
