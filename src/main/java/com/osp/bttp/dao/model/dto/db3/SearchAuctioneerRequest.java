package com.osp.bttp.dao.model.dto.db3;

import com.osp.bttp.dao.model.type.AuCardStatus;
import com.osp.bttp.dao.model.type.AuCertStatus;
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
public class SearchAuctioneerRequest {

    private String orgId;

    private String auctionInfo;

    private String orgInfo;

    private String departmentCode;

    private AuCardStatus cardStatus;

    private AuCertStatus certStatus;

    private String auctioneerOrOrg;

    private String provinceCode;
}
