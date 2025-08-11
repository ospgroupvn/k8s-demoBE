package com.osp.bttp.dao.service.dgts.impl;

import com.osp.bttp.dao.model.dto.PlaceOfIssueDto;
import com.osp.bttp.dao.model.dto.db3.DeptOfJusticeDto;
import com.osp.bttp.dao.model.dto.db3.ProvinceDto;
import com.osp.bttp.dao.model.dto.db3.WardDto;
import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import com.osp.bttp.dao.model.entity.db3.PlaceOfIssue;
import com.osp.bttp.dao.model.entity.db3.Province;
import com.osp.bttp.dao.model.entity.db3.Ward;
import com.osp.bttp.dao.model.mapper.db3.DeptOfJusticeMapper;
import com.osp.bttp.dao.model.mapper.db3.PlaceOfIssueMapper;
import com.osp.bttp.dao.model.mapper.db3.ProvinceMapper;
import com.osp.bttp.dao.model.mapper.db3.WardMapper;
import com.osp.bttp.dao.repository.db3.DeptOfJusticeRepository;
import com.osp.bttp.dao.repository.db3.PlaceOfIssueRepository;
import com.osp.bttp.dao.repository.db3.ProvinceRepository;
import com.osp.bttp.dao.repository.db3.WardRepository;
import com.osp.bttp.dao.service.dgts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ProvinceRepository provinceRepository;

    private final WardRepository wardRepository;

    private final DeptOfJusticeRepository deptOfJusticeRepository;

    private final PlaceOfIssueRepository placeOfIssueRepository;

    private final ProvinceMapper provinceMapper;

    private final WardMapper wardMapper;

    private final DeptOfJusticeMapper deptOfJusticeMapper;

    private final PlaceOfIssueMapper placeOfIssueMapper;

    @Override
    public List<ProvinceDto> getProvinces() {
        List<Province> provinces = provinceRepository.findAll();
        return provinceMapper.toDtoList(provinces);
    }

    @Override
    public List<WardDto> getWardByProvinceCode(String provinceCode) {
        List<Ward> wards = wardRepository.findByProvinceCode(provinceCode);
        return wardMapper.toDtoList(wards);
    }

    @Override
    public List<DeptOfJusticeDto> getDepartments() {
        List<DeptOfJustice> departments = deptOfJusticeRepository.findAll();
        return deptOfJusticeMapper.toDtoList(departments);
    }

    @Override
    public List<PlaceOfIssueDto> getPlaceOfIssue() {
        List<PlaceOfIssue> placesOfIssue = placeOfIssueRepository.findAll();
        return placeOfIssueMapper.toDtoList(placesOfIssue);
    }
}
