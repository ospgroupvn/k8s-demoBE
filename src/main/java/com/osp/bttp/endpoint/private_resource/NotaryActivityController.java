package com.osp.bttp.endpoint.private_resource;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.db3.NotaryActivityDTO;
import com.osp.bttp.dao.model.dto.db3.NotaryActivitySummaryDTO;
import com.osp.bttp.dao.service.tccc.NotaryActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sangnk
 * @Created 25/10/2024 - 11:44 SA
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@Slf4j
@RequestMapping("/v1/api/private/tccc/notary-activity")
@Secured({ConstantAuthor.NOTARY_ACTIVITY.author})
public class NotaryActivityController {
    @Autowired
    private NotaryActivityService notaryActivityService;

    @Operation(summary = "C1 - Danh sách dữ liệu hoạt động HNCC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOrganizationNotary(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") Long cityId,
            @RequestParam(value = "yearReport", required = false) @Parameter(description = "Năm báo cáo") String yearReport,
            @RequestParam(value = "monthReport", required = false) @Parameter(description = "Tháng báo cáo ( có thẻ chọn nhiều, cách nhau dấu phẩy )") String monthReport,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        try {
            return notaryActivityService.search(fromDate, toDate, cityId, pageNumber, numberPerPage, yearReport, monthReport);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "C1 - Nhập dữ liệu hoạt động HNCC")
    @PostMapping("/add")
    public ResponseEntity<ApiResponseV1<NotaryActivityDTO>> createNotaryActivity(@RequestBody @Valid NotaryActivityDTO notaryActivityDTO) {
        return notaryActivityService.createNotaryActivity(notaryActivityDTO);
    }

    @Operation(summary = "Lấy dữ liệu theo tháng năm")
    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<NotaryActivityDTO>> getActivitiesByMonth(@PathVariable int year, @PathVariable int month) {
        List<NotaryActivityDTO> activities = notaryActivityService.getActivitiesByMonth(year, month);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @Operation(summary = "Cập nhật dữ liệu hoạt động HNCC")
    @PutMapping("/{id}")
    public ResponseEntity<NotaryActivityDTO> updateNotaryActivity(@PathVariable Long id, @RequestBody NotaryActivityDTO notaryActivityDTO) throws BadRequestException {
        NotaryActivityDTO updatedActivity = notaryActivityService.updateNotaryActivity(id, notaryActivityDTO);
        return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
    }

    @Operation(summary = "Xóa dữ liệu hoạt động HNCC")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseV1> deleteNotaryActivity(@PathVariable Long id) {
        return notaryActivityService.deleteNotaryActivity(id);
    }


    @Operation(summary = "C2 : Tổng hợp theo sở tư pháp")
    @GetMapping("/aggregate-by-stp")
    public ResponseEntity<ApiResponseV1<PagingResult>> aggregateByStp(@RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
                                                                                  @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
                                                                                  @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
                                                                                  @RequestParam(value = "yearReport", required = true) @Parameter(description = "Năm báo cáo") String yearReport,
                                                                                  @RequestParam(value = "monthReport", required = true) @Parameter(description = "Tháng báo cáo ( có thẻ chọn nhiều, cách nhau dấu phẩy )") String monthReport,
                                                                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
                                                                      @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
                                                                      @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        return notaryActivityService.aggregateByStp(fromDate, toDate, cityId, pageNumber, numberPerPage, yearReport, monthReport, aTypes, true);
    }

    //xuất excel
    @Operation(summary = "C2 : Xuất excel")
    @GetMapping("/export-excel-aggregate-by-stp")
    public ResponseEntity<ApiResponseV1<?>> exportAggregateByStp(@RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
                                                                 @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
                                                                 @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
                                                                 @RequestParam(value = "yearReport", required = true) @Parameter(description = "Năm báo cáo") String yearReport,
                                                                 @RequestParam(value = "monthReport", required = true) @Parameter(description = "Tháng báo cáo ( có thẻ chọn nhiều, cách nhau dấu phẩy )") String monthReport,
                                                                 HttpServletResponse response) {
        return notaryActivityService.exportAggregateByStp(fromDate, toDate, cityId, yearReport, monthReport, response);
    }

}
