package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {

    MALE("Nam"),

    FEMALE("Nữ");

    private final String text;
}
