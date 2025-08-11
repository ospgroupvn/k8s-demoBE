package com.osp.bttp.dao.service.dgts.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.contants.ExcelConstant;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.common.utils.DateUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.PaginationUtil;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.common.utils.UtilsDate;
import com.osp.bttp.common.utils.ValidationUtil;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.AuctionAttachFileDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCardInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctionCertificateInfoDto;
import com.osp.bttp.dao.model.dto.db3.AuctioneerBasicInfo;
import com.osp.bttp.dao.model.dto.db3.AuctioneerDataBodyExcel;
import com.osp.bttp.dao.model.dto.db3.AuctioneerDto;
import com.osp.bttp.dao.model.dto.db3.CreateOrUpdateAuctioneerRequest;
import com.osp.bttp.dao.model.dto.db3.SearchAuctioneerRequest;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.AuctionCardInfo;
import com.osp.bttp.dao.model.entity.db3.AuctionCertificateInfo;
import com.osp.bttp.dao.model.entity.db3.AuctionOrganization;
import com.osp.bttp.dao.model.entity.db3.Auctioneer;
import com.osp.bttp.dao.model.entity.db3.Category;
import com.osp.bttp.dao.model.entity.db3.DeptOfJustice;
import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.entity.db3.PlaceOfIssue;
import com.osp.bttp.dao.model.entity.db3.Province;
import com.osp.bttp.dao.model.entity.db3.Ward;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.mapper.db3.AuctionCardInfoMapper;
import com.osp.bttp.dao.model.mapper.db3.AuctionCertificateInfoMapper;
import com.osp.bttp.dao.model.mapper.db3.AuctioneerMapper;
import com.osp.bttp.dao.model.mview.db2.AuctioneerView;
import com.osp.bttp.dao.repository.bttp.NotaryInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctionCardInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctionCertificateInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctionOrganizationRepository;
import com.osp.bttp.dao.repository.db3.AuctioneerRepository;
import com.osp.bttp.dao.repository.db3.CategoryRepository;
import com.osp.bttp.dao.repository.db3.DeptOfJusticeRepository;
import com.osp.bttp.dao.repository.db3.PlaceOfIssueRepository;
import com.osp.bttp.dao.repository.db3.ProvinceRepository;
import com.osp.bttp.dao.repository.db3.WardRepository;
import com.osp.bttp.dao.repository.db3.predicate.AuctionCardInfoPredicate;
import com.osp.bttp.dao.repository.db3.predicate.AuctionCertificateInfoPredicate;
import com.osp.bttp.dao.repository.db3.predicate.AuctioneerPredicate;
import com.osp.bttp.dao.repository.db3.predicate.BasePredicate;
import com.osp.bttp.dao.repository.db4.LLawyerRepository;
import com.osp.bttp.dao.service.FileStorageService;
import com.osp.bttp.dao.service.dgts.AuctioneerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jett.transform.ExcelTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctioneerServiceImpl implements AuctioneerService {

    @PersistenceContext(unitName = "db2")
    private EntityManager entityManager;

    private final CategoryRepository categoryRepository;

    private final AuctioneerRepository auctioneerRepository;

    private final WardRepository wardRepository;

    private final DeptOfJusticeRepository deptOfJusticeRepository;

    private final AuctionCertificateInfoRepository auctionCertificateInfoRepository;

    private final AuctionCardInfoRepository auctionCardInfoRepository;

    private final ProvinceRepository provinceRepository;

    private final PlaceOfIssueRepository placeOfIssueRepository;

    private final AuctionOrganizationRepository organizationRepository;

    private final NotaryInfoRepository notaryInfoRepository;

    private final LLawyerRepository lawyerRepository;

    private final FileStorageService fileStorageService;

    private final AuctioneerMapper auctioneerMapper;

    private final AuctionCardInfoMapper auctionCardInfoMapper;

    private final AuctionCertificateInfoMapper auctionCertificateInfoMapper;

    private final ObjectMapper objectMapper;

    @Value("classpath:/template/Danh sach ho so DGV_template.xlsx")
    private Resource auctioneerTemplateFile;

    @Override
    public Optional<PagingResult> searchAuctioneer(PagingResult page, int numberPerPage, String fullname, Long province, String cerCode, Long sex, Long auctioneerStatus, Long cerStatus, Long cardStatus, Long actType, Long other, Long orgId) {
        int offset = 0;
        if (page.getPageNumber() > 0) {
            page.setNumberPerPage(numberPerPage);
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long cityId = null;
        if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
            Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
            if (H.isTrue(category)) {
                cityId = category.getId();
            }
            else {
                return Optional.ofNullable(page);
            }
        }
        if ( H.isTrue( orgId ) && orgId < 100L ) {
            Category category = categoryRepository.getByCodeAndCatType(orgId, Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
            if (H.isTrue(category)) {
                cityId = category.getId();
            }
            else {
                return Optional.ofNullable(page);
            }
        }

        String sql = "SELECT "
                + "	auc.*, "
                + "	his.ACT_TYPE,his.DATE_OF_DECISION,his.EFFECTIVE_DATE,"
                + "     his.CER_CODE,his.CARD_CODE,his2.ACT_TYPE AS ACT_TYPE_NEW,"
                + "		his2.EFFECTIVE_DATE AS EFFECTIVE_DATE_NEW,his.CREATED_BY, org.FULLNAME AS ORG_NAME, " +
                " CASE \n" +
                "        WHEN ac.name LIKE 'Tỉnh %' THEN REPLACE(ac.name, 'Tỉnh ', '') \n" +
                "        WHEN ac.name LIKE 'Thành Phố %' THEN REPLACE(ac.name, 'Thành Phố ', '')\n" +
                "        WHEN ac.name LIKE 'Thành phố %' THEN REPLACE(ac.name, 'Thành phố ', '')\n" +
                "        ELSE ac.name\n" +
                "    END AS DEPT_NAME, " +
                " org.ADDR as DEPT_ADDRESS "
                + "FROM "
                + "	(( "
                + "	SELECT "
                + "		a.id,a.AUCTIONEER_TYPE, a.GEN_DATE, a.LAST_UPDATED, a.ID_CODE, a.ID_TYPE, a.FULLNAME, " +
                "       a.DOB, " +
//                "       '' as DOB, " +
                "       a.SEX, "
                + "		 a.ADDR_PERMANENT, a.ADDR_CURRENT, a.EMAIL, a.TEL_NUMBER, a.OTHER_INFO, a.ID_DOI, a.ID_POI, a.CER_DOI, a.CARD_DOI, "
                + "		 a.CARD_POI, a.AUCTIONEER_STATUS, a.IS_PUBLISH,a.CER_STATUS, a.CARD_STATUS, a.ADDR_CITY_ID, a.ORG_ID  "
                + "	FROM "
                + "AIMS_AUCTIONEER a) auc "
                + "LEFT JOIN ( "
                + "	SELECT "
                + "		dd.auctioneer_id, "
                + "		dd.act_type, "
                + "		dd.DATE_OF_DECISION,"
                + "             dd.EFFECTIVE_DATE,dd.CER_CODE,dd.CARD_CODE,dd.CREATED_BY, dd.ORG_ID "
                + "	FROM "
                + "AIMS_AUCTIONEER_HIS dd "
                + "	INNER JOIN ( "
                + "		SELECT "
                + "			MAX(b.GEN_DATE) times, "
                + "			b.auctioneer_id auID "
                + "		FROM "
                + "AIMS_AUCTIONEER_HIS b "
                + "		WHERE "
                + "			b.ACT_TYPE IN (" + Constants.ACT_TYPE.CAP_MOI_CCHN + "," + Constants.ACT_TYPE.CAP_LAI_CCHN + "," + Constants.ACT_TYPE.THU_HOI_CCHN + ") "
                + " AND (TO_CHAR(b.EFFECTIVE_DATE,'YYYYMMDD')<=TO_CHAR(SYSDATE,'YYYYMMDD') OR b.ACT_TYPE = 1) "
                + " 		GROUP BY "
                + "			b.auctioneer_id) abc ON "
                + "		dd.auctioneer_id = abc.auid "
                + "		AND dd.gen_date = abc.times) his ON "
                + "	auc.id = his.AUCTIONEER_ID "
                + " LEFT JOIN ( "
                + "		SELECT "
                + "			dd.auctioneer_id, "
                + "			dd.act_type, "
                + "			dd.DATE_OF_DECISION, "
                + "			dd.EFFECTIVE_DATE, "
                + "			dd.CER_CODE, "
                + "			dd.CARD_CODE "
                + "		FROM "
                + "AIMS_AUCTIONEER_HIS dd "
                + "		INNER JOIN ( "
                + "			SELECT "
                + "				MAX(b.GEN_DATE) times, "
                + "				b.auctioneer_id auID "
                + "			FROM "
                + "AIMS_AUCTIONEER_HIS b "
                + "			WHERE "
                + "			b.ACT_TYPE IN (" + Constants.ACT_TYPE.CAP_MOI_CCHN + "," + Constants.ACT_TYPE.CAP_LAI_CCHN + "," + Constants.ACT_TYPE.THU_HOI_CCHN + ") "
                + "			GROUP BY "
                + "				b.auctioneer_id) abc ON "
                + "			dd.auctioneer_id = abc.auid "
                + "			AND dd.gen_date = abc.times) his2 ON "
                + "		auc.id = his2.AUCTIONEER_ID  "
                + " ) "
                + " LEFT JOIN AIMS_ORGANIZATION org ON auc.org_id = org.id "
                + " LEFT JOIN AIMS_DEPT_OF_JUSTICE dept ON ( org.ADDR_CITY_ID = dept.ADDR_CITY_ID AND org.ADDR_DISTRICT_ID = dept.ADDR_DISTRICT_ID ) "
                + " LEFT JOIN AIMS_CATEGORY ac ON org.ADDR_CITY_ID = ac.id ";


        String sqlQuery = "Select * from (" + sql + ") where AUCTIONEER_TYPE != " + Constants.Auctioneer.IS_DELETED;
        if (StringUtils.hasText(fullname)) {
            sqlQuery += " and upper(FULLNAME) like :fullname ";
        }
        if (province != null) {
            sqlQuery += " and ADDR_CITY_ID = :province ";
        }
        if (StringUtils.hasText(cerCode)) {
            sqlQuery += " and upper(CER_CODE) like :cerCode ";
        }
        if (sex != null) {
            sqlQuery += " and SEX = :sex ";
        }
        if (auctioneerStatus != null) {
            sqlQuery += " AUCTIONEER_STATUS = :auctioneerStatus ";
        }
        if (H.isTrue(cerStatus)) {
            sqlQuery += " and CER_STATUS = :cerStatus ";
        }
        if (H.isTrue(cardStatus)) {
            sqlQuery += " and CARD_STATUS = :cardStatus ";
        }
        if (actType != null) {
            sqlQuery += " and ACT_TYPE = :actType ";
        }
        if (other != null) {
            sqlQuery += " and ACT_TYPE_NEW = :other and TO_CHAR(EFFECTIVE_DATE_NEW,'yyyymmdd') > " + DateUtils.date2str(new Date(), "yyyyMMdd");
        }
        if (cityId != null) {
            sqlQuery += " and ADDR_CITY_ID = :cityId ";
        }
        if (orgId != null) {
            sqlQuery += " and org_id = :orgId ";
        }

        sqlQuery += " order by GEN_DATE DESC ";
        String sqlCount = "Select count(id) from (" + sqlQuery + ") where AUCTIONEER_TYPE != " + Constants.Auctioneer.IS_DELETED;


        sqlQuery = UtilData.paginationOracle(sqlQuery, offset, page.getNumberPerPage());
        Query query = entityManager.createNativeQuery(sqlQuery);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        if (StringUtils.hasText(fullname)) {
            query.setParameter("fullname", "%" + fullname.trim().toUpperCase() + "%");
            queryCount.setParameter("fullname", "%" + fullname.trim().toUpperCase() + "%");
        }
        if (province != null) {
            query.setParameter("province", province);
            queryCount.setParameter("province", province);
        }
        if (StringUtils.hasText(cerCode)) {
            query.setParameter("cerCode", "%" + cerCode.trim().toUpperCase() + "%");
            queryCount.setParameter("cerCode", "%" + cerCode.trim().toUpperCase() + "%");
        }
        if (sex != null) {
            query.setParameter("sex", sex);
            queryCount.setParameter("sex", sex);
        }
        if (auctioneerStatus != null) {
            query.setParameter("auctioneerStatus", auctioneerStatus);
            queryCount.setParameter("auctioneerStatus", auctioneerStatus);
        }
        if (H.isTrue(cerStatus)) {
            query.setParameter("cerStatus", cerStatus);
            queryCount.setParameter("cerStatus", cerStatus);
        }
        if (H.isTrue(cardStatus)) {
            query.setParameter("cardStatus", cardStatus);
            queryCount.setParameter("cardStatus", cardStatus);
        }
        if (actType != null) {
            query.setParameter("actType", actType);
            queryCount.setParameter("actType", actType);
        }
        if (other != null) {
            query.setParameter("other", other);
            queryCount.setParameter("other", other);
        }
        if (cityId != null) {
            query.setParameter("cityId", cityId);
            queryCount.setParameter("cityId", cityId);
        }
        if(orgId != null) {
            query.setParameter("orgId", orgId);
            queryCount.setParameter("orgId", orgId);
        }
        try {
            List<Object[]> listRaw = query.getResultList();
            List<AuctioneerView> list = new ArrayList<>();
            for (Object[] obj : listRaw) {
                AuctioneerView view = new AuctioneerView();
                view.setId(Long.valueOf(obj[0].toString()));
                view.setFullname(obj[6] == null ? "" : obj[6].toString());
                view.setAddFull(obj[9] == null ? "" : obj[9].toString());
                view.setCerStatus(obj[21] == null ? null : Long.valueOf(obj[21].toString()));
                view.setCerCode(obj[28] == null ? "" : obj[28].toString());
                view.setCardCode(obj[29] == null ? "" : obj[29].toString());
                view.setCardStatus(obj[22] == null ? null : Long.valueOf(obj[22].toString()));
                view.setDob(obj[7] == null ? "" : obj[7].toString());
                view.setOrgName(obj[33] == null ? "" : obj[33].toString());
                view.setDeptName(obj[34] == null ? "" : obj[34].toString());
                view.setDeptAddress(obj[35] == null ? "" : obj[35].toString());
                list.add(view);
            }
            if (list != null && list.size() > 0) {
                page.setItems(list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        int count = ((Number) queryCount.getSingleResult()).intValue();
        page.setRowCount(count);

        return Optional.ofNullable(page);
    }


    @Override
    public PaginationDto<AuctioneerBasicInfo> searchAuctioneers(SearchAuctioneerRequest request, Integer page, Integer size) {

        Pageable pageable = PaginationUtil.init(page, size, BasePredicate.DEFAULT_ORDER_PROPERTY);
        AuctioneerPredicate predicate = new AuctioneerPredicate()
                .withOrgId(request.getOrgId())
                .withText(request.getAuctionInfo())
                .withDepartmentCode(request.getDepartmentCode())
                .withCardLatestHasEffective(request.getCardStatus())
                .withCertLatestHasEffective(request.getCertStatus())
                .withAuctioneerOrOrgName(request.getAuctioneerOrOrg())
                .withProvinceCode(request.getProvinceCode());
        Page<Auctioneer> auctioneerPage = auctioneerRepository.findAll(predicate.getCriteria(), pageable);
        List<String> auctioneerIds = auctioneerPage.getContent().stream().map(Auctioneer::getUuid).toList();
        List<String> orgIds = new ArrayList<>();

        // Fetch card info for the auctioneers
        AuctionCardInfoPredicate cardInfoPredicate = new AuctionCardInfoPredicate()
                .withOrgId(request.getOrgId())
                .withAuctioneerIds(auctioneerIds)
                .effectiveToDate(LocalDate.now());
        List<AuctionCardInfo> cardInfos = auctionCardInfoRepository.findAll(
                cardInfoPredicate.getCriteria(),
                PaginationUtil.Sorting.by(AuctionCardInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
        );
        Map<String, AuctionCardInfo> cardInfoMap = new HashMap<>();
        cardInfos.forEach(cardInfo -> {
            if (cardInfo.getOrganization() != null) {
                orgIds.add(cardInfo.getOrganization().getUuid());
            }
            cardInfoMap.putIfAbsent(cardInfo.getAuctioneer().getUuid(), cardInfo);
        });

        // Fetch certificate info for the auctioneers
        AuctionCertificateInfoPredicate certInfoPredicate = new AuctionCertificateInfoPredicate()
                .withAuctioneerIds(auctioneerIds)
                .effectiveToDate(LocalDate.now());
        List<AuctionCertificateInfo> certInfos = auctionCertificateInfoRepository.findAll(
                certInfoPredicate.getCriteria(),
                PaginationUtil.Sorting.by(AuctionCertificateInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
        );
        Map<String, AuctionCertificateInfo> certInfoMap = new HashMap<>();
        certInfos.forEach(certInfo -> certInfoMap.putIfAbsent(certInfo.getAuctioneer().getUuid(), certInfo));

        // Fetch organization details for the auctioneers
        List<AuctionOrganization> organizations = organizationRepository.findAllById(orgIds);
        Map<String, AuctionOrganization> organizationMap = new HashMap<>();
        organizations.forEach(org -> organizationMap.put(org.getUuid(), org));

        // Fetch ward and province details for the organizations
        List<String> provinceCodes = new ArrayList<>();
        List<String> wardCodes = new ArrayList<>();
        organizations.forEach(org -> {
            if (StringUtils.hasText(org.getProvinceCode())) {
                provinceCodes.add(org.getProvinceCode());
            }
            if (StringUtils.hasText(org.getWardCode())) {
                wardCodes.add(org.getWardCode());
            }
        });
        List<Ward> wards = wardRepository.findAllByWardCodeIn(wardCodes);
        Map<String, Ward> wardMap = new HashMap<>();
        wards.forEach(ward -> wardMap.put(ward.getWardCode(), ward));

        List<Province> provinces = provinceRepository.findAllByProvinceCodeIn(provinceCodes);
        Map<String, Province> provinceMap = new HashMap<>();
        provinces.forEach(province -> provinceMap.put(province.getProvinceCode(), province));


        List<AuctioneerBasicInfo> ret = new ArrayList<>();
        for (Auctioneer auctioneer : auctioneerPage.getContent()) {
            AuctioneerBasicInfo basicInfo = AuctioneerBasicInfo.builder()
                    .dob(auctioneer.getDob())
                    .uuid(auctioneer.getUuid())
                    .fullName(auctioneer.getFullName())
                    .idCode(auctioneer.getIdCode())
                    .build();
            basicInfo.setCreatedBy(auctioneer.getCreatedBy());
            basicInfo.setCreatedDate(auctioneer.getCreatedDate());
            basicInfo.setLastModifiedDate(auctioneer.getLastModifiedDate());
            basicInfo.setUpdatedBy(auctioneer.getUpdatedBy());

            String auctioneerId = auctioneer.getUuid();
            if (certInfoMap.containsKey(auctioneerId)) {
                basicInfo.setCertCode(certInfoMap.get(auctioneerId).getCertCode());
                basicInfo.setDateOfDecisionCert(certInfoMap.get(auctioneerId).getDateOfDecision());
                basicInfo.setCertStatus(certInfoMap.get(auctioneerId).getStatus());
            }
            if (cardInfoMap.containsKey(auctioneerId)) {
                AuctionCardInfo cardInfo = cardInfoMap.get(auctioneerId);

                basicInfo.setCardCode(cardInfo.getCardCode());
                basicInfo.setIssueDate(cardInfo.getIssueDate());

                String orgId = cardInfo.getOrganization() == null ? null : cardInfo.getOrganization().getUuid();
                if (StringUtils.hasText(orgId) && organizationMap.containsKey(orgId)) {
                    AuctionOrganization organization = organizationMap.get(orgId);
                    basicInfo.setOrganizationName(organization.getFullName());
                    basicInfo.setOrganizationAddress(organization.getAddress());
                    basicInfo.setOrgWardCode(organization.getWardCode());
                    basicInfo.setOrgProvinceCode(organization.getProvinceCode());

                    if (wardMap.containsKey(organization.getWardCode())) {
                        basicInfo.setOrgWardName(wardMap.get(organization.getWardCode()).getName());
                    }

                    if (provinceMap.containsKey(organization.getProvinceCode())) {
                        basicInfo.setOrgProvinceName(provinceMap.get(organization.getProvinceCode()).getName());
                    }
                }
                basicInfo.setCardStatus(cardInfo.getStatus());
            }
            ret.add(basicInfo);
        }
        return new PaginationDto<>(ret, auctioneerPage.getTotalElements(), auctioneerPage.getTotalPages());
    }

    @Override
    @Transactional
    public AuctioneerDto createAuctioneer(CreateOrUpdateAuctioneerRequest request) throws InternalException {

        validateAuctioneerRequest(request);

        String idCode = request.getIdCode();
        AuctioneerPredicate predicate = new AuctioneerPredicate().withIdCode(idCode);
        List<Auctioneer> auctioneers = auctioneerRepository.findAll(predicate.getCriteria());
        if (!auctioneers.isEmpty()) {
            throw new InternalException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một Đấu giá viên khác");
        }

        Auctioneer auctioneer = Auctioneer.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .dob(request.getDob())
                .telNumber(request.getTelNumber())
                .email(request.getEmail())
                .idCode(request.getIdCode())
                .idDoi(request.getIdDoi())
                .idPoi(request.getIdPoi())
                .addPermanent(request.getAddPermanent())
                .provinceCode(request.getProvinceCode())
                .wardCode(request.getWardCode())
                .build();
        auctioneer = auctioneerRepository.save(auctioneer);
        return auctioneerMapper.toDto(auctioneer);
    }

    private void validateAuctioneerRequest(CreateOrUpdateAuctioneerRequest request) throws InternalException {

        List<String> invalidFields = ValidationUtil.validateBasicInfoOfAuctioneer(request);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        Optional<Ward> wardOpt = wardRepository.findByWardCode(request.getWardCode());
        if (wardOpt.isEmpty() || !wardOpt.get().getWardCode().equals(request.getWardCode())
                || !wardOpt.get().getProvinceCode().equals(request.getProvinceCode())) {
            throw new InternalException("Invalid ward code or province code");
        }

        Optional<PlaceOfIssue> placeOfIssue = placeOfIssueRepository.findByCode(request.getIdPoi());
        if (placeOfIssue.isEmpty()) {
            throw new InternalException("Invalid place of issue ID");
        }

        List<LLawyer> lawyers = lawyerRepository.findAllByIdentityCardNumber(request.getIdCode());
        if (!lawyers.isEmpty()) {
            throw new InternalException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một LSTN khác");
        }

        List<NotaryInfo> notaryInfos = notaryInfoRepository.findAllByIdNo(request.getIdCode());
        if (!notaryInfos.isEmpty()) {
            throw new InternalException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một Công chứng viên khác");
        }
    }

    @Override
    @Transactional
    public AuctioneerDto updateAuctioneer(String auctionId, CreateOrUpdateAuctioneerRequest request) throws InternalException {

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        validateAuctioneerRequest(request);

        String idCode = request.getIdCode();
        if (!idCode.equals(auctioneer.getIdCode())) {
            AuctioneerPredicate predicate = new AuctioneerPredicate().withIdCode(idCode);
            List<Auctioneer> auctioneers = auctioneerRepository.findAll(predicate.getCriteria());
            if (!auctioneers.isEmpty()) {
                throw new InternalException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một Đấu giá viên khác");
            }
        }
        // update auctioneer details
        auctioneer.setFullName(request.getFullName());
        auctioneer.setGender(request.getGender());
        auctioneer.setDob(request.getDob());
        auctioneer.setTelNumber(request.getTelNumber());
        auctioneer.setEmail(request.getEmail());
        auctioneer.setIdCode(request.getIdCode());
        auctioneer.setIdDoi(request.getIdDoi());
        auctioneer.setIdPoi(request.getIdPoi());
        auctioneer.setAddPermanent(request.getAddPermanent());
        auctioneer.setProvinceCode(request.getProvinceCode());
        auctioneer.setWardCode(request.getWardCode());
        auctioneer = auctioneerRepository.save(auctioneer);
        return auctioneerMapper.toDto(auctioneer);
    }

    @Override
    @Transactional
    public AuctionCertificateInfoDto addCertInfo(String auctionId, AuctionCertificateInfoDto request) throws InternalException {

        List<String> invalidFields = ValidationUtil.verifyCondition(request, Require.class);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        if (request.getEffectiveDate().isBefore(request.getDateOfDecision())) {
            throw new InternalException("Effective date cannot be before the date of decision");
        }

        List<AuctionCertificateInfo> certInfoExisting = auctionCertificateInfoRepository.findAllByCertCode(request.getCertCode());
        if (!certInfoExisting.isEmpty()) {
            throw new InternalException(String.format("Certificate with code `%s` already exists", request.getCertCode()));
        }

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        List<AuctionCertificateInfo> existingCerts = auctioneer.getAuctionCertificateInfos();

        AuctionCertificateInfo certificateInfo = AuctionCertificateInfo.builder()
                .certCode(request.getCertCode())
                .status(request.getStatus())
                .numberOfDecision(request.getNumberOfDecision())
                .dateOfDecision(request.getDateOfDecision())
                .effectiveDate(request.getEffectiveDate())
                .auctioneer(auctioneer)
                .build();
        certificateInfo = auctionCertificateInfoRepository.save(certificateInfo);
        existingCerts.add(certificateInfo);
        return auctionCertificateInfoMapper.toDto(certificateInfo);
    }

    @Override
    @Transactional
    public AuctionCertificateInfoDto updateCertInfo(String auctionId, String certId, AuctionCertificateInfoDto request) throws InternalException {

        List<String> invalidFields = ValidationUtil.verifyCondition(request, Require.class);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        if (request.getEffectiveDate().isBefore(request.getDateOfDecision())) {
            throw new InternalException("Effective date cannot be before the date of decision");
        }

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        AuctionCertificateInfoPredicate predicate = new AuctionCertificateInfoPredicate()
                .withAuctioneerId(auctioneer.getUuid())
                .withId(certId);
        List<AuctionCertificateInfo> auctionCertificateInfos = auctionCertificateInfoRepository.findAll(predicate.getCriteria());

        if (auctionCertificateInfos.isEmpty()) {
            throw new InternalException(String.format("Certificate with ID `%s` not found for auctioneer `%s`", certId, auctionId));
        }
        AuctionCertificateInfo certificateInfo = auctionCertificateInfos.get(0);

        AuctionCertificateInfoPredicate certPredicate = new AuctionCertificateInfoPredicate()
                .withNotInIds(List.of(certificateInfo.getUuid()))
                .withCertCode(request.getCertCode());
        List<AuctionCertificateInfo> certInfoExisting = auctionCertificateInfoRepository.findAll(certPredicate.getCriteria());
        if (!certInfoExisting.isEmpty()) {
            throw new InternalException(String.format("Certificate with code `%s` already exists", request.getCertCode()));
        }

        certificateInfo.setCertCode(request.getCertCode());
        certificateInfo.setStatus(request.getStatus());
        certificateInfo.setNumberOfDecision(request.getNumberOfDecision());
        certificateInfo.setDateOfDecision(request.getDateOfDecision());
        certificateInfo.setEffectiveDate(request.getEffectiveDate());
        certificateInfo = auctionCertificateInfoRepository.save(certificateInfo);
        return auctionCertificateInfoMapper.toDto(certificateInfo);
    }

    @Override
    @Transactional
    public void deleteCertInfo(String auctionId, List<String> certIds) throws InternalException {

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        AuctionCertificateInfoPredicate predicate = new AuctionCertificateInfoPredicate()
                .withAuctioneerId(auctioneer.getUuid())
                .withIds(certIds);
        List<AuctionCertificateInfo> auctionCertificateInfos = auctionCertificateInfoRepository.findAll(predicate.getCriteria());

        if (auctionCertificateInfos.isEmpty()) {
            throw new InternalException(String.format("Not found for auctioneer `%s`", auctionId));
        }
        auctionCertificateInfoRepository.deleteAll(auctionCertificateInfos);
    }

    @Override
    @Transactional
    public AuctionCardInfoDto addCardInfo(String auctionId, AuctionCardInfoDto request) throws InternalException {

        List<String> invalidFields = ValidationUtil.verifyCondition(request, Require.class);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        Optional<DeptOfJustice> deptOfJustice = deptOfJusticeRepository.findByCode(request.getDepartmentCode());
        if (deptOfJustice.isEmpty()) {
            throw new InternalException("Invalid department");
        }

        AuctionCardInfoPredicate cardInfoPredicate = new AuctionCardInfoPredicate()
                .withOrgId(request.getOrgId())
                .withCardCode(request.getCardCode());
        List<AuctionCardInfo> cardInfoExisting = auctionCardInfoRepository.findAll(cardInfoPredicate.getCriteria());
        if (!cardInfoExisting.isEmpty()) {
            throw new InternalException(String.format("Card with code `%s` already exists", request.getCardCode()));
        }

        Optional<AuctionOrganization> organizationOpt = organizationRepository.findById(request.getOrgId());
        if (organizationOpt.isEmpty()) {
            throw new InternalException(String.format("Organization with ID `%s` not found", request.getOrgId()));
        }
        AuctionOrganization organization = organizationOpt.get();

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        List<AuctionCardInfo> existingCards = auctioneer.getAuctionCardInfos();

        AuctionCardInfo cardInfo = AuctionCardInfo.builder()
                .cardCode(request.getCardCode())
                .status(request.getStatus())
                .numberOfDecision(request.getNumberOfDecision())
                .dateOfDecision(request.getDateOfDecision())
                .effectiveDate(request.getEffectiveDate())
                .issueDate(request.getIssueDate())
                .departmentCode(request.getDepartmentCode())
                .auctioneer(auctioneer)
                .organization(organization)
                .build();
        cardInfo = auctionCardInfoRepository.save(cardInfo);
        existingCards.add(cardInfo);
        return auctionCardInfoMapper.toDto(cardInfo);
    }

    @Override
    @Transactional
    public AuctionCardInfoDto updateCardInfo(String auctionId, String cardId, AuctionCardInfoDto request) throws InternalException {

        List<String> invalidFields = ValidationUtil.verifyCondition(request, Require.class);
        if (!invalidFields.isEmpty()) {
            throw new InternalException("Invalid request", invalidFields);
        }

        Optional<DeptOfJustice> deptOfJustice = deptOfJusticeRepository.findByCode(request.getDepartmentCode());
        if (deptOfJustice.isEmpty()) {
            throw new InternalException("Invalid department");
        }

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        AuctionCardInfoPredicate predicate = new AuctionCardInfoPredicate()
                .withAuctioneerId(auctioneer.getUuid())
                .withUuid(cardId);
        List<AuctionCardInfo> auctionCardInfos = auctionCardInfoRepository.findAll(predicate.getCriteria());

        if (auctionCardInfos.isEmpty()) {
            throw new InternalException(String.format("Card with ID `%s` not found for auctioneer `%s`", cardId, auctionId));
        }

        Optional<AuctionOrganization> organizationOpt = organizationRepository.findById(request.getOrgId());
        if (organizationOpt.isEmpty()) {
            throw new InternalException(String.format("Organization with ID `%s` not found", request.getOrgId()));
        }

        AuctionCardInfo cardInfo = auctionCardInfos.get(0);
        AuctionCardInfoPredicate cardInfoPredicate = new AuctionCardInfoPredicate()
                .withNotInIds(List.of(cardInfo.getUuid()))
                .withOrgId(request.getOrgId())
                .withCardCode(request.getCardCode());
        List<AuctionCardInfo> cardInfoExisting = auctionCardInfoRepository.findAll(cardInfoPredicate.getCriteria());
        if (!cardInfoExisting.isEmpty()) {
            throw new InternalException(String.format("Card with code `%s` already exists", request.getCardCode()));
        }

        cardInfo.setCardCode(request.getCardCode());
        cardInfo.setStatus(request.getStatus());
        cardInfo.setNumberOfDecision(request.getNumberOfDecision());
        cardInfo.setDateOfDecision(request.getDateOfDecision());
        cardInfo.setEffectiveDate(request.getEffectiveDate());
        cardInfo.setIssueDate(request.getIssueDate());
        cardInfo.setDepartmentCode(request.getDepartmentCode());
        cardInfo.setOrganization(organizationOpt.get());
        cardInfo = auctionCardInfoRepository.save(cardInfo);
        return auctionCardInfoMapper.toDto(cardInfo);
    }

    @Override
    public void deleteCardInfo(String auctionId, List<String> cardIds) throws InternalException {

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();

        AuctionCardInfoPredicate predicate = new AuctionCardInfoPredicate()
                .withAuctioneerId(auctioneer.getUuid())
                .withUuids(cardIds);
        List<AuctionCardInfo> auctionCardInfos = auctionCardInfoRepository.findAll(predicate.getCriteria());

        if (auctionCardInfos.isEmpty()) {
            throw new InternalException(String.format("Not found for auctioneer `%s`", auctionId));
        }
        auctionCardInfoRepository.deleteAll(auctionCardInfos);
    }

    @Override
    @Transactional
    public List<AuctionAttachFileDto> addCertAttachments(String certId, List<MultipartFile> files) throws InternalException {

        if (files == null || files.isEmpty()) {
            throw new InternalException("No files to upload");
        }

        Optional<AuctionCertificateInfo> certOpt = auctionCertificateInfoRepository.findById(certId);
        if (certOpt.isEmpty()) {
            throw new InternalException(String.format("Certificate with ID `%s` not found", certId));
        }

        List<AuctionAttachFileDto> ret = new ArrayList<>();
        AuctionCertificateInfo certInfo = certOpt.get();
        Map<String, AuctionAttachFileDto> attachments = new HashMap<>();
        if (StringUtils.hasText(certInfo.getFileObj())) {
            try {
                attachments = objectMapper.readValue(certInfo.getFileObj(), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
            } catch (Exception ex) {
                attachments = new HashMap<>();
            }
        }
        for (MultipartFile file : files) {
            String path = fileStorageService.uploadFile("cert", file);
            AuctionAttachFileDto fileDto = AuctionAttachFileDto.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(path)
                    .build();
            attachments.put(path, fileDto);
            ret.add(fileDto);
        }

        try {
            String fileObj = objectMapper.writeValueAsString(attachments);
            certInfo.setFileObj(fileObj);
        } catch (Exception ex) {
            log.error("Error serializing attachments for certificate ID {}: {}", certId, ex.getMessage());
        }
        return ret;
    }

    @Override
    @Transactional
    public void deleteCertAttachments(String certId, List<String> attachPaths) throws InternalException {

        if (attachPaths == null || attachPaths.isEmpty()) {
            throw new InternalException("No attachments to delete");
        }

        Optional<AuctionCertificateInfo> certOpt = auctionCertificateInfoRepository.findById(certId);
        if (certOpt.isEmpty()) {
            throw new InternalException(String.format("Certificate with ID `%s` not found", certId));
        }

        AuctionCertificateInfo certInfo = certOpt.get();
        if (StringUtils.hasText(certInfo.getFileObj())) {
            try {
                Map<String, AuctionAttachFileDto> attachments = objectMapper.readValue(certInfo.getFileObj(),
                        objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
                for (String path : attachPaths) {
                    if (attachments.containsKey(path)) {
                        attachments.remove(path);
                        fileStorageService.deleteFile(path);
                    }
                }
                String fileObject = objectMapper.writeValueAsString(attachments);
                certInfo.setFileObj(fileObject);
                auctionCertificateInfoRepository.save(certInfo);
            } catch (Exception ex) {
                log.error("Error processing attachments for certificate ID {}: {}", certId, ex.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public List<AuctionAttachFileDto> addCardAttachments(String cardId, List<MultipartFile> files) throws InternalException {

        if (files == null || files.isEmpty()) {
            throw new InternalException("No files to upload");
        }

        Optional<AuctionCardInfo> cardOpt = auctionCardInfoRepository.findById(cardId);
        if (cardOpt.isEmpty()) {
            throw new InternalException(String.format("Card with ID `%s` not found", cardId));
        }

        List<AuctionAttachFileDto> ret = new ArrayList<>();
        AuctionCardInfo cardInfo = cardOpt.get();
        Map<String, AuctionAttachFileDto> attachments = new HashMap<>();
        if (StringUtils.hasText(cardInfo.getFileObj())) {
            try {
                attachments = objectMapper.readValue(cardInfo.getFileObj(), objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
            } catch (Exception ex) {
                attachments = new HashMap<>();
            }
        }

        for (MultipartFile file : files) {
            String path = fileStorageService.uploadFile("card", file);
            AuctionAttachFileDto fileDto = AuctionAttachFileDto.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(path)
                    .build();
            attachments.put(path, fileDto);
            ret.add(fileDto);
        }

        try {
            String fileObj = objectMapper.writeValueAsString(attachments);
            cardInfo.setFileObj(fileObj);
        } catch (Exception ex) {
            log.error("Error upload attachments for certificate ID {}: {}", cardId, ex.getMessage());
        }
        return ret;
    }

    @Override
    @Transactional
    public void deleteCardAttachments(String cardId, List<String> attachPaths) throws InternalException {

        if (attachPaths == null || attachPaths.isEmpty()) {
            throw new InternalException("No attachments to delete");
        }

        Optional<AuctionCardInfo> cardOpt = auctionCardInfoRepository.findById(cardId);
        if (cardOpt.isEmpty()) {
            throw new InternalException(String.format("Card with ID `%s` not found", cardId));
        }

        AuctionCardInfo cardInfo = cardOpt.get();
        if (StringUtils.hasText(cardInfo.getFileObj())) {
            try {
                Map<String, AuctionAttachFileDto> attachments = objectMapper.readValue(cardInfo.getFileObj(),
                        objectMapper.getTypeFactory().constructMapType(Map.class, String.class, AuctionAttachFileDto.class));
                for (String path : attachPaths) {
                    if (attachments.containsKey(path)) {
                        attachments.remove(path);
                        fileStorageService.deleteFile(path);
                    }
                }
                String fileObject = objectMapper.writeValueAsString(attachments);
                cardInfo.setFileObj(fileObject);
                auctionCardInfoRepository.save(cardInfo);
            } catch (Exception ex) {
                log.error("Error processing attachments for card ID {}: {}", cardId, ex.getMessage());
            }
        }
    }

    @Override
    public AuctioneerDto getAuctioneerById(String auctionId) throws InternalException {

        Optional<Auctioneer> auctioneerOpt = auctioneerRepository.findById(auctionId);
        if (auctioneerOpt.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with ID `%s` not found", auctionId));
        }
        Auctioneer auctioneer = auctioneerOpt.get();
        return mappingToAuctioneerDto(auctioneer);
    }

    private AuctioneerDto mappingToAuctioneerDto(Auctioneer auctioneer) {
        AuctioneerDto ret = getAuctioneerBasicInfo(auctioneer);

        List<AuctionCertificateInfoDto> certInfoDtoList = getCertInfos(auctioneer);
        ret.setAuctionCertificateInfos(certInfoDtoList);

        AuctionCardInfoPredicate auctionCardPredicate = new AuctionCardInfoPredicate().withAuctioneerId(auctioneer.getUuid());
        List<AuctionCardInfo> cardInfos = auctionCardInfoRepository.findAll(
                auctionCardPredicate.getCriteria(),
                PaginationUtil.Sorting.by(AuctionCardInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
        );
        List<AuctionCardInfoDto> cardInfoDtoList = auctionCardInfoMapper.toDtoList(cardInfos);
        ret.setAuctionCardInfos(cardInfoDtoList);
        return ret;
    }

    private AuctioneerDto getAuctioneerBasicInfo(Auctioneer auctioneer) {
        AuctioneerDto ret = auctioneerMapper.toDto(auctioneer);

        Optional<PlaceOfIssue> placeOfIssue = placeOfIssueRepository.findByCode(auctioneer.getIdPoi());
        placeOfIssue.ifPresent(poi -> ret.setTextPoi(poi.getName()));

        Optional<Ward> wardOpt = wardRepository.findByWardCode(auctioneer.getWardCode());
        wardOpt.ifPresent(ward -> ret.setTextWard(ward.getName()));

        Optional<Province> provinceOpt = provinceRepository.findByProvinceCode(auctioneer.getProvinceCode());
        provinceOpt.ifPresent(province -> ret.setTextProvince(province.getName()));

        return ret;
    }

    private List<AuctionCertificateInfoDto> getCertInfos(Auctioneer auctioneer) {
        AuctionCertificateInfoPredicate auctionCertPredicate = new AuctionCertificateInfoPredicate().withAuctioneerId(auctioneer.getUuid());
        List<AuctionCertificateInfo> certInfos = auctionCertificateInfoRepository.findAll(
                auctionCertPredicate.getCriteria(),
                PaginationUtil.Sorting.by(AuctionCertificateInfoPredicate.EFFECTIVE_DATE_ORDER_PROPERTY)
        );
        return auctionCertificateInfoMapper.toDtoList(certInfos);
    }

    @Override
    @Transactional
    public void deleteAuctioneer(List<String> auctioneerIds) throws InternalException {

        if (auctioneerIds == null || auctioneerIds.isEmpty()) {
            throw new InternalException("No auctioneer IDs provided for deletion");
        }
        List<Auctioneer> auctioneers = auctioneerRepository.findAllById(auctioneerIds);
        if (!auctioneers.isEmpty()) {
            auctioneerRepository.deleteAll(auctioneers);
        }
    }

    @Override
    public byte[] export(SearchAuctioneerRequest request, Integer page, Integer size) {
        PaginationDto<AuctioneerBasicInfo> dataPage = searchAuctioneers(request, page, size);
        List<AuctioneerBasicInfo> auctioneers = dataPage.getData();

        List<AuctioneerDataBodyExcel> data = new ArrayList<>();
        for (AuctioneerBasicInfo auctioneer : auctioneers) {
            data.add(AuctioneerDataBodyExcel.builder()
                    .fullName(auctioneer.getFullName())
                    .dob(UtilsDate.getLocalDateStr(auctioneer.getDob()))
                    .idCode(auctioneer.getIdCode())
                    .certStatus(auctioneer.getCertStatus() != null ? auctioneer.getCertStatus().getText() : "")
                    .cardStatus(auctioneer.getCardStatus() != null ? auctioneer.getCardStatus().getText() : "")
                    .certCode(auctioneer.getCertCode())
                    .dateOfDecisionCert(UtilsDate.getLocalDateStr(auctioneer.getDateOfDecisionCert()))
                    .cardCode(auctioneer.getCardCode())
                    .issueDate(UtilsDate.getLocalDateStr(auctioneer.getIssueDate()))
                    .createdBy(auctioneer.getCreatedBy())
                    .updatedBy(auctioneer.getUpdatedBy())
                    .createdDate(DateUtils.convertDateToStringWithType(auctioneer.getCreatedDate(), DateUtils.DATE_TIME_FORMAT))
                    .lastModifiedDate(DateUtils.convertDateToStringWithType(auctioneer.getLastModifiedDate(), DateUtils.DATE_TIME_FORMAT))
                    .build());
        }

        Map<String, Object> context = new HashMap<>();
        context.put(ExcelConstant.ARG_DATA_BODY, data);
        context.put(ExcelConstant.ARG_TOTAL_ELEMENT, dataPage.getTotalItem());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (InputStream template = new ByteArrayInputStream(auctioneerTemplateFile.getContentAsByteArray())) {
            ExcelTransformer transformer = new ExcelTransformer();
            Workbook workbook = transformer.transform(template, context);
            workbook.write(output);
        } catch (Exception exception) {
            log.error("Fail to generate the document", exception);
        }

        return output.toByteArray();
    }

    @Override
    public AuctioneerDto getAuctioneer(String certCode) throws InternalException {

        if (!StringUtils.hasText(certCode)) {
            throw new InternalException("Invalid request");
        }

        AuctioneerPredicate predicate = new AuctioneerPredicate().withCertCode(certCode);
        List<Auctioneer> auctioneers = auctioneerRepository.findAll(predicate.getCriteria());
        if (auctioneers.isEmpty()) {
            throw new InternalException(String.format("Auctioneer with certificate code `%s` not found", certCode));
        }

        Auctioneer auctioneer = auctioneers.get(0);

        AuctioneerDto ret = getAuctioneerBasicInfo(auctioneer);
        ret.setAuctionCardInfos(null);
        ret.setAuctionCertificateInfos(null);
        List<AuctionCertificateInfoDto> certInfoDtoList = getCertInfos(auctioneer);

        if (!certInfoDtoList.isEmpty()) {
            ret.setAuctionCertificateInfos(List.of(certInfoDtoList.get(0)));
        }
        return ret;
    }

    @Override
    public Boolean checkCertCodeExist(String certCode, String certId) throws InternalException {
        AuctionCertificateInfoPredicate predicate = new AuctionCertificateInfoPredicate()
                .withCertCode(certCode);
        if (StringUtils.hasText(certId)) {
            predicate = predicate.withNotInIds(List.of(certId));
        }
        List<AuctionCertificateInfo> certInfos = auctionCertificateInfoRepository.findAll(predicate.getCriteria());
        return !certInfos.isEmpty();
    }
}
