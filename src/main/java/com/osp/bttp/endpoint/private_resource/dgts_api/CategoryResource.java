package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.dao.model.dto.PlaceOfIssueDto;
import com.osp.bttp.dao.model.dto.db3.DeptOfJusticeDto;
import com.osp.bttp.dao.model.dto.db3.ProvinceDto;
import com.osp.bttp.dao.model.dto.db3.WardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(CategoryResource.CATEGORY_RESOURCE)
public interface CategoryResource {

    String CATEGORY_RESOURCE = Constants.API_VERSION1 + "/category";

    @GetMapping("/province")
    ResponseEntity<List<ProvinceDto>> getProvinces();

    @GetMapping("/ward")
    ResponseEntity<List<WardDto>> getWardByProvinceCode(@RequestParam(name = "province-code") String provinceCode);

    @GetMapping("/department")
    ResponseEntity<List<DeptOfJusticeDto>> getDepartments();

    @GetMapping("/place-of-issue")
    ResponseEntity<List<PlaceOfIssueDto>> getPlaceOfIssue();

}
