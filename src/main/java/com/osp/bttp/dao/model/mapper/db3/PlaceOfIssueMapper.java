package com.osp.bttp.dao.model.mapper.db3;

import com.osp.bttp.dao.model.dto.PlaceOfIssueDto;
import com.osp.bttp.dao.model.entity.db3.PlaceOfIssue;
import com.osp.bttp.dao.model.mapper.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class PlaceOfIssueMapper extends AbstractMapper<PlaceOfIssue, PlaceOfIssueDto> {
    @Override
    public Class<PlaceOfIssueDto> getDtoClass() {
        return PlaceOfIssueDto.class;
    }
}
