package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.dao.model.dto.db3.NotarySuspendWorkCreateDto;
import com.osp.bttp.dao.model.entity.db3.NotarySuspendWork;
import com.osp.bttp.dao.model.mview.bttp.NotarySuspendWorkResponse;

import java.util.List;

public interface NotarySuspendWorkService {
    NotarySuspendWork add(NotarySuspendWorkCreateDto notarySuspendWorkCreateDto);
    List<NotarySuspendWorkResponse> getSuspendWorkByNotary(Long notaryId);
    NotarySuspendWork edit(Long id, NotarySuspendWorkCreateDto dto);
    void delete(Long id);
    void deleteByNotaryId(Long notaryId);
}
