package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.OrganizationStatus;
import com.osp.bttp.dao.model.type.OrganizationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuctionOrganizationSearchReq {

    private String text;

    private String organizationName;

    private String departmentCode;

    private String provinceCode;

    List<OrganizationType> types;

    private OrganizationStatus status;
}
