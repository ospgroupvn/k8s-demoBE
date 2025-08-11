package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.dto.PagingResultExt;
import com.osp.bttp.dao.model.dto.CenterOrganizationAuctionner;
import com.osp.bttp.dao.model.dto.db2.AuctioneerOfProvinceDto;
import com.osp.bttp.dao.model.entity.db2.Organization;
import com.osp.bttp.dao.model.mview.db2.DetailDecisionView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author sangnk
 * @Created 09/10/2024 - 4:27 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface OrganizationAuctionService {
    Optional<PagingResult> getAllOrganizationAuction(PagingResult page, int pageNumber, String name, Long cityId, Long orgname, Long status, String orgType);

    Optional<PagingResultExt> getDetailAuctioneer(PagingResultExt page, Long id);

    CenterOrganizationAuctionner viewDetailOrg(Long id);

    Organization findId(Long orgRoot);


    ResponseEntity<ApiResponseV1<PagingResult>> reportQuantityOrganizationAuction(String cityId, String orgType, String fromDate, String toDate, int pageNumber, int numberPerPage, String aTypes, Integer status, boolean getDetailDistrict);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationAuction(String cityId, String fromDate, String toDate, int pageNumber, int numberPerPage, String actTypes, String aTypes, boolean getDetailDistrict);

    HashMap<String,Object> reportDashboardAuctionQuery(Long cityCode, String fromDate, String toDate);

    ResponseEntity<ApiResponseV1<PagingResult>> reportAuctionCard(String cityId, String fromDate, String toDate, int pageNumber, int numberPerPage, String actTypes, String aTypes);

    ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuction(String cityId, String fromDate, String toDate, int pageNumber, int numberPerPage, String actTypes);

    ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuctionAsset(String cityId, String fromDate, String toDate, int pageNumber, int numberPerPage, String aTypes);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportQuantityOrganizationAuction(String cityId, String orgType, String fromDate, String toDate, String fileType, HttpServletRequest request, HttpServletResponse response, String aTypes, Boolean getDetailDistrict);

    ResponseEntity<ApiResponseV1<?>> exportOperationOrganizationAuction(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes, String aTypes);

    ResponseEntity<ApiResponseV1<?>> exportAuctionCard(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes, String aTypes);

    ResponseEntity<ApiResponseV1<?>> exportNoticeAuction(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes);

    ResponseEntity<ApiResponseV1<?>> exportNoticeAuctionAsset(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String aTypes);

    ResponseEntity<ApiResponseV1<?>> getTcDGTSByMap();

    ResponseEntity<ApiResponseV1<?>> reportDashboard(Long cityCode, String fromDateRaw, String toDateRaw);

    ResponseEntity<ApiResponseV1<?>> organizationAuctionDetail(Long organizationAuctionId);

    ResponseEntity<ApiResponseV1<?>> auctioneerDetail(Long auctioneerId);

    ResponseEntity<ApiResponseV1<List<DetailDecisionView>>> auctioneerDetailProcess(Long id);

    ResponseEntity<ApiResponseV1<?>> reportPublicAuctionAssetByDay(Long cityId, String yearReport, String monthReport);

    AuctioneerOfProvinceDto getAuctioneerOfProvince(boolean isGetDataDetailProvince, String cityName);

    Long getAuctionOrgCount();
}
