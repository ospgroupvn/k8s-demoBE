package com.osp.bttp.endpoint.private_resource.lstc_api;

import com.osp.bttp.common.contants.ConstantAuthor;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.dto.request.db4.CreateDocChangeRequest;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.service.lawyer.LLicenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/private/lic")
@Secured({ConstantAuthor.SYSTEM.system})
public class LLicController {
    @Autowired
    private LLicenseService lLicenseService;

    @PostMapping("/update")
    public  ResponseEntity<ApiResponseV1<LLicense>>  updateLic(@RequestParam Integer isDomestic,@RequestBody LLicense license) {
        return lLicenseService.updateLicOfCSDLCSDL(isDomestic,license);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponseV1<LLicense>> addLic(@RequestParam Integer isDomestic,@RequestBody LLicense license) {
      return   lLicenseService.createLicOfLawCSDL(isDomestic,license);
    }

    @GetMapping("/delete/{id}")
    public void deleteLic(@PathVariable("id") Long id) {
        lLicenseService.deleteLicCSDL(id);
    }

    @PostMapping("/doc/add")
    public  ResponseEntity<ApiResponseV1<LLicense>>  addDocToOrg(@RequestBody CreateDocChangeRequest request) {
        return lLicenseService.addDocument(request);
    }

    @PostMapping("/doc/edit")
    public  ResponseEntity<ApiResponseV1<LLicense>>  editDocInOrg(@RequestBody CreateDocChangeRequest request) {
        return lLicenseService.updateDocument(request);
    }

    @PostMapping("/doc/delete/{id}")
    public  ResponseEntity<ApiResponseV1<?>>  deleteDocInOrg(@PathVariable("id") Long id) {
        return lLicenseService.deleteDocument(id);
    }

}
