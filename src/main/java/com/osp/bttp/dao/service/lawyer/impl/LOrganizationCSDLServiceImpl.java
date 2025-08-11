package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.contants.ConstantsLawyer;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.GetListLawyerInOrgRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.*;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawOrgArea;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportOrgActive;
import com.osp.bttp.dao.model.entity.db4.*;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import com.osp.bttp.dao.repository.db4.*;
import com.osp.bttp.dao.service.bttp.LogSystemService;
import com.osp.bttp.dao.service.lawyer.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:08 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@RequiredArgsConstructor
public class LOrganizationCSDLServiceImpl implements LOrganizationCSDLService {
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;

    private final LOrganizationRepository lOrganizationRepository;

    private final LLawyerService lLawyerService;

    private final LCategoryRepository lCategoryRepository;

    private final LLawyerRepository lLawyerRepository;

    private final LLicenseRepository lLicenseRepository;

    private final LLawyerAssociationService lawyerAssociationService;

    private final LOrganizationBranchRepository lOrganizationBranchRepository;

    private final LogSystemService logSystemService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationCSDLResponse>>> getOrganizations(@Valid GetListLOrganizationCSDLRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLOrganizationCSDLResponse> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder(" ");
            Map<String, Object> params = new HashMap<>();

            if (request.getOrgName() != null) {
                where.append(" AND o.ORG_NAME LIKE :orgName ");
                params.put("orgName", "%" + request.getOrgName() + "%");
            }
            if (request.getIsDomestic() != null) {
                where.append(" AND o.IS_DOMESTIC =:listIsDomestic ");
                params.put("listIsDomestic", request.getIsDomestic());
            }
            if (request.getListStatus() != null && !request.getListStatus().isEmpty()) {
                where.append(" AND o.ACTIVITY_STATUS IN (:listStatus) ");
                params.put("listStatus", request.getListStatus());
            }
            if (H.isTrue(request.getProvinceId())) {
                where.append(" AND o.PROVINCE_ID = :provinceId ");
                params.put("provinceId", request.getProvinceId());
            }
            if (H.isTrue(request.getParentId())) {
                where.append(" AND o.PARENT_ORG_ID = :parentId ");
                params.put("parentId", request.getParentId());
            }

            StringBuilder sqlTemp = new StringBuilder("");

            StringBuilder sql = new StringBuilder(sqlTemp);
            sql.append("SELECT o.ORG_NAME, o.ADDRESS,n.NATIONALITY_NAME, representative.FULL_NAME, o.registration_license_number \n" +
                    " , o.business_license_number,o.PHONE, o.STATUS, o.business_license_issue_date, o.registration_license_issue_date ,o.ORG_ID,o.gen_date \n" +
                    "FROM ORGANIZATIONS o " +
                    "LEFT JOIN LAWYERS representative ON o.lawyer_legal_representative_id = representative.LAWYER_ID \n" +
                    "LEFT JOIN NATIONALITIES n ON n.NATIONALITY_ID = o.NATIONALITY_ID \n" +
                    "WHERE 1 = 1 ");
            sql.append(where);
            sql.append(" ORDER BY o.ORG_ID ASC");

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
            List<GetListLOrganizationCSDLResponse> responseList = resultList.stream().map(row -> new GetListLOrganizationCSDLResponse(
                    H.isTrue(row[10]) ? ((Number) row[10]).longValue() : null, // orgId
                    H.isTrue(row[0]) ? row[0].toString() : null, // orgName
                    H.isTrue(row[1]) ? row[1].toString() : null, // address
                    H.isTrue(row[2]) ? row[2].toString() : null, // nationalName
                    H.isTrue(row[3]) ? row[3].toString() : null, // lawyerLegalRepresentative
                    H.isTrue(row[4]) ? row[4].toString() : null, // registrationNumber
                    H.isTrue(row[5]) ? row[5].toString() : null, // businessLicenseNumber
                    H.isTrue(row[6]) ? (row[6]).toString() : null, // phone
                    H.isTrue(row[7]) ? ((Number) row[7]).intValue() : null, // status
                    H.isTrue(row[8]) ? (Date) row[8] : null, // businessLicenseIssueDate
                    H.isTrue(row[9]) ? (Date) row[9] : null, // registrationLicenseIssueDate
                    H.isTrue(row[11]) ? (Date) row[11] : null// genDate
            )).collect(Collectors.toList());
            pagingResult.setItems(responseList);

            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách tổ chức thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh sách tổ chức", null), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<ApiResponseV1<DetailLOrganizationCSDLResponse>> getOrganizationById(Long orgId) {
        try {
            LOrganization infoOrg = lOrganizationRepository.findById(orgId).orElse(null);
            DetailLOrganizationCSDLResponse detailLOrganizationResponse = new DetailLOrganizationCSDLResponse(infoOrg);
            GetLawyerLegalRep lawyerLegalRep = lLawyerRepository.getLawyerLegalRep(infoOrg.getLawyerLegalRepresentativeId());
            if (lawyerLegalRep != null) {
                detailLOrganizationResponse.setLawyerLegalRep(lawyerLegalRep);
            }
            if (infoOrg.getIsDomestic() == 1) {
                LLawyerAssociation lawyerAssociation = lawyerAssociationService.findById(infoOrg.getLawyerAssociationId());
                if (lawyerAssociation != null) {
                    detailLOrganizationResponse.setLawyerAssName(lawyerAssociation.getAssocName());
                }
            }

            List<GetListOrgBranchResponse> branchOrgList = lOrganizationBranchRepository.getListOrgBranch(infoOrg.getOrgId());
            if (branchOrgList != null) {
                detailLOrganizationResponse.setBranchOrgList(branchOrgList);
            }
            List<GetListRegisOfOrgChange> regisOfOrgChanges = null;
            if (infoOrg.getIsDomestic() == 1) {
                regisOfOrgChanges = lLicenseRepository.getListRegisOfOrgChange(infoOrg.getOrgId(), ConstantsLawyer.LICENSE_TYPE.DKHD);
            } else {
                regisOfOrgChanges = lLicenseRepository.getListRegisOfOrgChange(infoOrg.getOrgId(), ConstantsLawyer.LICENSE_TYPE.GPTL);
            }
            if (regisOfOrgChanges != null) {
                detailLOrganizationResponse.setGetListRegisOrgChange(regisOfOrgChanges);
            }
            if (infoOrg.getIsDomestic() == 1) {
                LCategory lCategoryProvince = lCategoryRepository.findById(infoOrg.getProvinceId()).orElse(null);
                String provinceName = lCategoryProvince.getName();
                String wardName = "";
                detailLOrganizationResponse.setProvinceName(lCategoryProvince.getName());
                if (infoOrg.getWardId() != null) {
                    LCategory lCategoryWard = lCategoryRepository.findById(infoOrg.getWardId()).orElse(null);
                    detailLOrganizationResponse.setWardName(lCategoryWard.getName());
                    wardName = lCategoryWard.getName();
                }
                String addressName = infoOrg.getAddress();
                if (StringUtils.hasText(wardName)) addressName += " - " + wardName;
                if (StringUtils.hasText(provinceName)) addressName += " - " + provinceName;
                detailLOrganizationResponse.setAddressName(addressName);
            }

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin tổ chức thành công", detailLOrganizationResponse), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Không tìm thấy tổ chức", null), HttpStatus.NOT_FOUND);
    }

    @Override
    public LOrganization findById(Long organizationId) {
        return lOrganizationRepository.findById(organizationId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLawyerInOrg>>> getListLawyerInOrg(GetListLawyerInOrgRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLawyerInOrg> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder(" ");
            Map<String, Object> params = new HashMap<>();

            if (request.getOrganizationId() != null) {
                where.append(" AND  l.organization_id = :organizationId ");
                params.put("organizationId", request.getOrganizationId());
            }

            StringBuilder sqlTemp = new StringBuilder("");

            StringBuilder sql = new StringBuilder(sqlTemp);

            if (request.getIsDominic() == 1) {
                sql.append("WITH LatestCard AS (\n" +
                        "         SELECT OWNER_ID, LICENSE_NUMBER ,ISSUE_DATE,PRACTICE_FORM, \n" +
                        "                ROW_NUMBER() over (PARTITION BY OWNER_ID ORDER BY LICENSE_ID DESC) AS rn\n" +
                        "         FROM LICENSES\n" +
                        "         WHERE OWNER_TYPE = 1 AND LICENSE_TYPE = 2 \n" +
                        ")\n ");
                sql.append("SELECT l.lawyer_id, l.full_name, l.date_of_birth, l.certificate_number, li.license_number, li.license_number, li.practice_form, l.activity_status \n" +
                        "from Lawyers l \n" +
                        "left join LatestCard li on li.owner_id = l.lawyer_id \n" +
                        "WHERE   li.rn = 1  and 1=1 ");
            } else {

                sql.append("SELECT l.lawyer_id, l.full_name, l.date_of_birth, l.certificate_number, null as license_number, null as license_number, null as practice_form, l.activity_status \n" +
                        "from Lawyers l \n" +
//                        "left join LatestCard li on li.owner_id = l.lawyer_id \n" +
//                        "left join LatestCardGPHN liGPHN on liGPHN.owner_id = l.lawyer_id \n" +
                        "WHERE   1=1 \n");
            }

            sql.append(where);
            sql.append(" ORDER BY l.lawyer_id ASC");

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
            long totalRecords = ((Number) queryGetTotal.getResultList().size()).longValue();

            query.setFirstResult(pageNumber * numberPerPage);
            query.setMaxResults(numberPerPage);

            // Thực thi truy vấn lấy danh sách
            List<Object[]> resultList = query.getResultList();
            List<GetListLawyerInOrg> responseList = resultList.stream().map(row -> new GetListLawyerInOrg(
                    H.isTrue(row[0]) ? ((Number) row[0]).longValue() : null, // orgId
                    H.isTrue(row[1]) ? row[1].toString() : null, // fullName
                    H.isTrue(row[2]) ? (Date) row[2] : null, // dob
                    H.isTrue(row[3]) ? row[3].toString() : null, // certificateNumber
                    H.isTrue(row[4]) ? row[4].toString() : null, // GPHN
                    H.isTrue(row[5]) ? row[5].toString() : null, // theLS
                    H.isTrue(row[6]) ? ((Number) row[6]).intValue() : null, // practiceForm
                    H.isTrue(row[7]) ? ((Number) row[7]).intValue() : null // status
            )).collect(Collectors.toList());
            pagingResult.setItems(responseList);
            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách luật sư trong tổ chức thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh luật sư trong tổ chức", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<List<Long>>> addLawtoOrg(List<Long> ids, Long idOrg) {
        List<LLawyer> lLawyers = new ArrayList<>();
        System.out.println(ids);
        try {
            for (Long id : ids) {
                Optional<LLawyer> lLawyerOptional = lLawyerRepository.findById(id);
                if (!lLawyerOptional.isPresent()) throw new CustomException("Không tìm thấy luật sư");
                else {
                    if (lLawyerOptional.get().getOrganizationId() != null) {
                        throw new CustomException("Một luật sư chỉ thuộc 1 tổ chức");
                    }
                    lLawyerOptional.get().setOrganizationId(idOrg);
                    lLawyers.add(lLawyerOptional.get());
                }
            }
            lLawyerRepository.saveAll(lLawyers);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm luật sư thành công", ids), HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException("Thêm luật sư vào tổ chức thất bại");
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<GetListOrgBranchResponse>>> getListBranchInOrg(Long idOrg) {
        try {
            List<GetListOrgBranchResponse> list = lOrganizationBranchRepository.getListOrgBranch(idOrg);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy ds chi nhánh thành công", list), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy chi nhánh", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> addBranch(LOrganizationBranch organizationBranch) {
        try {
            LOrganizationBranch lOrganizationBranch = new LOrganizationBranch();
            lOrganizationBranch.setAddress(organizationBranch.getAddress());
            lOrganizationBranch.setOrgId(organizationBranch.getOrgId());
            lOrganizationBranch.setPhone(organizationBranch.getPhone());
            lOrganizationBranch.setOrgName(organizationBranch.getOrgName());
            lOrganizationBranch.setBusinessLicenseNumber(organizationBranch.getBusinessLicenseNumber());
            lOrganizationBranch.setBusinessLicenseIssueDate(organizationBranch.getBusinessLicenseIssueDate());
            lOrganizationBranch.setLawyerLegalRepresentativeId(organizationBranch.getLawyerLegalRepresentativeId());
            LOrganizationBranch lOrganizationBranchNew = lOrganizationBranchRepository.save(lOrganizationBranch);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm chi nhánh thành công", lOrganizationBranchNew), HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi thêm chi nhánh");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LOrganizationBranch>> updateBranch(LOrganizationBranch organizationBranch) {
        try {
            Optional<LOrganizationBranch> lOrganizationBranchUpdate = lOrganizationBranchRepository.findById(organizationBranch.getOrgBranchId());
            if (lOrganizationBranchUpdate.isEmpty()) throw new CustomException("Không tìm thấy chi nhánh");
            lOrganizationBranchUpdate.get().setAddress(organizationBranch.getAddress());
            lOrganizationBranchUpdate.get().setOrgId(organizationBranch.getOrgId());
            lOrganizationBranchUpdate.get().setPhone(organizationBranch.getPhone());
            lOrganizationBranchUpdate.get().setOrgName(organizationBranch.getOrgName());
            lOrganizationBranchUpdate.get().setBusinessLicenseNumber(organizationBranch.getBusinessLicenseNumber());
            lOrganizationBranchUpdate.get().setBusinessLicenseIssueDate(organizationBranch.getBusinessLicenseIssueDate());
            lOrganizationBranchUpdate.get().setLawyerLegalRepresentativeId(organizationBranch.getLawyerLegalRepresentativeId());
            LOrganizationBranch lOrganizationBranchNew = lOrganizationBranchRepository.save(lOrganizationBranchUpdate.get());
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm chi nhánh thành công", lOrganizationBranchNew), HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi thêm chi nhánh");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> removeBranchInOrg(Long idBranch) {
        try {
            lOrganizationBranchRepository.deleteById(idBranch);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Xóa chi nhánh khỏi tổ chức thành công", idBranch), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Xóa chi nhánh thất bại");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> removeLawInOrg(Long idLaw, Long idOrg) {
        try {
            List<LLawyer> lLawyerOptional = lLawyerRepository.findAllByLawyerIdAndOrganizationId(idLaw, idOrg);
            if (lLawyerOptional.isEmpty()) throw new CustomException("Không tìm thấy luật sư tại chi nhánh");
            else {
                lLawyerOptional.get(0).setOrganizationId(null);
                LLawyer lawyerRemove = lLawyerRepository.save(lLawyerOptional.get(0));
                return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Xóa luật sư khỏi tổ chức thành công", lawyerRemove), HttpStatus.OK);

            }
        } catch (Exception e) {

            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi xóa luật sư khỏi tổ chức", null), HttpStatus.BAD_REQUEST);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LOrganization>> editOrganization(DetailLOrganizationCSDLResponse response) {
        try {
            Optional<LOrganization> lOrganization = lOrganizationRepository.findById(response.getOrgId());
            if (lOrganization.isEmpty()) {
                throw new CustomException("Không tìm thấy tổ chức");
            }
            LOrganization lOrganizationExit = lOrganization.get();
            if (response.getParentOrgId() != null) {
                lOrganizationExit.setParentOrgId(response.getParentOrgId());
            }
            if (response.getOrgName() != null) {
                lOrganizationExit.setOrgName(response.getOrgName());
            }
            if (response.getOrgType() != null) {
                lOrganizationExit.setOrgType(response.getOrgType());
            }
            if (response.getAddress() != null) {
                lOrganizationExit.setAddress(response.getAddress());
            }
            if (response.getLawyerLegalRepresentativeId() != null) {
                lOrganizationExit.setLawyerLegalRepresentativeId(response.getLawyerLegalRepresentativeId());
            }
            if (response.getPhone() != null) {
                lOrganizationExit.setPhone(response.getPhone());
            }
            if (response.getEmail() != null) {
                lOrganizationExit.setEmail(response.getEmail());
            }
            if (response.getIsDomestic() != null) {
                lOrganizationExit.setIsDomestic(response.getIsDomestic());
            }
            if (response.getBusinessLicenseNumber() != null) {
                lOrganizationExit.setBusinessLicenseNumber(response.getBusinessLicenseNumber());
            }
            if (response.getBusinessLicenseIssueDate() != null) {
                lOrganizationExit.setBusinessLicenseIssueDate(response.getBusinessLicenseIssueDate());
            }
            if (response.getRegistrationLicenseNumber() != null) {
                lOrganizationExit.setRegistrationLicenseNumber(response.getRegistrationLicenseNumber());
            }
            if (response.getRegistrationLicenseIssueDate() != null) {
                lOrganizationExit.setRegistrationLicenseIssueDate(response.getRegistrationLicenseIssueDate());
            }
            if (response.getStatus() != null) {
                lOrganizationExit.setStatus(response.getStatus());
            }
            if (response.getLawyerAssociationId() != null) {
                lOrganizationExit.setLawyerAssociationId(response.getLawyerAssociationId());
            }
            if (response.getProvinceId() != null) {
                lOrganizationExit.setProvinceId(response.getProvinceId());
            }
            if (response.getWardId() != null) {
                lOrganizationExit.setWardId(response.getWardId());
            }

            if (response.getNationalityId() != null) {
                lOrganizationExit.setNationalityId(response.getNationalityId());
            }
            LOrganization organizationUpdate = lOrganizationRepository.save(lOrganizationExit);
            logSystemService.saveLog(organizationUpdate.getOrgName(),organizationUpdate.getOrgId().toString(), ActionType.EDIT, GroupType.LAWYER, ActorType.ORG);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Sửa thông tin tổ chức thành công", lOrganizationExit), HttpStatus.OK);

        } catch (Exception e) {

            throw new CustomException("Đã xảy ra lỗi khi sửa tổ chức");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<LOrganization>> addOrganization(LOrganization organizationNew) {
        try {
            LOrganization lOrganizationExit = new LOrganization();
            Long idLaw = organizationNew.getLawyerLegalRepresentativeId();
            if (organizationNew.getParentOrgId() != null) {
                lOrganizationExit.setParentOrgId(organizationNew.getParentOrgId());
            }
            if (organizationNew.getOrgName() != null) {
                lOrganizationExit.setOrgName(organizationNew.getOrgName());
            }
            if (organizationNew.getOrgType() != null) {
                lOrganizationExit.setOrgType(organizationNew.getOrgType());
            }
            if (organizationNew.getAddress() != null) {
                lOrganizationExit.setAddress(organizationNew.getAddress());
            }
            if (organizationNew.getLawyerLegalRepresentativeId() != null) {

                lOrganizationExit.setLawyerLegalRepresentativeId(idLaw);
            }
            if (organizationNew.getPhone() != null) {
                lOrganizationExit.setPhone(organizationNew.getPhone());
            }
            if (organizationNew.getEmail() != null) {
                lOrganizationExit.setEmail(organizationNew.getEmail());
            }
            if (organizationNew.getIsDomestic() != null) {
                lOrganizationExit.setIsDomestic(organizationNew.getIsDomestic());
            }
            if (organizationNew.getBusinessLicenseNumber() != null) {
                lOrganizationExit.setBusinessLicenseNumber(organizationNew.getBusinessLicenseNumber());
            }
            if (organizationNew.getBusinessLicenseIssueDate() != null) {
                lOrganizationExit.setBusinessLicenseIssueDate(organizationNew.getBusinessLicenseIssueDate());
            }
            if (organizationNew.getRegistrationLicenseNumber() != null) {
                lOrganizationExit.setRegistrationLicenseNumber(organizationNew.getRegistrationLicenseNumber());
            }
            if (organizationNew.getRegistrationLicenseIssueDate() != null) {
                lOrganizationExit.setRegistrationLicenseIssueDate(organizationNew.getRegistrationLicenseIssueDate());
            }
            if (organizationNew.getStatus() != null) {
                lOrganizationExit.setStatus(organizationNew.getStatus());
            }
            if (organizationNew.getLawyerAssociationId() != null) {
                lOrganizationExit.setLawyerAssociationId(organizationNew.getLawyerAssociationId());
            }
            if (organizationNew.getProvinceId() != null) {
                lOrganizationExit.setProvinceId(organizationNew.getProvinceId());
            }
            if (organizationNew.getWardId() != null) {
                lOrganizationExit.setWardId(organizationNew.getWardId());
            }

            if (organizationNew.getNationalityId() != null) {
                lOrganizationExit.setNationalityId(organizationNew.getNationalityId());
            }

            LOrganization lOrganization = lOrganizationRepository.save(lOrganizationExit);
            // luật sư đại diện thuộc tổ chức
            addLawtoOrg(Collections.singletonList(idLaw), lOrganization.getOrgId());
            logSystemService.saveLog(lOrganization.getOrgName(),lOrganization.getOrgId().toString(), ActionType.ADD, GroupType.LAWYER, ActorType.PER);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm thông tin tổ chức thành công", lOrganizationExit), HttpStatus.OK);

        } catch (Exception e) {

            throw new CustomException("Đã xảy ra lỗi khi thêm tổ chức");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawOrgArea>>> getReportOrgArea(List<Long> provinceIds, Integer pageNum, Integer numPerPage, Integer isOrg) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<ReportLawOrgArea> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(pageNum);
            pagingResult.setNumberPerPage(numPerPage);

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("");
            Map<String, Object> params = new HashMap<>();

            if (!provinceIds.isEmpty()) {
                if (isOrg == 1) {
                    where.append(" AND  o.province_id in :provinceIds  \n");
                    params.put("provinceIds", provinceIds);
                } else {
                    where.append(" AND  l.province_id in :provinceIds  \n");
                    params.put("provinceIds", provinceIds);
                }
            }

            StringBuilder sqlTemp = new StringBuilder("");

            StringBuilder sql = new StringBuilder(sqlTemp);
            if (isOrg == 1) {
                sql.append("SELECT \n" +
                        "cc.ID ,\n" +
                        "    cc.NAME AS ProvinceName,\n" +
                        "    SUM(CASE WHEN o.IS_DOMESTIC = 1 THEN 1 ELSE 0 END) AS Trong_nuoc,\n" +
                        "    SUM(CASE WHEN o.IS_DOMESTIC = 0 THEN 1 ELSE 0 END) AS Ngoai_nuoc\n" +
                        "FROM ORGANIZATIONS o\n" +
                        "JOIN C_CATEGORY cc ON o.PROVINCE_ID = cc.ID\n" +
                        "WHERE 1 = 1 \n");

            } else {
                sql.append("SELECT \n" +
                        "cc.ID, \n" +
                        "    cc.NAME AS ProvinceName,\n" +
                        "    SUM(CASE WHEN l.IS_DOMESTIC = 1 THEN 1 ELSE 0 END) AS Trong_nuoc,\n" +
                        "    SUM(CASE WHEN l.IS_DOMESTIC = 0 THEN 1 ELSE 0 END) AS Ngoai_nuoc\n" +
                        "FROM LAWYERS l \n" +
                        "JOIN C_CATEGORY cc ON l.PROVINCE_ID = cc.ID\n" +
                        "Where 1=1 \n");

            }

            sql.append(where);
            sql.append("GROUP BY cc.ID,cc.NAME \n");
            sql.append(" ORDER BY cc.ID ASC");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());
            // Đặt tham số động cho truy vấn
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
            // Phân trang
            int pageNumber = pageNum > 0 ? pageNum - 1 : 0; // Spring Boot đếm từ 0
            int numberPerPage = numPerPage > 0 ? numPerPage : 10; // Mặc định 10
            Query queryGetTotal = query;
            long totalRecords = ((Number) queryGetTotal.getResultList().size()).longValue();

            query.setFirstResult(pageNumber * numberPerPage);
            query.setMaxResults(numberPerPage);

            AtomicInteger sumTotalIn = new AtomicInteger(0);
            AtomicInteger sumTotalOut = new AtomicInteger(0);
            // Thực thi truy vấn lấy danh sách
            List<Object[]> resultList = query.getResultList();
            List<ReportLawOrgArea> responseList = resultList.stream().map(row -> {
                int totalIn = H.isTrue(row[2]) ? ((Number) row[2]).intValue() : 0;
                int totalOut = H.isTrue(row[3]) ? ((Number) row[3]).intValue() : 0;

                sumTotalIn.addAndGet(totalIn);
                sumTotalOut.addAndGet(totalOut);

                return new ReportLawOrgArea(
                        H.isTrue(row[0]) ? ((Number) row[0]).longValue() : null, // idProvince
                        H.isTrue(row[1]) ? row[1].toString() : null,             // provinceName
                        totalIn,                                                 // totalIn
                        totalOut                                                 // totalOut
                );
            }).collect(Collectors.toList());

            pagingResult.setItems(responseList);

// Lấy tổng
            int totalInSum = sumTotalIn.get();
            int totalOutSum = sumTotalOut.get();
            responseList.add(0, new ReportLawOrgArea(0L, "Tổng số", totalInSum, totalOutSum));
            pagingResult.setItems(responseList);
            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));


            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách luật sư trong tổ chức thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi lấy danh luật sư trong tổ chức");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> deleteOrganization(Long idOrg) {

        try {
            // xóa văn ban lien quan
            lLicenseRepository.deleteAllByOwnerIdAndOwnerType(idOrg, ConstantsLawyer.TYPE_OWNER.ORG);
            // thay doi luat su trong to chuc
            lLawyerRepository.removeLaw(idOrg);
            // xoa to chuc
            lOrganizationRepository.deleteById(idOrg);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Xóa tổ chức thành công", idOrg), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Lỗi khi xóa tổ chức");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<ReportOrgActive>>> getReportOrgActive(GetReportOrgActive reportOrgActive) {
        try {
            // Khởi tạo đối tượng phân trang
            Integer pageNum = reportOrgActive.getPageNum();
            Integer numPerPage = reportOrgActive.getPageSize();
            List<Long> provinceIds = reportOrgActive.getProvinceIds();
            List<Integer> isDomestics = reportOrgActive.getIsDomestics();
            PagingResult<ReportOrgActive> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(pageNum);
            pagingResult.setNumberPerPage(numPerPage);

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("");
            Map<String, Object> params = new HashMap<>();

            if (!provinceIds.isEmpty()) {
                where.append(" AND  o.province_id in :provinceIds  \n");
                params.put("provinceIds", provinceIds);
            }
            if (!isDomestics.isEmpty()) {
                where.append(" AND  o.is_domestic in :isDomestics  \n");
                params.put("isDomestics", isDomestics);
            }


            StringBuilder sqlTemp = new StringBuilder("");

            StringBuilder sql = new StringBuilder(sqlTemp);

            sql.append("SELECT \n" +
                    "cc.ID ,\n" +
                    "    cc.NAME AS ProvinceName,\n" +
                    "    SUM(CASE WHEN o.status = 1 THEN 1 ELSE 0 END) AS Dang_hoat_Dong,\n" +
                    "    SUM(CASE WHEN o.status = 0 THEN 1 ELSE 0 END) AS Ngung_hoat_dong\n" +
                    "FROM ORGANIZATIONS o\n" +
                    "JOIN C_CATEGORY cc ON o.PROVINCE_ID = cc.ID\n" +
                    "WHERE 1 = 1 \n");
            sql.append(where);
            sql.append("GROUP BY cc.ID,cc.NAME\n");
            sql.append(" ORDER BY cc.ID ASC");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());
            // Đặt tham số động cho truy vấn
            for (Map.Entry<String, Object> param : params.entrySet()) {
                query.setParameter(param.getKey(), param.getValue());
            }
            // Phân trang
            int pageNumber = pageNum > 0 ? pageNum - 1 : 0; // Spring Boot đếm từ 0
            int numberPerPage = numPerPage > 0 ? numPerPage : 10; // Mặc định 10
            Query queryGetTotal = query;
            long totalRecords = ((Number) queryGetTotal.getResultList().size()).longValue();

            query.setFirstResult(pageNumber * numberPerPage);
            query.setMaxResults(numberPerPage);

            AtomicInteger sumTotalIn = new AtomicInteger(0);
            AtomicInteger sumTotalOut = new AtomicInteger(0);
            // Thực thi truy vấn lấy danh sách
            List<Object[]> resultList = query.getResultList();
            List<ReportOrgActive> responseList = resultList.stream().map(row -> {
                int totalIn = H.isTrue(row[2]) ? ((Number) row[2]).intValue() : 0;
                int totalOut = H.isTrue(row[3]) ? ((Number) row[3]).intValue() : 0;

                sumTotalIn.addAndGet(totalIn);
                sumTotalOut.addAndGet(totalOut);

                return new ReportOrgActive(
                        H.isTrue(row[0]) ? ((Number) row[0]).longValue() : null, // idProvince
                        H.isTrue(row[1]) ? row[1].toString() : null,             // provinceName
                        totalIn,
                        totalOut,
                        0,
                        0, 0// totalOut
                );
            }).collect(Collectors.toList());

            pagingResult.setItems(responseList);

// Lấy tổng
            int totalInSum = sumTotalIn.get();
            int totalOutSum = sumTotalOut.get();
            responseList.add(0, new ReportOrgActive(0L, "Tổng số", totalInSum, totalOutSum, 0, 0, 0));
            pagingResult.setItems(responseList);
            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));


            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách tổ chức thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh  tổ chức", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgActive(GetReportOrgActive reportOrgActive, HttpServletResponse response) {
        try {
            PagingResult data = getReportOrgActive(reportOrgActive).getBody().getData();
            List<ReportOrgActive> items = data.getItems();
            List<String> header = new ArrayList<>();
            String fileName = "";
            String title = "";
            header.add("STT");
            header.add("Địa danh hành chính");
            header.add("Đang hoạt động");
            header.add("Tạm ngừng hoạt động");
            header.add("Đã bị thu hồi ĐKHĐ");
            header.add("Đã bị thu hồi giấy phép ĐKHĐ");
            header.add("Chấm dứt hoạt động");

            fileName = "Bctk_Tinh_trang_hoat_dong_to_chuc_hanh_nghe_luat_su.xlsx";
            title = "Báo cáo thống kê tình hình hoạt động tổ chức hành nghề luật sư ";

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getProvinceName());
                row.add(items.get(i).getTotalActive().toString());
                row.add(items.get(i).getTotalDisable().toString());
                row.add(items.get(i).getTotalRevoke1().toString());
                row.add(items.get(i).getTotalRevoke2().toString());
                row.add(items.get(i).getTotalInActive().toString());
                dataExport.add(row);
            }

            String sheetName = "Sheet1";
            String subTitle = "";

            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, null);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<?>> exportExcelLOrgArea(List<Long> provinceIds, Integer isOrg, HttpServletResponse response) {
        try {
            PagingResult data = getReportOrgArea(provinceIds, 1, 1000, isOrg).getBody().getData();
            List<ReportLawOrgArea> items = data.getItems();
            List<String> header = new ArrayList<>();
            String fileName = "";
            String title = "";
            header.add("STT");
            header.add("Tỉnh/Thành phố");
            if (isOrg == 0) {
                header.add("Luật sư trong nước");
                header.add("Luật sư nước ngoài");
                fileName = "Bctk_Phan_bo_luat_su.xlsx";
                title = "Báo cáo thống kê phân bố luật sư";
            } else {
                header.add("Tổ chức trong nước");
                header.add("Tổ chức nước ngoài");
                fileName = "Bctk_Phan_bo_to_chuc.xlsx";
                title = "Báo cáo thống kê phân bố tổ chức ";
            }
            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getProvinceName());
                row.add(items.get(i).getTotalIn().toString());
                row.add(items.get(i).getTotalOut().toString());
                dataExport.add(row);

            }

            String sheetName = "Sheet1";
            String subTitle = "";

            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, null);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
