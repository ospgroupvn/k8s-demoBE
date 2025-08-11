package com.osp.bttp.endpoint.public_resource.tccc_api;

import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.entity.db1.DmAdministration;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.NotaryStatus;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.service.tccc.TcccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 10:42 SA
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/public/tccc")
@Slf4j
public class TcccController {
    @Autowired
    private TcccService tcccService;

    @Operation(summary = "lấy thông tin bảng sở tư pháp", description = "lấy thông tin bảng sở tư pháp type: 1 cục 2 sở")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/getAdministrationByType")
    public ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(@RequestParam @Valid @Parameter(description = "Loại tổ chức") Long type, HttpServletRequest request) {
        return tcccService.getAdministrationByType(type);
    }

    @Operation(summary = "A2 - Danh sách tccc hành nghề", description = "Danh sách tccc hành nghề")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @RequestMapping(value = "/notary-manage/search", method = RequestMethod.GET)
    public ResponseEntity<ApiResponseV1<PagingResult>> search(
            @RequestParam(value = "name", required = false) @Parameter(description = "Tên tổ chức") String name,
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "orgId", required = false) @Parameter(description = "Id sở tư pháp ( lấy từ api getAdministrationByType ) ") Long orgId,
            @RequestParam(value = "statusOrg", required = false) @Parameter(description = "" +
                    "Trạng thái hoạt động. 0: đang hoạt động.  \n" +
                    "Trạng thái hoạt động. 1: Chờ thành lập. \n" +
                    "Trạng thái hoạt động. 2: Giải thể. \n" +
                    "Trạng thái hoạt động. 3: Chấm dứt. \n" +
                    "Trạng thái hoạt động. 4: Chưa hoạt động. \n" +
                    "" +
                    "")
            String status,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage,
            HttpServletRequest request) {
        try {
            PagingResult page = new PagingResult();
            page.setPageNumber(pageNumber);
            page.setNumberPerPage(numberPerPage);
            page = tcccService.list_search(name, orgId, status, page, fromDate, toDate);
//        logAccessDao.addLog("Tìm kiếm thông tin hồ sơ tổ chức HNCC", Constants.Log.LIST_HO_SO_TCHNCC, Utils.getIpClient(request));
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", page), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Thất bại", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "A3_1: Danh sách công chứng viên", description = "Danh sách công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/ccv/search")
    public ResponseEntity<ApiResponseV1<PagingResult>> searchCcv(
            @RequestParam(value = "name", required = false) @Parameter(description = "Tên công chứng viên") String name,
            @RequestParam(value = "orgId", required = false) @Parameter(description = "Id sở tư pháp ( lấy từ api getAdministrationByType ) ") Long orgId,
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "status", required = false) @Parameter(description = "Trạng thái hoạt động") String status,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage,
            HttpServletRequest request) {
        try {
            PagingResult page = new PagingResult();
            page.setPageNumber(pageNumber);
            page.setNumberPerPage(numberPerPage);
            page = tcccService.list_ccv(name, orgId, status, page, fromDate, toDate);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", page), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Thất bại", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Danh sách trạng thái công chứng viên", description = "Danh sách trạng thái công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/ccv/getNotaryStatus")
    public ResponseEntity<ApiResponseV1<List<NotaryStatus>>> getNotaryStatus(HttpServletRequest request) {
        List<NotaryStatus> res = ConstantsTccc.NOTARY_STATUS.LST_NOTARY_STATUS;
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", res), HttpStatus.OK);
    }

    @Operation(summary = "Danh sách TCCC theo bản đồ", description = "Danh sách TCCC theo bản đồ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/getTcccByMap")
    public ResponseEntity<ApiResponseV1<?>> getTcccByMap(
            @RequestParam(value = "type", required = false) @Parameter(description = "Loại tổ chức") Integer type,
            HttpServletRequest request) {
        return tcccService.getTcccByMap(type);
    }

    @Operation(summary = "A0 : Trang chủ TCCC ", description = "Trang chủ TCCC ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/tcccDashboard")
    public ResponseEntity<ApiResponseV1<?>> tcccDashboard(
            @Parameter(description = "cityCode ( 2 chữ số )") @RequestParam(required = false) Long cityCode,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate,
            @Parameter(description = "type. 1 là TCHNCC, 2 là DGV ( hiện chỉ khác nhau cái map )") @RequestParam(required = false) Integer type
    ) {
        try {
            return tcccService.tcccDashboard(cityCode, fromDate, toDate, type);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getDataCCVMap")
    public List<Reaport> getDataCCVMap() {
        return tcccService.getDataCCVMap();
    }



}
