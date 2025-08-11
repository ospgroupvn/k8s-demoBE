package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.db3.DeptOfJusticeDto;
import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class DeptOfJusticeMapper extends AbstractMapper<DeptOfJustice, DeptOfJusticeDto> {
    @Override
    public Class<DeptOfJusticeDto> getDtoClass() {
        return DeptOfJusticeDto.class;
    }
}
