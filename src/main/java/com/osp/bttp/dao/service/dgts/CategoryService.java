package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.dao.model.dto.PlaceOfIssueDto;
import com.osp.bttp.dao.model.dto.db3.DeptOfJusticeDto;
import com.osp.bttp.dao.model.dto.db3.ProvinceDto;
import com.osp.bttp.dao.model.dto.db3.WardDto;

import java.util.List;

public interface CategoryService {

    List<ProvinceDto> getProvinces();

    List<WardDto> getWardByProvinceCode(String provinceCode);

    List<DeptOfJusticeDto> getDepartments();

    List<PlaceOfIssueDto> getPlaceOfIssue();
}
