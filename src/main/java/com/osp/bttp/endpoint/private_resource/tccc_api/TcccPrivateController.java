package com.osp.bttp.endpoint.private_resource.tccc_api;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.FileOrgNotary;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.NotaryInfoView;
import com.osp.bttp.dao.model.mview.db1.TimeLineView;
import com.osp.bttp.dao.service.AccUserService;
import com.osp.bttp.dao.service.tccc.TcccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sangnk
 * @Created 11/10/2024 - 11:22 SA
 * @project = bttp
 * @_ Mô tả:
 */

@RestController
@Slf4j
@RequestMapping("/v1/api/private/tccc")
@Secured({ConstantAuthor.SYSTEM.system, ConstantAuthor.SYSTEM.report})
public class TcccPrivateController {
    @Autowired
    private TcccService tcccService;
    @Autowired
    private AccUserService accUserService;


    //A2 - Chi tiết tổ chức hành nghề công chứng
    @Operation(summary = "A2 : Chi tiết tổ chức hành nghề công chứng", description = "Chi tiết tổ chức hành nghề công chứng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/detailOrganizationNotary/{idOrgNotaryInfo}")
    public ResponseEntity<ApiResponseV1<FileOrgNotary>> detailOrganizationNotary(
            @PathVariable("idOrgNotaryInfo") @Parameter(description = "Id tổ chức hành nghề công chứng") Long id) {
        try {
            return tcccService.detailOrganizationNotary(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //A3 - chi tiết  công chứng viên
    @Operation(summary = "A3 : Chi tiết công chứng viên", description = "Chi tiết công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/detailNotary/{idNotaryInfo}")
    public ResponseEntity<ApiResponseV1<NotaryInfoView>> detailNotary(
            @PathVariable("idNotaryInfo") @Parameter(description = "Id công chứng viên") Long id) {
        try {
            return tcccService.detailNotary(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //A3_2 Quá trình hành nghề của công chứng viên
    @Operation(summary = "A3_2 : Quá trình hành nghề của công chứng viên", description = "Quá trình hành nghề của công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/detailNotaryProcess/{idNotaryInfo}")
    public ResponseEntity<ApiResponseV1<List<TimeLineView>>> detailNotaryProcess(
            @PathVariable("idNotaryInfo") @Parameter(description = "Id công chứng viên") Long id) {
        try {
            return tcccService.getTimeLineCCV(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "A4 Sheet 1: Báo cáo số liệu tổ chức hành nghề công chứng viên", description = "Báo cáo số liệu tổ chức hành nghề công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/reportOrganizationNotary")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOrganizationNotary(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            @RequestParam(value = "status", required = false) @Parameter(description = "Tiêu chí theo status, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2") String status,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage)
    {
        try {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Boolean getDetailDistrict = false;
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                getDetailDistrict = true;
            }
            return tcccService.reportOrganizationNotary(fromDate, toDate, cityId, pageNumber, numberPerPage, status, aTypes, null, getDetailDistrict);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //export Excel A4
    @Operation(summary = "A4 : Xuất Excel báo cáo số liệu tổ chức hành nghề công chứng viên", description = "Xuất Excel báo cáo số liệu tổ chức hành nghề công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/exportExcelReportOrganizationNotary")
    public ResponseEntity<ApiResponseV1<?>> exportExcelOrganizationNotary(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "status", required = false) @Parameter(description = "Tiêu chí, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2") String status,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        try {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Boolean getDetailDistrict = false;
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                getDetailDistrict = true;
            }
            return tcccService.exportExcelReportOrganizationNotary(fromDate, toDate, cityId, status, httpServletRequest, httpServletResponse, aTypes, getDetailDistrict);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "A5 - Sheet1 : Báo cáo số tình trạng hoạt động tổ chức hành nghề công chứng", description = "Báo cáo số tình trạng hoạt động tổ chức hành nghề công chứng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/reportOperationOrganizationNotary")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationNotary(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
//            @RequestParam(value = "type", required = false) @Parameter(description = "" +
//                    "Tiêu chí: pcc_tl = 1, pcc_gt =2, pcc_cd = 3, vpcc_tl = 4, " +
//                    "vpcc_tctl = 5, vpcc_th_tl = 6, vpcc_cp_dkhd = 7, vpcc_tc_cp_dkhd= 8, " +
//                    "vpcc_th_dkhd = 9, vpcc_duoc_hop_nhat = 10, vpcc_duoc_hop_nhat = 11, vpcc_sat_nhap = 12, vpcc_nhan_sat_nhap = 13, " +
//                    "vpcc_chuyen_nhuong = 14, vpcc_td_nd_dkhd = 15, tc_hncc_xlvp = 16, cham_dut_hd = 17 ") String type,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage)
    {
        try {
            return tcccService.reportOperationOrganizationNotary(fromDate, toDate, cityId, aTypes, pageNumber, numberPerPage, aTypes, false);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //export Excel A5
    @Operation(summary = "A5 : Xuất Excel báo cáo số tình trạng hoạt động tổ chức hành nghề công chứng", description = "Xuất Excel báo cáo số tình trạng hoạt động tổ chức hành nghề công chứng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/exportExcelReportOperationOrganizationNotary")
    public ResponseEntity<ApiResponseV1<?>> exportExcelOperationOrganizationNotary(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
//            @RequestParam(value = "type", required = false) @Parameter(description = "Tiêu chí: pcc_tl, pcc_gt, pcc_cd, vpcc_tl, vpcc_tctl, vpcc_th_tl, vpcc_cp_dkhd, vpcc_tc_cp_dkhd, vpcc_th_dkhd, vpcc_hop_nhat, vpcc_sat_nhap, vpcc_chuyen_nhuong, vpcc_td_nd_dkhd, tc_hncc_xlvp, pvcc_cham_dut_hd ") String type,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
        try {
            return tcccService.exportExcelReportOperationOrganizationNotary(fromDate, toDate, cityId, aTypes, httpServletRequest, httpServletResponse, aTypes);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "A6 : Báo cáo tình hình bổ nhiệm công chứng viên", description = "Báo cáo tình hình bổ nhiệm công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/reportOperationSuggestAppoint")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationSuggestAppoint(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí") String type,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        try {
            return tcccService.reportOperationSuggestAppoint(fromDate, toDate, cityId, type, pageNumber, numberPerPage, false);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //export Excel A6
    @Operation(summary = "A6 : Xuất Excel báo cáo tình hình bổ nhiệm công chứng viên", description = "Xuất Excel báo cáo tình hình bổ nhiệm công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/exportExcelReportOperationSuggestAppoint")
    public ResponseEntity<ApiResponseV1<?>> exportExcelOperationSuggestAppoint(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí:" +
                    "1: Số lg người tập sự, 2: số lg người đề nghị bổ nhiệm ccv, 3:miễn nhiệm ccv, 4: đề nghị bổ nhiệm lại ccv, 5: số lg bổ nhiệm ccv, 6: số lg miễn nhiệm ccv, 7: bổ nhiểm lại ccv") String type,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return tcccService.exportExcelReportOperationSuggestAppoint(fromDate, toDate, cityId,type, httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //report A7 Báo cáo xử lý vi phạm của Công chứng viên
    @Operation(summary = "A7 : Báo cáo xử lý vi phạm của Công chứng viên", description = "Báo cáo xử lý vi phạm của Công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/reportOperationViolation")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolation(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        try {
            return tcccService.reportOperationViolation(fromDate, toDate, cityId, pageNumber, numberPerPage);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //export excel A7
    @Operation(summary = "A7 : Xuất Excel báo cáo xử lý vi phạm của Công chứng viên", description = "Xuất Excel báo cáo xử lý vi phạm của Công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/exportExcelReportOperationViolation")
    public ResponseEntity<ApiResponseV1<?>> exportExcelOperationViolation(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return tcccService.exportExcelReportOperationViolation(fromDate, toDate, cityId, httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //report A8 Báo cáo xử lý vi phạm của tổ chức  HNCC
    @Operation(summary = "A8 : Báo cáo xử lý vi phạm của tổ chức  HNCC", description = "Báo cáo xử lý vi phạm của tổ chức  HNCC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/reportOperationViolationOrganization")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolationOrganization(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        try {
            return tcccService.reportOperationViolationOrganization(fromDate, toDate, cityId, pageNumber, numberPerPage);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //export excel A8
    @Operation(summary = "A8 : Xuất Excel báo cáo xử lý vi phạm của tổ chức  HNCC", description = "Xuất Excel báo cáo xử lý vi phạm của tổ chức  HNCC")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/exportExcelReportOperationViolationOrganization")
    public ResponseEntity<ApiResponseV1<?>> exportExcelOperationViolationOrganization(
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "cityId", required = false) @Parameter(description = "Mã tỉnh thành") String cityId,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return tcccService.exportExcelReportOperationViolationOrganization(fromDate, toDate, cityId, httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getDataChartCity")
    public ResponseEntity<ApiResponseV1<?>> getDataChartCity(
            @RequestParam(value = "cityCode", required = true) Long cityCode,
            @RequestParam(value = "fromDateRaw", required = false) String fromDateRaw,
            @RequestParam(value = "toDateRaw", required = false) String toDateRaw
    ) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Object data = tcccService.getDataChartCity(cityCode, 1, fromDateRaw, toDateRaw, userLogin);

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);
    }

}
