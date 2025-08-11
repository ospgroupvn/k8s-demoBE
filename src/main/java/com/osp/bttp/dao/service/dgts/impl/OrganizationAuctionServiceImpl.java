package com.osp.bttp.dao.service.dgts.impl;

import com.osp.bttp.common.QueryBuilder;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.dto.PagingResultExt;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.CenterOrganizationAuctionner;
import com.osp.bttp.dao.model.dto.db2.AuctioneerOfProvinceDto;
import com.osp.bttp.dao.model.dto.db2.ReportPublicAuctionAssetByDay;
import com.osp.bttp.dao.model.entity.db2.*;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.Category;
import com.osp.bttp.dao.model.mview.db2.*;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.repository.db3.CategoryRepository;
import com.osp.bttp.dao.service.dgts.AuctioneerDAO;
import com.osp.bttp.dao.service.tccc.TcccService;
import com.osp.bttp.dao.service.dgts.OrganizationAuctionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sangnk
 * @Created 09/10/2024 - 4:27 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class OrganizationAuctionServiceImpl implements OrganizationAuctionService {
    @PersistenceContext(unitName = "db2")
    private EntityManager entityManager;

    @Autowired
    private TcccService tcccService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;
    

    @Autowired
    private AuctioneerDAO auctioneerDAO;

    @Override
    public Optional<PagingResult> getAllOrganizationAuction(PagingResult page, int numberPerPage, String name, Long cityId, Long orgId, Long status, String orgType) {
        int offset = 0;

        //        Long adminId = 0L;
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
            Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
            if (H.isTrue(category)) {
                cityId = category.getId();
            } else {
                return Optional.ofNullable(page);
            }
        }
        if (H.isTrue(orgId) && orgId < 100L) {
            Category category = categoryRepository.getByCodeAndCatType(orgId, Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
            if (H.isTrue(category)) {
                cityId = category.getId();
            } else {
                return Optional.ofNullable(page);
            }
        }
        String dieukien = "";
        List<Integer> listOrgType = new ArrayList<>();
        if (H.isTrue(orgType)) {
            String[] orgTypeArr = orgType.split(",");
            for (String orgTypeStr : orgTypeArr) {
                listOrgType.add(Integer.parseInt(orgTypeStr));
            }
            dieukien = dieukien + " AND a.ORG_TYPE IN :orgType ";
        }
        if (name != null) {
            dieukien = dieukien + " AND LOWER (a.FULLNAME) LIKE :name ";
        }
//        if (orgId != null) {
//            dieukien = dieukien + " AND a.ID = :orgId ";
//        }
        if (cityId != null) {
            dieukien = dieukien + " AND a.ADDR_CITY_ID = :cityId ";
        }
        if (H.isTrue(status)) {
            if (status.intValue() == 15) {
                dieukien = dieukien + " AND (a.STATUS = :status or a.STATUS = 16) ";
            }
            else dieukien = dieukien + " AND a.STATUS = :status ";
        }
//        else {
//            dieukien = dieukien + " AND a.STATUS =" + Constants.Organization.STATUS.ORG_STATUS_ACTIVE + " ";
//        }
        if (page.getPageNumber() > 0) {
            page.setNumberPerPage(numberPerPage);
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        String init = "" +
                "WITH latest_auctioneer AS (\n" +
                "    SELECT\n" +
                "        a.org_id, \n" +
                "        a.auctioneer_id, \n" +
                "            b.fullname,\n" +
                "            ROW_NUMBER() OVER (PARTITION BY a.org_id ORDER BY a.id DESC) AS rn\n" +
                "    FROM\n" +
                "        aims_auctioneer_his a \n" +
                "            LEFT JOIN aims_auctioneer b ON a.auctioneer_id = b.id\n" +
                "    WHERE\n" +
                "        a.auctioneer_type = 2 \n" +
                "        AND a.source_log = 0 \n" +
                ") \n";
        String sql = "\n" +
                " SELECT\n" +
                "    info.*,  \n" +
                "    DECODE(count_auctioneer.count_auctioneer, NULL, 0, count_auctioneer.count_auctioneer) AS QUANTITY_AUCTIONEER\n" +
                "FROM\n" +
                "    (\n" +
                "    SELECT\n" +
                "        a.ID,\n" +
                "        a.org_type, -- Loại hình tổ chức\n" +
                "        CASE \n" +
                "            WHEN a.org_root = 0 THEN a.fullname \n" +
                "            ELSE a.fullname || ' - ' || d.fullname \n" +
                "        END AS FULLNAME,\n" +
                "        b.fullname AS AUCTIONEER_NAME, \n" +
                "        a.org_root, \n" +
                "        a.ADDR_DISTRICT_ID, \n" +
                "        a.addr_city_id, \n" +
                "        (a.Addr || ',' || aicDistrict.name || ',' ||  aicProvince.name ) AS addr,\n" +
                "        a.eff_date, a.status, " +
                " CASE \n" +
                "        WHEN aicProvince.NAME LIKE 'Tỉnh %' THEN REPLACE(aicProvince.NAME, 'Tỉnh ', '') \n" +
                "        WHEN aicProvince.NAME LIKE 'Thành Phố %' THEN REPLACE(aicProvince.NAME, 'Thành Phố ', '')\n" +
                "        WHEN aicProvince.NAME LIKE 'Thành phố %' THEN REPLACE(aicProvince.NAME, 'Thành phố ', '')\n" +
                "        WHEN aicProvince.NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(aicProvince.NAME, 'Sở Tư Pháp ', '')\n" +
                "        ELSE aicProvince.NAME \n" +
                "    END AS PROVINCE " +
//                "        aicProvince.NAME AS PROVINCE -- Ngày hiệu lực\n" +
                "    FROM\n" +
                "        aims_organization a -- Bảng tổ chức\n" +
                "        LEFT JOIN aims_organization d ON a.org_root = d.id\n" +
                "        LEFT JOIN aims_category aicProvince ON a.addr_city_id = aicProvince.id\n" +
                "        LEFT JOIN aims_category aicDistrict ON a.ADDR_DISTRICT_ID = aicDistrict.id\n" +
                "        LEFT JOIN latest_auctioneer b ON a.id = b.org_id and b.rn = 1 \n" +
                "    WHERE\n" +
                "        (a.status != 3 OR a.status IS NULL)\n" + dieukien +
                "            AND ((a.org_type IN (0, 1, 2, 11) AND b.fullname IS NOT NULL )  OR a.org_type IN (4))\n" +
                "    ) info\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            org.id, -- ID tổ chức\n" +
                "            COUNT(au.id) AS count_auctioneer \n" +
                "        FROM\n" +
                "            aims_organization org\n" +
                "        JOIN aims_auctioneer au ON org.id = au.org_id\n" +
                "        WHERE\n" +
                "            au.card_code IS NOT NULL \n" +
                "        GROUP BY\n" +
                "            org.id \n" +
                "    ) count_auctioneer ON info.ID = count_auctioneer.ID \n" +
                "ORDER BY\n" +
                "    PROVINCE, DECODE(org_type,0,1,  2,2,  1,3,  11,4,  5)   \n";
        String sqlCount = "select count(*) from (" + sql + ")";
        sql = UtilData.paginationOracle(sql, offset, page.getNumberPerPage());
        sqlCount = init + sqlCount;
        sql = init + sql;
        Query query = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        if (name != null) {
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            queryCount.setParameter("name", "%" + name.toLowerCase() + "%");
        }
//        if (orgId != null) {
//            query.setParameter("orgId", orgId);
//            queryCount.setParameter("orgId", orgId);
//        }
        if (cityId != null) {
            query.setParameter("cityId", cityId);
            queryCount.setParameter("cityId", cityId);
        }
        if (H.isTrue(status)) {
            query.setParameter("status", status);
            queryCount.setParameter("status", status);
        }
        if (H.isTrue(listOrgType)) {
            query.setParameter("orgType", listOrgType);
            queryCount.setParameter("orgType", listOrgType);
        }
        try {
            List<Object[]> listRaw = query.getResultList();
            List<OrganizationView> list = new ArrayList<>();
            for (Object[] obj : listRaw) {
                OrganizationView item = new OrganizationView();
                item.setId(H.isTrue(obj[0]) ? Long.parseLong(obj[0].toString()) : null);
                item.setOrgType(H.isTrue(obj[1]) ? Long.parseLong(obj[1].toString()) : null);
                item.setFullname(H.isTrue(obj[2]) ? obj[2].toString() : null);
                item.setAuctioneerName(H.isTrue(obj[3]) ? obj[3].toString() : null);
                item.setOrgRoot(H.isTrue(obj[4]) ? Long.parseLong(obj[4].toString()) : null);
                item.setDistrictId(H.isTrue(obj[5]) ? Long.parseLong(obj[5].toString()) : null);
                item.setCityId(H.isTrue(obj[6]) ? Long.parseLong(obj[6].toString()) : null);
                item.setAddress(H.isTrue(obj[7]) ? obj[7].toString() : null);
                item.setEffDate(H.isTrue(obj[8]) ? (Date) obj[8] : null);
                item.setStatus(H.isTrue(obj[9]) ? Long.parseLong(obj[9].toString()) : null);
                item.setProvince(H.isTrue(obj[10]) ? obj[10].toString() : null);
                item.setQuantityAuctioneer(H.isTrue(obj[11]) ? Long.parseLong(obj[11].toString()) : null);
                list.add(item);
            }
            page.setItems(list);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            int count = ((Number) queryCount.getSingleResult()).intValue();
            page.setRowCount(count);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.ofNullable(page);
    }

    @Override
    public Optional<PagingResultExt> getDetailAuctioneer(PagingResultExt page, Long id) {
        int offset = 0;
        String sql = "SELECT"
                + "	ID,FULLNAME,CARD_CODE,CER_CODE,ADDR_FULL,DOB,ORG_ID "
                + " FROM "
                + "	("
                + "	SELECT"
                + "		*"
                + "	FROM "
                + "AIMS_AUCTIONEER a "
                + "	WHERE a.id = ? "
                + "		AND a.IS_PUBLISH = 1) aa"
                + " LEFT JOIN ("
                + "	SELECT "
                + "		au.ID AS AU_ID,"
                + "		(au.ADDR_PERMANENT || ', ' || catDist.NAME || ', ' || au.city) AS ADDR_FULL "
                + "	FROM "
                + "		("
                + "		SELECT"
                + "			a.id,"
                + "			a.ADDR_DISTRICT_ID,"
                + "			a.ADDR_PERMANENT,"
                + "			cat.NAME AS city"
                + "		FROM "
                + "AIMS_AUCTIONEER a,"
                + "AIMS_CATEGORY cat"
                + "		WHERE"
                + "			a.ADDR_CITY_ID = cat.id) au"
                + "	LEFT JOIN "
                + "AIMS_CATEGORY catDist ON"
                + "		au.ADDR_DISTRICT_ID = catDist.ID) addr ON"
                + "	aa.id = addr.AU_ID ";
        Query query = entityManager.createNativeQuery(sql, AuctioneerView.class);
        query.setParameter(1, id);
        try {
            List<Object[]> list = query.getResultList();
            if (list != null && list.size() > 0) {
                page.setItems(list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //lay lich su dau gia vien
        if (page.getPageNumber() > 0) {
            page.setNumberPerPage(10);
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        sql = "select a.*,b.FULLNAME as ORG_NAME,b.ADDR as ORG_ADDRESS from "
                + "AIMS_AUCTIONEER_HIS a," + "AIMS_ORGANIZATION b where a.ORG_ID=b.ID and a.id=?";
        sql = UtilData.paginationOracle(sql, offset, page.getNumberPerPage());
        query = entityManager.createNativeQuery(sql, AuctioneerHistoryView.class);
        query.setParameter(1, id);
        try {
            List<Object[]> list = query.getResultList();
            if (list != null && list.size() > 0) {
                page.setItemsExt(list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        StringBuffer sqlBufferCount = new StringBuffer("select count(a.id) from AuctioneerHistory a");
        QueryBuilder buildercount = new QueryBuilder(entityManager, sqlBufferCount);
        buildercount.and(QueryBuilder.EQ, "a.auctioneerID", id);
        Query queryCount = buildercount.initQuery(false);
        Long rowCount = (Long) queryCount.getSingleResult();
        if (rowCount != null) {
            page.setRowCount(rowCount.longValue());
        }
        return Optional.ofNullable(page);
    }

    @Override
    public CenterOrganizationAuctionner viewDetailOrg(Long orgId) {
        List<Organization> ltsOrganizations = new ArrayList<>();
        List<Organization> dscnvaDN = new ArrayList<>();
        dscnvaDN = danhsachChiNhanhAll(orgId);
        Organization organization = new Organization();
        organization = entityManager.find(Organization.class, orgId);
        if (!H.isTrue(organization)) {
            return null;
        }
        String tp = "";
        String qh = "";
        if (organization.getDistrictId() != null) {
            qh = entityManager.find(AuCategory.class, organization.getDistrictId()).getName();
            organization.setAddress(organization.getAddress().concat(", ").concat(qh));
        }
        if (organization.getCityId() != null) {
            tp = entityManager.find(AuCategory.class, organization.getCityId()).getName();
            organization.setAddress(organization.getAddress().concat(", ").concat(tp));
        }

        entityManager.clear();
        entityManager.close();


        List<Auctioneer> lts = new ArrayList<>();
        lts = findAuctioneersByOrgId(orgId);
        Long status = organization.getStatus();
        Auctioneer manager = findManagerByOrgId(orgId, status);
        String tp2 = "";
        String qh2 = "";
        if (manager.getDistrictId() != null) {
            qh2 = entityManager.find(AuCategory.class, manager.getDistrictId()).getName();
            manager.setAddPermanent(manager.getAddPermanent().concat(", ").concat(qh2));
            entityManager.clear();
            entityManager.close();
        }
        if (manager.getCityId() != null) {
            tp2 = entityManager.find(AuCategory.class, manager.getCityId()).getName();
            manager.setAddPermanent(manager.getAddPermanent().concat(", ").concat(tp2));
            entityManager.clear();
            entityManager.close();
        }
        entityManager.clear();
        entityManager.close();
        List<AuMemberParter> listTVHD = new ArrayList<>();
        listTVHD = TimTVHD(orgId);
        CenterOrganizationAuctionner centerOrganizationAuctionner = new CenterOrganizationAuctionner();
        centerOrganizationAuctionner.setAuctioneers(lts);
        centerOrganizationAuctionner.setManager(manager);
        centerOrganizationAuctionner.setLtsMemberParters(listTVHD);


        centerOrganizationAuctionner.setOrganization(organization);

        List<OrganizationHis> ltsOrganizationHises = new ArrayList<>();
        ltsOrganizationHises = findListDsQD(organization.getId());
        centerOrganizationAuctionner.setListOrganizationHises(ltsOrganizationHises);
        if (organization.getOrgType() == 11) {
            Organization orgDN = new Organization();
            orgDN = entityManager.find(Organization.class, organization.getOrgRoot());
            String tp3 = "";
            String qh3 = "";
            if (orgDN.getDistrictId() != null) {
                qh3 = entityManager.find(AuCategory.class, orgDN.getDistrictId()).getName();
                orgDN.setAddress(orgDN.getAddress().concat(", ").concat(qh3));
                entityManager.clear();
                entityManager.close();
            }
            if (orgDN.getCityId() != null) {
                tp3 = entityManager.find(AuCategory.class, orgDN.getCityId()).getName();
                orgDN.setAddress(orgDN.getAddress().concat(", ").concat(tp3));
                entityManager.clear();
                entityManager.close();
            }

            centerOrganizationAuctionner.setToChucDuocSN(orgDN);
        }
        centerOrganizationAuctionner.setListCNvaVP(dscnvaDN);

        return centerOrganizationAuctionner;
    }

    @Override
    public Organization findId(Long id) {
        Organization organization = new Organization();
        organization = entityManager.find(Organization.class, id);
        String qh = "";
        if (organization.getDistrictId() != null) {
            qh = entityManager.find(AuCategory.class, organization.getDistrictId()).getName();
            organization.setAddress(organization.getAddress().concat(", ").concat(qh));
            entityManager.clear();
            entityManager.close();
        }
        String tp = "";
        if (organization.getCityId() != null) {
            tp = entityManager.find(AuCategory.class, organization.getCityId()).getName();
            organization.setAddress(organization.getAddress().concat(", ").concat(tp));
            entityManager.clear();
            entityManager.close();
        }


        return organization;
    }


    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportQuantityOrganizationAuction(String cityId, String orgType, String fromDateRaw, String toDateRaw, int pageNumber, int numberPerPage, String aTypes, Integer status, boolean getDetailDistrict) {
        PagingResult result = new PagingResult();
        List<Reaport> items = new ArrayList<>();

        try {
            List<String> cityIds = new ArrayList<>();
            List<String> cityIds_bk = new ArrayList<>();
            if (H.isTrue(cityId)) {
                String[] cityIdArr = cityId.split(",");
                for (String cityIdStr : cityIdArr) {
                    cityIds.add(cityIdStr);
                }
            }

            if (H.isTrue(cityIds) && cityIds.get(0).length() < 3) {
                for (String cityIdStr : cityIds) {
                    Category category = categoryRepository.getByCodeAndCatType(Long.valueOf(cityIdStr), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                    if (H.isTrue(category)) {
                        cityIds_bk.add(category.getId().toString());
                    }
                }
            }

            if (H.isTrue(cityIds_bk)) {
                cityIds = cityIds_bk;
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityIds = Collections.singletonList(category.getId().toString());
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị quản lý", null), HttpStatus.OK);
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            int offset = 0;
            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);

            List<Integer> orgTypesList = new ArrayList<>();
            if (H.isTrue(orgType)) {
                String[] orgTypeArr = orgType.split(",");
                for (String orgTypeStr : orgTypeArr) {
                    orgTypesList.add(Integer.parseInt(orgTypeStr));
                }
            }

            if (H.isTrue(aTypes)) {
                String[] aTypesArr = aTypes.split(",");
                for (String aTypeStr : aTypesArr) {
                    if (aTypeStr.equals("2")) {
                        orgTypesList.add(0);
                    } else if (aTypeStr.equals("3")) {
                        orgTypesList.add(1);
                    } else if (aTypeStr.equals("4")) {
                        orgTypesList.add(2);
                    } else if (aTypeStr.equals("5")) {
                        orgTypesList.add(11);
                    } else if (aTypeStr.equals("6")) {
                        orgTypesList.add(4);
                    }
                }
            }

            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(cityIds)) {
                whereClause += " AND aio.ADDR_CITY_ID in :cityId ";
                if(getDetailDistrict)  whereClauseAddress += " AND parent_id in :cityId ";
                else whereClauseAddress += " AND id in :cityId ";
            }
            if (fromDate != null) {
                whereClause += " AND aio.LICENSE_DATE >= :fromDate ";
            }
            if (toDate != null) {
                whereClause += " AND aio.LICENSE_DATE <= :toDate ";
            }
            if (H.isTrue(orgTypesList)) {
                whereClause += " AND aio.ORG_TYPE IN :orgTypes ";
            }
            if (H.isTrue(status)) {
                whereClause += " AND aio.STATUS = :status ";
            }

            String init = "" +
                    " WITH distinct_province AS (\n" +
                    " SELECT " +
//                    " name as province_name " +
                    " CASE \n" +
                    "        WHEN name LIKE 'Tỉnh %' THEN REPLACE(name, 'Tỉnh ', '') \n" +
                    "        WHEN name LIKE 'Thành Phố %' THEN REPLACE(name, 'Thành Phố ', '')\n" +
                    "        WHEN name LIKE 'Thành phố %' THEN REPLACE(name, 'Thành phố ', '')\n" +
                    "        WHEN name LIKE 'Sở Tư Pháp %' THEN REPLACE(name, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE name \n" +
                    "    END AS PROVINCE_NAME " +
                    " from AIMS_CATEGORY\n";
            if (getDetailDistrict) {
                init += " WHERE CAT_TYPE = 'QH' \n ";
            } else init += " WHERE CAT_TYPE = 'TP' \n ";

            init += "" + whereClauseAddress + " \n" +
                    " group by name ORDER BY name\n" +
                    ") ";
            String sql = "" +

                    " SELECT \n" +
                    "    dka.PROVINCE_NAME AS dbhc, \n" +
                    "    TO_NUMBER(decode(tt_dgts, null, 0, tt_dgts)) AS tt_dich_vu_dgts,\n" +
                    "    TO_NUMBER(decode(dgtn, null, 0, dgtn)) AS dgtn,\n" +
                    "    TO_NUMBER(decode(dghd, null, 0, dghd)) AS dghd,\n" +
                    "    TO_NUMBER(decode(cndn_dgts, null, 0, cndn_dgts)) AS cndn_dgts,\n" +
                    "    TO_NUMBER(decode(\"VAMC\", null, 0, \"VAMC\")) AS vamc\n" +
                    "FROM distinct_province dka left join (\n" +
                    "SELECT * \n" +
                    "    FROM (\n" +
                    "        SELECT \n" +
                    "            COUNT(1) AS total,\n" +
                    "            CASE \n" +
                    "                WHEN aio.org_type = 0 THEN 'tt_dgts'\n" +
                    "                WHEN aio.org_type = 1 THEN 'dgtn'\n" +
                    "                WHEN aio.org_type = 2 THEN 'dghd'\n" +
                    "                WHEN aio.org_type = 11 THEN 'cndn_dgts'\n" +
                    "                WHEN aio.org_type = 4 THEN 'vamc'\n" +
                    "            END AS loaiToChucStr,\n" +
//                    "            cat.name AS province_name\n" +
                    "            CASE \n" +
                    "               WHEN cat.name LIKE 'Tỉnh %' THEN REPLACE(cat.name, 'Tỉnh ', '') \n" +
                    "               WHEN cat.name LIKE 'Thành Phố %' THEN REPLACE(cat.name, 'Thành Phố ', '')\n" +
                    "               WHEN cat.name LIKE 'Thành phố %' THEN REPLACE(cat.name, 'Thành phố ', '')\n" +
                    "            ELSE cat.name\n" +
                    "            END AS province_name " +
                    "        FROM AIMS_ORGANIZATION aio\n";
            if (getDetailDistrict) {
                sql += "        INNER JOIN AIMS_CATEGORY cat ON aio.ADDR_DISTRICT_ID = cat.id \n";
            } else sql += "        INNER JOIN AIMS_CATEGORY cat ON aio.ADDR_CITY_ID = cat.id \n" ;
            sql += "        WHERE aio.STATUS = 0 \n" +
                    "        AND aio.ORG_TYPE IN (0, 1, 2, 4, 11)\n" + whereClause +
                    "        GROUP BY \n" +
                    "            CASE \n" +
                    "                WHEN aio.org_type = 0 THEN 'tt_dgts'\n" +
                    "                WHEN aio.org_type = 1 THEN 'dgtn'\n" +
                    "                WHEN aio.org_type = 2 THEN 'dghd'\n" +
                    "                WHEN aio.org_type = 11 THEN 'cndn_dgts'\n" +
                    "                WHEN aio.org_type = 4 THEN 'vamc'\n" +
                    "            END, \n" +
                    "            cat.name\n" +
                    "    ) \n" +
                    "    PIVOT (\n" +
                    "        SUM(total) \n" +
                    "        FOR loaiToChucStr IN ('tt_dgts' as tt_dgts, 'dgtn' as dgtn, 'dghd' as dghd, 'cndn_dgts' as cndn_dgts, 'vamc' as vamc)\n" +
                    "    ) \n" +
                    ") aa ON aa.province_name = dka.PROVINCE_NAME order by dka.PROVINCE_NAME \n";
            String sqlTotal = sql;
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
            Query query = entityManager.createNativeQuery(init + sql);
            Query queryTotal = entityManager.createNativeQuery(init + sqlTotal);
            if (H.isTrue(cityIds)) {
                query.setParameter("cityId", cityIds);
                queryTotal.setParameter("cityId", cityIds);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
            }
            if (H.isTrue(orgTypesList)) {
                query.setParameter("orgTypes", orgTypesList);
                queryTotal.setParameter("orgTypes", orgTypesList);
            }
            if (H.isTrue(status)) {
                query.setParameter("status", status);
                queryTotal.setParameter("status", status);
            }

            List<Object[]> totalList = queryTotal.getResultList();
            List<Object[]> dataList = query.getResultList();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                i++;

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
                total.setCol_6(total.getCol_6() == null ? reaport.getCol_6() : String.valueOf((Long.parseLong(reaport.getCol_6()) + Long.parseLong(total.getCol_6()))));

            });
            if (totalList != null && totalList.size() > 0) {
                dataList.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();

                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                    i++;

                    items.add(reaport);
                });
                items.add(0, total);

            }
            result.setRowCount(rowcount.get());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 0, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
        result.setItems(items);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));

    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationAuction(String cityId, String fromDate, String toDate, int pageNumber, int numberPerPage, String actTypes, String aTypes, boolean getDetailDistrict) {
        PagingResult result = new PagingResult();
        List<Reaport> items = new ArrayList<>();

        try {
            List<String> cityIds = new ArrayList<>();
            List<String> cityIds_bk = new ArrayList<>();

            if (H.isTrue(cityId)) {
                String[] cityIdArr = cityId.split(",");
                for (String cityIdStr : cityIdArr) {
                    cityIds.add(cityIdStr);
                }
            }

            if (H.isTrue(cityId) && cityIds.get(0).length() < 3) {
                for (String cityIdStr : cityIds) {
                    Category category = categoryRepository.getByCodeAndCatType(Long.valueOf(cityIdStr), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                    if (H.isTrue(category)) {
                        cityIds_bk.add(category.getId().toString());
                    }
                }
            }

            if (H.isTrue(cityIds_bk)) {
                cityIds = cityIds_bk;
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityIds = Collections.singletonList(category.getId().toString());
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị quản lý", null), HttpStatus.OK);
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            int offset = 0;
            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            Date fromDateRaw = StringUtils.isBlank(fromDate) ? null : format.parse(fromDate);
            Date toDateRaw = StringUtils.isBlank(toDate) ? null : format.parse(toDate);

            List<Integer> actTypesList = new ArrayList<>();
            if (H.isTrue(actTypes)) {
                String[] actTypesArr = actTypes.split(",");
                for (String actTypeStr : actTypesArr) {
                    actTypesList.add(Integer.parseInt(actTypeStr));
                }
            }

            if (H.isTrue(aTypes)) {
                //map theo thứ tự bắt đầu từ 2
                /*
                 " 8 ->'capmoi'\n" +
                    " 1 ->'caplai'\n" +
                    " 11-> 'capdkhd'\n" +
                    " 12-> 'tl_dkhd'\n" +
                    " 9 ->'thaydoi'\n" +
                    " 10-> 'chuyendoi\n" +
                    " 3 ->'thuhoi'\n" +
                    " 4 ->'satnhap'\n" +
                    " 6 ->'giaithe'\n" +
                    " 7 ->'phasan'\n" +
                    " 5 ->'hopnhat'
                 */
                String[] aTypesArr = aTypes.split(",");
                for (String aTypeStr : aTypesArr) {
                    if (aTypeStr.equals("2")) {
                        actTypesList.add(8);
                    } else if (aTypeStr.equals("3")) {
                        actTypesList.add(1);
                    } else if (aTypeStr.equals("4")) {
                        actTypesList.add(11);
                    } else if (aTypeStr.equals("5")) {
                        actTypesList.add(12);
                    } else if (aTypeStr.equals("6")) {
                        actTypesList.add(9);
                    } else if (aTypeStr.equals("7")) {
                        actTypesList.add(10);
                    } else if (aTypeStr.equals("8")) {
                        actTypesList.add(3);
                    } else if (aTypeStr.equals("9")) {
                        actTypesList.add(4);
                    } else if (aTypeStr.equals("10")) {
                        actTypesList.add(6);
                    } else if (aTypeStr.equals("11")) {
                        actTypesList.add(7);
                    } else if (aTypeStr.equals("12")) {
                        actTypesList.add(5);
                    }
                }
            }

            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(cityIds)) {
                whereClause += " AND a.ADDR_CITY_ID in :cityId ";
                whereClauseAddress += " AND id in :cityId ";
            }
            if (fromDateRaw != null) {
                whereClause += " AND a.LICENSE_DATE >= :fromDate ";
            }
            if (toDateRaw != null) {
                whereClause += " AND a.LICENSE_DATE <= :toDate ";
            }
            if (H.isTrue(actTypesList)) {
                whereClause += " AND a.ACT_TYPE IN :actTypes ";
            }
            String init = " " +
                    " WITH distinct_province AS (\n" +
                    "      SELECT\n" +
                    "      CASE\n" +
                    "          \n" +
                    "        WHEN\n" +
                    "          name LIKE 'Tỉnh %' THEN\n" +
                    "            REPLACE ( name, 'Tỉnh ', '' ) \n" +
                    "            WHEN name LIKE 'Thành Phố %' THEN\n" +
                    "            REPLACE ( name, 'Thành Phố ', '' ) \n" +
                    "            WHEN name LIKE 'Thành phố %' THEN\n" +
                    "            REPLACE ( name, 'Thành phố ', '' ) \n" +
                    "            WHEN name LIKE 'Sở Tư Pháp %' THEN\n" +
                    "            REPLACE ( name, 'Sở Tư Pháp ', '' ) ELSE name \n" +
                    "          END AS PROVINCE_NAME \n" +
                    "        FROM\n" +
                    "          AIMS_CATEGORY \n" +
                    "        WHERE\n" +
                    "          CAT_TYPE = 'TP' \n" + whereClauseAddress + " \n" +
                    "        GROUP BY\n" +
                    "          name \n" +
                    "        ORDER BY\n" +
                    "          name \n" +
                    ")  ";
            String sql = "" +
                    "SELECT \n" +
                    "    dka.PROVINCE_NAME AS dbhc, \n" +
                    "    TO_NUMBER(decode(capmoi, null, 0, capmoi)) AS capmoi,\n" +
                    "    TO_NUMBER(decode(caplai, null, 0, caplai)) AS caplai,\n" +
                    "    TO_NUMBER(decode(capdkhd, null, 0, capdkhd)) AS capdkhd,\n" +
                    "    TO_NUMBER(decode(tl_dkhd, null, 0, tl_dkhd)) AS tl_dkhd,\n" +
                    "    TO_NUMBER(decode(thaydoi, null, 0, thaydoi)) AS thaydoi,\n" +
                    "    TO_NUMBER(decode(chuyendoi, null, 0, chuyendoi)) AS chuyendoi,\n" +
                    "    TO_NUMBER(decode(thuhoi, null, 0, thuhoi)) AS thuhoi,\n" +
                    "    TO_NUMBER(decode(satnhap, null, 0, satnhap)) AS satnhap,\n" +
                    "    TO_NUMBER(decode(giaithe, null, 0, giaithe)) AS giaithe,\n" +
                    "    TO_NUMBER(decode(hopnhat, null, 0, hopnhat)) AS hopnhat, \n" +
                    "    TO_NUMBER(decode(phasan, null, 0, phasan)) AS phasan \n" +
                    "FROM distinct_province dka left join (\n" +
                    "SELECT * \n" +
                    "    FROM (\n" +
                    "        SELECT \n" +
                    "            COUNT(1) AS total,\n" +
                    "            CASE \n" +
                    "                WHEN aoh.act_type = 8 THEN 'capmoi'\n" +
                    "                WHEN aoh.act_type = 1 THEN 'caplai'\n" +
                    "                WHEN aoh.act_type = 11 THEN 'capdkhd'\n" +
                    "                WHEN aoh.act_type = 12 THEN 'tl_dkhd'\n" +
                    "                WHEN aoh.act_type = 9 THEN 'thaydoi'\n" +
                    "                WHEN aoh.act_type = 10 THEN 'chuyendoi'\n" +
                    "                WHEN aoh.act_type = 3 THEN 'thuhoi'\n" +
                    "                WHEN aoh.act_type = 4 THEN 'satnhap'\n" +
                    "                WHEN aoh.act_type = 6 THEN 'giaithe'\n" +
                    "                WHEN aoh.act_type = 7 THEN 'phasan'\n" +
                    "                WHEN aoh.act_type = 5 THEN 'hopnhat'\n" +
                    "            END AS loaiToChucStr,\n" +
//                    "            cat.name AS province_name\n" +
                    "            CASE \n" +
                    "               WHEN cat.name LIKE 'Tỉnh %' THEN REPLACE(cat.name, 'Tỉnh ', '') \n" +
                    "               WHEN cat.name LIKE 'Thành Phố %' THEN REPLACE(cat.name, 'Thành Phố ', '')\n" +
                    "               WHEN cat.name LIKE 'Thành phố %' THEN REPLACE(cat.name, 'Thành phố ', '')\n" +
                    "            ELSE cat.name\n" +
                    "            END AS province_name " +
                    "        FROM (\n" +
                    "     SELECT \n" +
                    "     a.license_no,\n" +
                    "    a.license_date,\n" +
                    "    a.act_type, a.ADDR_CITY_ID, a.org_type, a.status\n" +
                    "     FROM aims_org_his a \n" +
                    "     WHERE ( a.status != 3 OR a.status IS NULL ) \n" + whereClause +
                    "     GROUP BY\n" +
                    "     a.license_no,\n" +
                    "     a.license_date,\n" +
                    "     a.act_type, a.ADDR_CITY_ID, a.org_type, a.status\n" +
                    "    ) aoh\n" +
                    "        INNER JOIN AIMS_CATEGORY cat ON aoh.ADDR_CITY_ID = cat.id \n" +
                    "        WHERE ( aoh.status != 3 OR aoh.status IS NULL )   and aoh.org_type!=4\n" +
                    "        GROUP BY \n" +
                    "           CASE \n" +
                    "                WHEN aoh.act_type = 8 THEN 'capmoi'\n" +
                    "                WHEN aoh.act_type = 1 THEN 'caplai'\n" +
                    "                WHEN aoh.act_type = 11 THEN 'capdkhd'\n" +
                    "                WHEN aoh.act_type = 12 THEN 'tl_dkhd'\n" +
                    "                WHEN aoh.act_type = 9 THEN 'thaydoi'\n" +
                    "                WHEN aoh.act_type = 10 THEN 'chuyendoi'\n" +
                    "                WHEN aoh.act_type = 3 THEN 'thuhoi'\n" +
                    "                WHEN aoh.act_type = 4 THEN 'satnhap'\n" +
                    "                WHEN aoh.act_type = 6 THEN 'giaithe'\n" +
                    "                WHEN aoh.act_type = 7 THEN 'phasan'\n" +
                    "                WHEN aoh.act_type = 5 THEN 'hopnhat'\n" +
                    "            END, \n" +
                    "            cat.name\n" +
                    "     \n" +
                    "    ) \n" +
                    "    PIVOT (\n" +
                    "        SUM(total) \n" +
                    "        FOR loaiToChucStr IN ('capmoi' as capmoi, 'caplai' as caplai, 'capdkhd' as capdkhd, 'tl_dkhd' as tl_dkhd, 'thaydoi' as thaydoi, 'chuyendoi' as chuyendoi, 'thuhoi' as thuhoi, 'satnhap' as satnhap, 'giaithe' as giaithe, 'phasan' as phasan, 'hopnhat' as hopnhat)\n" +
                    "    ) \n" +
                    "  \n" +
                    ") aa ON aa.province_name = dka.PROVINCE_NAME  order by dka.PROVINCE_NAME \n";
            String sqlTotal = sql;
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
            Query query = entityManager.createNativeQuery(init + sql);
            Query queryTotal = entityManager.createNativeQuery(init + sqlTotal);
            if (H.isTrue(cityIds)) {
                query.setParameter("cityId", cityIds);
                queryTotal.setParameter("cityId", cityIds);
            }
            if (fromDateRaw != null) {
                query.setParameter("fromDate", fromDateRaw);
                queryTotal.setParameter("fromDate", fromDateRaw);
            }
            if (toDateRaw != null) {
                query.setParameter("toDate", toDateRaw);
                queryTotal.setParameter("toDate", toDateRaw);
            }
            if (H.isTrue(actTypesList)) {
                query.setParameter("actTypes", actTypesList);
                queryTotal.setParameter("actTypes", actTypesList);
            }

            List<Object[]> totalList = queryTotal.getResultList();
            List<Object[]> dataList = query.getResultList();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_7(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_8(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_9(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_10(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_11(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_12(record[i] == null ? "0" : record[i].toString());
                i++;

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
                total.setCol_6(total.getCol_6() == null ? reaport.getCol_6() : String.valueOf((Long.parseLong(reaport.getCol_6()) + Long.parseLong(total.getCol_6()))));
                total.setCol_7(total.getCol_7() == null ? reaport.getCol_7() : String.valueOf((Long.parseLong(reaport.getCol_7()) + Long.parseLong(total.getCol_7()))));
                total.setCol_8(total.getCol_8() == null ? reaport.getCol_8() : String.valueOf((Long.parseLong(reaport.getCol_8()) + Long.parseLong(total.getCol_8()))));
                total.setCol_9(total.getCol_9() == null ? reaport.getCol_9() : String.valueOf((Long.parseLong(reaport.getCol_9()) + Long.parseLong(total.getCol_9()))));
                total.setCol_10(total.getCol_10() == null ? reaport.getCol_10() : String.valueOf((Long.parseLong(reaport.getCol_10()) + Long.parseLong(total.getCol_10()))));
                total.setCol_11(total.getCol_11() == null ? reaport.getCol_11() : String.valueOf((Long.parseLong(reaport.getCol_11()) + Long.parseLong(total.getCol_11()))));
                total.setCol_12(total.getCol_12() == null ? reaport.getCol_12() : String.valueOf((Long.parseLong(reaport.getCol_12()) + Long.parseLong(total.getCol_12()))));
            });
            if (totalList != null && totalList.size() > 0) {
                dataList.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();

                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_7(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_8(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_9(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_10(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_11(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_12(record[i] == null ? "0" : record[i].toString());
                    i++;

                    items.add(reaport);
                });
                items.add(0, total);

            }
            result.setRowCount(rowcount.get());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
        result.setItems(items);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
    }

    @Override
    public HashMap<String, Object> reportDashboardAuctionQuery(Long cityCode, String fromDateRaw, String toDateRaw) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = null;
        Date toDate = null;
        Long cityId = null;
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
            Category city = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
            if (H.isTrue(city)) {
                cityId = city.getId();
            } else return null;
        }
        if (H.isTrue(cityCode)) {
            if (cityCode.intValue() < 100) {
                Category city = categoryRepository.getByCodeAndCatType(cityCode, Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(city)) {
                    cityId = city.getId();
                } else return null;
            } else {
                cityId = cityCode.longValue();
            }

        }
        try {
            if (H.isTrue(fromDateRaw)) {
                fromDate = format.parse(fromDateRaw);
            }
            if (H.isTrue(toDateRaw)) {
                toDate = format.parse(toDateRaw);
            }

            String whereClauseReport1 = "";
            String whereClauseReport2 = "";
            String whereClauseReport3 = "";
            String whereClauseReport4 = "";

            if (H.isTrue(fromDate)) {
                whereClauseReport1 += " AND choice.RECEIVE_TIME_START >= :fromDate ";
//                whereClauseReport2 += " AND a.LICENSE_DATE >= :fromDate ";
                whereClauseReport3 += " AND aio.LICENSE_DATE >= :fromDate ";
                whereClauseReport4 += " AND GEN_DATE >= :fromDate ";
            }
            if (H.isTrue(toDate)) {
                whereClauseReport1 += " AND choice.RECEIVE_TIME_END <= :toDate ";
//                whereClauseReport2 += " AND a.AUC_TIME <= :toDate ";
                whereClauseReport3 += " AND aio.LICENSE_DATE <= :toDate ";
                whereClauseReport4 += " AND GEN_DATE <= :toDate ";
            }
            if (H.isTrue(cityId)) {
                whereClauseReport1 += " AND d.ADDR_CITY = :cityId ";
                whereClauseReport2 += " AND d.ADDR_CITY = :cityId ";
                whereClauseReport3 += " AND aio.ADDR_CITY_ID = :cityId ";
                whereClauseReport4 += " AND ADDR_CITY_ID = :cityId ";
            }


            String sql = "WITH \n" +
                    " tk_tb_lua_chon AS (\n" +
                    " select count(choice.id) from " +
                    " aims_choice_org_notice choice " +
                    "  INNER JOIN aims_property_owner d ON choice.owner_id = d.id " +
                    " where choice.PUBLISH_STATUS in (2,4) " + whereClauseReport1 + "\n" +
                    "),\n" +
                    " tk_tb_dau_gia AS (\n" +
                    " SELECT\n" +
                    " count( 1 ) \n" +
                    "FROM\n" +
                    " (\n" +
                    " SELECT\n" +
                    "  info.AUC_STATUS,\n" +
                    "  owner_address.ADDR_OWNER,\n" +
                    "  choice.property_id,\n" +
                    "  choice.receive_addr,\n" +
                    "  choice.other_info,\n" +
                    "  choice.category_id,\n" +
                    "  choice.contact_info,\n" +
                    "  choice.receive_time_start,\n" +
                    "  choice.receive_time_end,\n" +
                    "  0 AS LEVEL_AUC_INFO \n" +
                    " FROM\n" +
                    "  (\n" +
                    "  SELECT\n" +
                    "   a.ID,\n" +
                    "   a.CHOICE_ORG_ID,\n" +
                    "   a.AUC_STATUS,\n" +
                    "   a.PUBLISH_TIME_2,\n" +
                    "   a.USER_ID_CREATE,\n" +
                    "   a.AUC_TIME,\n" +
                    "   a.PUBLISH_TIME_1,\n" +
                    "   a.ORG_ID,\n" +
                    "   a.LAST_UPDATED,\n" +
                    "   a.AUC_REG_TIME_START,\n" +
                    "   a.AUC_REG_TIME_END,\n" +
                    "   d.FULLNAME,\n" +
                    "   d.id AS property_owner_id,\n" +
                    "   d.ADDR_CITY,\n" +
                    "   d.ADDR_TOWN,\n" +
                    "   a.OWNER_ID,\n" +
                    "   b.FULLNAME AS org_name \n" +
                    "  FROM\n" +
                    "   AIMS_AUC_INFO a\n" +
                    "   INNER JOIN aims_organization b ON a.org_id = b.id and b.status = 0 \n " +
                    "   INNER JOIN aims_property_owner d ON a.owner_id = d.id\n" +
                    "   LEFT JOIN ( SELECT root_id FROM aims_auc_info A2 WHERE A2.root_id IS NOT NULL AND A2.root_id <> 0 AND A2.auc_status = 4 ) ro ON a.id = ro.root_id \n" +
                    "  WHERE\n" +
                    "   ro.root_id IS NULL \n" + whereClauseReport2 + "\n" +
                    "   AND a.IS_BLOCK IS NULL \n" +
                    "  ) info\n" +
                    "  LEFT JOIN (\n" +
                    "  SELECT\n" +
                    "   auCity.ID AS owner_id,\n" +
                    "   ( auCity.ADDR_DETAIL || ', ' || catDist.NAME || ', ' || auCity.city ) AS ADDR_OWNER \n" +
                    "  FROM\n" +
                    "   (\n" +
                    "   SELECT\n" +
                    "    au.id,\n" +
                    "    au.ADDR_TOWN,\n" +
                    "    au.ADDR_DETAIL,\n" +
                    "    cat.NAME AS city \n" +
                    "   FROM\n" +
                    "    aims_property_owner au,\n" +
                    "    AIMS_CATEGORY cat \n" +
                    "   WHERE\n" +
                    "    au.ADDR_CITY = cat.id \n" +
                    "   ) auCity\n" +
                    "   LEFT JOIN AIMS_CATEGORY catDist ON auCity.ADDR_TOWN = catDist.ID \n" +
                    "  ) owner_address ON info.OWNER_ID = owner_address.owner_id\n" +
                    "  LEFT JOIN (\n" +
                    "  SELECT\n" +
                    "   c.CHOICE_ORG_ID,\n" +
                    "   c.PROPERTY_ID,\n" +
                    "   e.RECEIVE_ADDR,\n" +
                    "   e.CONTACT_INFO,\n" +
                    "   e.OTHER_INFO,\n" +
                    "   f.CATEGORY_ID,\n" +
                    "   e.RECEIVE_TIME_START,\n" +
                    "   e.RECEIVE_TIME_END \n" +
                    "  FROM\n" +
                    "   AIMS_CHOICE_ORG_PROPERTY c,\n" +
                    "   AIMS_CHOICE_ORG_NOTICE e,\n" +
                    "   AIMS_PROPERTY_TYPE f \n" +
                    "  WHERE\n" +
                    "   c.CHOICE_ORG_ID = e.ID \n" +
                    "   AND c.PROPERTY_ID = f.PROPERTY_ID \n" +
                    "  ) choice ON info.CHOICE_ORG_ID = choice.CHOICE_ORG_ID \n" +
                    " ) \n" +
                    "WHERE\n" +
                    " AUC_STATUS = 4\n" +
                    " ),\n" +
                    " \n" +
                    " tk_to_chuc AS (\n" +  //clause 3
                    " \n" +
                    " SELECT count(*) from (\n" +
                    " SELECT\n" +
                    " a.id\n" +
                    "FROM\n" +
                    " AIMS_ORGANIZATION a \n" +
                    " JOIN (\n" +
                    "  SELECT\n" +
                    "    * \n" +
                    "  FROM\n" +
                    "    (\n" +
                    "    SELECT\n" +
                    "      a.auctioneer_id,\n" +
                    "      a.org_id \n" +
                    "    FROM\n" +
                    "      BTP_DGTS.aims_auctioneer_his a\n" +
                    "      INNER JOIN (\n" +
                    "      SELECT\n" +
                    "        MAX( a.id ) C_ID,\n" +
                    "        a.org_id C_ORGID \n" +
                    "      FROM\n" +
                    "        BTP_DGTS.aims_auctioneer_his a \n" +
                    "      WHERE\n" +
                    "        a.auctioneer_type = 2 \n" +
                    "        AND a.source_log = 0 \n" +
                    "      GROUP BY\n" +
                    "        a.org_id \n" +
                    "      ) x ON a.id = x.C_ID \n" +
                    "    ) y \n" +
                    "  ) c ON a.id = c.org_id \n" +
                    "  JOIN aims_auctioneer b ON c.auctioneer_id = b.id \n" +
                    "WHERE\n" +
                    " a.STATUS = 0 \n" +
                    " AND a.org_type IN ( 0, 1, 2  )\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "  a.ID\n" +
                    "FROM\n" +
                    "  BTP_DGTS.aims_organization a\n" +
                    "  LEFT JOIN BTP_DGTS.aims_organization d ON a.org_root = d.id\n" +
                    "  LEFT JOIN BTP_DGTS.adm_users e ON a.user_create = e.id\n" +
                    "  LEFT JOIN BTP_DGTS.adm_users ue ON a.user_update = ue.id \n" +
                    "WHERE\n" +
                    "  ( a.status != 3 OR a.status IS NULL ) \n" +
                    "  AND a.org_type = 4 \n" +
                    ") \n" +
                    " ),\n" +
                    " tk_dgv AS (\n" +
                    " SELECT\n" +
                    " COUNT( * ) \n" +
                    "FROM\n" +
                    " (\n" +
                    " SELECT\n" +
                    "  auc.id,\n" +
                    "  auc.AUCTIONEER_TYPE," +
                    "  auc.GEN_DATE, auc.ADDR_CITY_ID \n" +
                    " FROM\n" +
                    "  ( SELECT a.id, a.AUCTIONEER_TYPE, a.GEN_DATE, a.ADDR_CITY_ID FROM AIMS_AUCTIONEER a ) auc\n" +
                    "  LEFT JOIN (\n" +
                    "  SELECT\n" +
                    "   dd.auctioneer_id \n" +
                    "  FROM\n" +
                    "   AIMS_AUCTIONEER_HIS dd\n" +
                    "   INNER JOIN (\n" +
                    "   SELECT\n" +
                    "    MAX( b.GEN_DATE ) times,\n" +
                    "    b.auctioneer_id auID \n" +
                    "   FROM\n" +
                    "    AIMS_AUCTIONEER_HIS b \n" +
                    "   WHERE\n" +
                    "    b.ACT_TYPE IN ( 1, 2 ) \n" +
                    "    AND ( TO_CHAR( b.EFFECTIVE_DATE, 'YYYYMMDD' ) <= TO_CHAR( SYSDATE, 'YYYYMMDD' ) OR b.ACT_TYPE = 1 ) \n" +
                    "   GROUP BY\n" +
                    "    b.auctioneer_id \n" +
                    "   ) abc ON dd.auctioneer_id = abc.auid \n" +
                    "   AND dd.gen_date = abc.times \n" +
                    "  ) his ON auc.id = his.AUCTIONEER_ID\n" +
                    "  LEFT JOIN (\n" +
                    "  SELECT\n" +
                    "   dd.auctioneer_id \n" +
                    "  FROM\n" +
                    "   AIMS_AUCTIONEER_HIS dd\n" +
                    "   INNER JOIN (\n" +
                    "   SELECT\n" +
                    "    MAX( b.GEN_DATE ) times,\n" +
                    "    b.auctioneer_id auID \n" +
                    "   FROM\n" +
                    "    AIMS_AUCTIONEER_HIS b \n" +
                    "   WHERE\n" +
                    "    b.ACT_TYPE IN ( 1, 2 ) \n" +
                    "   GROUP BY\n" +
                    "    b.auctioneer_id \n" +
                    "   ) abc ON dd.auctioneer_id = abc.auid \n" +
                    "   AND dd.gen_date = abc.times \n" +
                    "  ) his2 ON auc.id = his2.AUCTIONEER_ID \n" +
                    " ) \n" +
                    "WHERE\n" +
                    " AUCTIONEER_TYPE != 3\n" + whereClauseReport4 + "\n" +
                    " )\n" +
                    " \n" +
                    "SELECT\n" +
                    "    (SELECT * FROM tk_tb_lua_chon) AS tk_tb_lua_chon,\n" +
                    "    (SELECT * FROM tk_tb_dau_gia) AS tk_tb_dau_gia,\n" +
                    "  (SELECT * FROM tk_to_chuc) AS tk_to_chuc,\n" +
                    "  (SELECT * FROM tk_dgv) AS tk_dgv\n" +
                    "\n" +
                    "FROM dual";
            Query query = entityManager.createNativeQuery(sql);
            if (cityId != null) {
                query.setParameter("cityId", cityId);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
            }
            List<Object[]> dataList = query.getResultList();
            HashMap<String, Object> data = new HashMap<>();
            if (dataList.size() > 0) {
                Object[] record = dataList.get(0);
                data.put("tk_dgts_tb_lua_chon", record[0]);
                data.put("tk_dgts_tb_dau_gia", record[1]);
                data.put("tk_dgts_to_chuc", record[2]);
                data.put("tk_dgts_dgv", record[3]);
            }


            data.put("dataMap", getTcDGTSByMap().getBody().getData());
            data.put("tk_pie_dgts_th_theo_loai_tc_hndg", loadDataQuantityOrganizationAuction(cityCode, fromDateRaw, toDateRaw));
            data.put("tk_pie_dgts_th_theo_cap_cchn_dg", loadDataReportAuctionCard(cityCode, fromDateRaw, toDateRaw));


//            data = tcccService.reportA1(data, cityCode, fromDate, toDate);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi hệ thống");
        }
    }

    private Object loadDataReportAuctionCard(Long cityCode, String fromDateRaw, String toDateRaw) {
        PagingResult<Reaport> dataList = reportAuctionCard(String.valueOf(cityCode), fromDateRaw, toDateRaw, 1, 1000000, null, null).getBody().getData();
//        Reaport total = dataList.getItems().get(0);
        Reaport total = H.isTrue(dataList.getItems()) ? dataList.getItems().get(0) : null;
        if (total == null) {
            return null;
        }
        /*
         * col tương ứng từ col_2: Địa danh hành chính	Cấp mới CCHN đấu giá	Cấp lại CCHN đấu giá	Thu hồi CCHN đấu giá	Cấp mới thẻ ĐGV	Cấp lại thẻ ĐGV	Thu hồi thẻ ĐGV
         */
        if (total.getCol_2() == null) total.setCol_2("0");
        if (total.getCol_3() == null) total.setCol_3("0");
        if (total.getCol_4() == null) total.setCol_4("0");
        if (total.getCol_5() == null) total.setCol_5("0");
        if (total.getCol_6() == null) total.setCol_6("0");
        if (total.getCol_7() == null) total.setCol_7("0");
        Long sumTotal = Long.parseLong(total.getCol_2()) + Long.parseLong(total.getCol_3()) + Long.parseLong(total.getCol_4()) + Long.parseLong(total.getCol_5()) + Long.parseLong(total.getCol_6()) + Long.parseLong(total.getCol_7());
        if (sumTotal == 0) {
            return null;
        }

        String precentCol2 = String.valueOf(Math.round((Long.parseLong(total.getCol_2()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol3 = String.valueOf(Math.round((Long.parseLong(total.getCol_3()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol4 = String.valueOf(Math.round((Long.parseLong(total.getCol_4()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol5 = String.valueOf(Math.round((Long.parseLong(total.getCol_5()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol6 = String.valueOf(Math.round((Long.parseLong(total.getCol_6()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol7 = String.valueOf(Math.round((Long.parseLong(total.getCol_7()) * 100.0 / sumTotal) * 100.0) / 100.0);

        List<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Cấp mới CCHN đấu giá");
        map.put("value", Arrays.asList(precentCol2, total.getCol_2()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Cấp lại CCHN đấu giá");
        map.put("value", Arrays.asList(precentCol3, total.getCol_3()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Thu hồi CCHN đấu giá");
        map.put("value", Arrays.asList(precentCol4, total.getCol_4()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Cấp mới thẻ ĐGV");
        map.put("value", Arrays.asList(precentCol5, total.getCol_5()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Cấp lại thẻ ĐGV");
        map.put("value", Arrays.asList(precentCol6, total.getCol_6()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Thu hồi thẻ ĐGV");
        map.put("value", Arrays.asList(precentCol7, total.getCol_7()));
        data.add(map);

        return data;

    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> reportDashboard(Long cityCode, String fromDateRaw, String toDateRaw) {
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String cityCodeStr = cityCode == null ? "" : cityCode.toString();
        //dùng redis cache lại kết quả dataAll 10 phút
        HashMap<String, Object> dataaAll = new HashMap<>();
        String keyReportAuctionDashBoardDataAll = "reportAuctionDashBoardDataAll" + cityCodeStr + fromDateRaw + toDateRaw + userLogin.getId();
        if ( redisEnable && redisTemplate.hasKey(keyReportAuctionDashBoardDataAll)) {
            dataaAll = (HashMap<String, Object>) redisTemplate.opsForValue().get(keyReportAuctionDashBoardDataAll);
        } else {
            dataaAll = reportDashboardAuctionQuery(cityCode, fromDateRaw, toDateRaw);
            if ( redisEnable ) redisTemplate.opsForValue().set(keyReportAuctionDashBoardDataAll, dataaAll, 10, TimeUnit.MINUTES);
        }

        //data năm hiện tại
        HashMap<String, Object> dataCurrentYear = new HashMap<>();
        String keyReportAuctionDashBoardDataCurrentYear = "reportAuctionDashBoardDataCurrentYear" + cityCodeStr + fromDateRaw + toDateRaw + userLogin.getId();
        if ( redisEnable && redisTemplate.hasKey(keyReportAuctionDashBoardDataCurrentYear)) {
            dataCurrentYear = (HashMap<String, Object>) redisTemplate.opsForValue().get(keyReportAuctionDashBoardDataCurrentYear);
        } else {
            dataCurrentYear = reportDashboardAuctionQuery(cityCode, "01/01/" + Calendar.getInstance().get(Calendar.YEAR), "31/12/" + Calendar.getInstance().get(Calendar.YEAR));
            if ( redisEnable ) redisTemplate.opsForValue().set(keyReportAuctionDashBoardDataCurrentYear, dataCurrentYear, 10, TimeUnit.MINUTES);
        }

        //data 1 năm trước
        HashMap<String, Object> dataLastYear = new HashMap<>();
        String keyReportAuctionDashBoardDataLastYear = "reportAuctionDashBoardDataLastYear" + cityCodeStr + fromDateRaw + toDateRaw;
        if ( redisEnable && redisTemplate.hasKey(keyReportAuctionDashBoardDataLastYear)) {
            dataLastYear = (HashMap<String, Object>) redisTemplate.opsForValue().get(keyReportAuctionDashBoardDataLastYear);
        } else {
            dataLastYear = reportDashboardAuctionQuery(cityCode, "01/01/" + (Calendar.getInstance().get(Calendar.YEAR) - 1), "31/12/" + (Calendar.getInstance().get(Calendar.YEAR) - 1));
            if ( redisEnable ) redisTemplate.opsForValue().set(keyReportAuctionDashBoardDataLastYear, dataLastYear, 100, TimeUnit.MINUTES);
        }

        //Tính % tăng trưởng ( năm trước só với năm nay ) 4 giá trị làm tròn 2 số thập phân
//        tk_dgts_tb_lua_chon
//        tk_dgts_tb_dau_gia
//        tk_dgts_to_chuc
//        tk_dgts_dgv
        HashMap<String, Object> result = dataaAll;
        Long tk_dgts_tb_lua_chon_last_year = Long.parseLong(dataLastYear.get("tk_dgts_tb_lua_chon").toString());
        Long tk_dgts_tb_lua_chon_current_year = Long.parseLong(dataCurrentYear.get("tk_dgts_tb_lua_chon").toString());
        Long tk_dgts_tb_dau_gia_last_year = Long.parseLong(dataLastYear.get("tk_dgts_tb_dau_gia").toString());
        Long tk_dgts_tb_dau_gia_current_year = Long.parseLong(dataCurrentYear.get("tk_dgts_tb_dau_gia").toString());
        Long tk_dgts_to_chuc_last_year = Long.parseLong(dataLastYear.get("tk_dgts_to_chuc").toString());
        Long tk_dgts_to_chuc_current_year = Long.parseLong(dataCurrentYear.get("tk_dgts_to_chuc").toString());
        Long tk_dgts_dgv_last_year = Long.parseLong(dataLastYear.get("tk_dgts_dgv").toString());
        Long tk_dgts_dgv_current_year = Long.parseLong(dataCurrentYear.get("tk_dgts_dgv").toString());

        String tk_dgts_tb_lua_chon_growth = String.valueOf(Math.round(((tk_dgts_tb_lua_chon_current_year - tk_dgts_tb_lua_chon_last_year) * 100.0 / tk_dgts_tb_lua_chon_last_year) * 100.0) / 100.0);
        String tk_dgts_tb_dau_gia_growth = String.valueOf(Math.round(((tk_dgts_tb_dau_gia_current_year - tk_dgts_tb_dau_gia_last_year) * 100.0 / tk_dgts_tb_dau_gia_last_year) * 100.0) / 100.0);
        String tk_dgts_to_chuc_growth = String.valueOf(Math.round(((tk_dgts_to_chuc_current_year - tk_dgts_to_chuc_last_year) * 100.0 / tk_dgts_to_chuc_last_year) * 100.0) / 100.0);
        String tk_dgts_dgv_growth = String.valueOf(Math.round(((tk_dgts_dgv_current_year - tk_dgts_dgv_last_year) * 100.0 / tk_dgts_dgv_last_year) * 100.0) / 100.0);

        //nếu năm trước = 0, năm sau có giá trị thì tăng trưởng = 100%
        // nếu năm trước có giá trị, năm sau = 0 thì tăng trưởng = -100%
        if (tk_dgts_tb_lua_chon_last_year == 0 && tk_dgts_tb_lua_chon_current_year > 0) {
            tk_dgts_tb_lua_chon_growth = "100";
        }
        if (tk_dgts_tb_lua_chon_last_year > 0 && tk_dgts_tb_lua_chon_current_year == 0) {
            tk_dgts_tb_lua_chon_growth = "-100";
        }
        if (tk_dgts_tb_dau_gia_last_year == 0 && tk_dgts_tb_dau_gia_current_year > 0) {
            tk_dgts_tb_dau_gia_growth = "100";
        }
        if (tk_dgts_tb_dau_gia_last_year > 0 && tk_dgts_tb_dau_gia_current_year == 0) {
            tk_dgts_tb_dau_gia_growth = "-100";
        }
        if (tk_dgts_to_chuc_last_year == 0 && tk_dgts_to_chuc_current_year > 0) {
            tk_dgts_to_chuc_growth = "100";
        }
        if (tk_dgts_to_chuc_last_year > 0 && tk_dgts_to_chuc_current_year == 0) {
            tk_dgts_to_chuc_growth = "-100";
        }
        if (tk_dgts_dgv_last_year == 0 && tk_dgts_dgv_current_year > 0) {
            tk_dgts_dgv_growth = "100";
        }
        if (tk_dgts_dgv_last_year > 0 && tk_dgts_dgv_current_year == 0) {
            tk_dgts_dgv_growth = "-100";
        }

        result.put("tk_dgts_tb_lua_chon_growth", tk_dgts_tb_lua_chon_growth);
        result.put("tk_dgts_tb_dau_gia_growth", tk_dgts_tb_dau_gia_growth);
        result.put("tk_dgts_to_chuc_growth", tk_dgts_to_chuc_growth);
        result.put("tk_dgts_dgv_growth", tk_dgts_dgv_growth);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> organizationAuctionDetail(Long orgId) {
        try {
            CenterOrganizationAuctionner centerOrganizationAuctionner = new CenterOrganizationAuctionner();
            centerOrganizationAuctionner = viewDetailOrg(orgId);
            if (!H.isTrue(centerOrganizationAuctionner)) {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị", null), HttpStatus.OK);
            }
//            OrganizationHis organizationHis = new OrganizationHis();
//            organizationHis = timkiemDNThemMoi(orgId);
//        List<AuFiles> lts = new ArrayList<>();
//        lts = findOrgHisFileByHisId(organizationHis.getId());
//        List<FileUpload> fileUploads = new ArrayList<>();
//        for (AuFiles fileU : lts) {
//            FileUpload f = new FileUpload();
//            f.setFileName(fileU.getFileTitle());
//            f.setIdFile(fileU.getId());
//            //Hungnn mã hóa path file
////            f.setLinkFile(EncodeUtil.endCoding(fileU.getPath()));
//            fileUploads.add(f);
//        }


//        centerOrganizationAuctionner.setListFile(fileUploads);
            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", centerOrganizationAuctionner));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> auctioneerDetail(Long id) {
        //Thông tin chung
        AuctioneerInfo detail = new AuctioneerInfo();

        //Chứng chỉ hành nghề
        List<ViewDetailAuctioneerHistory> listFullCCHN = new ArrayList<>();

        //thẻ đấu giá viên
        List<ViewDetailAuctioneerHistory> listFullCardDGV = new ArrayList<>();

        try {
            label_detail:
            {
                Long idl = Long.valueOf(id);
                detail = auctioneerDAO.getAuctioneerWithFullAddrByID(idl).orElse(new AuctioneerInfo());
            }

            label_cchn:
            {
                List<ListInfoDecision> listInfoDecision = new ArrayList<>();
                PagingResult page = new PagingResult();
                page.setPageNumber(1);
                Long idl = Long.valueOf(id);
                ViewDetailAuctioneerHistory tmp;
                try {
                    page = auctioneerDAO.getDetailInfo(page, idl, "CERTIFICATE").orElse(new PagingResult());
                    listInfoDecision = (List<ListInfoDecision>) page.getItems();
                    for (ListInfoDecision item : listInfoDecision) {
                        tmp = new ViewDetailAuctioneerHistory(item.getId(), item.getAuctioneerID(),
                                item.getAuctioneerType(), item.getOrgID(), item.getCerCode(), item.getCardCode(), item.getActType(),
                                item.getNumberOfDecision(), item.getDateOfDecision(), item.getEffectiveDate(), item.getOrgName());
                        listFullCCHN.add(tmp);
                    }
                } catch (Exception e) {
                    log.error("Have error in AuctioneerController:" + e.getMessage());
                    e.printStackTrace();
                    break label_cchn;
                }
            }

            label_card_dgv:
            {
                List<ListInfoDecision> listInfoDecision = new ArrayList<>();
                PagingResult page = new PagingResult();
                page.setPageNumber(1);
                Long idl = Long.valueOf(id);
                ViewDetailAuctioneerHistory tmp;
                try {
                    page = auctioneerDAO.getDetailInfo(page, idl, "CARD").orElse(new PagingResult());
                    listInfoDecision = (List<ListInfoDecision>) page.getItems();
                    for (ListInfoDecision item : listInfoDecision) {
                        tmp = new ViewDetailAuctioneerHistory(item.getId(), item.getAuctioneerID(),
                                item.getAuctioneerType(), item.getOrgID(), item.getCerCode(), item.getCardCode(), item.getActType(),
                                item.getNumberOfDecision(), item.getDateOfDecision(), item.getEffectiveDate(), item.getOrgName());
                        listFullCardDGV.add(tmp);
                    }
                } catch (Exception e) {
                    log.error("Have error in AuctioneerController:" + e.getMessage());
                    e.printStackTrace();
                    break label_card_dgv;
                }
            }


            HashMap<String, Object> data = new HashMap<>();
            data.put("detail", detail);
            data.put("listFullCCHN", listFullCCHN);
            data.put("listFullCardDGV", listFullCardDGV);

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", data));


        } catch (Exception e) {
            log.error("Have error in AuctioneerController:" + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<DetailDecisionView>>> auctioneerDetailProcess(Long id) {
        String sql = "SELECT * "
                + "FROM "
                + "	(  "
                + "	SELECT "
                + " org.ID, "
                + " org.FULLNAME, a.NUMBER_OF_DECISION, a.DATE_OF_DECISION, a.EFFECTIVE_DATE, a.CARD_CODE, a.ACT_TYPE "
                + "	FROM "
                + " AIMS_AUCTIONEER_HIS a "
                + " INNER JOIN AIMS_AUCTIONEER b ON a.AUCTIONEER_ID = b.ID "
                + " INNER JOIN aims_organization org ON b.ORG_ID = org.id "
                + "	WHERE "
                + "		 b.ID = :id) info ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("id", id);
        List<Object[]> dataList = query.getResultList();
        List<DetailDecisionView> data = new ArrayList<>();
        for (Object[] record : dataList) {
            DetailDecisionView item = new DetailDecisionView();
            item.setOrgID(record[0] == null ? null : Long.parseLong(record[0].toString()));
            item.setOrgName(record[1] == null ? null : record[1].toString());
            item.setNumberOfDecision(record[2] == null ? null : record[2].toString());
            item.setDateOfDecision(record[3] == null ? null : (Timestamp) record[3]);
            item.setEffectiveDate(record[4] == null ? null : (Timestamp) record[4]);
            item.setActType(record[6] == null ? null : Long.parseLong(record[6].toString()));
            data.add(item);
        }

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", data));


    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> reportPublicAuctionAssetByDay(Long cityId, String yearReport, String monthReport) {
        try {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityId = category.getId();
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin tổ chức", null), HttpStatus.OK);
                }
            } else if (H.isTrue(cityId) && cityId < 100L) {
                Category category = categoryRepository.getByCodeAndCatType(cityId, Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityId = category.getId();
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin tổ chức", null), HttpStatus.OK);
                }
            }

            Date fromDatePublish = null;
            Date toDatePublish = null;

            if (H.isTrue(yearReport) && H.isTrue(monthReport)) {
                //get First and Last date of month
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(yearReport));
                calendar.set(Calendar.MONTH, Integer.parseInt(monthReport) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                //set time to start of day
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                fromDatePublish = calendar.getTime();


                //set time to end of day
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                toDatePublish = calendar.getTime();
            } else {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Thiếu thông tin thời gian báo cáo", null), HttpStatus.OK);
            }
            String whereClauseReport1 = "";
            if (H.isTrue(cityId)) {
                whereClauseReport1 = " AND aic.ID = :cityId ";
            }

            String sql = "  SELECT \n" +
                    "    time,\n" +
                    "    SUM(count_per_day) AS count_per_day,\n" +
                    "    SUM(count_per_day2) AS count_per_day2\n" +
                    "FROM (\n" +
                    "\n" +
                    "SELECT \n" +
                    "    TRUNC(publish_time) AS time,\n" +
                    "    COUNT(1) AS count_per_day,\n" +
                    "    0 as count_per_day2\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        COALESCE(PUBLISH_TIME_1, PUBLISH_TIME_2) AS publish_time\n" +
                    "    FROM\n" +
                    "        AIMS_AUC_INFO a\n" +
                    "        INNER JOIN AIMS_ORGANIZATION aig ON aig.id = a.ORG_ID\n" +
                    "        LEFT JOIN (\n" +
                    "            SELECT root_id \n" +
                    "            FROM aims_auc_info A2 \n" +
                    "            WHERE A2.root_id IS NOT NULL \n" +
                    "            AND A2.auc_status = 4 \n" +
                    "        ) ro ON a.id = ro.root_id\n" +
                    "        INNER JOIN AIMS_CATEGORY aic ON aic.id = aig.ADDR_CITY_ID\n" +
                    "    WHERE\n" +
                    "        a.AUC_STATUS = 4 \n" +
                    "        AND COALESCE(PUBLISH_TIME_1, PUBLISH_TIME_2) >= :fromDatePublish \n" +
                    "        AND COALESCE(PUBLISH_TIME_1, PUBLISH_TIME_2) <= :toDatePublish \n" +
                    "        AND a.IS_BLOCK IS NULL \n" +
                    "        AND ro.root_id IS NULL\n" + whereClauseReport1 +
                    ")\n" +
                    "GROUP BY \n" +
                    "    TRUNC(publish_time)\n" +
                    "\n" +
                    " UNION ALL\n" +
                    "\n" +
                    "\n" +
                    "SELECT \n" +
                    "    TRUNC(time_start) AS time,\n" +
                    "    0 AS count_per_day,\n" +
                    "    COUNT(1) AS count_per_day2\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        a.RECEIVE_TIME_START AS time_start\n" +
                    "     FROM \n" +
                    "              aims_choice_org_notice a \n" +
                    "                          JOIN aims_property_owner b ON b.id = a.owner_id \n" +
                    "                          LEFT JOIN aims_choice_org_notice a2 ON a.id = a2.root_id  \n" +
                    "                          AND a2.PUBLISH_STATUS = 4 \n" +
                    "                          LEFT JOIN AIMS_CATEGORY aic ON aic.id = b.ADDR_CITY  \n" +
                    "    WHERE a2.id IS NULL\n" + whereClauseReport1 +
                    "        AND a.IS_BLOCK IS NULL\n" +
                    "        AND a.PUBLISH_STATUS = 4 \n" +
                    "        AND a.RECEIVE_TIME_START >=  :fromDatePublish \n" +
                    "        AND a.RECEIVE_TIME_END >= :fromDatePublish \n" +
                    "        AND a.RECEIVE_TIME_END <= :toDatePublish \n" +
                    "        AND a.RECEIVE_TIME_START <= :toDatePublish \n" +
                    ")\n" +
                    "GROUP BY \n" +
                    "    TRUNC(time_start)\n" +
                    ")\n" +
                    "GROUP BY \n" +
                    "    time\n" +
                    "ORDER BY \n" +
                    "    time  \n";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("fromDatePublish", fromDatePublish);
            query.setParameter("toDatePublish", toDatePublish);
            if (H.isTrue(cityId)) {
                query.setParameter("cityId", cityId);
            }

            List<Object[]> dataList = query.getResultList();
            List<ReportPublicAuctionAssetByDay> data = new ArrayList<>();
            for (Object[] record : dataList) {
                ReportPublicAuctionAssetByDay item = new ReportPublicAuctionAssetByDay();
                item.setPublishDate(record[0] == null ? null : (Date) record[0]);
                item.setCountPerDay1(record[1] == null ? null : Long.parseLong(record[1].toString()));
                item.setCountPerDay2(record[2] == null ? null : Long.parseLong(record[2].toString()));
                data.add(item);
            }

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", data));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public AuctioneerOfProvinceDto getAuctioneerOfProvince(boolean isGetDataDetailProvince, String cityName) {

        String sql = "SELECT COUNT(*) FROM AIMS_AUCTIONEER WHERE AUCTIONEER_TYPE != 3";
        Query query = entityManager.createNativeQuery(sql, Long.class);
        Long totalCount = (Long) query.getSingleResult();

        List<AuctioneerOfProvinceDto.Data> data = new ArrayList<>();
        if (isGetDataDetailProvince) {
            String sqlDetail = """
                    SELECT aic.ID, aic.CODE, aic.NAME, COUNT(1) AS count FROM AIMS_AUCTIONEER a 
                    INNER JOIN AIMS_CATEGORY aic ON aic.id = a.ADDR_CITY_ID
                    WHERE a.AUCTIONEER_TYPE != 3
                    """;
            if (org.springframework.util.StringUtils.hasText(cityName)) {
                sqlDetail += " AND LOWER(aic.NAME) LIKE :cityName";
            }
            sqlDetail += " GROUP BY aic.ID, aic.CODE, aic.NAME";
            Query queryDetail = entityManager.createNativeQuery(sqlDetail);
            if(org.springframework.util.StringUtils.hasText(cityName)) {
                queryDetail.setParameter("cityName", "%" + cityName.toLowerCase() + "%");
            }

            @SuppressWarnings("unchecked")
            List<Object[]> dataList = queryDetail.getResultList();

            for (Object[] record : dataList) {
                AuctioneerOfProvinceDto.Data item = AuctioneerOfProvinceDto.Data.builder()
                        .cityId(record[0] == null ? null : ((Number) record[0]).longValue())
                        .cityCode(record[1] == null ? null : record[1].toString())
                        .cityName(record[2] == null ? null : record[2].toString())
                        .auctioneerCount(record[3] == null ? 0L : ((Number) record[3]).longValue())
                        .build();
                data.add(item);
            }
        }
        return AuctioneerOfProvinceDto.builder()
                .totalCount(totalCount == null || totalCount <= 0 ? 0L : totalCount)
                .data(data)
                .build();
    }

    @Override
    public Long getAuctionOrgCount() {
        String sql = """
                SELECT COUNT( * ) FROM
                (
                SELECT a.id FROM AIMS_ORGANIZATION a WHERE a.STATUS = 0 AND a.org_type IN ( 0, 1, 2 ) 
                UNION
                SELECT a.ID FROM AIMS_ORGANIZATION a WHERE ( a.status != 3 OR a.status IS NULL ) AND a.org_type = 4
                )
                """;
        Query query = entityManager.createNativeQuery(sql, Long.class);
        Long totalCount = (Long) query.getSingleResult();
        return (totalCount == null || totalCount <= 0) ? 0L : totalCount;
    }

    public List<AuFiles> findOrgHisFileByHisId(Long hisId) {
        String sql = "select  b.* from aims_org_his_files a, aims_files b where a.org_his_id=:hisId and a.file_id=b.id";
        Query query = entityManager.createNativeQuery(sql, AuFiles.class);
        List<AuFiles> auFiles = new ArrayList<>();
        query.setParameter("hisId", hisId);
        auFiles = query.getResultList();

        return auFiles;
    }

    public OrganizationHis timkiemDNThemMoi(Long orgId) {
        OrganizationHis organizationhis = new OrganizationHis();
        try {
            String sql = "select * "
                    + " from aims_org_his a"
                    + " where a.org_id =:orgId AND a.act_type in (8,11,12) ";
            Query query = entityManager.createNativeQuery(sql, OrganizationHis.class);
            query.setParameter("orgId", orgId);

            organizationhis = (OrganizationHis) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return organizationhis;
    }

    private Object loadDataQuantityOrganizationAuction(Long cityCode, String fromDate, String toDate) {
        PagingResult result = reportQuantityOrganizationAuction(String.valueOf(cityCode), null, fromDate, toDate, 1, 100000, null, 0, false).getBody().getData();
        if(!H.isTrue(result) || !H.isTrue(result.getItems())){
            return null;
        }
        List<Reaport> items = result.getItems();
        //bỏ dòng tổng số
        //tính % từ column 2 đến column 6
        Reaport total = H.isTrue(items) ? items.get(0) : null;
        if (!H.isTrue(total)) {
            return null;
        }
        //if any column is null, set it to 0
        if (total.getCol_2() == null) total.setCol_2("0");
        if (total.getCol_3() == null) total.setCol_3("0");
        if (total.getCol_4() == null) total.setCol_4("0");
        if (total.getCol_5() == null) total.setCol_5("0");
        if (total.getCol_6() == null) total.setCol_6("0");
        Long sumTotal = Long.parseLong(total.getCol_2()) + Long.parseLong(total.getCol_3()) + Long.parseLong(total.getCol_4()) + Long.parseLong(total.getCol_5()) + Long.parseLong(total.getCol_6());
        //làm trong 2 số thập phân
        String precentCol2 = String.valueOf(Math.round((Long.parseLong(total.getCol_2()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol3 = String.valueOf(Math.round((Long.parseLong(total.getCol_3()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol4 = String.valueOf(Math.round((Long.parseLong(total.getCol_4()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol5 = String.valueOf(Math.round((Long.parseLong(total.getCol_5()) * 100.0 / sumTotal) * 100.0) / 100.0);
        String precentCol6 = String.valueOf(Math.round((Long.parseLong(total.getCol_6()) * 100.0 / sumTotal) * 100.0) / 100.0);

        List<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Trung tâm dịch vụ đấu giá tài sản");
        map.put("value", Arrays.asList(precentCol2, total.getCol_2()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Doanh nghiệp đấu giá tư nhân");
        map.put("value", Arrays.asList(precentCol3, total.getCol_3()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Công ty đấu giá hợp danh");
        map.put("value", Arrays.asList(precentCol4, total.getCol_4()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "Chi nhánh doanh nghiệp đấu giá");
        map.put("value", Arrays.asList(precentCol5, total.getCol_5()));
        data.add(map);

        map = new HashMap<>();
        map.put("name", "VAMC");
        map.put("value", Arrays.asList(precentCol6, total.getCol_6()));
        data.add(map);

        return data;
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportAuctionCard(String cityId, String fromDateRaw, String toDateRaw, int pageNumber, int numberPerPage, String actTypes, String aTypes) {
        int offset = 0;
        PagingResult result = new PagingResult();
        try {
            List<String> cityIds = new ArrayList<>();
            List<String> cityIds_bk = new ArrayList<>();

            if (H.isTrue(cityId)) {
                String[] cityArr = cityId.split(",");
                for (String city : cityArr) {
                    cityIds.add(city);
                }
            }

            if (H.isTrue(cityId) && cityIds.get(0).length() < 3) {
                for (String city : cityIds) {
                    Category category = categoryRepository.getByCodeAndCatType(Long.parseLong(city), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                    if (H.isTrue(category)) {
                        cityIds_bk.add(category.getId().toString());
                    }
                }
            }

            if(H.isTrue(cityIds_bk)){
                cityIds = cityIds_bk;
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityIds = Collections.singletonList(category.getId().toString());
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị quản lý", null), HttpStatus.OK);
                }
            }

            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);

            List<Integer> actTypeList = new ArrayList<>();
            if (StringUtils.isNotBlank(actTypes)) {
                String[] actTypeArr = actTypes.split(",");
                for (String actType : actTypeArr) {
                    actTypeList.add(Integer.parseInt(actType));
                }
            }

            if (H.isTrue(aTypes)) {
                String[] aTypeArr = aTypes.split(",");
                for (String aType : aTypeArr) {
                    /*
                    Map lại bắt đầu từ 2
                    er(description = "Tiêu chí: actTypes: " +
                    " 1 -> Cấp mới CCHN đấu giá \n" +
                    " 2 -> Cấp lại CCHN đấu giá \n " +
                    " 3 -> Thu hồi CCHN đấu giá \n" +
                    " 4 -> Cấp mới thẻ ĐGV \n" +
                    " 5-> Cấp lại thẻ ĐGV \n" +
                    " 6 -> Thu hồi thẻ ĐGV
                     */
                    if (aType.equals("2")) actTypeList.add(1);
                    if (aType.equals("3")) actTypeList.add(2);
                    if (aType.equals("4")) actTypeList.add(3);
                    if (aType.equals("5")) actTypeList.add(5);
                    if (aType.equals("6")) actTypeList.add(6);
                    if (aType.equals("7")) actTypeList.add(7);
                }
            }

            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(cityIds) ) {
                whereClause += " AND rp.ADDR_CITY_ID in :cityId ";
                whereClauseAddress += " AND ID in :cityId ";
            }
            if (fromDate != null) {
                whereClause += " and rp.DATE_OF_DECISION >= :fromDate ";
            }
            if (toDate != null) {
                whereClause += " and rp.DATE_OF_DECISION <= :toDate ";
            }
            if (actTypeList.size() > 0) {
                whereClause += " and rp.ACT_TYPE in (:actTypeList) ";
            }


            String initQuery = "" +
                    " WITH distinct_province AS (\n" +
                    " SELECT " +
//                    " name as province_name " +
                    " CASE \n" +
                    "        WHEN name LIKE 'Tỉnh %' THEN REPLACE(name, 'Tỉnh ', '') \n" +
                    "        WHEN name LIKE 'Thành Phố %' THEN REPLACE(name, 'Thành Phố ', '')\n" +
                    "        WHEN name LIKE 'Thành phố %' THEN REPLACE(name, 'Thành phố ', '')\n" +
                    "        WHEN name LIKE 'Sở Tư Pháp %' THEN REPLACE(name, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE name \n" +
                    "    END AS PROVINCE_NAME " +
                    " from AIMS_CATEGORY\n" +
                    " WHERE CAT_TYPE = 'TP' \n" + whereClauseAddress + " \n" +
                    " group by name ORDER BY name\n" +
                    "), " +
                    " \n" +
                    " report_B6 AS (\n" +
                    "    SELECT\n" +
                    "        his.id,\n" +
                    "        his.ACT_TYPE,\n" +
                    "        his.AUCTIONEER_ID," +
                    "        his.DATE_OF_DECISION, \n" +
                    "        au.ADDR_CITY_ID \n" +
                    "    FROM\n" +
                    "        AIMS_AUCTIONEER_HIS his\n" +
                    "        LEFT JOIN AIMS_AUCTIONEER au ON his.AUCTIONEER_ID = au.ID\n" +
                    "    WHERE\n" +
                    "        his.ACT_TYPE IN (1, 2, 3, 5, 6, 7, 10)\n" +
                    "        AND au.AUCTIONEER_TYPE <> 3\n" +
                    "),\n" +
                    " summary_report AS (\n" +
                    "    SELECT \n" +
//                    "        aic.name AS province_name, \n" +
                    " CASE \n" +
                    "        WHEN aic.name LIKE 'Tỉnh %' THEN REPLACE(aic.name, 'Tỉnh ', '') \n" +
                    "        WHEN aic.name LIKE 'Thành Phố %' THEN REPLACE(aic.name, 'Thành Phố ', '')\n" +
                    "        WHEN aic.name LIKE 'Thành phố %' THEN REPLACE(aic.name, 'Thành phố ', '')\n" +
                    "        ELSE aic.name\n" +
                    "    END AS province_name, " +
                    "        rp.ACT_TYPE, \n" +
                    "        COUNT(1) AS act_count\n" +
                    "    FROM \n" +
                    "        report_B6 rp\n" +
                    "        LEFT JOIN AIMS_CATEGORY aic ON aic.id = rp.ADDR_CITY_ID\n" +
                    "    WHERE \n" +
                    "        rp.ACT_TYPE IN (1, 2, 3, 5, 6, 7)\n" + whereClause +
                    "    GROUP BY \n" +
                    "        aic.name, rp.ACT_TYPE\n" +
                    ")\n";
            String sqlQuery = "" +
                    "SELECT \n" +
                    "    dka.province_name AS \"col_1\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 1 THEN act_count ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 2 THEN act_count ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 3 THEN act_count ELSE 0 END) AS \"col_4\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 5 THEN act_count ELSE 0 END) AS \"col_5\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 6 THEN act_count ELSE 0 END) AS \"col_6\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 7 THEN act_count ELSE 0 END) AS \"col_7\"\n" +
                    "FROM distinct_province dka left join \n" +
                    "    summary_report rp ON dka.PROVINCE_NAME = rp.province_name\n" +
                    "GROUP BY \n" +
                    "    dka.province_name\n" +
                    "ORDER BY \n" +
                    "    dka.province_name\n";

            String sqlQueryTotal = "" +
                    "SELECT \n" +
                    "     'Tổng số' as \"col_1\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 1 THEN act_count ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 2 THEN act_count ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 3 THEN act_count ELSE 0 END) AS \"col_4\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 5 THEN act_count ELSE 0 END) AS \"col_5\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 6 THEN act_count ELSE 0 END) AS \"col_6\",\n" +
                    "    SUM(CASE WHEN ACT_TYPE = 7 THEN act_count ELSE 0 END) AS \"col_7\"\n" +
                    "FROM \n" +
                    "    summary_report\n" +
                    "      ";
            String sqlCountRow = initQuery + "\n" + "Select count(1) from (" + sqlQuery + ")";

            sqlQuery = UtilData.paginationOracle(sqlQuery, offset, numberPerPage);
            String sql = initQuery + sqlQuery;
            String sqlTotal = initQuery + sqlQueryTotal;
            Query query = entityManager.createNativeQuery(sql);
            Query queryTotal = entityManager.createNativeQuery(sqlTotal);
            Query queryCountRow = entityManager.createNativeQuery(sqlCountRow);

            if (H.isTrue(cityIds)) {
                query.setParameter("cityId", cityIds);
                queryTotal.setParameter("cityId", cityIds);
                queryCountRow.setParameter("cityId", cityIds);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryCountRow.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryCountRow.setParameter("toDate", toDate);
            }
            if (actTypeList.size() > 0) {
                query.setParameter("actTypeList", actTypeList);
                queryTotal.setParameter("actTypeList", actTypeList);
                queryCountRow.setParameter("actTypeList", actTypeList);
            }
            List<Object[]> totalList = queryTotal.getResultList();
            List<Object[]> dataList = query.getResultList();
            List<Reaport> items = new ArrayList<>();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_7(record[i] == null ? "0" : record[i].toString());
                i++;

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
                total.setCol_6(total.getCol_6() == null ? reaport.getCol_6() : String.valueOf((Long.parseLong(reaport.getCol_6()) + Long.parseLong(total.getCol_6()))));
                total.setCol_7(total.getCol_7() == null ? reaport.getCol_7() : String.valueOf((Long.parseLong(reaport.getCol_7()) + Long.parseLong(total.getCol_7()))));
            });
            if (totalList != null && totalList.size() > 0) {
                dataList.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();

                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_6(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_7(record[i] == null ? "0" : record[i].toString());
                    i++;

                    items.add(reaport);
                });
                items.add(0, total);

            }
            int count = ((Number) queryCountRow.getSingleResult()).intValue();
            result.setRowCount(count);

            result.setItems(items);

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuction(String cityId, String fromDateRaw, String toDateRaw, int pageNumber, int numberPerPage, String actTypes) {
        int offset = 0;
        PagingResult result = new PagingResult();
        try {
            List<String> cityIds = new ArrayList<>();
            List<String> cityIds_bk = new ArrayList<>();

            if (H.isTrue(cityId)) {
                String[] cityArr = cityId.split(",");
                for (String city : cityArr) {
                    cityIds.add(city);
                }
            }

            if (H.isTrue(cityId) && cityIds.get(0).length() < 3) {
                for (String city : cityIds) {
                    Category category = categoryRepository.getByCodeAndCatType(Long.parseLong(city), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                    if (H.isTrue(category)) {
                        cityIds_bk.add(category.getId().toString());
                    }
                }
            }

            if(H.isTrue(cityIds_bk)){
                cityIds = cityIds_bk;
            }


            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityIds = Collections.singletonList(category.getId().toString());
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị quản lý", null), HttpStatus.OK);
                }
            }

            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);

            List<Integer> listStatusOK = new ArrayList<>();
            if (H.isTrue(actTypes)) {
                String[] actTypeArr = actTypes.split(",");
                for (String actType : actTypeArr) {
                    if (actType.equals("2")) listStatusOK.addAll(Arrays.asList(5, 6));
                    if (actType.equals("3")) listStatusOK.addAll(Arrays.asList(0, 1, 2));
                    if (actType.equals("4")) listStatusOK.add(4);
                }
            }
            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(cityIds)) {
                whereClause += " AND b.ADDR_CITY in :cityId ";
                whereClauseAddress += " AND ID in :cityId ";
            }
            if (fromDate != null) {
                whereClause += " and a.RECEIVE_TIME_START >= :fromDate ";
            }
            if (toDate != null) {
                whereClause += " and a.RECEIVE_TIME_START <= :toDate ";
            }
            if (H.isTrue(listStatusOK)) {
                whereClause += " and a.PUBLISH_STATUS in (:listStatusOK) ";
            }


            String initQuery = "" +
                    " WITH distinct_province AS (\n" +
                    " SELECT " +
//                    " name as province_name " +
                    " CASE \n" +
                    "        WHEN name LIKE 'Tỉnh %' THEN REPLACE(name, 'Tỉnh ', '') \n" +
                    "        WHEN name LIKE 'Thành Phố %' THEN REPLACE(name, 'Thành Phố ', '')\n" +
                    "        WHEN name LIKE 'Thành phố %' THEN REPLACE(name, 'Thành phố ', '')\n" +
                    "        WHEN name LIKE 'Sở Tư Pháp %' THEN REPLACE(name, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE name \n" +
                    "    END AS PROVINCE_NAME " +
                    " from AIMS_CATEGORY\n" +
                    " WHERE CAT_TYPE = 'TP' \n" + whereClauseAddress + " \n" +
                    " group by name ORDER BY name\n" +
                    "), " +
                    " report_b7 AS (\n" +
                    "   SELECT\n" +
                    "      a.id,\n" +
                    "      a.RECEIVE_TIME_START,\n" +
                    "      a.publish_status,\n" +
                    "      a.IS_BLOCK,\n" +
//                    "      aic.name AS province \n" +
                    " CASE \n" +
                    "        WHEN aic.name LIKE 'Tỉnh %' THEN REPLACE(aic.name, 'Tỉnh ', '') \n" +
                    "        WHEN aic.name LIKE 'Thành Phố %' THEN REPLACE(aic.name, 'Thành Phố ', '')\n" +
                    "        WHEN aic.name LIKE 'Thành phố %' THEN REPLACE(aic.name, 'Thành phố ', '')\n" +
                    "        ELSE aic.name\n" +
                    "    END AS province " +
                    "   FROM\n" +
                    "      aims_choice_org_notice a\n" +
                    "      JOIN aims_property_owner b ON b.id = a.owner_id\n" +
                    "      LEFT JOIN aims_choice_org_notice a2 ON a.id = a2.root_id \n" +
                    "      AND a2.PUBLISH_STATUS = 4\n" +
                    "      LEFT JOIN AIMS_CATEGORY aic ON aic.id = b.ADDR_CITY \n" +
                    "   WHERE\n" +
                    "      a2.id IS NULL \n" +
                    "      AND a.IS_BLOCK IS NULL \n" + whereClause +
                    "   ),\n" +
                    "   summary_report AS ( \n" +
                    "   SELECT rp.province, rp.publish_status, COUNT(1) AS total FROM report_b7 rp GROUP BY rp.province, rp.publish_status\n" +
                    "   ) ";

            String sqlQuery = "" +
                    "   SELECT \n" +
                    "   dka.PROVINCE_NAME as \"col_1\",\n" +
                    "   SUM(CASE WHEN publish_status in (5,6) THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "   SUM(CASE WHEN publish_status in (0,1,2) THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "   SUM(CASE WHEN publish_status = 4 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "   \n" +
                    "   from distinct_province dka left join summary_report sm on dka.PROVINCE_NAME = sm.province\n" +
                    "   GROUP BY dka.PROVINCE_NAME \n";

            String sqlQueryTotal = "" +
                    "   SELECT \n" +
                    "   'Tổng số' as \"col_1\",\n" +
                    "   SUM(CASE WHEN publish_status in (5,6) THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "   SUM(CASE WHEN publish_status in (0,1,2) THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "   SUM(CASE WHEN publish_status = 4 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "   \n" +
                    "   from summary_report sm\n" +
                    "";

            String sqlCountRow = initQuery + "\n" + "Select count(1) from (" + sqlQuery + ")";
            sqlQuery = UtilData.paginationOracle(sqlQuery, offset, numberPerPage);
            String sql = initQuery + sqlQuery;
            String sqlTotal = initQuery + sqlQueryTotal;
            Query query = entityManager.createNativeQuery(sql);
            Query queryTotal = entityManager.createNativeQuery(sqlTotal);
            Query queryCountRow = entityManager.createNativeQuery(sqlCountRow);

            if (H.isTrue(cityIds)) {
                query.setParameter("cityId", cityIds);
                queryTotal.setParameter("cityId", cityIds);
                queryCountRow.setParameter("cityId", cityIds);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryCountRow.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryCountRow.setParameter("toDate", toDate);
            }
            if (H.isTrue(listStatusOK)) {
                query.setParameter("listStatusOK", listStatusOK);
                queryTotal.setParameter("listStatusOK", listStatusOK);
                queryCountRow.setParameter("listStatusOK", listStatusOK);
            }
            List<Object[]> totalList = queryTotal.getResultList();
            List<Object[]> dataList = query.getResultList();
            List<Reaport> items = new ArrayList<>();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                i++;

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
            });
            if (totalList != null && totalList.size() > 0) {
                dataList.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();

                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                    i++;

                    items.add(reaport);
                });
                items.add(0, total);

            }
            int count = ((Number) queryCountRow.getSingleResult()).intValue();
            result.setRowCount(count);

            result.setItems(items);

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportNoticeAuctionAsset(String cityId, String fromDateRaw, String toDateRaw, int pageNumber, int numberPerPage, String aTypes) {
        int offset = 0;
        PagingResult result = new PagingResult();
        try {
            List<String> cityIds = new ArrayList<>();
            List<String> cityIds_bk = new ArrayList<>();

            if (H.isTrue(cityId)) {
                String[] cityArr = cityId.split(",");
                for (String city : cityArr) {
                    cityIds.add(city);
                }
            }

            if (H.isTrue(cityId) && cityIds.get(0).length() < 3) {
                for (String city : cityIds) {
                    Category category = categoryRepository.getByCodeAndCatType(Long.parseLong(city), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                    if (H.isTrue(category)) {
                        cityIds_bk.add(category.getId().toString());
                    }
                }
            }

            if(H.isTrue(cityIds_bk)){
                cityIds = cityIds_bk;
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                Category category = categoryRepository.getByCodeAndCatType(userLogin.getAdministrationId(), Constants.CATEGORY_TYPE.PROVINCE).orElse(null);
                if (H.isTrue(category)) {
                    cityIds = Collections.singletonList(category.getId().toString());
                } else {
                    return new ResponseEntity<>(new ApiResponseV1<>(false, 40, "Không tìm thấy thông tin đơn vị quản lý", null), HttpStatus.OK);
                }
            }

            if (pageNumber > 0) {
                offset = (pageNumber - 1) * numberPerPage;
            }
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);
            String whereClause = "";
            String whereClause2 = "";
            String whereClauseAddress = "";

            List<Integer> listStatusOk1 = new ArrayList<>();
            List<Integer> listStatusOk2 = new ArrayList<>();
            if (H.isTrue(aTypes)) {
                String[] aTypeArr = aTypes.split(",");
                for (String aType : aTypeArr) {
                    if (aType.equals("2")) listStatusOk1.addAll(Arrays.asList(5, 8));
                    if (aType.equals("3")) listStatusOk1.add(4);
                    if (aType.equals("4")) listStatusOk2.addAll(Arrays.asList(5, 8));
                    if (aType.equals("5")) listStatusOk2.add(4);
                }
            }

            if (H.isTrue(cityIds)) {
                whereClause += " AND aic.id in :cityId ";
                whereClause2 += " AND aic.id in :cityId ";
                whereClauseAddress += " AND ID in :cityId ";
            }
            if (fromDate != null) {
                whereClause += " and a.RECEIVE_TIME_START >= :fromDate ";
                whereClause2 += " and a.AUC_TIME >= :fromDate ";
            }
            if (toDate != null) {
                whereClause += " and a.RECEIVE_TIME_START <= :toDate ";
                whereClause2 += " and a.AUC_TIME <= :toDate ";
            }
            if (H.isTrue(listStatusOk1)) {
                whereClause += " and a.PUBLISH_STATUS in (:listStatusOk1) ";
            }
            if (H.isTrue(listStatusOk2)) {
                whereClause2 += " and a.AUC_STATUS in (:listStatusOk2) ";
            }
            if (H.isTrue(listStatusOk1) && !H.isTrue(listStatusOk2)) {
                whereClause2 += " and 1=0 ";
            }
            if (!H.isTrue(listStatusOk1) && H.isTrue(listStatusOk2)) {
                whereClause += " and 1=0 ";
            }
            String initQuery = "" +
                    " WITH distinct_province AS (\n" +
                    " SELECT " +
//                    " name as province_name " +
                    " CASE \n" +
                    "        WHEN name LIKE 'Tỉnh %' THEN REPLACE(name, 'Tỉnh ', '') \n" +
                    "        WHEN name LIKE 'Thành Phố %' THEN REPLACE(name, 'Thành Phố ', '')\n" +
                    "        WHEN name LIKE 'Thành phố %' THEN REPLACE(name, 'Thành phố ', '')\n" +
                    "        WHEN name LIKE 'Sở Tư Pháp %' THEN REPLACE(name, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE name \n" +
                    "    END AS PROVINCE_NAME " +
                    " from AIMS_CATEGORY\n" +
                    " WHERE CAT_TYPE = 'TP' \n" + whereClauseAddress + " \n" +
                    " group by name ORDER BY name\n" +
                    "), " +
                    "    report_b8 AS (\n" +
                    "         SELECT\n" +
                    "            a.ID,\n" +
                    "            a.PUBLISH_STATUS,\n" +
//                    "            MAX(aic.name) as province\n" +
                    " CASE \n" +
                    "        WHEN MAX(aic.name) LIKE 'Tỉnh %' THEN REPLACE(MAX(aic.name), 'Tỉnh ', '') \n" +
                    "        WHEN MAX(aic.name) LIKE 'Thành Phố %' THEN REPLACE(MAX(aic.name), 'Thành Phố ', '')\n" +
                    "        WHEN MAX(aic.name) LIKE 'Thành phố %' THEN REPLACE(MAX(aic.name), 'Thành phố ', '')\n" +
                    "        ELSE MAX(aic.name) \n" +
                    "    END AS province " +
                    "        FROM AIMS_CHOICE_ORG_NOTICE a\n" +
                    "        LEFT JOIN\n" +
                    "            AIMS_CHOICE_RESULT b ON b.CHOICE_ORG_ID = a.ID\n" +
                    "        LEFT JOIN\n" +
                    "            AIMS_ORGANIZATION e ON b.ORG_ID = e.ID\n" +
//                    "        LEFT JOIN\n" +
//                    "            AIMS_PROPERTY_OWNER f ON a.OWNER_ID = f.ID\n" +
                    "        INNER JOIN \n" +
                    "            AIMS_CATEGORY aic ON aic.id = e.ADDR_CITY_ID \n  " +
                    "        WHERE 1=1 " + whereClause +
                    "       GROUP BY a.id, a.PUBLISH_STATUS " +
                    "    ),\n" +
                    "    \n" +
                    "    report_b8_2 AS (\n" +
                    "        SELECT\n" +
                    "            a.ID,\n" +
                    "            a.AUC_STATUS,\n" +
                    "            aig.ADDR_CITY_ID,\n" +
                    "            a.IS_BLOCK,\n" +
//                    "            aic.name AS province \n" +
                    " CASE \n" +
                    "        WHEN aic.name LIKE 'Tỉnh %' THEN REPLACE(aic.name, 'Tỉnh ', '') \n" +
                    "        WHEN aic.name LIKE 'Thành Phố %' THEN REPLACE(aic.name, 'Thành Phố ', '')\n" +
                    "        WHEN aic.name LIKE 'Thành phố %' THEN REPLACE(aic.name, 'Thành phố ', '')\n" +
                    "        ELSE aic.name\n" +
                    "    END AS province  " +
                    "        FROM\n" +
                    "            AIMS_AUC_INFO a\n" +
                    "        INNER JOIN \n" +
                    "            AIMS_ORGANIZATION aig ON aig.id = a.ORG_ID \n" +
//                    "        INNER JOIN \n" +
//                    "            aims_property_owner d ON a.owner_id = d.id\n" +
                    "        LEFT JOIN \n" +
                    "            (SELECT root_id FROM aims_auc_info A2 WHERE A2.root_id IS NOT NULL AND A2.auc_status = 4) ro \n" +
                    "            ON a.id = ro.root_id\n" +
                    "        LEFT JOIN \n" +
                    "            AIMS_CATEGORY aic ON aic.id = aig.ADDR_CITY_ID \n" +
                    "        WHERE 1=1 \n" +
                    "            \n" + whereClause2 +
                    "            AND a.IS_BLOCK IS NULL \n" +
                    "            AND ro.root_id IS NULL\n" +
                    "    ),\n" +
                    "    \n" +
                    "    combined_summary AS (\n" +
                    "        SELECT \n" +
                    "            rp.province, \n" +
                    "            rp.publish_status AS status, \n" +
                    "            COUNT(1) AS total, \n" +
                    "            'report_b8' AS source \n" +
                    "        FROM report_b8 rp \n" +
                    "        GROUP BY rp.province, rp.publish_status\n" +
                    "        \n" +
                    "        UNION ALL\n" +
                    "        \n" +
                    "        SELECT \n" +
                    "            rp.province, \n" +
                    "            rp.AUC_STATUS AS status, \n" +
                    "            COUNT(1) AS total, \n" +
                    "            'report_b8_2' AS source \n" +
                    "        FROM report_b8_2 rp \n" +
                    "        GROUP BY rp.province, rp.AUC_STATUS\n" +
                    "    ) ";


            String sqlQuery = "" +
                    "  SELECT \n" +
                    "    dka.PROVINCE_NAME AS \"col_1\",\n" +
                    "    SUM(CASE WHEN status IN (5, 8) AND source = 'report_b8' THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN status = 4 AND source = 'report_b8' THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN status IN (5, 8, 13) AND source = 'report_b8_2' THEN total ELSE 0 END) AS \"col_4\", \n" +
                    "    SUM(CASE WHEN status = 4 AND source = 'report_b8_2' THEN total ELSE 0 END) AS \"col_5\" \n" +
                    "FROM distinct_province dka left join  \n" +
                    "    combined_summary rp ON dka.PROVINCE_NAME = rp.province \n" +
                    "GROUP BY \n" +
                    "    dka.PROVINCE_NAME order by dka.PROVINCE_NAME\n";

            String sqlQueryTotal = "" +
                    "  SELECT \n" +
                    "    'Tổng số' AS \"col_1\",\n" +
                    "    SUM(CASE WHEN status IN (5, 8) AND source = 'report_b8' THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN status = 4 AND source = 'report_b8' THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN status IN (5, 8, 13) AND source = 'report_b8_2' THEN total ELSE 0 END) AS \"col_4\", \n" +
                    "    SUM(CASE WHEN status = 4 AND source = 'report_b8_2' THEN total ELSE 0 END) AS \"col_5\" \n" +
                    "FROM \n" +
                    "    combined_summary\n  \n" +
                    "";

            String sqlCountRow = initQuery + "\n" + "Select count(1) from (" + sqlQuery + ")";
            sqlQuery = UtilData.paginationOracle(sqlQuery, offset, numberPerPage);
            String sql = initQuery + sqlQuery;
            String sqlTotal = initQuery + sqlQueryTotal;
            Query query = entityManager.createNativeQuery(sql);
            Query queryTotal = entityManager.createNativeQuery(sqlTotal);
            Query queryCountRow = entityManager.createNativeQuery(sqlCountRow);

            if (H.isTrue(cityIds)) {
                query.setParameter("cityId", cityIds);
                queryTotal.setParameter("cityId", cityIds);
                queryCountRow.setParameter("cityId", cityIds);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryCountRow.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryCountRow.setParameter("toDate", toDate);
            }
            if (H.isTrue(listStatusOk1)) {
                query.setParameter("listStatusOk1", listStatusOk1);
                queryTotal.setParameter("listStatusOk1", listStatusOk1);
                queryCountRow.setParameter("listStatusOk1", listStatusOk1);
            }
            if (H.isTrue(listStatusOk2)) {
                query.setParameter("listStatusOk2", listStatusOk2);
                queryTotal.setParameter("listStatusOk2", listStatusOk2);
                queryCountRow.setParameter("listStatusOk2", listStatusOk2);
            }
            List<Object[]> totalList = queryTotal.getResultList();
            List<Object[]> dataList = query.getResultList();
            List<Reaport> items = new ArrayList<>();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
            AtomicInteger rowcount = new AtomicInteger();

            totalList.stream().forEach((record) -> {
                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                i++;

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
            });

            if (totalList != null && totalList.size() > 0) {
                dataList.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();
                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_3(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_4(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_5(record[i] == null ? "0" : record[i].toString());
                    i++;

                    items.add(reaport);
                });
                items.add(0, total);
            }
            int count = ((Number) queryCountRow.getSingleResult()).intValue();
            result.setRowCount(count);

            result.setItems(items);

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportQuantityOrganizationAuction(String cityId, String orgType, String fromDate, String toDate, String fileType, HttpServletRequest request, HttpServletResponse response, String aTypes, Boolean getDetailDistrict) {
        try {
            PagingResult data = reportQuantityOrganizationAuction(cityId, orgType, fromDate, toDate, 1, 100000, aTypes, null, getDetailDistrict).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title =Báo cáo THỐNG KÊ SỐ LƯỢNG TỔ CHỨC HÀNH NGHỀ ĐẤU GIÁ
            //header = STT	Địa danh hành chính	Trung tâm dịch vụ đấu giá tài sản	Doanh nghiệp đấu giá tư nhân	Công ty đấu giá hợp danh	Chi nhánh DN đấu giá tài sản	VAMC
            header.add("STT");
            header.add("Địa danh hành chính");
            header.add("Trung tâm dịch vụ đấu giá tài sản");
            header.add("Doanh nghiệp đấu giá tư nhân");
            header.add("Công ty đấu giá hợp danh");
            header.add("Chi nhánh DN đấu giá tài sản");
            header.add("VAMC");


            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getCol_1());
                row.add(items.get(i).getCol_2());
                row.add(items.get(i).getCol_3());
                row.add(items.get(i).getCol_4());
                row.add(items.get(i).getCol_5());
                row.add(items.get(i).getCol_6());
                dataExport.add(row);
            }
            String fileName = "BctkSoLuongToChucHnDauGia.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo thống kê số lượng tổ chức hành nghề đầu giá";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportOperationOrganizationAuction(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes, String aTypes) {
        try {
            PagingResult data = reportOperationOrganizationAuction(cityId, fromDate, toDate, 1, 100000, actTypes, aTypes, false).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title =Báo cáo thống kê tình hình hoạt động của tổ chức hành nghề đầu giá
            //header = STT	Địa bàn hành chính	Thành lập/cấp mới	Cấp lại giấy ĐKHĐ doanh nghiệp	"Cấp giấy ĐKHĐ
            //chi nhánh"	Quyết định thành lập VPĐD	Thay đổi thông tin TCĐG	Chuyển đổi TT sang Doanh nghiệp	Thu hồi ĐKHĐ	Sáp nhập	Giải thể	Hợp nhất	Phá sản
            header.add("STT");
            header.add("Địa bàn hành chính");
            header.add("Thành lập/cấp mới");
            header.add("Cấp lại giấy ĐKHĐ doanh nghiệp");
            header.add("Cấp giấy ĐKHĐ chi nhánh");
            header.add("Quyết định thành lập VPĐD");
            header.add("Thay đổi thông tin TCĐG");
            header.add("Chuyển đổi TT sang Doanh nghiệp");
            header.add("Thu hồi ĐKHĐ");
            header.add("Sáp nhập");
            header.add("Giải thể");
            header.add("Hợp nhất");
            header.add("Phá sản");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getCol_1());
                row.add(items.get(i).getCol_2());
                row.add(items.get(i).getCol_3());
                row.add(items.get(i).getCol_4());
                row.add(items.get(i).getCol_5());
                row.add(items.get(i).getCol_6());
                row.add(items.get(i).getCol_7());
                row.add(items.get(i).getCol_8());
                row.add(items.get(i).getCol_9());
                row.add(items.get(i).getCol_10());
                row.add(items.get(i).getCol_11());
                row.add(items.get(i).getCol_12());
                dataExport.add(row);
            }
            String fileName = "BctkDongCuaToChucHanNghiepDauGia.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo thống kê tình hình hoạt động của tổ chức hành nghề đầu giá";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportAuctionCard(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes, String aTypes) {
        try {
            PagingResult data = reportAuctionCard(cityId, fromDate, toDate, 1, 100000, actTypes, aTypes).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title =Báo cáo thống kê tình hình quản lý thẻ đấu giá viên
            //header =  STT	Địa bàn hành chính	Cấp mới CCHN đấu giá	Cấp lại CCHN đấu giá 	Thu hồi CCHN đấu giá		Cấp mới thẻ ĐGV		Cấp lại thẻ ĐGV		Thu hồi thẻ ĐGV
            header.add("STT");
            header.add("Địa bàn hành chính");
            header.add("Cấp mới CCHN đấu giá");
            header.add("Cấp lại CCHN đấu giá");
            header.add("Thu hồi CCHN đấu giá");
            header.add("Cấp mới thẻ ĐGV");
            header.add("Cấp lại thẻ ĐGV");
            header.add("Thu hồi thẻ ĐGV");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getCol_1());
                row.add(items.get(i).getCol_2());
                row.add(items.get(i).getCol_3());
                row.add(items.get(i).getCol_4());
                row.add(items.get(i).getCol_5());
                row.add(items.get(i).getCol_6());
                row.add(items.get(i).getCol_7());
                dataExport.add(row);
            }
            String fileName = "BctkTheDauGiaToChucHanNghiepDauGia.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo thống kê tình hình quản lý thẻ đấu giá viên";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportNoticeAuction(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String actTypes) {
        try {
            PagingResult data = reportNoticeAuction(cityId, fromDate, toDate, 1, 100000, actTypes).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title = Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá
            //header =  STT	Địa bàn hành chính	Thông báo có nội dung không phù hợp		Thông báo có nội dung phù hợp				Thông báo đã công khai
            header.add("STT");
            header.add("Địa bàn hành chính");
            header.add("Thông báo có nội dung không phù hợp");
            header.add("Thông báo có nội dung phù hợp");
            header.add("Thông báo đã công khai");


            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getCol_1());
                row.add(items.get(i).getCol_2());
                row.add(items.get(i).getCol_3());
                row.add(items.get(i).getCol_4());
                dataExport.add(row);

            }
            String fileName = "BctkThongBaoDauGiaToChucHanNghiepDauGia.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo tổng hợp số liệu thông báo công khai việc đấu giá";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportNoticeAuctionAsset(String cityId, String fromDate, String toDate, HttpServletRequest request, HttpServletResponse response, String aTypes) {
        try {
            PagingResult data = reportNoticeAuctionAsset(cityId, fromDate, toDate, 1, 100000, aTypes).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title = Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản
            //header =  STT	Địa bàn hành chính	Thông báo kết quả lựa chọn có nội dung không phù hợp		Thông báo kết quả lựa chọn đã công khai			Thông báo việc đấu giá chờ duyệt		Thông báo việc đấu giá đã công khai
            //
            header.add("STT");
            header.add("Địa bàn hành chính");
            header.add("Thông báo kết quả lựa chọn có nội dung không phù hợp");
            header.add("Thông báo kết quả lựa chọn đã công khai");
            header.add("Thông báo việc đấu giá chờ duyệt");
            header.add("Thông báo việc đấu giá đã công khai");


            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(items.get(i).getCol_1());
                row.add(items.get(i).getCol_2());
                row.add(items.get(i).getCol_3());
                row.add(items.get(i).getCol_4());
                row.add(items.get(i).getCol_5());
                dataExport.add(row);

            }
            String fileName = "BctkThongBaoDauGiaTaiSanToChucHanNghiepDauGia.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo tổng hợp số liệu thông báo lựa chọn tổ chức đấu giá tài sản";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(response, header, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), org.springframework.http.HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> getTcDGTSByMap() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            PagingResult data = reportQuantityOrganizationAuction(null, "0,1,2,4", null, null, 1, 100000, null, 0, false).getBody().getData();
            List<Reaport> items = data.getItems();
            //bỏ dòng 1. col 1 là name lấy tổng col 2 -> col 7
            for (int i = 1; i < items.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();

                Reaport item = items.get(i);
                Long total = 0L;
                if (H.isTrue(item.getCol_2())) {
                    total += Long.parseLong(item.getCol_2());
                }
                if (H.isTrue(item.getCol_3())) {
                    total += Long.parseLong(item.getCol_3());
                }
                if (H.isTrue(item.getCol_4())) {
                    total += Long.parseLong(item.getCol_4());
                }
                if (H.isTrue(item.getCol_5())) {
                    total += Long.parseLong(item.getCol_5());
                }
                if (H.isTrue(item.getCol_6())) {
                    total += Long.parseLong(item.getCol_6());
                }
                if (H.isTrue(item.getCol_7())) {
                    total += Long.parseLong(item.getCol_7());
                }
                map.put("name", item.getCol_1());
                map.put("value", total);
                result.put(item.getCol_1(), map);
            }
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", result), org.springframework.http.HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<Organization> danhsachChiNhanhAll(Long orgRoot) {
        String sql = " select a.* from aims_organization a  where a.status!=3 and a.org_root=" + orgRoot;
        Query query = entityManager.createNativeQuery(sql, Organization.class);
        List<Organization> list = new ArrayList<>();
        if (query.getResultList().size() > 0) {
            list = query.getResultList();
        }
        return list;
    }

    public List<Auctioneer> findAuctioneersByOrgId(Long orgId) {

        List<Auctioneer> lts = new ArrayList<>();
        String sql = " select a.* from aims_auctioneer a  where a.org_id=" + orgId + " OR (a.id in (select  DISTINCT(c.auctioneer_id) from aims_auctioneer_org c where c.org_id in (select  a.id from aims_organization  a where a.org_root=" + orgId + " OR a.id=" + orgId + ")))";
        Query query = entityManager.createNativeQuery(sql, Auctioneer.class);
        if (query.getResultList().size() > 0) {
            lts = (List<Auctioneer>) query.getResultList();
        }
        return lts;
    }

    public List<AuMemberParter> TimTVHD(Long orgId) {
        String sql = "SELECT "
                + "    a.* "
                + "FROM AIMS_MEMBER_PARTER a "
                + "WHERE "
                + "    a.org_id = " + orgId + " ";

        Query query = entityManager.createNativeQuery(sql, AuMemberParter.class);
        List<AuMemberParter> lts = new ArrayList<>();
        try {
            lts = query.getResultList();
        } catch (Exception e) {
        }

        return lts;
    }

    public List<OrganizationHis> findListDsQD(Long orgId) {

        List<OrganizationHis> lst = new ArrayList<>();
        try {
            String sql = "select a.* from aims_org_his a where (a.status!=3 or a.status is null) and a.org_id=" + orgId;
            Query query = entityManager.createNativeQuery(sql, OrganizationHis.class);
            lst = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lst;
    }

    public Auctioneer findManagerByOrgId(Long orgId, Long status) {
        Auctioneer auctioneer = new Auctioneer();
        String sql = "";
        if (status == Constants.Organization.STATUS.ORG_STATUS_ACTIVE) {
            sql = "SELECT a.* from AIMS_AUCTIONEER a, AIMS_AUCTIONEER_ORG b WHERE a.ID=b.AUCTIONEER_ID  AND b.ORG_ID=" + orgId;
            //a.AUCTIONEER_TYPE=2 AND
        } else {
            sql = "SELECT "
                    + "    f.* "
                    + "FROM "
                    + "    ( "
                    + "        SELECT "
                    + "            a.* "
                    + "        FROM "
                    + "            aims_auctioneer       a, "

                    + "            aims_auctioneer_his   c "
                    + "        WHERE "
                    + "            c.source_log=0 "
                    + "            AND a.id = c.auctioneer_id "
                    + "            AND c.auctioneer_type = 2 "
                    + "            AND c.org_id =" + orgId + " "
                    + "        ORDER BY "
                    + "            a.id desc "
                    + "    ) f "
                    + "WHERE "
                    + "    ROWNUM = 1";
        }


        Query query = entityManager.createNativeQuery(sql, Auctioneer.class);
        if (query.getResultList().size() > 0) {
            auctioneer = (Auctioneer) query.getResultList().get(0);
        }
        return auctioneer;
    }
}
