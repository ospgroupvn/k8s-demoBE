package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.NotaryInfoCreateDto;
import com.osp.bttp.dao.model.mview.bttp.NotaryChiefResponse;
import com.osp.bttp.dao.model.mview.bttp.NotaryInfoDetailView;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface NotaryInfoService {
    Long add(NotaryInfoCreateDto notaryInfo);

    Long edit(Long id, NotaryInfoCreateDto notaryInfo);

    void delete(Long id);
    String deleteNotaryInfo(Long id);

    PagingResult list_ccv(String name, Long orgId,Long orgCode,String status, PagingResult page, String fromDateRaw, String toDateRaw);

    ResponseEntity<ApiResponseV1<NotaryInfoDetailView>> detailNotary(Long idNotary);

    PaginationDto<NotaryChiefResponse> getListNotaryChiefCategory(String name, Long pageNo, Long pageSize);

    ResponseEntity<ApiResponseV1<?>> exportExcelNotaryInfo(String name, Long orgId, String status, PagingResult page, String fromDateRaw, String toDateRaw, HttpServletResponse httpServletResponse);

}
