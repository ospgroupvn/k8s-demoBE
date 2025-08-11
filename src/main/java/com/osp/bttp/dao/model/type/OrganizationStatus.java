package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrganizationStatus {

    ACTIVE("Đang hoạt động"),
    INACTIVE("Ngừng hoạt động"),
    TEMPORARILY_SUSPENDED("Tạm ngừng hoạt động");

    private final String label;
}
