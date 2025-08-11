package com.osp.bttp.endpoint.private_resource.lstc_api;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLawyerInOrgRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.*;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawOrgArea;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportOrgActive;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.entity.db4.LOrganizationBranch;
import com.osp.bttp.dao.service.lawyer.LLawyerCSDLService;
import com.osp.bttp.dao.service.lawyer.LOrganizationCSDLService;
import com.osp.bttp.dao.service.lawyer.impl.LOrganizationCSDLServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/private/lorg")
@Secured({ConstantAuthor.SYSTEM.system})
public class LOrgController {
    @Autowired
    private LOrganizationCSDLService lOrganizationCSDLService;

    @PostMapping("/getPage")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationCSDLResponse>>> getListOrg(@RequestBody GetListLOrganizationCSDLRequest request) {
        return lOrganizationCSDLService.getOrganizations(request);
    }

    // M6, M8: Chi tiết luật sư (trong nước hoặc nước ngoài)
    @GetMapping("/{idOrg}")
    public ResponseEntity<ApiResponseV1<DetailLOrganizationCSDLResponse>> getDetailOrg(@PathVariable("idOrg") Long idOrg) {
        return lOrganizationCSDLService.getOrganizationById(idOrg);
    }

    @PostMapping("/law")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLawyerInOrg>>> getListLawInOrg(@RequestBody GetListLawyerInOrgRequest request) {
        return lOrganizationCSDLService.getListLawyerInOrg(request);
    }

    @PostMapping("law/add")
    public ResponseEntity<ApiResponseV1<List<Long>>> addLawToOrg(@RequestBody List<Long> idLaws, @RequestParam Long idOrg) {
        return lOrganizationCSDLService.addLawtoOrg(idLaws, idOrg);
    }

    @PostMapping("law/remove")
    public ResponseEntity<ApiResponseV1<?>> removeLawInOrg(@RequestParam Long idLaw, @RequestParam Long idOrg) {
        return lOrganizationCSDLService.removeLawInOrg(idLaw, idOrg);
    }

    @PostMapping("org/add")
    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> addBranchToOrg(@RequestBody LOrganizationBranch lOrganizationBranch) {
        return lOrganizationCSDLService.addBranch(lOrganizationBranch);
    }

    @PostMapping("org/update")
    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> updateBranchInOrg(@RequestBody LOrganizationBranch lOrganizationBranch) {
        return lOrganizationCSDLService.updateBranch(lOrganizationBranch);
    }

    @PostMapping("org/branch/{id}")
    public ResponseEntity<ApiResponseV1<List<GetListOrgBranchResponse>>> getListBranchInOrg(@PathVariable("id") Long id) {
        return lOrganizationCSDLService.getListBranchInOrg(id);
    }

    @PostMapping("org/remove/{idBranch}")
    public ResponseEntity<ApiResponseV1<?>> removeBranchInOrg(@PathVariable("idBranch") Long idBranch) {
        return lOrganizationCSDLService.removeBranchInOrg(idBranch);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseV1<LOrganization>> addOrganization(@RequestBody @Valid LOrganization lOrganization) {
        return lOrganizationCSDLService.addOrganization(lOrganization);
    }

    @PostMapping("/edit")
    public ResponseEntity<ApiResponseV1<LOrganization>> editOrganization(@RequestBody DetailLOrganizationCSDLResponse response) {
        return lOrganizationCSDLService.editOrganization(response);
    }

    @PostMapping("/delete/{idOrg}")
    public ResponseEntity<ApiResponseV1<?>> deleteOrganization(@PathVariable("idOrg") Long idOrg) {
        return lOrganizationCSDLService.deleteOrganization(idOrg);
    }

    @PostMapping("/report/area")
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawOrgArea>>> reportLOrg(@RequestBody List<Long> provinceIds, @RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam Integer isOrg) {
        return lOrganizationCSDLService.getReportOrgArea(provinceIds, pageNum, pageSize, isOrg);
    }

    @PostMapping("/report/area/excel")
    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgArea(@RequestBody List<Long> provinceIds, @RequestParam Integer isOrg, HttpServletResponse response) {
        return lOrganizationCSDLService.exportExcelLOrgArea(provinceIds, isOrg, response);
    }

    @PostMapping("/report/org-active")
    public ResponseEntity<ApiResponseV1<PagingResult<ReportOrgActive>>> reportOrgActive(@RequestBody GetReportOrgActive reportOrgActive) {
        return lOrganizationCSDLService.getReportOrgActive(reportOrgActive);

    }

    @PostMapping("/report/org-active/excel")
    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgActive(@RequestBody GetReportOrgActive reportOrgActive, HttpServletResponse response) {
        return lOrganizationCSDLService.exportExcelLOrgActive(reportOrgActive, response);
    }
}
