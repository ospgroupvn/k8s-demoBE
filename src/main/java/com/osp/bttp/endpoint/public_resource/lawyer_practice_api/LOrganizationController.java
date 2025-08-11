package com.osp.bttp.endpoint.public_resource.lawyer_practice_api;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationRequest;
import com.osp.bttp.dao.model.dto.response.db4.DetailLOrganizationResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListLOrganizationResponse;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.service.lawyer.LOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:10 CH
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/lawyer-organizations")
public class LOrganizationController {
    @Autowired
    private LOrganizationService organizationService;

    // M1: Danh sách tổ chức trong nước
    @PostMapping("/getPage")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationResponse>>> getDomesticOrganizations(@RequestBody @Valid GetListLOrganizationRequest request) {
        return organizationService.getOrganizations(request);
    }

    // M3: Danh sách tổ chức nước ngoài
//    @PostMapping("/foreign")
//    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationResponse>>> getForeignOrganizations(@RequestBody @Valid GetListLOrganizationRequest request) {
//        return organizationService.getOrganizations(request);
//    }

    // M2, M4: Chi tiết tổ chức (trong nước hoặc nước ngoài)
    @GetMapping("/{orgId}")
    public ResponseEntity<ApiResponseV1<DetailLOrganizationResponse>> getOrganizationById(@PathVariable Long orgId) {
        return organizationService.getOrganizationById(orgId);
    }

    @Operation(summary = "Dashboard Lawyers ", description = "Dashboard Lawyers ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/lawyer-dashboard")
    public ResponseEntity<ApiResponseV1<?>> lawyerDashboard(
            @Parameter(description = "cityCode ( 2 chữ số hoặc 5 số )") @RequestParam(required = false) Long cityCode,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate
    ) {
        try {
            return organizationService.reportDashboard(cityCode, fromDate, toDate);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
