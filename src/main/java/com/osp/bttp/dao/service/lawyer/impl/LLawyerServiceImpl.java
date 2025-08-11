package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.contants.ConstantsLawyer;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerResponse;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerRequest;
import com.osp.bttp.dao.model.dto.response.db4.GetListLLawyerResponse;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.entity.db4.LLawyerUpdateInfoHis;
import com.osp.bttp.dao.model.entity.db4.LLicense;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.repository.db4.LLawyerRepository;
import com.osp.bttp.dao.service.lawyer.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:09 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@RequiredArgsConstructor
public class LLawyerServiceImpl implements LLawyerService {
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;

    private final LLawyerRepository lLawyerRepository;

    private final LLicenseService lLicenseService;

    private final LLawyerAssociationService lLawyerAssociationService;

    private final LLawyerUpdateInfoHisService lLawyerUpdateInfoHisService;

    private final LawyerCommonService lawyerCommonService;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;

    @Autowired
    @Lazy
    private LOrganizationService lOrganizationService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerResponse>>> getLawyers(GetListLLawyerRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLLawyerResponse> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("  ");
            Map<String, Object> params = new HashMap<>();

            if (request.getFullName() != null) {
                where.append(" AND l.FULL_NAME LIKE :fullName ");
                params.put("fullName", "%" + request.getFullName() + "%");
            }

            if (H.isTrue(request.getListIsDomestic())) {
                where.append(" AND l.IS_DOMESTIC IN (:listIsDomestic) ");
                params.put("listIsDomestic", request.getListIsDomestic());
            }

            if (H.isTrue(request.getListStatus())) {
                where.append(" AND l.ACTIVITY_STATUS IN (:listStatus) ");
                params.put("listStatus", request.getListStatus());
            }

            if (H.isTrue(request.getOrganizationId())) {
                where.append(" AND l.ORGANIZATION_ID = :organizationId ");
                params.put("organizationId", request.getOrganizationId());
            }

            if (H.isTrue(request.getProvinceId())) {
                where.append(" AND l.PROVINCE_ID = :provinceId ");
                params.put("provinceId", request.getProvinceId());
            }

            // Truy vấn chính (Native SQL) với subquery trong FROM
            StringBuilder sqlTemp = new StringBuilder(
                    "WITH " +
//                            "LatestCCHN AS (\n" +
//                            "    SELECT OWNER_ID, LICENSE_NUMBER AS certificateNumber,\n" +
//                            "           ROW_NUMBER() over (PARTITION BY OWNER_ID ORDER BY ISSUE_DATE DESC) AS rn\n" +
//                            "    FROM LICENSES\n" +
//                            "    WHERE OWNER_TYPE = :typeLawyer AND LICENSE_TYPE = :typeCCHN \n" +
//                            "),\n" +
//                            "\n" +
                            "LatestCard AS (\n" +
                            "         SELECT OWNER_ID, LICENSE_NUMBER AS lawyerCardNumber, PRACTICE_FORM as practiceForm,\n" +
                            "                ROW_NUMBER() over (PARTITION BY OWNER_ID ORDER BY LICENSE_ID DESC) AS rn\n" +
                            "         FROM LICENSES\n" +
                            "         WHERE OWNER_TYPE = :typeLawyer AND LICENSE_TYPE = :typeTheLs \n" +
                            ")\n "
            );
            StringBuilder sql = new StringBuilder(sqlTemp);
            sql.append(" SELECT o.ORG_NAME as ORG_NAME, l.FULL_NAME,  l.GEN_DATE, \n" +
                    "       l.CERTIFICATE_NUMBER, card.lawyerCardNumber, \n" +
                    "       l.ADDRESS, l.ACTIVITY_STATUS, l.DATE_OF_BIRTH, l.LAWYER_ID, card.practiceForm,l.ISSUE_DATE ,n.NATIONALITY_NAME  \n" +
                    "FROM LAWYERS l\n" +
                    "         LEFT JOIN ORGANIZATIONS o ON l.ORGANIZATION_ID = o.ORG_ID\n" +
//                    "         LEFT JOIN LatestCCHN cchn ON l.LAWYER_ID = cchn.OWNER_ID AND cchn.rn = 1\n" +
                    "         LEFT JOIN LatestCard card ON l.LAWYER_ID = card.OWNER_ID AND card.rn = 1\n" +
                    "LEFT JOIN NATIONALITIES n ON n.NATIONALITY_ID = l.NATIONALITY_ID \n" +
                    "WHERE 1 = 1 \n");

            sql.append(where);
            sql.append(" ORDER BY l.LAWYER_ID ASC");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());

