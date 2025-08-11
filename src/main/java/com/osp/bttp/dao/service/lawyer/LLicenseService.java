package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.request.db4.CreateDocChangeRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLicenseRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLicOfLawRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLicenseResponse;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author sangnk
 * @Created 15/03/2025 - 9:45 SA
 * @project = bttp
 * @_ Mô tả:
 */

public interface LLicenseService {
    public List<LLicense> getAllLicenses();

    public Optional<LLicense> getLicenseById(Long licenseId);

    public List<LLicense> findLicenseByLicNumber(String licNumber);

    public List<LLicense> getLicensesByOwnerAndTypeOwnerAndLoai(Long ownerId, Integer typeOwner, Integer licenseType, Integer status);

    public List<LLicense> getLicensesByOwnerAndTypeOwner(Long ownerId, Integer typeOwner, Integer licenseType);
    public LLicense createLicense(LLicense license);


    public LLicense updateLicense(Long licenseId, LLicense updatedLicense);

    public void deleteLicense(Long licenseId);

    ResponseEntity<ApiResponseV1<PagingResult<GetListLLicenseResponse>>> getPage(@Valid GetListLLicenseRequest request);

     ResponseEntity<ApiResponseV1<List<LLicense>>> getListLicOfLaw(GetListLicOfLawRequest request);
    public ResponseEntity<ApiResponseV1<LLicense>> createLicOfLawCSDL(Integer isDomestic, LLicense license);

    public  ResponseEntity<ApiResponseV1<LLicense>>  updateLicOfCSDLCSDL(Integer isDomestic,LLicense license);

    public void deleteLicCSDL(Long idLic);
    public ResponseEntity<ApiResponseV1<?>> deleteDocument(Long idDoc);

    public ResponseEntity<ApiResponseV1<LLicense>> addDocument( CreateDocChangeRequest request);

    public ResponseEntity<ApiResponseV1<LLicense>> updateDocument(CreateDocChangeRequest license);
}
