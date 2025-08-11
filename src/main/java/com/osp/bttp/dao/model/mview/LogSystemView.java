package com.osp.bttp.dao.model.mview;

import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LogSystemView {
    private long id;
    private String objectName;

    private String objectId;

    private String ip;

    private String actions;

    private String group;

    private String actor;

    private String createdBy;

    private String createByStr;

    private Date genDate;

    public void setGroup(String group) {
        this.group = Objects.requireNonNull(GroupType.convert(group)).getDescription();
    }

    public void setActions(String actions) {
        this.actions = Objects.requireNonNull(ActionType.convert(actions)).getDescription();
    }

    public void setActor(String actor) {
        this.actor = Objects.requireNonNull(ActorType.convert(actor)).getDescription();
    }
}
