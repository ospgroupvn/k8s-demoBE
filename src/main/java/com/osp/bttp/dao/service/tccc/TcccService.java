package com.osp.bttp.dao.service.tccc;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.FileOrgNotary;
import com.osp.bttp.dao.model.entity.db1.DmAdministration;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.NotaryInfoView;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.model.mview.db1.TimeLineView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 10:58 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface TcccService {
    PagingResult list_search(String search, Long orgId, String staus, PagingResult page, String fromDate, String toDate);

    ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(Long type);

    PagingResult list_ccv(String name, Long orgId, String status, PagingResult page, String fromDate, String toDate);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOrganizationNotary(String fromDateRaw, String toDateRaw, String cityId, int pageNumber, int numberPerPage, String status, String aTypes, Integer addressTypeGet, Boolean getDetailDistrict);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationNotary(String fromDate, String toDate, String cityId, String type, int pageNumber, int numberPerPage, String aTypes, boolean getDetailDistrict);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOperationSuggestAppoint(String fromDate, String toDate, String cityId, String type, int pageNumber, int numberPerPage, boolean getDetailDistrict);

    HashMap<String, Object> reportA1(HashMap<String, Object> data, Long cityCode, Date fromDate, Date toDate);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportOrganizationNotary(String fromDate, String toDate, String cityId, String status, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String aTypes, Boolean getDetailDistrict);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationOrganizationNotary(String fromDate, String toDate, String cityId, String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String aTypes);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationSuggestAppoint(String fromDate, String toDate, String cityId, String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolation(String fromDate, String toDate, String cityId, int pageNumber, int numberPerPage);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationViolation(String fromDate, String toDate, String cityId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolationOrganization(String fromDate, String toDate, String cityId, int pageNumber, int numberPerPage);

    ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationViolationOrganization(String fromDate, String toDate, String cityId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    ResponseEntity<ApiResponseV1<?>> getTcccByMap(Integer type);

    ResponseEntity<ApiResponseV1<?>> tcccDashboardQuery(AccUser user, Integer type, Long cityCode, String fromDate, String toDate);

    ResponseEntity<ApiResponseV1<?>> tcccDashboard(Long cityCode, String fromDate, String toDate, Integer type);

    ResponseEntity<ApiResponseV1<FileOrgNotary>> detailOrganizationNotary(Long id);

    ResponseEntity<ApiResponseV1<NotaryInfoView>> detailNotary(Long id);

    ResponseEntity<ApiResponseV1<List<TimeLineView>>> getTimeLineCCV(Long id);

    ResponseEntity<ApiResponseV1<List<Reaport>>> reportOrganizationNotaryChart(String fromDate, String toDate, String cityId, String status);

    List<Reaport> getDataCCVMap();

    Object getDataChartCity(Long cityCode, Integer type, String fromDateRaw, String toDateRaw, AccUser userLogin);
}
