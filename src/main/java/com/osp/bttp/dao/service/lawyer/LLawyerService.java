package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerResponse;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerCSDLResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerResponse;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import org.springframework.http.ResponseEntity;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:08 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerService {

    ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerResponse>>> getLawyers(GetListLLawyerRequest request);

    ResponseEntity<ApiResponseV1<DetailLLawyerResponse>> getLawyerById(Long lawyerId);

    DetailLLawyerResponse getDetail(Long lawyerLegalRepresentativeId);

    LLawyer findById(Long lawyerLegalRepresentativeId);

    ResponseEntity<ApiResponseV1<?>> reportDashboard(Long cityCode, String fromDate, String toDate);
}
