package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogSystemCreateDto {
    private String objectName;

    private String objectId;

    private ActionType actionType;

    private GroupType groupType;

    private ActorType actorType;

}
