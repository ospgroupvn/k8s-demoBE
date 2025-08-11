package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuCertStatus {

    NEW_CERT("Cấp mới CCHN đấu giá"),

    RENEW_CERT("Cấp lại CCHN đấu giá"),

    REVOKE_CERT("Thu hồi CCHN đấu giá");

    private final String text;
}
