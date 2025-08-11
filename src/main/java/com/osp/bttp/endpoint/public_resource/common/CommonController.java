package com.osp.bttp.endpoint.public_resource.common;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerAssoc;
import com.osp.bttp.dao.model.dto.response.db4.GetListOrg;
import com.osp.bttp.dao.model.entity.db3.Category;
import com.osp.bttp.dao.model.entity.db4.LNationality;
import com.osp.bttp.dao.service.common.CommonService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 4:20 CH
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/public/common")
@Slf4j
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/getListProvince")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseV1<List>> getListProvince() {
        List<Category> list = new ArrayList<>();
        try {
            list = commonService.getAllProvince();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Success", list), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Have error in ChoiceOrgNoticeController:" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Internal Server Error", list), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getListWard/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponseV1<List>> getListWard(@PathVariable("id") Long id) {
            List<Category> list = new ArrayList<>();
            list = commonService.getAllWardByProvinceId(id);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Success", list), HttpStatus.OK);
    }

    @GetMapping("/assoc")
    public ResponseEntity<ApiResponseV1<List<GetListLawyerAssoc>>> getAllLawyerAssoc() {
        return commonService.getAllLawyerAssoc();
    }

    @GetMapping("/lorg")
    public ResponseEntity<ApiResponseV1<List<GetListOrg>>> getAllLOrg() {
        return commonService.getAllOrg();
    }

    @GetMapping("/nation")
    public ResponseEntity<ApiResponseV1<List<LNationality>>> getAllLNational() {
        return commonService.getAllNational();
    }
}
