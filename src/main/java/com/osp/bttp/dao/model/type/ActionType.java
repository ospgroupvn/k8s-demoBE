package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionType {
    ADD("Thêm"),
    EDIT("Sửa"),
    DELETE("Xóa");

    private final String description;

    public static ActionType convert(String value) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.name().equals(value)) {
                return actionType;
            }
        }
        return null;
    }
}
