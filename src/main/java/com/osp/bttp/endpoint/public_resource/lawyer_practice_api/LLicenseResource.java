package com.osp.bttp.endpoint.public_resource.lawyer_practice_api;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLicenseRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLicenseResponse;
import com.osp.bttp.dao.service.lawyer.LLicenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sangnk
 * @Created 15/03/2025 - 12:33 CH
 * @project = bttp
 * @_ Mô tả:
 */
@RestController
@RequestMapping("/v1/api/lawyer-license")
public class LLicenseResource {

    @Autowired
    private LLicenseService lLicenseService;

    @PostMapping("/getPage")
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLicenseResponse>>> getDomesticLawyers(@RequestBody @Valid GetListLLicenseRequest request) {
        return lLicenseService.getPage(request);
    }

}
