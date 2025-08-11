package com.osp.bttp.endpoint.public_resource.dgts_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.CenterOrganizationAuctionner;
import com.osp.bttp.dao.model.entity.db2.Auctioneer;
import com.osp.bttp.dao.model.entity.db2.Organization;
import com.osp.bttp.dao.model.mview.db2.AuctioneerView;
import com.osp.bttp.dao.model.mview.db2.DetailDecisionView;
import com.osp.bttp.dao.service.dgts.AuctioneerService;
import com.osp.bttp.dao.service.dgts.OrganizationAuctionService;
import com.osp.bttp.dao.service.dgts.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sangnk
 * @Created 09/10/2024 - 4:26 CH
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/public/dgts")
@Slf4j
public class DGTSController {

    @Autowired
    private OrganizationAuctionService organizationAuctionService;

    @Autowired
    private AuctioneerService auctioneerService;


    @Autowired
    private OrganizationService organizationService;
    @Operation(summary = "Api Danh sách sở tư pháp", description = "Api Danh sách sở tư pháp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Thất bại")
    })
    @GetMapping("/getAllOrganization")
    public ResponseEntity<ApiResponseV1<?>> getAllOrganization() {
        try {
            return organizationService.getAllOrganization();
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "B1 - Api tổ chức hành nghề đấu giá", description = "Api tổ chức hành nghề đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công")
    })
    @GetMapping("/getAllOrganizationAuction")
    public ResponseEntity<ApiResponseV1<PagingResult>> getAllOrganizationAuction(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
                                                                                @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage,
                                                                                @RequestParam(value = "status", required = false) @Parameter(description = "Trạng thái hoạt động. Hoạt động 0, ngừng hoạt động 1, đã xóa 3") Long status,
                                                                                @RequestParam(value = "orgId", required = false) @Parameter(description = "ID tổ chức/CN") Long orgId,
                                                                                @RequestParam(value = "orgType", required = false) @Parameter(description = "loại tổ chức orgType") String orgType,
                                                                                @RequestParam(value = "cityId", required = false) @Parameter(description = "Id tỉnh thành Sở tư pháp") Long cityId,
                                                                                @RequestParam(value = "name", required = false) @Parameter(description = "Tên tổ chức đấu giá") String name,
                                                                                HttpServletRequest request) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        try {
            page = organizationAuctionService.getAllOrganizationAuction(page, numberPerPage, name, cityId, orgId, status, orgType).orElse(new PagingResult());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<ApiResponseV1<PagingResult>>(new ApiResponseV1<PagingResult>(true, 1, "Thành công", page), HttpStatus.OK);
    }

    @Operation(summary = "B2 - Api Chi tiết tổ chức hành nghề đấu giá", description = "Api Chi tiết tổ chức hành nghề đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/getDetailAuctioneer")
    public ResponseEntity<ApiResponseV1<CenterOrganizationAuctionner>> getDetailAuctioneer(@RequestParam(value = "id", required = true) Long id,
                                                               HttpServletRequest request) {
        try {
            CenterOrganizationAuctionner viewOrganization = new CenterOrganizationAuctionner();
            viewOrganization = organizationAuctionService.viewDetailOrg(id);
            List<Auctioneer> lts = new ArrayList<>();
            Organization organization=viewOrganization.getOrganization();
            for (Auctioneer auctioneer : viewOrganization.getAuctioneers()) {
                if (auctioneer.getIsPublish() == Constants.IS_PUBLISH.IS_PUBLISH_YES) {
                    lts.add(auctioneer);
                }
            }
            viewOrganization.setAuctioneers(lts);
            if(viewOrganization.getOrganization().getOrgType()==11){
                String namecha=organizationAuctionService.findId(viewOrganization.getOrganization().getOrgRoot()).getFullname();
                organization.setFullname(viewOrganization.getOrganization().getFullname()+" - "+namecha);
                viewOrganization.setOrganization(organization);
            }

            return new ResponseEntity<ApiResponseV1<CenterOrganizationAuctionner>>(new ApiResponseV1<CenterOrganizationAuctionner>(true, 1, "Thành công", viewOrganization), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<ApiResponseV1<CenterOrganizationAuctionner>>(new ApiResponseV1<CenterOrganizationAuctionner>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //B2 - Chi tiết tổ chức hành nghề đấu giá
    @Operation(summary = "B2 - Chi tiết tổ chức hành nghề đấu giá", description = "B2 - Chi tiết tổ chức hành nghề đấu giá")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy tổ chức hành nghề đấu giá"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/organization-auction-detail/{organizationAuctionId}")
    public ResponseEntity<ApiResponseV1<?>> organizationAuctionDetail(
            @Parameter(description = "organizationAuctionId: id tổ chức hndg") @PathVariable Long organizationAuctionId) {
        return organizationAuctionService.organizationAuctionDetail(organizationAuctionId);
    }

    @Operation(summary = "B3 - Thông tin chi tiết một đấu giá viên", description = "B3 - Thông tin chi tiết một đấu giá viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Success"),
            @ApiResponse(responseCode = "40", description = "Không tìm thấy đấu giá viên"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/auctioneer-detail/{auctioneerId}")
    public ResponseEntity<ApiResponseV1<?>> auctioneerDetail(
            @Parameter(description = "auctioneerId: id đấu giá viên") @PathVariable Long auctioneerId) {
        return organizationAuctionService.auctioneerDetail(auctioneerId);
    }


    @Operation(summary = "B3_2 : Quá trình hành nghề của công chứng viên", description = "Quá trình hành nghề của công chứng viên")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/auctioneer-detail-process/{idNotaryInfo}")
    public ResponseEntity<ApiResponseV1<List<DetailDecisionView>>> detailNotaryProcess(
            @PathVariable("idNotaryInfo") @Parameter(description = "Id công chứng viên") Long id) {
        try {
            return organizationAuctionService.auctioneerDetailProcess(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "B3 - Api Danh sách đấu giá viên trong tổ chức", description = "Api Danh sách đấu giá viên trong tổ chức")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/searchAuctioneer")
    public ResponseEntity<ApiResponseV1<PagingResult<AuctioneerView>>> searchAuctioneer(
            @RequestParam(value = "p", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") int numberPerPage,
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "province", required = false) Long province,
            @RequestParam(value = "cerCode", required = false) String cerCode,
            @RequestParam(value = "sex", required = false) Long sex,
            @RequestParam(value = "orgId", required = false) Long orgId,
            @RequestParam(value = "auctioneerStatus", required = false) @Parameter(description = "Trạng thái hoạt động. Hoạt động 0, ngừng hoạt động 1, đã xóa 3") Long auctioneerStatus,
            @RequestParam(value = "cerStatus", required = false) @Parameter(description = "Trạng thái cchn. cấp mới 1 , cấp lại 2, thu hồi 3") Long cerStatus,
            @RequestParam(value = "cardStatus", required = false) @Parameter(description = "Trạng thái tdg. cấp mới 5 , cấp lại 6, thu hồi 7, thôi hành nghề 10") Long cardStatus,
            @RequestParam(value = "actType", required = false) Long actType,
            @RequestParam(value = "other", required = false) Long other,
            HttpServletRequest request) {
        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        try {
            page = auctioneerService.searchAuctioneer(page, numberPerPage, fullname, province, cerCode, sex, auctioneerStatus, cerStatus, cardStatus, actType, other, orgId).orElse(new PagingResult());
            return new ResponseEntity<ApiResponseV1<PagingResult<AuctioneerView>>>(new ApiResponseV1<PagingResult<AuctioneerView>>(true, 1, "Thành công", page), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Have error in AuctioneerController:" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<ApiResponseV1<PagingResult<AuctioneerView>>>(new ApiResponseV1<PagingResult<AuctioneerView>>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @Operation(summary = "A1 - Báo cáo tổng quan - dashboard", description = "A1 - Báo cáo tổng quan - dashboard")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "1", description = "Success"),
//            @ApiResponse(responseCode = "400", description = "Bad Request"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "Forbidden"),
//            @ApiResponse(responseCode = "404", description = "Not Found"),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    })
////    @GetMapping("/report-dashboard")
//    public ResponseEntity<ApiResponseV1<?>> reportDashboard(
//            @Parameter(description = "cityCode ( 2 chữ số )") @RequestParam(required = false) Long cityCode,
//            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
//            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate) {
//        return organizationAuctionService.reportDashboardAuctionQuery(cityCode, fromDate, toDate);
//    }

    @Operation(summary = "Danh sách TCCC theo bản đồ", description = "Danh sách TCCC theo bản đồ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "0", description = "Thất bại"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/getTcDGTSByMap")
    public ResponseEntity<ApiResponseV1<?>> getTcDGTSByMap(HttpServletRequest request) {
        return organizationAuctionService.getTcDGTSByMap();
    }

    @Operation(summary = "A0 : Trang chủ DGTS ", description = "Trang chủ DGTS ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1", description = "Thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống")
    })
    @GetMapping("/dgtsDashboard")
    public ResponseEntity<ApiResponseV1<?>> tcccDashboard(
            @Parameter(description = "cityCode ( 2 chữ số )") @RequestParam(required = false) Long cityCode,
            @Parameter(description = "fromDate (dd/MM/yyyy)") @RequestParam(required = false) String fromDate,
            @Parameter(description = "toDate (dd/MM/yyyy)") @RequestParam(required = false) String toDate
    ) {
        try {
            return organizationAuctionService.reportDashboard(cityCode, fromDate, toDate);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
