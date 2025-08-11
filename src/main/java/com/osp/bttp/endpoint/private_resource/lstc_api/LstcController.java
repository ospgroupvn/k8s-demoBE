package com.osp.bttp.endpoint.private_resource.lstc_api;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLicOfLawRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerCSDLResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetLawyerLegalRep;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerCSDLResponse;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawActive;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.service.lawyer.LLawyerCSDLService;
import com.osp.bttp.dao.service.lawyer.LLicenseService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/api/private/lstc")
@Secured({ConstantAuthor.SYSTEM.system})
public class LstcController {
    @Autowired
    private LLawyerCSDLService lawyerService;

    @Autowired
    private LLicenseService lLicenseService;
    // M5: Danh sách luật sư trong nước
    @PostMapping("/getPage")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerCSDLResponse>>> getLawyers(@RequestBody @Valid GetListLLawyerCSDLRequest request) {
        return lawyerService.getLawyerCSDLs(request);
    }

    // M6, M8: Chi tiết luật sư (trong nước hoặc nước ngoài)
    @GetMapping("/{lawyerId}")
    public ResponseEntity<ApiResponseV1<DetailLLawyerCSDLResponse>> getLawyerById(@PathVariable Long lawyerId) {
        return lawyerService.getLawyerById(lawyerId);
    }

    @PostMapping("/edit")
    public ResponseEntity<ApiResponseV1<?>> editLawyerById(@RequestBody DetailLLawyerCSDLResponse requestLawyer) {
        return lawyerService.editLawyer(requestLawyer);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseV1<?>> addLawyer(@RequestParam Integer isDomestic,@RequestBody @Valid LLawyer lawyer) {
        return lawyerService.addLawyer(isDomestic,lawyer);
    }


    @GetMapping("/delete/{idLaw}")
    public ResponseEntity<ApiResponseV1<?>> addLawyer(@PathVariable("idLaw") Long idLaw) {
        return lawyerService.deleteLawyer(idLaw);
    }

    @PostMapping("/lic")
    public ResponseEntity<ApiResponseV1<List<LLicense>>> getListLicOfLaw(@RequestBody GetListLicOfLawRequest request) {
        return lLicenseService.getListLicOfLaw(request);
    }

    @GetMapping("/law-rep-unique")
    public ResponseEntity<ApiResponseV1<List<GetLawyerLegalRep>>> getListLawyerRepUnique() {
        return lawyerService.getListLawyerRepUnique();
    }

    @PostMapping("/report/law-ccnh")
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawCCHN>>> getReportLawCCHN(@RequestBody @Valid GetListLLawyerCSDLRequest request){
        return lawyerService.getReportLawCCHN(request);
    }

    @PostMapping("/report/law-active")
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawActive>>> getReportLawActive(@RequestBody @Valid GetReportOrgActive request){
        return lawyerService.getReportLawActive(request);
    }

    @PostMapping("/report/law-cchn/excel")
    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawCCHN(@RequestBody @Valid GetListLLawyerCSDLRequest request , HttpServletResponse response) {
        return lawyerService.getExportExcelLawCCHN(request, response);
    }

    @PostMapping("/report/law-active/excel")
    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawActive(@RequestBody @Valid GetReportOrgActive request , HttpServletResponse response) {
        return lawyerService.getExportExcelLawActive(request, response);
    }

}
