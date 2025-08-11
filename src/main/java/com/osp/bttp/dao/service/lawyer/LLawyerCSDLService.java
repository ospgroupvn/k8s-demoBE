package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.*;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawActive;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LLawyerCSDLService {
    ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerCSDLResponse>>> getLawyerCSDLs(GetListLLawyerCSDLRequest request);

     ResponseEntity<ApiResponseV1<DetailLLawyerCSDLResponse>> getLawyerById(Long lawyerId);

    DetailLLawyerCSDLResponse getDetail(Long lawyerId);
    ResponseEntity<ApiResponseV1<?>> editLawyer(DetailLLawyerCSDLResponse detailLLawyerCSDLResponse);

    public ResponseEntity<ApiResponseV1<?>> addLawyer(Integer isDomestic, LLawyer lawyerNew);


    public ResponseEntity<ApiResponseV1<?>> deleteLawyer(Long idLaw);

    public ResponseEntity<ApiResponseV1<List<GetLawyerLegalRep>>> getListLawyerRepUnique();
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawCCHN>>> getReportLawCCHN(GetListLLawyerCSDLRequest request);

    ResponseEntity<ApiResponseV1<PagingResult<ReportLawActive>>> getReportLawActive(GetReportOrgActive request);

    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawCCHN( GetListLLawyerCSDLRequest request, HttpServletResponse response);

    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawActive(GetReportOrgActive request , HttpServletResponse response);
}
