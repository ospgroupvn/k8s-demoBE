package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.contants.ConstantsLawyer;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.dto.request.db4.GetListLLawyerCSDLRequest;
import com.osp.bttp.dao.model.dto.request.db4.dashBoard.GetReportOrgActive;
import com.osp.bttp.dao.model.dto.response.db4.*;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawActive;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportLawCCHN;
import com.osp.bttp.dao.model.dto.response.db4.dashBoard.ReportOrgActive;
import com.osp.bttp.dao.model.entity.db3.Auctioneer;
import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.entity.db4.*;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import com.osp.bttp.dao.model.validator.LLawyerValidator;
import com.osp.bttp.dao.repository.bttp.NotaryInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctioneerRepository;
import com.osp.bttp.dao.repository.db4.*;
import com.osp.bttp.dao.service.bttp.LogSystemService;
import com.osp.bttp.dao.service.lawyer.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:09 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
public class LLawyerServiceCSDLImpl implements LLawyerCSDLService {
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;

    @Autowired
    private LLawyerRepository lLawyerRepository;

    @Autowired
    private LLicenseService lLicenseService;

    @Autowired
    private LLawyerAssociationService lLawyerAssociationService;

    @Autowired
    private LNationalityRepository lNationalityRepository;

    @Autowired
    private LOrganizationRepository lOrganizationRepository;

    @Autowired
    private LCategoryRepository lCategoryRepository;

    @Autowired
    private LLicenseRepository licenseRepository;

    @Autowired
    private LOrganizationService lOrganizationService;

    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private AuctioneerRepository auctioneerRepository;

    @Autowired
    private LogSystemService logSystemService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<GetListLLawyerCSDLResponse>>> getLawyerCSDLs(GetListLLawyerCSDLRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<GetListLLawyerCSDLResponse> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("  ");
            Map<String, Object> params = new HashMap<>();

            if (request.getFullName() != null) {
                where.append(" AND l.FULL_NAME LIKE :fullName ");
                params.put("fullName", "%" + request.getFullName() + "%");
            }

            if (H.isTrue(request.getIsDomestic())) {
                where.append(" AND l.IS_DOMESTIC = :listIsDomestic ");
                params.put("listIsDomestic", request.getIsDomestic());
            }

            if (H.isTrue(request.getListStatus())) {
                where.append(" AND l.activity_status IN (:listStatus) ");
                params.put("listStatus", request.getListStatus());
            }

            if (H.isTrue(request.getOrganizationId())) {
                where.append(" AND l.ORGANIZATION_ID = :organizationId ");
                params.put("organizationId", request.getOrganizationId());
            }

            if (H.isTrue(request.getAssocId())) {
                where.append(" AND l.LAWYER_ASSOCIATION_ID = :lawyerAssociationId ");
                params.put("lawyerAssociationId", request.getOrganizationId());
            }

            if (H.isTrue(request.getProvinceId())) {
                where.append(" AND l.PROVINCE_ID = :provinceId ");
                params.put("provinceId", request.getProvinceId());
            }

            // Truy vấn chính (Native SQL) với subquery trong FROM
            StringBuilder sqlTemp = new StringBuilder(
                    "WITH LatestCard AS (\n" +
                            "         SELECT OWNER_ID, LICENSE_NUMBER AS lawyerCardNumber,ISSUE_DATE,PRACTICE_FORM, \n" +
                            "                ROW_NUMBER() over (PARTITION BY OWNER_ID ORDER BY LICENSE_ID DESC) AS rn \n" +
                            "         FROM LICENSES\n" +
                            "         WHERE OWNER_TYPE = :typeLawyer AND LICENSE_TYPE = :typeThe \n" +
                            ")\n "
            );
            StringBuilder sql = new StringBuilder(sqlTemp);
            sql.append(" SELECT la.assoc_name as ASSOC_NAME, l.FULL_NAME,  l.DATE_OF_BIRTH,n.NATIONALITY_NAME, l.PHONE, \n" +
                    "       o.ORG_NAME AS ORG_NAMES ,\n" +
                    "       l.certificate_number, \n" +
                    " card.lawyerCardNumber, \n" +
                    "       card.ISSUE_DATE, card.PRACTICE_FORM, \n" +
                    "       l.activity_status,l.GEN_DATE, l.LAWYER_ID \n" +
                    "FROM LAWYERS l\n" +
                    "         LEFT JOIN ORGANIZATIONS o ON l.organization_id = o.ORG_ID\n" +
                    "         LEFT JOIN LAWYER_ASSOCIATIONS la ON l.LAWYER_ASSOCIATION_ID = la.ASSOC_ID\n" +
                    "         LEFT JOIN NATIONALITIES n  ON l.NATIONALITY_ID = n.NATIONALITY_ID\n" +
                    "         LEFT JOIN LatestCard card ON l.LAWYER_ID = card.OWNER_ID AND card.rn = 1\n" +
                    "WHERE l.activity_status= 4 and 1 = 1 \n");

            sql.append(where);
//            sql.append("GROUP BY l.LAWYER_ID, la.assoc_name, l.FULL_NAME, l.DATE_OF_BIRTH,n.NATIONALITY_NAME, l.PHONE,\n" +
//                    "l.certificate_number, card.lawyerCardNumber, card.ISSUE_DATE, card.PRACTICE_FORM,l.activity_status, l.GEN_DATE");
            sql.append(" ORDER BY l.LAWYER_ID");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());

