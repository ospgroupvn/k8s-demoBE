package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.db3.ProvinceDto;
import com.osp.bttp.dao.model.entity.db3.Province;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class ProvinceMapper extends AbstractMapper<Province, ProvinceDto> {
    @Override
    public Class<ProvinceDto> getDtoClass() {
        return ProvinceDto.class;
    }
}
