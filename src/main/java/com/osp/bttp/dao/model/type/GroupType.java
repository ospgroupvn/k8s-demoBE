package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupType {
    LAWYER("Luật sư"),
    NOTARY("Công chứng"),
    Auction("Đấu giá");

    private final String description;

    public static GroupType convert(String value) {
        for (GroupType groupType : GroupType.values()) {
            if (groupType.name().equals(value)) {
                return groupType;
            }
        }
        return null;
    }
}