            query.setParameter("typeLawyer", ConstantsLawyer.TYPE_OWNER.LAWYER);
            if (request.getIsDomestic() == 1) {
                query.setParameter("typeThe", ConstantsLawyer.LICENSE_TYPE.THE_LS);
            } else {
                query.setParameter("typeThe", ConstantsLawyer.LICENSE_TYPE.GPHN);
            }


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

            List<GetListLLawyerCSDLResponse> responseList = resultList.stream().map(row -> new GetListLLawyerCSDLResponse(
                    H.isTrue(row[12]) ? ((Number) row[12]).longValue() : null, // lawyerId
                    H.isTrue(row[0]) ? row[0].toString() : null, // assocName
                    H.isTrue(row[1]) ? row[1].toString() : null, // fullName
                    H.isTrue(row[2]) ? (Date) row[2] : null, // dateOfBirth
                    H.isTrue(row[3]) ? row[3].toString() : null, // nationalName
                    H.isTrue(row[4]) ? row[4].toString() : null, // phone
                    H.isTrue(row[5]) ? row[5].toString() : null, // orgName
                    H.isTrue(row[6]) ? row[6].toString() : null, // certificateNumber
                    H.isTrue(row[7]) ? row[7].toString() : null, // lawyerCardNumber || GPHNNumber
                    H.isTrue(row[8]) ? (Date) row[8] : null, // dateIssue
                    H.isTrue(row[9]) ? row[9].toString() : null, // typeWork
                    H.isTrue(row[10]) ? ((Number) row[10]).intValue() : null, // status
                    H.isTrue(row[11]) ? (Date) row[11] : null // genDate
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
    public ResponseEntity<ApiResponseV1<DetailLLawyerCSDLResponse>> getLawyerById(Long lawyerId) {
        try {
            DetailLLawyerCSDLResponse detailLLawyerResponse = getDetail(lawyerId);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy thông tin luật sư thành công", detailLLawyerResponse), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy thông tin luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public DetailLLawyerCSDLResponse getDetail(Long lawyerId) {

        LLawyer lawyer = lLawyerRepository.getByLawyerIdAndActivityStatus(lawyerId, ConstantsLawyer.ACTIVITY_STATUS.ACTIVE);

        if (lawyer == null || lawyer.getLawyerId() == null)
            throw new CustomException("Không tìm thấy hoặc  đã ngừng hoạt động với luật sư id " + lawyerId);

        DetailLLawyerCSDLResponse detailLLawyerResponse = new DetailLLawyerCSDLResponse(lawyer);
        if (lawyer.getIsDomestic() == 0) {
            //số giấy phép hành nghề luật sư nước ngoài lấy theo lic
            List<LLicense> GPHNLicenses = lLicenseService.getLicensesByOwnerAndTypeOwner(lawyer.getLawyerId(), ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.GPHN)
                    .stream()
                    .sorted((a, b) -> b.getLicenseId().compareTo(a.getLicenseId())) // Sắp xếp theo ngày mới nhất
                    .collect(Collectors.toList());
            if (!GPHNLicenses.isEmpty()) {
                detailLLawyerResponse.setCertificateNumber(GPHNLicenses.get(0).getLicenseNumber());
            }
        }

        if (detailLLawyerResponse.getYearReport() == null) {
            detailLLawyerResponse.setYearReport(String.valueOf(LocalDate.now().getYear()));
        }
        if (lawyer.getLawyerAssociationId() != null) {
            LLawyerAssociation lLawyerAssociation = lLawyerAssociationService.findById(lawyer.getLawyerAssociationId());
            detailLLawyerResponse.setAssocName(lLawyerAssociation.getAssocName());
        }
        if (lawyer.getOrganizationId() != null) {
            LOrganization org = lOrganizationService.findById(lawyer.getOrganizationId());
            detailLLawyerResponse.setOrgName(org.getOrgName());
        }
        if (lawyer.getIsDomestic() == 0) {
            LNationality lNationality = lNationalityRepository.findById(lawyer.getNationalityId()).orElse(null);
            detailLLawyerResponse.setNationalityName(lNationality.getNationalityName());
        } else {
            LCategory lCategoryProvince = lCategoryRepository.findById(lawyer.getProvinceId()).orElse(null);
            String provinceName = lCategoryProvince.getName();
            String wardName = "";
            detailLLawyerResponse.setProvinceName(lCategoryProvince.getName());
            if (lawyer.getWardId() != null) {
                LCategory lCategoryWard = lCategoryRepository.findById(lawyer.getWardId()).orElse(null);
                detailLLawyerResponse.setWardName(lCategoryWard.getName());
                wardName = lCategoryWard.getName();
            }
            String addressName = lawyer.getAddress();
            if (StringUtils.hasText(wardName)) addressName += " - " + wardName;
            if (StringUtils.hasText(provinceName)) addressName += " - " + provinceName;
            detailLLawyerResponse.setAddressName(addressName);

        }
        return detailLLawyerResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> editLawyer(DetailLLawyerCSDLResponse detailLLawyerCSDLResponse) {
        List<String> erosList = new ArrayList<>();
        try {
            Long lawyerId = detailLLawyerCSDLResponse.getLawyerId();
            Optional<LLawyer> optionalLLawyer = lLawyerRepository.findById(lawyerId);
            if (optionalLLawyer.isEmpty()) throw new CustomException("Không tìm thấy luật sư có id " + lawyerId);
            if (StringUtils.hasText(detailLLawyerCSDLResponse.getCertificateNumber())) {
                List<LLawyer> listDupLaw = lLawyerRepository.findAllByIdentityCardNumberAndLawyerIdNot(detailLLawyerCSDLResponse.getIdentityCardNumber(), lawyerId);
                List<Auctioneer> listDupAuc = auctioneerRepository.findAllByIdCode(detailLLawyerCSDLResponse.getIdentityCardNumber());
                List<NotaryInfo> listDupNo = notaryInfoRepository.findAllByIdNo(detailLLawyerCSDLResponse.getIdentityCardNumber());
                if (!listDupLaw.isEmpty() || !listDupAuc.isEmpty() || !listDupNo.isEmpty()) {
                    throw new CustomException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một {Đấu giá viên/Luật sư/Công chứng viên} khácCD");
                }
            }
            Long idProvince = detailLLawyerCSDLResponse.getProvinceId();
            if(idProvince != null) {
                Optional<LCategory> lCategoryProvince = lCategoryRepository.findById(idProvince);
                if(lCategoryProvince.isEmpty()) {
                    throw new CustomException("Tỉnh không tồn tại "+idProvince);
                }
                Long idWard = detailLLawyerCSDLResponse.getWardId();
                if(idWard != null) {
                    Optional<LCategory> lCategoryWard = lCategoryRepository.findById(idWard);
                    if(lCategoryWard.isEmpty()) {
                        throw new CustomException("Quận huyện không tồn tại "+idWard);
                    }
                }
            }
            Long idWard = detailLLawyerCSDLResponse.getWardId();

            //  String erosFieldCommon = LLawyerValidator.validateCommonField(detailLLawyerCSDLResponse);
            String erosFieldCommon = "";
            if (StringUtils.hasText(erosFieldCommon)) erosList.add(erosFieldCommon);

            String erosCCHN = validateCCHN(detailLLawyerCSDLResponse);
            if (StringUtils.hasText((erosCCHN))) erosList.add(erosCCHN);
            boolean isRevoke = false;
            if (erosList.isEmpty()) {
//            }
                if (detailLLawyerCSDLResponse.getIsDomestic() == 0) {
                    // check GPHN present , is revoke
                    List<LLicense> getLicNew = lLicenseService.getLicensesByOwnerAndTypeOwner(lawyerId, ConstantsLawyer.TYPE_OWNER.LAWYER, ConstantsLawyer.LICENSE_TYPE.GPHN)
                            .stream()
                            .sorted((a, b) -> b.getLicenseId().compareTo(a.getLicenseId())) // Sắp xếp theo giấy phép mới nhất
                            .toList();
                    if (getLicNew.get(0).getStatus().equals(ConstantsLawyer.LICENSE_STATUS.GPHN.THU_HOI)) {
                        isRevoke = true;
                    }
                }
                Date revokeDate = detailLLawyerCSDLResponse.getRevokeDate();
                String numberRevoke = detailLLawyerCSDLResponse.getRevokeDecisionNumber();
                // check revoke CCHN
                if (revokeDate != null && numberRevoke != null && revokeDate.after(new Date())) isRevoke = true;
                LLawyer lawyer = updateLawyer(isRevoke, optionalLLawyer.get(), detailLLawyerCSDLResponse);
                logSystemService.saveLog(lawyer.getFullName(),lawyerId.toString(), ActionType.EDIT, GroupType.LAWYER, ActorType.PER);
                return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Sửa thông tin luật sư thành công", lawyer), HttpStatus.OK);

            } else {
                throw new CustomException("Đã xảy ra lỗi khi sửa luật sư : " + erosList);
            }

        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi sửa luật sư : " + erosList);
        }

    }

    String validateCCHN(DetailLLawyerCSDLResponse detailLLawyerCSDLResponse) {
        StringBuilder listEros = new StringBuilder();
        String numberCer = detailLLawyerCSDLResponse.getCertificateNumber();

        Date dateIssue = detailLLawyerCSDLResponse.getIssueDate();

        String numberRevoke = detailLLawyerCSDLResponse.getRevokeDecisionNumber();

        Date dateRevoke = detailLLawyerCSDLResponse.getRevokeDate();
        if (numberCer == null) listEros.append("Số chứng chỉ không dc để trống \n");
//        else {
//            List<LLawyer> numberCers = lLawyerRepository.getAllByCertificateNumberAndActivityStatus(numberCer,ConstantsLawyer.ACTIVITY_STATUS.ACTIVE);
//            if(numberCers.size()>1) listEros.append("Số chứng chỉ phải là duy nhất \n");
//        }

        if (dateIssue == null) listEros.append("Ngày cấp không được để trống \n");
        else {
            if (numberRevoke != null && dateRevoke != null) {
                List<LLawyer> numberRevokes = lLawyerRepository.getAllByRevokeDecisionNumberAndActivityStatus(numberRevoke, ConstantsLawyer.ACTIVITY_STATUS.IN_ACTIVE);
                if (!numberRevokes.isEmpty()) listEros.append("Sô quyết định thu hồi phải là duy nhất \n");

                if (dateRevoke.after(dateIssue)) listEros.append("Ngày thu hồi phải lớn hơn ngày cấp \n");
            }
        }
        return listEros.toString();

    }


    LLawyer updateLawyer(boolean isRevoke, LLawyer lawUpdate, DetailLLawyerCSDLResponse response) {
        lawUpdate.setFullName(safeText(response.getFullName()));
        lawUpdate.setOrganizationId(response.getOrganizationId());
        lawUpdate.setDateOfBirth(response.getDateOfBirth());
        lawUpdate.setIdentityCardNumber(safeText(response.getIdentityCardNumber()));
        lawUpdate.setGender(response.getGender());
        lawUpdate.setPhone(safeText(response.getPhone()));
        lawUpdate.setAddress(safeText(response.getAddress()));
        lawUpdate.setEmail(safeText(response.getEmail()));
        lawUpdate.setIsDomestic(response.getIsDomestic());
        lawUpdate.setLawyerAssociationId(response.getLawyerAssociationId());
        lawUpdate.setProvinceId(response.getProvinceId());
        lawUpdate.setWardId(response.getWardId() != null ? response.getWardId() : null);
        lawUpdate.setNationalityId(response.getNationalityId());
        lawUpdate.setCertificateNumber(safeText(response.getCertificateNumber()));
        lawUpdate.setDecisionNumber(safeText(response.getDecisionNumber()));
        lawUpdate.setIssueDate(response.getIssueDate());
        lawUpdate.setNote(safeText(response.getNote()));
        if (isRevoke) {
            lawUpdate.setActivityStatus(ConstantsLawyer.ACTIVITY_STATUS.IN_ACTIVE);
            lawUpdate.setRevokeDate(response.getRevokeDate());
            lawUpdate.setRevokeDecisionNumber(response.getRevokeDecisionNumber());
        }
        lawUpdate.setActivityStatus(ConstantsLawyer.ACTIVITY_STATUS.ACTIVE);

        lawUpdate.setYearReport(response.getYearReport());
        return lLawyerRepository.save(lawUpdate);
    }

    public static String safeText(String input) {
        return (input == null || input.trim().isEmpty()) ? "" : input;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> addLawyer(Integer isDomestic, LLawyer lawyerNew) {
        try {
            List<LLawyer> lLawyerOptional = lLawyerRepository.findAllByCertificateNumber(lawyerNew.getCertificateNumber());
            if (!lLawyerOptional.isEmpty()) new CustomException("Số chứng chỉ luật sư vn phải là duy nhất");
            LLawyer lawyer = new LLawyer();
            lawyer.setFullName(lawyerNew.getFullName());
            lawyer.setActivityStatus(ConstantsLawyer.ACTIVITY_STATUS.ACTIVE);
            if (lawyerNew.getOrganizationId() != null) {

                lawyer.setOrganizationId(lawyerNew.getOrganizationId());
            }

            if (lawyerNew.getDateOfBirth() != null) {
                lawyer.setDateOfBirth(lawyerNew.getDateOfBirth());
            }

            if (lawyerNew.getIdentityCardNumber() != null) {
                List<LLawyer> listDupLaw = lLawyerRepository.findAllByIdentityCardNumber(lawyerNew.getIdentityCardNumber());
                List<Auctioneer> listDupAuc = auctioneerRepository.findAllByIdCode(lawyerNew.getIdentityCardNumber());
                List<NotaryInfo> listDupNo = notaryInfoRepository.findAllByIdNo(lawyerNew.getIdentityCardNumber());
                if (!listDupLaw.isEmpty() || !listDupAuc.isEmpty() || !listDupNo.isEmpty()) {
                    throw new CustomException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một {Đấu giá viên/Luật sư/Công chứng viên} khácCD");
                }
                lawyer.setIdentityCardNumber(lawyerNew.getIdentityCardNumber());
            }

            if (lawyerNew.getGender() != null) {
                lawyer.setGender(lawyerNew.getGender());
            }

            if (lawyerNew.getPhone() != null) {
                lawyer.setPhone(lawyerNew.getPhone());
            }

            if (lawyerNew.getAddress() != null) {
                lawyer.setAddress(lawyerNew.getAddress());
            }

            if (lawyerNew.getEmail() != null) {
                lawyer.setEmail(lawyerNew.getEmail());
            }

            if (lawyerNew.getIsDomestic() != null) {
                lawyer.setIsDomestic(lawyerNew.getIsDomestic());
            }

            if (lawyerNew.getLawyerAssociationId() != null) {
                LLawyerAssociation lLawyerAssociation = lLawyerAssociationService.findById(lawyerNew.getLawyerAssociationId());
                if (lLawyerAssociation == null) {
                    throw new CustomException("Không tìm thấy đoàn luật sư");
                }
                lawyer.setLawyerAssociationId(lawyerNew.getLawyerAssociationId());
            }
            Long idProvince = lawyerNew.getProvinceId();
            if (idProvince != null) {
                    Optional<LCategory> lCategoryProvince = lCategoryRepository.findById(idProvince);
                    if(lCategoryProvince.isEmpty()) {
                        throw new CustomException("Tỉnh không tồn tại "+idProvince);
                    }
                    lawyer.setProvinceId(lawyerNew.getProvinceId());
                    Long idWard = lawyerNew.getWardId();
                    if(idWard != null) {
                        Optional<LCategory> lCategoryWard = lCategoryRepository.findById(idWard);
                        if(lCategoryWard.isEmpty()) {
                            throw new CustomException("Quận huyện không tồn tại "+idWard);
                        }
                        lawyer.setWardId(idWard);
                    }
                }
            if (lawyerNew.getNationalityId() != null) {
                lawyer.setNationalityId(lawyerNew.getNationalityId());
            }

            lawyer.setCertificateNumber(lawyerNew.getCertificateNumber());
            if (lawyerNew.getDecisionNumber() != null) {
                lawyer.setDecisionNumber(lawyerNew.getDecisionNumber());
            }

            if (lawyerNew.getIssueDate() != null) {
                lawyer.setIssueDate(lawyerNew.getIssueDate());
            }

            if (lawyerNew.getNote() != null) {
                lawyer.setNote(lawyerNew.getNote());
            }

            LLawyer lawyer1 = lLawyerRepository.save(lawyer);
            // nếu là luật sư nước ngoài thêm mới GPHN thì sẽ tạo 1 lic GPHN với status DA_CAP
//            if(isDomestic==0){
//                LLicense licenseGPHN = new LLicense();
//                licenseGPHN.setStatus(ConstantsLawyer.LICENSE_STATUS.GPHN.DA_CAP);
//                licenseGPHN.setLicenseType(ConstantsLawyer.LICENSE_TYPE.GPHN);
//                licenseGPHN.setOwnerId(lawyer1.getLawyerId());
//                licenseGPHN.setLicenseNumber(lawyerNew.getCertificateNumber());
//                licenseGPHN.setIssueDate(new Date());
//                licenseGPHN.setOwnerType(ConstantsLawyer.TYPE_OWNER.LAWYER);
//                lLicenseService.createLicense(licenseGPHN);
//            }
            logSystemService.saveLog(lawyer1.getFullName(),lawyer1.getLawyerId().toString(), ActionType.ADD, GroupType.LAWYER, ActorType.PER);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thêm mới luật sư thành công", lawyer1), HttpStatus.OK);

        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi khi thêm luật sư : ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponseV1<?>> deleteLawyer(Long idLaw) {
        try {
            List<LOrganization> listOrgLawRep = lOrganizationRepository.findAllByLawyerLegalRepresentativeId(idLaw);
            if (!listOrgLawRep.isEmpty()) throw new CustomException("Luật sư đang đại diện cho một tổ chức k thể xóa");
            licenseRepository.deleteAllByOwnerIdAndOwnerType(idLaw, ConstantsLawyer.TYPE_OWNER.LAWYER);
            lLawyerRepository.deleteById(idLaw);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 141, "Xóa thành công luật sư", null), HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Đã xảy ra lỗi xóa luật sư");
        }

    }

    @Override
    public ResponseEntity<ApiResponseV1<List<GetLawyerLegalRep>>> getListLawyerRepUnique() {
        try {
            List<GetLawyerLegalRep> lawyerLegalReps = lLawyerRepository.getLawyerLegalRepUnique();
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sach luật sư đại diện thành công", lawyerLegalReps), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 141, "Đã xảy ra lỗi khi lấy luật sư đại diện", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawCCHN>>> getReportLawCCHN(GetListLLawyerCSDLRequest request) {
        try {
            // Khởi tạo đối tượng phân trang
            PagingResult<ReportLawCCHN> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(request.getPageNumber());
            pagingResult.setNumberPerPage(request.getNumberPerPage());

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("  ");
            Map<String, Object> params = new HashMap<>();

            if (request.getFullName() != null) {
                where.append(" AND l.FULL_NAME LIKE :fullName ");
                params.put("fullName", "%" + request.getFullName() + "%");
            }

            if (H.isTrue(request.getIsDomestic())) {
                where.append(" AND l.IS_DOMESTIC = :listIsDomestic ");
                params.put("listIsDomestic", request.getIsDomestic());
            }

            if (H.isTrue(request.getListStatus())) {
                where.append(" AND l.activity_status IN (:listStatus) ");
                params.put("listStatus", request.getListStatus());
            }


            if (H.isTrue(request.getProvinceId())) {
                where.append(" AND l.PROVINCE_ID = :provinceId ");
                params.put("provinceId", request.getProvinceId());
            }

            // Truy vấn chính (Native SQL) với subquery trong FROM
            StringBuilder sqlTemp = new StringBuilder(
                    "WITH LatestCard AS (\n" +
                            "         SELECT OWNER_ID,STATUS, \n" +
                            "                ROW_NUMBER() over (PARTITION BY OWNER_ID ORDER BY LICENSE_ID DESC) AS rn \n" +
                            "         FROM LICENSES\n" +
                            "         WHERE OWNER_TYPE = :typeLawyer AND LICENSE_TYPE = :typeThe \n" +
                            ")\n "
            );
            StringBuilder sql = new StringBuilder(sqlTemp);
            sql.append(" SELECT l.LAWYER_ID, l.FULL_NAME,  l.DATE_OF_BIRTH,l.GENDER,l.ADDRESS,l.PROVINCE_ID,\n " +
                    "l.certificate_number,l.decision_number, l.issue_date, \n" +
                    " card.status \n" +
                    "FROM LAWYERS l\n" +
                    "         LEFT JOIN LAWYER_ORG lo ON l.LAWYER_ID = lo.LAWYER_ID\n" +
                    "         LEFT JOIN LatestCard card ON l.LAWYER_ID = card.OWNER_ID AND card.rn = 1\n" +
                    "WHERE l.activity_status= 4 and 1 = 1 \n");

            sql.append(where);
            sql.append(" ORDER BY l.LAWYER_ID");

            // Tạo truy vấn native
            Query query = entityManager.createNativeQuery(sql.toString());

            query.setParameter("typeLawyer", ConstantsLawyer.TYPE_OWNER.LAWYER);
            if (request.getIsDomestic() == 1) {
                query.setParameter("typeThe", ConstantsLawyer.LICENSE_TYPE.THE_LS);
            } else {
                query.setParameter("typeThe", ConstantsLawyer.LICENSE_TYPE.GPHN);
            }


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
//            SELECT l.LAWYER_ID, l.FULL_NAME,  l.DATE_OF_BIRTH,l.GENDER,l.ADDRESS,\n " +
//            "l.certificate_number,l.decision_number, l.issue_date, \n" +
//                    " card.status \n" +
            List<ReportLawCCHN> responseList = resultList.stream().map(row -> new ReportLawCCHN(
                    H.isTrue(row[0]) ? ((Number) row[0]).longValue() : null, // lawyerId
                    H.isTrue(row[1]) ? row[1].toString() : null, // fullName
                    H.isTrue(row[2]) ? (Date) row[2] : null, // dateOfBirth
                    H.isTrue(row[3]) ? ((Number) row[3]).intValue() : null, // gender
                    H.isTrue(row[4]) ? row[4].toString() : null, // address
                    H.isTrue(row[5]) ? ((Number) row[5]).longValue() : null, // provinceIđ
                    H.isTrue(row[6]) ? row[6].toString() : null, // certificateNumber
                    H.isTrue(row[7]) ? row[7].toString() : null, // desicition
                    H.isTrue(row[8]) ? (Date) row[8] : null, // dateIssue
                    H.isTrue(row[9]) ? ((Number) row[9]).intValue() : null // status
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
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<PagingResult<ReportLawActive>>> getReportLawActive(GetReportOrgActive request) {
        try {
            // Khởi tạo đối tượng phân trang
            Integer pageNum = request.getPageNum();
            Integer numPerPage = request.getPageSize();
            List<Long> provinceIds = request.getProvinceIds();
            List<Integer> isDomestics = request.getIsDomestics();
            PagingResult<ReportLawActive> pagingResult = new PagingResult<>();
            pagingResult.setPageNumber(pageNum);
            pagingResult.setNumberPerPage(numPerPage);

            // Xây dựng điều kiện WHERE
            StringBuilder where = new StringBuilder("");
            Map<String, Object> params = new HashMap<>();

            if (!provinceIds.isEmpty()) {
                where.append(" AND  l.province_id in :provinceIds  \n");
                params.put("provinceIds", provinceIds);
            }
            if (!isDomestics.isEmpty()) {
                where.append(" AND  l.is_domestic in :isDomestics  \n");
                params.put("isDomestics", isDomestics);
            }


            StringBuilder sqlTemp = new StringBuilder("");

            StringBuilder sql = new StringBuilder(sqlTemp);

            sql.append("WITH LatestCard AS ( \n" +
                    "    SELECT OWNER_ID, LICENSE_NUMBER, ISSUE_DATE, PRACTICE_FORM, STATUS,\n" +
                    "           ROW_NUMBER() OVER (PARTITION BY OWNER_ID ORDER BY LICENSE_ID DESC) AS rn\n" +
                    "    FROM LICENSES\n" +
                    "    WHERE OWNER_TYPE = 1 AND LICENSE_TYPE = 2\n" +
                    ")\n" +
                    "SELECT cc.ID , cc.NAME AS ProvinceName,\n" +
                    "    SUM(CASE WHEN l.ACTIVITY_STATUS = 4 THEN 1 ELSE 0 END) AS Dang_hanh_nghe,\n" +
                    "    SUM(CASE WHEN l.ACTIVITY_STATUS = 6 THEN 1 ELSE 0 END) AS Da_thu_hoi_cchn,\n" +
                    "    SUM(CASE WHEN li.LICENSE_NUMBER IS NULL THEN 1 ELSE 0 END) AS Chua_cap_the_ls,\n" +
                    "    SUM(CASE WHEN li.LICENSE_NUMBER IS NOT NULL THEN 1 ELSE 0 END) AS Da_cap_the_ls\n" +
                    "FROM C_CATEGORY cc\n" +
                    " JOIN LAWYERS l ON l.PROVINCE_ID = cc.ID\n" +
                    "LEFT JOIN LatestCard li ON li.OWNER_ID = l.LAWYER_ID AND li.rn = 1\n" +
                    "WHERE 1 = 1");
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

            AtomicInteger sumTotalActive = new AtomicInteger(0);
            AtomicInteger sumTotalRevoke = new AtomicInteger(0);
            AtomicInteger sumTotalCard = new AtomicInteger(0);
            AtomicInteger sumTotalNotCard = new AtomicInteger(0);
            // Thực thi truy vấn lấy danh sách
            List<Object[]> resultList = query.getResultList();
            List<ReportLawActive> responseList = resultList.stream().map(row -> {
                int totalActive = H.isTrue(row[2]) ? ((Number) row[2]).intValue() : 0;
                int totalRevoke = H.isTrue(row[3]) ? ((Number) row[3]).intValue() : 0;
                int totalNotCard = H.isTrue(row[4]) ? ((Number) row[4]).intValue() : 0;
                int totalCard = H.isTrue(row[5]) ? ((Number) row[5]).intValue() : 0;

                sumTotalActive.addAndGet(totalActive);
                sumTotalRevoke.addAndGet(totalRevoke);
                sumTotalNotCard.addAndGet(totalNotCard);
                sumTotalCard.addAndGet(totalCard);

                return new ReportLawActive(
                        H.isTrue(row[0]) ? ((Number) row[0]).longValue() : null, // idProvince
                        H.isTrue(row[1]) ? row[1].toString() : null,             // provinceName
                        totalActive,
                        totalRevoke,
                        totalNotCard,
                        totalCard
                );
            }).collect(Collectors.toList());

            pagingResult.setItems(responseList);

// Lấy tổng
            int totalActiveSum = sumTotalActive.get();
            int totalRevokeSum = sumTotalRevoke.get();
            int totalNotCardSum = sumTotalNotCard.get();
            int totalCardSum = sumTotalCard.get();

            responseList.add(0, new ReportLawActive(0L, "Tổng số", totalActiveSum, totalRevokeSum, totalNotCardSum, totalCardSum));
            pagingResult.setItems(responseList);
            // Đặt thông tin phân trang
            pagingResult.setRowCount(totalRecords);
            pagingResult.setPageCount((int) Math.ceil((double) totalRecords / numberPerPage));


            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Lấy danh sách tình trạng hoạt động luật sư", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 140, "Đã xảy ra lỗi khi lấy danh  luật sư", null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawCCHN(GetListLLawyerCSDLRequest request, HttpServletResponse response) {
        try {
            PagingResult data = getReportLawCCHN(request).getBody().getData();
            List<ReportLawCCHN> items = data.getItems();
            List<String> header = new ArrayList<>();
            String fileName = "";
            String title = "";
            header.add("STT");
            header.add("Họ tên");
            header.add("Ngày sinh");
            header.add("Giới tính");
            header.add("Địa chỉ thường trú");
            header.add("Số CCHN luật sư");
            header.add("Quyết định");
            header.add("Ngày cấp");
            header.add("Trạng thái cấp thẻ");

            fileName = "Bctk_Danh_sach_cap_chung_chi_hanh_nghe_luat_su.xlsx";
            title = "Báo cáo thống kê danh sách cấp chứng chỉ hành nghề luật sư ";

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getFullName());
                row.add(items.get(i).getDateOfBirth().toString());
                row.add(items.get(i).getGender().toString());
                row.add(items.get(i).getAddress());
                row.add(items.get(i).getCertificateNumber());
                row.add(items.get(i).getDecisionNumber());
                row.add(items.get(i).getIssueDate().toString());
                row.add(items.get(i).getStatus() == 24 ? "Đã cấp thẻ Ls" : "Đã thu hồi thẻ LS");
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
    public ResponseEntity<ApiResponseV1<?>> getExportExcelLawActive(GetReportOrgActive request, HttpServletResponse response) {
        try {
            PagingResult data = getReportLawActive(request).getBody().getData();
            List<ReportLawActive> items = data.getItems();
            List<String> header = new ArrayList<>();
            String fileName = "";
            String title = "";
            header.add("STT");
            header.add("Địa danh hành chính");
            header.add("Đanh hành nghề");
            header.add("Đã bị thu hồi CCHNLS");
            header.add("Chưa cấp thẻ luật sư");
            header.add("Đã cấp thẻ luật sư");

            fileName = "Bctk_Tinh_hinh_hoat_dong_hanh_nghe_luat_su.xlsx";
            title = "Báo cáo thống kê tình hình hoạt động hành nghề luật sư ";

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getProvinceName());
                row.add(items.get(i).getTotalActive().toString());
                row.add(items.get(i).getTotalRevoke().toString());
                row.add(items.get(i).getTotalNotCard().toString());
                row.add(items.get(i).getTotalNotCard().toString());
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
