package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionOrganizationBasicInfo {

    private String uuid;

    private String departmentCode;

    private String departmentName;

    private String fullName;

    private String managerUuid;

    private String managerName;

    private String provinceCode;

    private String provinceName;

    private String wardCode;

    private String wardName;

    private String address;

    private OrganizationStatus status;

    private Long auctioneerCount;

    public AuctionOrganizationBasicInfo(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }
}
