package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.db3.WardDto;
import com.osp.bttp.dao.model.entity.db3.Ward;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class WardMapper extends AbstractMapper<Ward, WardDto> {
    @Override
    public Class<WardDto> getDtoClass() {
        return WardDto.class;
    }
}