            query.setParameter("typeLawyer", ConstantsLawyer.TYPE_OWNER.LAWYER);
//            query.setParameter("typeCCHN", ConstantsLawyer.LICENSE_TYPE.CCHN);
            query.setParameter("typeTheLs", ConstantsLawyer.LICENSE_TYPE.THE_LS);


            // Đặt tham số cho truy vấn
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

            List<GetListLLawyerResponse> responseList = resultList.stream().map(row -> new GetListLLawyerResponse(
                    H.isTrue(row[0]) ? row[0].toString() : null, // orgName
                    H.isTrue(row[1]) ? row[1].toString() : null, // fullName
                    H.isTrue(row[2]) ? (Date) row[2] : null, // dateOfBirth
                    H.isTrue(row[3]) ? row[3].toString() : null, // certificateNumber
                    H.isTrue(row[4]) ? row[4].toString() : null, // lawyerCardNumber
                    H.isTrue(row[5]) ? row[5].toString() : null, // address
                    H.isTrue(row[6]) ? ((Number) row[6]).intValue() : null, // status
                    H.isTrue(row[7]) ? (Date) row[7] : null, // dateOfBirth
                    H.isTrue(row[8]) ? ((Number) row[8]).longValue() : null, // lawyerId
                    H.isTrue(row[9]) ? ((Number) row[9]).intValue() : null, // practiceForm
                    H.isTrue(row[10]) ? (Date) row[10] : null,           // issueDate
                    H.isTrue(row[11]) ? row[11].toString() : null // nationalName
            )).collect(Collectors.toList());
            pagingResult.setItems(responseList);


            pagingResult.setRowCount(totalRecords.longValue());
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords.longValue() / numberPerPage));

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách luật sư thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh sách luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public ResponseEntity<ApiResponseV1<DetailLLawyerResponse>> getLawyerById(Long lawyerId) {
//        return lLawyerRepository.findById(lawyerId)
//                .map(lawyer -> new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin luật sư thành công", lawyer), HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Không tìm thấy luật sư", null), HttpStatus.NOT_FOUND));
        try {
            DetailLLawyerResponse detailLLawyerResponse = getDetail(lawyerId);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin luật sư thành công", detailLLawyerResponse), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy thông tin luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public DetailLLawyerResponse getDetail(Long lawyerLegalRepresentativeId) {
        LLawyer lawyer = lLawyerRepository.findById(lawyerLegalRepresentativeId).orElse(null);
        DetailLLawyerResponse detailLLawyerResponse = new DetailLLawyerResponse(lawyer);
        if (H.isTrue(lawyer.getLawyerAssociationId())) {
            detailLLawyerResponse.setLawyerAssociation(lLawyerAssociationService.findById(lawyer.getLawyerId()));
        }
        if (H.isTrue(lawyer.getOrganizationId())) {
            detailLLawyerResponse.setOrganization(lOrganizationService.findById(lawyer.getOrganizationId()));
        }
//        List<LLicense> cchnLicenses = lLicenseService.getLicensesByOwnerAndTypeOwnerAndLoai(lawyer.getLawyerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.CCHN, ConstantsLawyer.LICENSE_STATUS.CCHN.DANG_HANH_NGHE_CCHN);
//        detailLLawyerResponse.setCchnLicenses(cchnLicenses);

        List<LLicense> theLsLicenses = lLicenseService.getLicensesByOwnerAndTypeOwnerAndLoai(lawyer.getLawyerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.THE_LS, ConstantsLawyer.LICENSE_STATUS.THE_LS.DA_CAP_THE_LS);
        detailLLawyerResponse.setLsCardLicenses(theLsLicenses);

        List<LLawyerUpdateInfoHis> lLawyerUpdateInfoHisList = lLawyerUpdateInfoHisService.getAllByLawyerId(lawyer.getLawyerId());
        detailLLawyerResponse.setListLawyerUpdateInfoHis(lLawyerUpdateInfoHisList);

//        List<LLicense> gphnLicenses = lLicenseService.getLicensesByOwnerAndTypeOwnerAndLoai(lawyer.getLawyerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.GPHN, ConstantsLawyer.LICENSE_STATUS.GPHN.DANG_HANH_NGHE);
//        detailLLawyerResponse.setGphnLicenses(gphnLicenses);

        return detailLLawyerResponse;
    }

    @Override
    public LLawyer findById(Long lawyerLegalRepresentativeId) {
        return lLawyerRepository.findById(lawyerLegalRepresentativeId).orElse(null);
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

            data.put("dataMap", getLawyerByMap(1, cityCode).getBody().getData());


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponseV1<?>> getLawyerByMap(Integer type, Long cityCode) {
        try {
            List<HashMap<String, Object>> data = new ArrayList<>();
            if (!H.isTrue(type)) type = 1;

            List<Reaport> items = lawyerCommonService.getDataMap(type, cityCode);
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
