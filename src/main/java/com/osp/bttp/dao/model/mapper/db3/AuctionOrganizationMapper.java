package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.db3.AuctionOrganizationDto;
import com.osp.bttp.dao.model.entity.db3.AuctionOrganization;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import com.osp.bttp.dao.model.type.OrganizationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionOrganizationMapper extends AbstractMapper<AuctionOrganization, AuctionOrganizationDto> {

    @Override
    public Class<AuctionOrganizationDto> getDtoClass() {
        return AuctionOrganizationDto.class;
    }

    @Override
    public AuctionOrganizationDto toDto(AuctionOrganization organization) {
        AuctionOrganizationDto ret = super.toDto(organization);

        OrganizationType type = OrganizationType.fromCode(organization.getOrgType());
        ret.setType(type);
        return ret;
    }
}
