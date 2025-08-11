package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.entity.db3.LogSystem;
import com.osp.bttp.dao.model.mview.LogSystemView;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;

public interface LogSystemService {
    PaginationDto<LogSystemView> searchLogSystem(
            Integer page,
            Integer size,
            String groups,
            String actor,
            String input);

    LogSystem saveLog(
            String objectName,
            String objectId,
            ActionType actions,
            GroupType groups,
            ActorType actor);
}


