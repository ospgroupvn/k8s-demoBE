package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.dao.model.dto.PlaceOfIssueDto;
import com.osp.bttp.dao.model.dto.db3.DeptOfJusticeDto;
import com.osp.bttp.dao.model.dto.db3.ProvinceDto;
import com.osp.bttp.dao.model.dto.db3.WardDto;
import com.osp.bttp.dao.service.dgts.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController implements CategoryResource {

    private final CategoryService categoryService;

    @Override
    public ResponseEntity<List<ProvinceDto>> getProvinces() {
        return ResponseEntity.ok(categoryService.getProvinces());
    }

    @Override
    public ResponseEntity<List<WardDto>> getWardByProvinceCode(String provinceCode) {
        return ResponseEntity.ok(categoryService.getWardByProvinceCode(provinceCode));
    }

    @Override
    public ResponseEntity<List<DeptOfJusticeDto>> getDepartments() {
        return ResponseEntity.ok(categoryService.getDepartments());
    }

    @Override
    public ResponseEntity<List<PlaceOfIssueDto>> getPlaceOfIssue() {
        return ResponseEntity.ok(categoryService.getPlaceOfIssue());
    }
}
