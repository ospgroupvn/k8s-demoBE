package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActorType {
    PER("Cá nhân"),
    ORG("Tổ chức");

    private final String description;

    public static ActorType convert(String value) {
        for (ActorType actorType : ActorType.values()) {
            if (actorType.name().equals(value)) {
                return actorType;
            }
        }
        return null;
    }

}
