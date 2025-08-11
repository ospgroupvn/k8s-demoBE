package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.db3.AuctioneerDto;
import com.osp.bttp.dao.model.entity.db3.Auctioneer;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class AuctioneerMapper extends AbstractMapper<Auctioneer, AuctioneerDto> {
    @Override
    public Class<AuctioneerDto> getDtoClass() {
        return AuctioneerDto.class;
    }
}