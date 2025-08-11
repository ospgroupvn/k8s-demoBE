package com.osp.bttp.endpoint.private_resource.bttp_api;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.OrgNotaryInfoCreateDto;
import com.osp.bttp.dao.model.entity.db3.DmAdministration;
import com.osp.bttp.dao.model.entity.db3.OrgNotaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryInfoInOrg;
import com.osp.bttp.dao.model.mview.bttp.OrgCategoryInfo;
import com.osp.bttp.dao.model.mview.bttp.OrgNotaryDetailResponse;
import com.osp.bttp.dao.service.bttp.OrgNotaryInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/private/bttp/org")
public class OrgNotaryBttpController {

    @Autowired
    private OrgNotaryInfoService orgNotaryInfoService;

    @PostMapping
    @Operation(summary = "Thêm tổ chức hành nghề công chứng", description = "Thêm mới một tổ chức hành nghề công chứng")
    public ResponseEntity<ApiResponseV1<OrgNotaryInfo>> addOrgNotaryInfo(
            @Valid @RequestBody OrgNotaryInfoCreateDto dto) {
        OrgNotaryInfo orgNotaryInfo = orgNotaryInfoService.add(dto);
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", orgNotaryInfo), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Chỉnh sửa tổ chức hành nghề công chứng", description = "Cập nhật thông tin tổ chức công chứng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy tổ chức"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    public ResponseEntity<ApiResponseV1<OrgNotaryInfo>> editOrgNotaryInfo(
            @PathVariable Long id,
            @Valid @RequestBody OrgNotaryInfoCreateDto dto) {
        OrgNotaryInfo orgNotaryInfo = orgNotaryInfoService.edit(id, dto);
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", orgNotaryInfo), HttpStatus.OK);
    }

    @Operation(summary = "A2 - Danh sách tccc hành nghề", description = "Danh sách tccc hành nghề")
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
            page = orgNotaryInfoService.list_search(name, orgId, status, page, fromDate, toDate);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", page), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Thất bại", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //A2 - Chi tiết tổ chức hành nghề công chứng
    @Operation(summary = "A2 : Chi tiết tổ chức hành nghề công chứng", description = "Chi tiết tổ chức hành nghề công chứng")
    @GetMapping("/detailOrganizationNotary/{idOrgNotaryInfo}")
    public ResponseEntity<ApiResponseV1<OrgNotaryDetailResponse>> detailOrganizationNotary(
            @PathVariable("idOrgNotaryInfo") @Parameter(description = "Id tổ chức hành nghề công chứng") Long id) {
        try {
            return orgNotaryInfoService.detailOrganizationNotary(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category")
    public ResponseEntity<PaginationDto<OrgCategoryInfo>> searchOrgCategories(
            @RequestParam(required = false) Long adminId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") Long pageNo,
            @RequestParam(defaultValue = "10") Long pageSize
    ) {
        PaginationDto<OrgCategoryInfo> result = orgNotaryInfoService.getListOrgCategory( adminId,name, pageNo, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/notary-in-org/{id}")
    public ResponseEntity<PagingResult<NotaryInfoInOrg>> listNotaryInOrg(
           @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PagingResult<NotaryInfoInOrg> result = orgNotaryInfoService.getPageNotary(id, pageNo, pageSize);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "lấy thông tin bảng sở tư pháp", description = "lấy thông tin bảng sở tư pháp type: 1 cục 2 sở")
    @GetMapping("/getAdministrationByType")
    public ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(@RequestParam(defaultValue = "2") @Valid @Parameter(description = "Loại tổ chức")  Long type, HttpServletRequest request) {
        return orgNotaryInfoService.getAdministrationByType(type);
    }

}
