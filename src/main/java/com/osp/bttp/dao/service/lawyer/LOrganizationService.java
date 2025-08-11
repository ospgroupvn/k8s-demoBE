package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationRequest;
import com.osp.bttp.dao.model.dto.response.db4.DetailLOrganizationResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListLOrganizationResponse;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:07 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LOrganizationService {

    ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationResponse>>> getOrganizations(@Valid GetListLOrganizationRequest request);

    ResponseEntity<ApiResponseV1<DetailLOrganizationResponse>> getOrganizationById(Long orgId);

    LOrganization findById(Long organizationId);

    ResponseEntity<ApiResponseV1<?>> reportDashboard(Long cityCode, String fromDate, String toDate);
}
