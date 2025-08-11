package com.osp.bttp.endpoint.public_resource.lawyer_practice_api;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerResponse;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerResponse;
import com.osp.bttp.dao.service.lawyer.LLawyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:11 CH
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/lawyers")
public class LLawyerController {
    @Autowired
    private LLawyerService lawyerService;
    // M5: Danh sách luật sư trong nước
    @PostMapping("/getPage")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerResponse>>> getDomesticLawyers(@RequestBody @Valid GetListLLawyerRequest request) {
        return lawyerService.getLawyers(request);
    }


    // M6, M8: Chi tiết luật sư (trong nước hoặc nước ngoài)
    @GetMapping("/{lawyerId}")
    public ResponseEntity<ApiResponseV1<DetailLLawyerResponse>> getLawyerById(@PathVariable Long lawyerId) {
        return lawyerService.getLawyerById(lawyerId);
    }


    @Operation(summary = "Dashboard Lawyers ", description = "Dashboard Lawyers ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/lawyer-dashboard")
    public ResponseEntity<ApiResponseV1<?>> tcccDashboard(
            @Parameter(description = "cityCode ( 2 chữ số hoặc 5 số )") @RequestParam(required = false) Long cityCode,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate
    ) {
        try {
            return lawyerService.reportDashboard(cityCode, fromDate, toDate);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
