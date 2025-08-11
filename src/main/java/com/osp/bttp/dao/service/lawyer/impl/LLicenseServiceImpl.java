package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.contants.ConstantsLawyer;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.db4.CreateDocChangeRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLicenseRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLicOfLawRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLicenseResponse;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.model.entity.db4.LLicenseChange;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.validator.LLicValidator;
import com.osp.bttp.dao.repository.db4.LLicenseChangeRepository;
import com.osp.bttp.dao.repository.db4.LLicenseRepository;
import com.osp.bttp.dao.service.lawyer.LLicenseChangeService;
import com.osp.bttp.dao.service.lawyer.LLicenseService;
import com.osp.bttp.dao.service.lawyer.LOrganizationService;
import io.swagger.v3.oas.models.info.License;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 15/03/2025 - 9:47 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Service
public class LLicenseServiceImpl implements LLicenseService {
    @Autowired
    private LLicenseRepository licenseRepository;

    @Autowired
    private LLicenseChangeService licenseChangeService;

    @Autowired
    private LOrganizationService lOrganizationService;
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;


    @Override
    public List<LLicense> getAllLicenses() {
        return licenseRepository.findAll();
    }

    @Override
    public Optional<LLicense> getLicenseById(Long licenseId) {
        return licenseRepository.findById(licenseId);
    }

    @Override
    public List<LLicense> findLicenseByLicNumber(String licNumber) {
        return licenseRepository.findByLicenseNumber(licNumber);
    }

    @Override
    public List<LLicense> getLicensesByOwnerAndTypeOwnerAndLoai(Long ownerId, Integer typeOwner, Integer licenseType, Integer status) {
        return licenseRepository.findByOwnerIdAndOwnerTypeAndLicenseTypeAndStatus(ownerId, typeOwner, licenseType, status);
    }

    @Override
    public List<LLicense> getLicensesByOwnerAndTypeOwner(Long ownerId, Integer typeOwner, Integer licenseType) {
        return licenseRepository.findByOwnerIdAndOwnerTypeAndLicenseType(ownerId, typeOwner, licenseType);
    }

    @Override
    public LLicense createLicense(LLicense license) {
        return licenseRepository.save(license);
    }

