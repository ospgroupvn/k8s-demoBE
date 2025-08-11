package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuCardStatus {

    NEW_CARD("Cấp mới thẻ ĐGV"),

    RENEW_CARD("Cấp lại thẻ ĐGV"),

    REVOKE_CARD("Thu hồi thẻ ĐGV"),

    TERMINATED_FROM_ORGANIZATION("Đã thôi làm việc tại Tổ chức");

    private final String text;
}
