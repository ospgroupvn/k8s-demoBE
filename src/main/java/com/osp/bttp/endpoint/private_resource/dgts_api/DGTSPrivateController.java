package com.osp.bttp.endpoint.private_resource.dgts_api;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.TimeLineView;
import com.osp.bttp.dao.model.mview.db2.DetailDecisionView;
import com.osp.bttp.dao.service.dgts.OrganizationAuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 8:11 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Secured({ConstantAuthor.SYSTEM.system, ConstantAuthor.SYSTEM.report})
@RestController
@RequestMapping("/v1/api/private/dgts")
public class DGTSPrivateController {

    @Autowired
    private OrganizationAuctionService organizationAuctionService;




    @Operation(summary = "B4 - Báo cáo số lượng tổ chức hành nghề đấu giá", description = "B4 - Báo cáo số lượng tổ chức hành nghề đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-quantity-organization-auction")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportQuantityOrganizationAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @Parameter(description = "orgType. Tiêu chí: 0 - trung tâm dịch vụ đgts\n" +
                    "1 - DN đấu giá tư nhân\n" +
                    "2 - công ty đấu giá hợp danh\n" +
                    "11 - chi nhánh dn đấu giá tài sản\n" +
                    "4 - VAMC\n") @RequestParam(required = false) String orgTypes,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean getDetailDistrict = false;
        if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
            getDetailDistrict = true;
        }
        return organizationAuctionService.reportQuantityOrganizationAuction(cityId, orgTypes, fromDate, toDate, pageNumber, numberPerPage, aTypes, null, getDetailDistrict);
    }

    //export Excel B4
    @Operation(summary = "B4 - Export excel số lượng tổ chức hành nghề đấu giá", description = "B4 - Export excel số lượng tổ chức hành nghề đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-quantity-organization-auction/export")
    public ResponseEntity<ApiResponseV1<?>> exportQuantityOrganizationAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "orgType. Tiêu chí: 0 - trung tâm dịch vụ đgts\n" +
                    "1 - DN đấu giá tư nhân\n" +
                    "2 - công ty đấu giá hợp danh\n" +
                    "11 - chi nhánh dn đấu giá tài sản\n" +
                    "4 - VAMC\n") @RequestParam(required = false) String orgTypes,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            HttpServletRequest request, HttpServletResponse response) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean getDetailDistrict = false;
        if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
            getDetailDistrict = true;
        }
        return organizationAuctionService.exportExcelReportQuantityOrganizationAuction(cityId, orgTypes, fromDate, toDate, "xlsx", request, response, aTypes, getDetailDistrict);
    }

    @Operation(summary = "B5 - Tình hình hoạt động của TCĐG", description = "B5 - Tình hình hoạt động của TCĐG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-operation-organization-auction")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @Parameter(description = "actTypes: danh sách tiêu chí. " +
                    " 8 ->'capmoi'\n" +
                    " 1 ->'caplai'\n" +
                    " 11-> 'capdkhd'\n" +
                    " 12-> 'tl_dkhd'\n" +
                    " 9 ->'thaydoi'\n" +
                    " 10-> 'chuyendoi\n" +
                    " 3 ->'thuhoi'\n" +
                    " 4 ->'satnhap'\n" +
                    " 6 ->'giaithe'\n" +
                    " 7 ->'phasan'\n" +
                    " 5 ->'hopnhat'") @RequestParam(required = false) String actTypes,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        return organizationAuctionService.reportOperationOrganizationAuction(cityId, fromDate, toDate, pageNumber, numberPerPage, actTypes, aTypes, false);
    }

    //export Excel B5
    @Operation(summary = "B5 - Export excel Tình hình hoạt động của TCĐG", description = "B5 - Export excel Tình hình hoạt động của TCĐG")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-operation-organization-auction/export")
    public ResponseEntity<ApiResponseV1<?>> exportOperationOrganizationAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "actTypes: danh sách tiêu chí. " +
                    " 8 ->'capmoi'\n" +
                    " 1 ->'caplai'\n" +
                    " 11-> 'capdkhd'\n" +
                    " 12-> 'tl_dkhd'\n" +
                    " 9 ->'thaydoi'\n" +
                    " 10-> 'chuyendoi\n" +
                    " 3 ->'thuhoi'\n" +
                    " 4 ->'satnhap'\n" +
                    " 6 ->'giaithe'\n" +
                    " 7 ->'phasan'\n" +
                    " 5 ->'hopnhat'") @RequestParam(required = false) String actTypes,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            HttpServletRequest request, HttpServletResponse response) {
        return organizationAuctionService.exportOperationOrganizationAuction(cityId, fromDate, toDate, request, response, actTypes, aTypes);
    }

    @Operation(summary = "B6 - Báo cáo cấp thẻ Đấu giá viên", description = "B6 - Báo cáo cấp thẻ Đấu giá viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-auction-card")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportAuctionCard(
            @Parameter(description = "cityId ( 6 chữ số hoặc 2 số)") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @Parameter(description = "Tiêu chí: actTypes: " +
                    " 1 -> Cấp mới CCHN đấu giá \n" +
                    " 2 -> Cấp lại CCHN đấu giá \n " +
                    " 3 -> Thu hồi CCHN đấu giá \n" +
                    " 4 -> Cấp mới thẻ ĐGV \n" +
                    " 5-> Cấp lại thẻ ĐGV \n" +
                    " 6 -> Thu hồi thẻ ĐGV ") @RequestParam(required = false) String types,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        return organizationAuctionService.reportAuctionCard(cityId, fromDate, toDate, pageNumber, numberPerPage, types, aTypes);
    }

    // export Excel B6
    @Operation(summary = "B6 - Export excel Báo cáo cấp thẻ Đấu giá viên", description = "B6 - Export excel Báo cáo cấp thẻ Đấu giá viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-auction-card/export")
    public ResponseEntity<ApiResponseV1<?>> exportAuctionCard(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @Parameter(description = "Tiêu chí: actTypes: " +
                    " 1 -> Cấp mới CCHN đấu giá \n" +
                    " 2 -> Cấp lại CCHN đấu giá \n " +
                    " 3 -> Thu hồi CCHN đấu giá \n" +
                    " 4 -> Cấp mới thẻ ĐGV \n" +
                    " 5-> Cấp lại thẻ ĐGV \n" +
                    " 6 -> Thu hồi thẻ ĐGV ") @RequestParam(required = false) String types,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            HttpServletRequest request, HttpServletResponse response) {
        return organizationAuctionService.exportAuctionCard(cityId, fromDate, toDate, request, response, types, aTypes);
    }


    @Operation(summary = "B7 - Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá", description = "B7 - Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-notice-auction")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        return organizationAuctionService.reportNoticeAuction(cityId, fromDate, toDate, pageNumber, numberPerPage, aTypes);
    }

    // export Excel B7
    @Operation(summary = "B7 - Export excel Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá", description = "B7 - Export excel Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-notice-auction/export")
    public ResponseEntity<ApiResponseV1<?>> exportNoticeAuction(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,

            HttpServletRequest request, HttpServletResponse response) {
        return organizationAuctionService.exportNoticeAuction(cityId, fromDate, toDate, request, response, aTypes);
    }

    @Operation(summary = "B8 - Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản", description = "B8 - Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-notice-auction-asset")
    public ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuctionAsset(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            @RequestParam(value = "pageNumber", required = true, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = true, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage) {
        return organizationAuctionService.reportNoticeAuctionAsset(cityId, fromDate, toDate, pageNumber, numberPerPage, aTypes);
    }

    // export Excel B8
    @Operation(summary = "B8 - Export excel Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản", description = "B8 - Export excel Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-notice-auction-asset/export")
    public ResponseEntity<ApiResponseV1<?>> exportNoticeAuctionAsset(
            @Parameter(description = "cityId ( 6 chữ số )") @RequestParam(required = false) String cityId,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @RequestParam(value = "aTypes", required = false) @Parameter(description = "Tiêu chí theo thứ tự cột, nếu nhiều thì cách nhau dấu phẩy. ví dụ 1,2.") String aTypes,
            HttpServletRequest request, HttpServletResponse response) {
        return organizationAuctionService.exportNoticeAuctionAsset(cityId, fromDate, toDate, request, response, aTypes);
    }


    //B9 : Thống kê diễn biến công khai việc đấu giá tài sản
    @Operation(summary = "B9 - Thống kê diễn biến công khai việc đấu giá tài sản", description = "B9 - Thống kê diễn biến công khai việc đấu giá tài sản")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đơn vị quản lý"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/report-public-auction-asset-by-day")
    public ResponseEntity<ApiResponseV1<?>> reportPublicAuctionAssetByDay(
            @Parameter(description = "cityId ( 6 chữ số hoặc 2 số đều được)") @RequestParam(required = false) Long cityId,
            @RequestParam(value = "yearReport", required = true) @Parameter(description = "Năm báo cáo") String yearReport,
            @RequestParam(value = "monthReport", required = true) @Parameter(description = "Tháng báo cáo ") String monthReport) {
        return organizationAuctionService.reportPublicAuctionAssetByDay(cityId, yearReport, monthReport);
    }


}
