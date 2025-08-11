package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLawyerInOrgRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.*;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawOrgArea;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportOrgActive;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.entity.db4.LOrganizationBranch;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:07 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LOrganizationCSDLService {

    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationCSDLResponse>>> getOrganizations(@Valid GetListLOrganizationCSDLRequest request);
    ResponseEntity<ApiResponseV1<DetailLOrganizationCSDLResponse>> getOrganizationById(Long orgId);

    LOrganization findById(Long organizationId);


    public ResponseEntity<ApiResponseV1<PagingResult<GetListLawyerInOrg>>> getListLawyerInOrg(GetListLawyerInOrgRequest request);


    ResponseEntity<ApiResponseV1<List<Long>>> addLawtoOrg(List<Long> ids, Long idOrg);

    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> addBranch(LOrganizationBranch organizationBranch);

    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> updateBranch(LOrganizationBranch organizationBranch);
    public ResponseEntity<ApiResponseV1<List<GetListOrgBranchResponse>>> getListBranchInOrg(Long idOrg);

    ResponseEntity<ApiResponseV1<?>> removeBranchInOrg(Long idBranch);

    ResponseEntity<ApiResponseV1<?>> removeLawInOrg(Long idLaw,Long idOrg) ;

    public ResponseEntity<ApiResponseV1<LOrganization>> editOrganization(DetailLOrganizationCSDLResponse response);

    public ResponseEntity<ApiResponseV1<LOrganization>> addOrganization(LOrganization organizationNew);

    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawOrgArea>>> getReportOrgArea(List<Long> provinceId, Integer pageNum, Integer numPerPage, Integer isOrg); //1 org 0 law

    public ResponseEntity<ApiResponseV1<?>> deleteOrganization(Long idOrg);

    public ResponseEntity<ApiResponseV1<PagingResult<ReportOrgActive>>> getReportOrgActive(GetReportOrgActive reportOrgActive);

    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgArea(List<Long> provinceIds, Integer isOrg, HttpServletResponse response) ;

    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgActive(GetReportOrgActive reportOrgActive, HttpServletResponse response);

}