    @Override
    public LLicense updateLicense(Long licenseId, LLicense updatedLicense) {
        return licenseRepository.findById(licenseId)
                .map(existing -> {
                    existing.setLicenseNumber(updatedLicense.getLicenseNumber());
                    existing.setDecisionNumber(updatedLicense.getDecisionNumber());
                    existing.setIssueDate(updatedLicense.getIssueDate());
                    existing.setRevocationDate(updatedLicense.getRevocationDate());
                    existing.setPracticeForm(updatedLicense.getPracticeForm());
                    existing.setStatus(updatedLicense.getStatus());
                    existing.setChangeContent(updatedLicense.getChangeContent());
                    existing.setTypeChange(updatedLicense.getTypeChange());
                    return licenseRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("License not found"));
    }

    @Override
    public void deleteLicense(Long licenseId) {
        licenseRepository.deleteById(licenseId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLicenseResponse>>> getPage(GetListLLicenseRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLLicenseResponse> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder(" ");
            Map<String, Object> params = new HashMap<>();

            if (request.getOwnerType() != null) {
                where.append(" AND l.OWNER_TYPE LIKE :ownerType ");
                params.put("ownerType", request.getOwnerType());
            }
            if (request.getOwnerId() != null) {
                where.append(" AND l.OWNER_ID = :ownerId ");
                params.put("ownerId", request.getOwnerId());
            }
            if (request.getLicenseType() != null) {
                where.append(" AND l.LICENSE_TYPE = :licenseType ");
                params.put("licenseType", request.getLicenseType());
            }


            StringBuilder sql = new StringBuilder("");
            sql.append("SELECT l.OWNER_ID, l.OWNER_TYPE, l.LICENSE_TYPE, l.LICENSE_NUMBER, l.PRACTICE_PLACE, l.PRACTICE_FORM, l.STATUS" +
                    " FROM LICENSES l " +
                    "WHERE 1 = 1 ");
            sql.append(where);

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());


            // Đặt tham số động cho truy vấn
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }

            // Phân trang
            int pageNumber = request.getPageNumber() > 0 ? request.getPageNumber() - 1 : 0; // Spring Boot đếm từ 0
            int numberPerPage = request.getNumberPerPage() > 0 ? request.getNumberPerPage() : 10; // Mặc định 10
            Query queryGetTotal = query;
            Long totalRecords = ((Number) queryGetTotal.getResultList().size()).longValue();

            query.setFirstResult(pageNumber * numberPerPage);
            query.setMaxResults(numberPerPage);

            // Thực thi truy vấn lấy danh sách
            List<Object[]> resultList = query.getResultList();
            List<GetListLLicenseResponse> responseList = resultList.stream().map(row -> new GetListLLicenseResponse(
                    H.isTrue(row[0]) ? Long.parseLong(row[0].toString()) : null, // OWNER_ID
                    H.isTrue(row[1]) ? Integer.parseInt(row[1].toString()) : null, // OWNER_TYPE
                    H.isTrue(row[2]) ? Integer.parseInt(row[2].toString()) : null, // LICENSE_TYPE
                    H.isTrue(row[3]) ? row[3].toString() : null, // LICENSE_NUMBER
                    H.isTrue(row[4]) ? row[4].toString() : null, // PRACTICE_PLACE
                    H.isTrue(row[5]) ? Integer.parseInt(row[5].toString()) : null, // PRACTICE_FORM
                    H.isTrue(row[6]) ? Integer.parseInt(row[6].toString()) : null // STATUS
            )).collect(Collectors.toList());
            pagingResult.setItems(responseList);

            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh sách", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LLicense>> createLicOfLawCSDL(Integer isDomestic, LLicense license) {
        String erros = LLicValidator.validatorLic(isDomestic, license);
        StringBuilder errosAdd = new StringBuilder(erros);
        try {
            String licNumber = license.getLicenseNumber();
            List<LLicense> lLicenseNum = licenseRepository.findByLicenseNumber(licNumber);
            if (!lLicenseNum.isEmpty()) throw new CustomException("Số thẻ đã tồn tại ," + erros);
            List<LLicense> licensePresents = null;
            if (isDomestic == 1) {
                licensePresents = licenseRepository.
                        findByOwnerIdAndOwnerTypeAndLicenseTypeAndStatus
                                (license.getOwnerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.THE_LS, ConstantsLawyer.LICENSE_STATUS.THE_LS.DA_CAP_THE_LS);

                if (!licensePresents.isEmpty()) {
                    errosAdd.append("Thẻ hiện tại đang hoạt động , không được thêm mới");
                }

            } else {
                licensePresents = licenseRepository.findByOwnerIdAndOwnerTypeAndLicenseType
                                (license.getOwnerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.GPHN)
                        .stream()
                        .sorted((a, b) -> b.getLicenseId().compareTo(a.getLicenseId())) // Sắp xếp theo Id mới nhất
                        .toList();
                if (!licensePresents.isEmpty()) {
                    LLicense lLicenseGPHN = licensePresents.get(0);
                    Integer countChange = lLicenseGPHN.getCountChange() +1;
                    if(license.getLicenseType().equals(ConstantsLawyer.LICENSE_STATUS.GPHN.GIA_HAN))
                    {
                        license.setCountChange(countChange);
                    }else {
                        license.setCountChange(0);
                    }
                    if (lLicenseGPHN.getStatus().equals(ConstantsLawyer.LICENSE_STATUS.GPHN.THU_HOI)) {
                        if (!license.getStatus().equals(ConstantsLawyer.LICENSE_STATUS.GPHN.DA_CAP)) {
                            errosAdd.append("GPHN cũ đã thu hồi thi trạng thái mới phải là đã cấp \n");
                        }
                    }
                }else {
                    license.setCountChange(1);
                }
            }
            if (!errosAdd.isEmpty()) throw new CustomException("Đã có lỗi khi thêm thẻ " + errosAdd.toString());
            LLicense licenseNew = createLicense(license);
            license.setLicenseId(licenseNew.getLicenseId());
            licenseChangeService.insertLicChange(license);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm thẻ luật sư thành công", licenseNew), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Thêm thẻ luật sư thất bại");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LLicense>> updateLicOfCSDLCSDL(Integer isDomestic, LLicense license) {
        String erros = LLicValidator.validatorLic(isDomestic, license);
        try {

            Optional<LLicense> optionalLLicense = licenseRepository.findById(license.getLicenseId());
            if (optionalLLicense.isEmpty()) throw new CustomException("Không tìm thấy thẻ , " + erros);

            licenseChangeService.insertLicChange(license);
            LLicense licenseUpdate = updateLicense(license.getLicenseId(), license);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Cập nhật thẻ luật sư thành công", licenseUpdate), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Cập nhật thẻ luật sư thất bại");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLicCSDL(Long idLic) {
        try {
            deleteLicense(idLic);
            licenseChangeService.deleteLicChange(idLic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public ResponseEntity<ApiResponseV1<List<LLicense>>> getListLicOfLaw(GetListLicOfLawRequest request) {
        Long idLaw = request.getIdLaw();
        Integer isDominic = request.getIsDomestic();
        List<LLicense> licensePresents = null;
        try {

            if (isDominic == 1) {
                licensePresents = licenseRepository.
                        findByOwnerIdAndOwnerTypeAndLicenseType
                                (idLaw, ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.THE_LS);

            } else {
                licensePresents = licenseRepository.findByOwnerIdAndOwnerTypeAndLicenseType
                        (idLaw, ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.GPHN);
            }
            licensePresents = licensePresents.stream()
                    .sorted((a, b) -> b.getLicenseId().compareTo(a.getLicenseId())) // Sắp xếp theo Id mới nhất
                    .toList();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách thẻ luật thành công", licensePresents), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Danh sách thẻ luật sư không hợp lệ", null), HttpStatus.OK);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> deleteDocument(Long idDoc) {
        try {
            licenseRepository.deleteById(idDoc);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Xóa thành công", idDoc), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi xóa");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LLicense>> addDocument(CreateDocChangeRequest request) {
        try {
            LLicense license = new LLicense();
            license.setOwnerType(request.getOwnerType());
            if (request.getIsDomestic() == 1) {
                license.setLicenseType(ConstantsLawyer.LICENSE_TYPE.DKHD);
            } else {
                license.setLicenseType(ConstantsLawyer.LICENSE_TYPE.GPTL);
            }
            license.setIssueDate(request.getIssueDate());
            license.setChangeContent(request.getChangeContent());
            LOrganization organization = lOrganizationService.findById(request.getOwnerId());
            if (organization == null) throw new CustomException("Không tìm thấy tổ chức");
            license.setOwnerId(request.getOwnerId());
            if(organization.getIsDomestic()==1)
            {
                license.setLicenseNumber(organization.getBusinessLicenseNumber());
            }else {
                license.setLicenseNumber(organization.getRegistrationLicenseNumber());
                license.setTypeChange(request.getTypeOrgChange());

            }
            license.setApprovalDocument(request.getVbChange());

            LLicense licenseNew = createLicense(license);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm doc thành công", licenseNew), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Lỗi thêm mới văn bản thay đổi");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LLicense>> updateDocument(CreateDocChangeRequest request) {
        try {
            Optional<LLicense> licenseExit = licenseRepository.findById(request.getIdLic());
            if (licenseExit.isEmpty()) throw new CustomException("Không tìm thấy document");
            licenseExit.get().setOwnerType(request.getOwnerType());
            if (request.getIsDomestic() == 1) {
                licenseExit.get().setLicenseType(ConstantsLawyer.LICENSE_TYPE.DKHD);
            } else {
                licenseExit.get().setLicenseType(ConstantsLawyer.LICENSE_TYPE.GPTL);
                licenseExit.get().setTypeChange(request.getTypeOrgChange());
            }
            licenseExit.get().setIssueDate(request.getIssueDate());
            licenseExit.get().setChangeContent(request.getChangeContent());
            licenseExit.get().setApprovalDocument(request.getVbChange());
            licenseExit.get().setOwnerId(request.getOwnerId());
            LLicense licenseUpdate = updateLicense(licenseExit.get().getLicenseId(), licenseExit.get());
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "update thành công", licenseUpdate), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("lỗi update");
        }
    }
}
