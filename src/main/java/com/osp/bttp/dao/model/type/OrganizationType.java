package com.osp.bttp.dao.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum OrganizationType {

    AUCTION_SERVICE_CENTER(0, "Trung tâm dịch vụ đấu giá tài sản"),
    PRIVATE_AUCTION_ENTERPRISE(1, "Doanh nghiệp đấu giá tư nhân"),
    PARTNERSHIP_AUCTION_COMPANY(2, "Công ty đấu giá hợp danh"),
    AUCTION_BRANCH(11, "Chi nhánh doanh nghiệp đấu giá tài sản"),
    VAMC(12, "VAMC");

    private final Integer code;
    private final String description;

    private static final Map<Integer, OrganizationType> OrganizationTypeMap = new HashMap<>();

    static {
        for (OrganizationType type : OrganizationType.values()) {
            OrganizationTypeMap.put(type.getCode(), type);
        }
    }

    public static OrganizationType fromCode(Integer code) {
        return OrganizationTypeMap.get(code);
    }
}
