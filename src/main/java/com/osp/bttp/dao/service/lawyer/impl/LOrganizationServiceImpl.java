package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.db4.GetListLOrganizationRequest;
import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerResponse;
import com.osp.bttp.dao.model.dto.response.db4.DetailLOrganizationResponse;
import com.osp.bttp.dao.model.dto.response.db4.GetListLOrganizationResponse;
import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.model.entity.db4.LOrganization;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.repository.db4.LOrganizationRepository;
import com.osp.bttp.dao.service.lawyer.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:08 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@RequiredArgsConstructor
public class LOrganizationServiceImpl implements LOrganizationService {
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;

    private final LOrganizationRepository lOrganizationRepository;

    private final LLawyerService lLawyerService;

    private final LLawyerAssociationService lawyerAssociationService;

    private final LLicenseService licenseService;

    private final LawyerCommonService lawyerCommonService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLOrganizationResponse>>> getOrganizations(@Valid GetListLOrganizationRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLOrganizationResponse> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder(" ");
            Map<String, Object> params = new HashMap<>();

            if (request.getOrgName() != null) {
                where.append(" AND o.ORG_NAME LIKE :orgName ");
                params.put("orgName", "%" + request.getOrgName() + "%");
            }
            if (request.getListIsDomestic() != null && !request.getListIsDomestic().isEmpty()) {
                where.append(" AND o.IS_DOMESTIC IN (:listIsDomestic) ");
                params.put("listIsDomestic", request.getListIsDomestic());
            }
            if (request.getListStatus() != null && !request.getListStatus().isEmpty()) {
                where.append(" AND o.ACTIVITY_STATUS IN (:listStatus) ");
                params.put("listStatus", request.getListStatus());
            }
            if(H.isTrue(request.getProvinceId())) {
                where.append(" AND o.PROVINCE_ID = :provinceId ");
                params.put("provinceId", request.getProvinceId());
            }
            if(H.isTrue(request.getParentId())) {
                where.append(" AND o.PARENT_ORG_ID = :parentId ");
                params.put("parentId", request.getParentId());
            }

            // Truy vấn chính (Native SQL) với subquery trong FROM
            StringBuilder sqlTemp = new StringBuilder(
//                    "WITH " +
//                            "LatestRegistration AS ( " +
//                            "    SELECT OWNER_ID, LICENSE_NUMBER AS registrationNumber, " +
//                            "           ROW_NUMBER() OVER (PARTITION BY OWNER_ID ORDER BY ISSUE_DATE DESC) AS rn " +
//                            "    FROM LICENSES " +
//                            "    WHERE OWNER_TYPE = :typeOrganization AND LICENSE_TYPE = :typeDKHD " +
//                            "), " +
//                            "LawyerCount AS ( " +
//                            "    SELECT ORGANIZATION_ID, COUNT(LAWYER_ID) AS lawyerCount " +
//                            "    FROM LAWYERS " +
//                            "    GROUP BY ORGANIZATION_ID " +
//                            ") "
            );
            StringBuilder sql = new StringBuilder(sqlTemp);
            sql.append("SELECT o.ORG_NAME, o.ADDRESS, representative.FULL_NAME, o.registration_license_number, o.STATUS, \n" +
                    "       o.ORG_ID, o.business_license_number, o.business_license_issue_date, o.registration_license_issue_date,cc.NAME,n.NATIONALITY_NAME \n" +
                    "FROM ORGANIZATIONS o \n" +
                    "LEFT JOIN LAWYERS representative ON o.lawyer_legal_representative_id = representative.LAWYER_ID \n" +
//                    "LEFT JOIN LatestRegistration reg ON o.ORG_ID = reg.OWNER_ID AND reg.rn = 1 " +
//                    "LEFT JOIN LawyerCount lc ON o.ORG_ID = lc.ORGANIZATION_ID \n" +
                    "LEFT JOIN C_CATEGORY cc ON cc.ID = o.PROVINCE_ID \n" +
                    "LEFT JOIN NATIONALITIES n ON n.NATIONALITY_ID = o.NATIONALITY_ID \n" +
                    "WHERE 1 = 1 ");
            sql.append(where);
            sql.append(" ORDER BY o.ORG_ID ASC");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());

            // Đặt tham số cố định
//            query.setParameter("typeOrganization", ConstantsLawyer.TYPE_OWNER.ORG);
//            query.setParameter("typeDKHD", ConstantsLawyer.LICENSE_TYPE.DKHD);

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
            List<GetListLOrganizationResponse> responseList = resultList.stream().map(row -> new GetListLOrganizationResponse(
                    H.isTrue(row[0]) ? row[0].toString() : null, // orgName
                    H.isTrue(row[1]) ? row[1].toString() : null, // address
                    H.isTrue(row[2]) ? row[2].toString() : null, // lawyerLegalRepresentative
                    H.isTrue(row[3]) ? row[3].toString() : null, // registrationNumber
                    H.isTrue(row[4]) ? ((Number) row[4]).intValue() : null, // status
                    H.isTrue(row[5]) ? ((Number) row[5]).longValue() : null, // orgId
                    H.isTrue(row[6]) ? row[6].toString() : null, // businessLicense
                    H.isTrue(row[7]) ? (Date) row[7] : null, // businessLicenseIssueDate
                    H.isTrue(row[8]) ? (Date) row[8] : null, // registrationLicenseIssueDate
                    H.isTrue(row[9]) ? row[9].toString() : null, // provinceName
                    H.isTrue(row[10]) ? row[10].toString() : null // nationalName
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
    public ResponseEntity<ApiResponseV1<DetailLOrganizationResponse>> getOrganizationById(Long orgId) {
//        return lOrganizationRepository.findById(orgId)
//                .map(organization -> new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin tổ chức thành công", organization), HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Không tìm thấy tổ chức", null), HttpStatus.NOT_FOUND));
        try {
            LOrganization infoOrg = lOrganizationRepository.findById(orgId).orElse(null);
            DetailLOrganizationResponse detailLOrganizationResponse = new DetailLOrganizationResponse(infoOrg);
            if(infoOrg.getLawyerAssociationId()!=null) {
                LLawyerAssociation lawyerAssociation = lawyerAssociationService.findById(infoOrg.getLawyerAssociationId());
                if (lawyerAssociation != null) {
                    detailLOrganizationResponse.setLawyerAssociation(lawyerAssociation);
                }
            }
            DetailLLawyerResponse lawyerLegalRepresentative = lLawyerService.getLawyerById(infoOrg.getLawyerLegalRepresentativeId()).getBody().getData();
            if (lawyerLegalRepresentative != null) {
                detailLOrganizationResponse.setLawyerLegalRepresentative(lawyerLegalRepresentative);
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
    public ResponseEntity<ApiResponseV1<?>> reportDashboard(Long cityCode, String fromDateRaw, String toDateRaw) {
        HashMap<String, Object> data = new HashMap<>();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = null;
        Date toDate = null;


        try {
            if (H.isTrue(fromDateRaw)) {
                fromDate = format.parse(fromDateRaw);
            }
            if (H.isTrue(toDateRaw)) {
                toDate = format.parse(toDateRaw);
            }

            data.put("dataMap", getOrgByMap(cityCode).getBody().getData());


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponseV1<?>> getOrgByMap(Long cityCode) {
        try {
            List<HashMap<String, Object>> data = new ArrayList<>();

            List<Reaport> items = lawyerCommonService.getDataMap(2, cityCode);
            for (Reaport item : items) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", item.getCol_1());
                map.put("value", item.getCol_2());
                data.add(map);
            }

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
