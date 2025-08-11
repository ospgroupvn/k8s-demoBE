package com.osp.bttp.dao.service.tccc.impl;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.FileOrgNotary;
import com.osp.bttp.dao.model.entity.db1.DmAdministration;
import com.osp.bttp.dao.model.entity.db1.OrgNotaryInfo;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.NotaryInfoView;
import com.osp.bttp.dao.model.mview.db1.OrgNotaryInfoView;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.model.mview.db1.TimeLineView;
import com.osp.bttp.dao.service.AccUserService;
import com.osp.bttp.dao.service.tccc.NotaryActivityService;
import com.osp.bttp.dao.service.tccc.TcccService;
import com.osp.bttp.dao.service.tccc.ProbationaryInfoDAO;
import jakarta.persistence.*;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 10/10/2024 - 10:58 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class TcccServiceImpl implements TcccService {
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;
    @Autowired
    private AccUserService accUserService;
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Value("${redis.enable}")
    private boolean redisEnable;
    @Autowired
    private ProbationaryInfoDAO probationaryInfoDAO;
    @Autowired
    private NotaryActivityService notaryActivityService;

    public final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public PagingResult list_search(String name, Long orgId, String status, PagingResult page, String fromDate, String toDate) {
        String key_cache = "list_search_" + name + "_" + orgId + "_" + status + "_" + fromDate + "_" + toDate + "_" + page.getPageNumber() + "_" + page.getNumberPerPage();
        Integer minutesCache = 180;
        if (H.isTrue(fromDate) && H.isTrue(toDate)) {
            //check nếu trong tháng hiện tại thì houseCache = 1
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            String[] arrFromDate = fromDate.split("/");
            String[] arrToDate = toDate.split("/");
            if (Integer.parseInt(arrFromDate[1]) == month && Integer.parseInt(arrToDate[1]) == month && Integer.parseInt(arrFromDate[2]) == year && Integer.parseInt(arrToDate[2]) == year) {
                minutesCache = 10;
            }
            if ( redisEnable && redisTemplate.hasKey(key_cache)) {
                return (PagingResult) redisTemplate.opsForValue().get(key_cache);
            }
        }


        int offset = 0;
        int numberPerPage = page.getNumberPerPage();

        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }

//        Long provinceCode = H.isTrue(orgId) ? orgId.toString() : null;
//            String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
            orgId = Long.parseLong(String.valueOf(userLogin.getAdministrationId()));
        } else if (H.isTrue(orgId)) {
            orgId = orgId;
        } else {
            orgId = 0L;
        }


        List<OrgNotaryInfoView> items = new ArrayList<>();
        List<Object[]> db = new ArrayList<>();

        Date fromDateSign = null;
        Date toDateSign = null;


        String init = "" +
                "WITH \n" +
                " rank_org_notary_action AS (\n" +
                " SELECT ona.*, ROW_NUMBER() OVER (PARTITION BY org_notary_info_id,type ORDER BY id DESC) AS rn\n" +
                " FROM org_notary_action ona WHERE ona.type = 1 \n" +
                " )," +
                " ranked_notary_reg_practice AS (\n" +
                "    SELECT nrp.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                "    FROM notary_reg_practice nrp\n" +
                "    WHERE nrp.active = :active AND nrp.status = :nrpStatus\n" + // Điều kiện active và status
                "),  \n" +
                // Lấy số công chứng viên có trong tổ chức
                " count_notary_in_org AS (\n" +
                "    SELECT org_notary_info_id, COUNT(*) AS count_notary\n" +
                "    FROM notary_reg_practice\n" +
                "    WHERE active = :active AND status = :nrpStatus\n" + // Điều kiện lọc theo active và status
                "    GROUP BY org_notary_info_id\n" +
                ")  \n";


        StringBuilder stringFilter = new StringBuilder("");
        try {

            if (H.isTrue(fromDate)) {
                fromDateSign = format.parse(fromDate);
                stringFilter.append(" and doc.date_sign >= :fromDate ");
            }
            if (H.isTrue(toDate)) {
                toDateSign = format.parse(toDate);
                stringFilter.append(" and doc.date_sign <= :toDate ");
            }

            if (H.isTrue(status)) {
                stringFilter.append(" and org.STATUS=:status ");
            }

            if (H.isTrue(orgId)) {
                stringFilter.append(" AND org.ADMINISTRATION_ID IN ( SELECT stat.id FROM DM_ADMINISTRATION stat START WITH stat.ID = :idAdminisLogin CONNECT BY PRIOR stat.ID = stat.PARENT_ID ) ");
            }

            if (H.isTrue(name)) {
                stringFilter.append(" and (upper(org.name) like upper(:name) ) ");
            }

            String sql = "select org.id as ONI_ID,org.status,org.name as ONI_NAME,info.name,    \n" +
                    "     decode(org.ADDRESS_ID  , null, org.ADDRESS,  org.ADDRESS||' - '||dma.COMMUNE_NAME||' - '||dma.DISTRICT_NAME||' - '||dma.PROVINCE_NAME) as orgADDRESS,     \n" +
                    "     doc.dispatch_code ,doc.date_sign,          \n" +
                    "     dm.NAME as administration_name, " +
                    "     org.ADMINISTRATION_ID, " +
                    "     cno.count_notary as count_notary, org.tel " +
                    " \n" +
                    "FROM org_notary_info org         \n" +
                    "     LEFT JOIN notary_info info on org.notary_id_office_chief=info.id     \n" +
                    "     LEFT JOIN rank_org_notary_action ac on ac.org_notary_info_id= org.id and ac.active=0 and ac.rn =1      \n" +
                    "     LEFT JOIN dm_document doc on ac.document_id=doc.id and doc.active=0      \n" +
                    "     LEFT JOIN dm_area dma on dma.ID=org.ADDRESS_ID          \n" +
                    "     LEFT JOIN dm_administration dm on dm.id=org.administration_id " +
                    "    LEFT JOIN ranked_notary_reg_practice nrp ON info.id = nrp.notary_info_id AND nrp.rn = 1\n" +
                    "    LEFT JOIN count_notary_in_org cno ON cno.org_notary_info_id = org.id\n" +
                    "where org.active=0 AND dm.type = 2 ";
            sql = init + " \n " + sql;
            sql += stringFilter.toString();
            sql += " order by dm.FULL_NAME, decode(org.status,0,1,2)  ";
            String sqlCount = "select count(*) from (" + sql + ")";
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
//            Query query = entityManager.createNativeQuery("select bf.* FROM (" + sql + ") bf  ");
            Query query = entityManager.createNativeQuery(sql);
            Query queryCount = entityManager.createNativeQuery(sqlCount);

            query.setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            query.setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE);

            queryCount.setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            queryCount.setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE);
            if (H.isTrue(status)) {
                query.setParameter("status", status);
                queryCount.setParameter("status", status);
            }
            if (H.isTrue(orgId)) {
                query.setParameter("idAdminisLogin", orgId);
                queryCount.setParameter("idAdminisLogin", orgId);
            }
            if (H.isTrue(name)) {
                query.setParameter("name", "%" + name + "%");
                queryCount.setParameter("name", "%" + name + "%");
            }
            if (H.isTrue(fromDate)) {
                query.setParameter("fromDate", fromDateSign);
                queryCount.setParameter("fromDate", fromDateSign);
            }
            if (H.isTrue(toDate)) {
                query.setParameter("toDate", toDateSign);
                queryCount.setParameter("toDate", toDateSign);
            }
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView row = new OrgNotaryInfoView();
                row.setIdOrgNotaryInfo(record[0] == null ? null : Long.parseLong(record[0].toString()));
                row.setStatusOrg(record[1] == null ? null : Long.parseLong(record[1].toString()));
                row.setName(record[2] == null ? null : ((String) record[2]));
                row.setOfficeChiefName(record[3] == null ? null : ((String) record[3]));
                row.setAddress(record[4] == null ? null : ((String) record[4]));
                row.setEstablishment(record[5] == null ? null : ((String) record[5]));
                row.setDateEstablishment(record[6] == null ? null : ((Date) record[6]));
                row.setAdminName(record[7] == null ? null : ((String) record[7]).trim());
                row.setCountNotary(record[9] == null ? null : Long.parseLong(record[9].toString()));
                row.setPhoneNumber(record[10] == null ? null : ((String) record[10]));

                items.add(row);
            });

            try {
                int count = ((Number) queryCount.getSingleResult()).intValue();
                page.setRowCount(count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("loi tai OrgNotaryFileDAO.search" + e.getMessage());
        }

        page.setItems(items);

        if (H.isTrue(fromDate) && H.isTrue(toDate)) {
            if ( redisEnable ) redisTemplate.opsForValue().set(key_cache, page, minutesCache, TimeUnit.MINUTES);
        }

        return page;
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(Long type) {
        List<DmAdministration> list = entityManager.createQuery("SELECT dm from DmAdministration dm where dm.isActive = 0 and dm.type=:type").setParameter("type", type).getResultList();
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", list));
    }

    @Override
    public PagingResult list_ccv(String name, Long orgId, String status, PagingResult page, String fromDateRaw, String toDateRaw) {

        int offset = 0;
        int numberPerPage = page.getNumberPerPage();

        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        Integer minutesCache = 150;
        String keyCache = "list_ccv_" + name + "_" + orgId + "_" + status + "_" + fromDateRaw + "_" + toDateRaw + "_" + offset + "_" + numberPerPage;
        if (H.isTrue(fromDateRaw) && H.isTrue(toDateRaw)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            String[] arrFromDate = fromDateRaw.split("/");
            String[] arrToDate = toDateRaw.split("/");
            if (Integer.parseInt(arrFromDate[1]) == month && Integer.parseInt(arrToDate[1]) == month && Integer.parseInt(arrFromDate[2]) == year && Integer.parseInt(arrToDate[2]) == year) {
                minutesCache = 10;
            }
            if ( redisEnable && redisTemplate.hasKey(keyCache)) {
                return (PagingResult) redisTemplate.opsForValue().get(keyCache);
            }
        }


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<NotaryInfoView> items = new ArrayList();
        List<Object[]> db = new ArrayList<>();

        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
            orgId = Long.parseLong(String.valueOf(userLogin.getAdministrationId()));
        } else if (H.isTrue(orgId)) {
            orgId = orgId;
        } else {
            orgId = 0L;
        }

        Date dateSign = null;
        Date dateSignTo = null;


        try {
            if (H.isTrue(fromDateRaw)) {
                dateSign = format.parse(fromDateRaw);
            }
            if (H.isTrue(toDateRaw)) {
                dateSignTo = format.parse(toDateRaw);
            }
//            JSONObject searchObject = new JSONObject(search);

            String init = "" +
                    "WITH ranked_probationary AS (\n" +
                    "    SELECT proinfo.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM probationary_info proinfo\n" +
                    "    WHERE proinfo.active = 0\n" +
                    "),\n" +
                    "ranked_notary_request AS (\n" +
                    "    SELECT nre.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id, REQUEST_TYPE ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre\n" +
                    "    WHERE nre.active = 0\n" +
                    "),\n" +
                    "ranked_notary_appoint AS (\n" +
                    "    SELECT app.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id, type_appoint ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_appoint app\n" +
                    "    WHERE app.active = 0\n" +
                    "),\n" +
                    "ranked_notary_reapppointed AS (\n" +
                    "    SELECT reapp.*, ROW_NUMBER() OVER (PARTITION BY type_reappoint, notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_reapppointed reapp\n" +
                    "    WHERE reapp.active = 0 \n" +
                    "),\n" +
                    "ranked_notary_reg_practice AS (\n" +
                    "    SELECT nrp.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_reg_practice nrp\n" +
                    "    WHERE nrp.active = 0 \n" +
                    "),\n" +
                    "ranked_NOTARY_REQUEST4 AS (\n" +
                    "    SELECT nre4.*, ROW_NUMBER() OVER (PARTITION BY REQUEST_TYPE, notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre4\n" +
                    "    WHERE nre4.active = 0 and nre4.REQUEST_TYPE = 4\n" +
                    "),\n" +
                    "ranked_NOTARY_REQUEST2_3 AS (\n" +
                    "    SELECT nre2_3.*, ROW_NUMBER() OVER (PARTITION BY REQUEST_TYPE, notary_info_id  ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre2_3\n" +
                    "    WHERE nre2_3.active = 0 and nre2_3.REQUEST_TYPE in (2,3)\n" +
                    ")" +
                    "";
            String sql = "FROM    (\n" +
                    "   SELECT\n" +
                    "    info.id,\n" +
                    "    info.status,\n" +
                    "    info.name,\n" +
//                    "    info.birth_day,\n" +
                    "    '' as birth_day,\n" +
                    "    info.id_no,\n" +
                    "    DECODE( redoc.dispatch_code, NULL, doc.dispatch_code, redoc.dispatch_code ) AS dispatch_code,\n" +
                    "    DECODE( redoc.date_sign, NULL, doc.date_sign, redoc.date_sign ) AS date_sign,\n" +
                    "    nrp.number_cad,\n" +
                    "    oni.name AS orgname,\n" +
                    "    info.CREATED_BY,\n" +
                    "    info.UPDATED_BY,\n" +
                    "    info.GEN_DATE,\n" +
                    "    info.LAST_UPDATE,\n" +
                    "    decode( nrp.CREATED_BY, NULL, decode( nre.CREATED_BY, NULL, proinfo.CREATED_BY, nre.CREATED_BY ), nrp.CREATED_BY ) AS userCreate,\n" +
                    "    decode( nrp.ORG_NOTARY_INFO_ID, NULL, proinfo.ORG_NOTARY_INFO_ID, nrp.ORG_NOTARY_INFO_ID ) AS orgNotaryId,\n" +
                    "    nrp.CREATED_BY AS nrpcre,\n" +
                    "    proinfo.CREATED_BY AS procre,\n" +
                    "    nre.CREATED_BY AS nrecre,\n" +
                    "    app.CREATED_BY AS appcre,\n" +
                    "    reapp.CREATED_BY AS reappcre,\n" +
                    "    nre4.CREATED_BY AS nre4cre,\n" +
                    "    nre2_3.CREATED_BY nre2_3cre,\n" +
                    "    nre.REQUEST_TYPE AS request_type_cho_bn,-- chờ bổ nhiệm\n" +
                    "    nre4.REQUEST_TYPE AS request_type_cho_bnl,-- chờ bổ nhiệm lại\n" +
                    "    nre2_3.REQUEST_TYPE AS request_type_cho_mn, --chờ miễn nhiệm\n " +
                    "    DECODE(oni.address_id, NULL, oni.address, oni.address||' - '||dm.commune_name||' - '|| dm.district_name||' - '|| dm.province_name) as ADDRESS " +
                    "    " +
                    "    \n" +
                    "   FROM\n" +
                    "    notary_info info\n" +
                    "    LEFT JOIN ranked_probationary proinfo ON info.id = proinfo.notary_info_id AND proinfo.rn = 1\n" +
                    "    LEFT JOIN org_notary_info org ON proinfo.org_notary_info_id = org.id  \n" +
                    "    LEFT JOIN ranked_notary_request nre ON info.id = nre.notary_info_id AND nre.REQUEST_TYPE = 1 AND nre.rn = 1\n" +
                    "    LEFT JOIN ranked_notary_appoint app ON info.id = app.notary_info_id AND app.type_appoint = 1 AND app.rn = 1\n" +
                    "    LEFT JOIN dm_document doc ON app.document_id = doc.id AND doc.active = 0 \n" +
                    "    LEFT JOIN ranked_notary_reapppointed reapp ON info.id = reapp.notary_info_id AND reapp.type_reappoint = 1 AND reapp.rn = 1\n" +
                    "    LEFT JOIN dm_document redoc ON reapp.document_id = redoc.id \n" +
                    "    AND redoc.active = 0\n" +
                    "    LEFT JOIN ranked_notary_reg_practice nrp ON info.id = nrp.notary_info_id AND nrp.rn = 1 \n " +
//                    "    LEFT JOIN dm_document doc ON nrp.document_id = doc.id AND doc.active = 0 \n" +
                    "    LEFT JOIN org_notary_info oni ON nrp.org_notary_info_id = oni.id \n" +
                    "    AND oni.active = 0\n" +
                    "    left join dm_area dm on dm.id = oni.address_id \n" +
                    "     LEFT JOIN ranked_NOTARY_REQUEST4 nre4 ON info.id = nre4.notary_info_id AND nre4.rn = 1\n" +
                    "    LEFT JOIN ranked_NOTARY_REQUEST2_3 nre2_3 on info.id = nre2_3.notary_info_id AND app.rn = 1\n" +
                    "   WHERE\n" +
                    "    info.ACTIVE = 0 \n" +
                    "   ) aa     \n" +
                    "    INNER JOIN adm_users ureq ON  1=1 \n" +
                    "    and (   ureq.user_name=aa.nrpcre \n" +
                    "            or ureq.user_name=aa.procre \n" +
                    "            or ureq.user_name=aa.nrecre \n" +
                    "            or ureq.user_name=aa.appcre \n" +
                    "            or ureq.user_name=aa.reappcre \n" +
                    "            or ureq.user_name=aa.nre4cre \n" +
                    "            or ureq.user_name=aa.nre2_3cre \n" +
                    "            or ureq.user_name=aa.CREATED_BY \n" +
                    "            or ureq.user_name=aa.UPDATED_BY \n" +
                    "        ) --ureq.user_name = aa.userCreate     \n " +
                    "    inner join DM_ADMINISTRATION adm on adm.id = ureq.ADMINISTRATION_ID \n" +
                    "  \n";
            if (H.isTrue(orgId)) {
                sql += "    AND ureq.administration_id IN ( SELECT stat.id FROM dm_administration stat START WITH stat.id = :adminisId CONNECT BY PRIOR stat.id = stat.parent_id)      \n";
            }
            sql += "    where  1=1      \n";


//            if (H.isTrue(orgId) ) {
//                adminisId = Long.parseLong(searchObject.get("adminisId").toString().trim());
//            }
            if (H.isTrue(name)) {
                sql += "    and upper(aa.name) like upper(:fullName) ";
            }
            if (H.isTrue(status)) {
                sql += "    and aa.status = :status ";
            }
            if (H.isTrue(dateSign)) {
                sql += "    and aa.date_sign >= :dateSignFrom ";
            }
            if (H.isTrue(dateSignTo)) {
                sql += "    and aa.date_sign <= :dateSignTo ";
            }


            StringBuffer sqlColumn = new StringBuffer();
            sqlColumn.append(" SELECT * FROM ( SELECT  DISTINCT  aa.*, adm.NAME AS NAME_ADMIN, ROW_NUMBER() OVER (PARTITION BY aa.id ORDER BY aa.NAME desc) AS rownum_filter  ");
            sqlColumn.append(sql);
            sqlColumn.append(" order by adm.NAME, decode(aa.status, 8,1 ,2) ");
            sqlColumn.append(" ) WHERE rownum_filter = 1 ");

            String sqlCount = "SELECT count(*) FROM    ( SELECT  DISTINCT  aa.* " + sql + ")";
            String sqlQuery = UtilData.paginationOracle(sqlColumn.toString(), offset, numberPerPage);
            //add init to first of sql
            sqlQuery = init + " \n " + sqlQuery;
            sqlCount = init + " \n " + sqlCount;
            Query query = entityManager.createNativeQuery(sqlQuery);
            Query queryCount = entityManager.createNativeQuery(sqlCount);

            if (H.isTrue(name)) {
                query.setParameter("fullName", "%" + name + "%");
                queryCount.setParameter("fullName", "%" + name + "%");
            }
            if (H.isTrue(status)) {
                query.setParameter("status", status);
                queryCount.setParameter("status", status);
            }
            if (H.isTrue(orgId)) {
                query.setParameter("adminisId", orgId);
                queryCount.setParameter("adminisId", orgId);
            }
            if (H.isTrue(dateSign)) {
                query.setParameter("dateSignFrom", dateSign);
                queryCount.setParameter("dateSignFrom", dateSign);
            }
            if (H.isTrue(dateSignTo)) {
                query.setParameter("dateSignTo", dateSignTo);
                queryCount.setParameter("dateSignTo", dateSignTo);
            }

            int count = ((Number) queryCount.getSingleResult()).intValue();


            if (count > 0L) {
                db = query.getResultList();
                db.stream().forEach((record) -> {
                    NotaryInfoView row = new NotaryInfoView();
                    row.setIdNotaryInfo(record[0] == null ? null : Long.parseLong(record[0].toString()));
                    row.setStatusNotaryInfo(record[1] == null ? null : Long.parseLong(record[1].toString()));
                    row.setNameNotaryInfo(record[2] == null ? null : ((String) record[2]));
                    row.setBirthDay(record[3] == null ? null : ((Date) record[3]));
                    row.setIdNo(record[4] == null ? null : ((String) record[4]));

                    row.setDispatchCode(record[5] == null ? null : ((String) record[5]));
                    row.setDateSign(record[6] == null ? null : (Date) record[6]);

                    row.setNumberCad(record[7] == null ? null : ((String) record[7]));
                    row.setNameOrgNotaryInfo(record[8] == null ? null : ((String) record[8]));
                    row.setCreatedBy(record[9] == null ? null : ((String) record[9]));
                    row.setUpdatedBy(record[10] == null ? null : ((String) record[10]));
                    row.setGenDate(record[11] == null ? null : ((Date) record[11]));
                    row.setLastUpdate(record[12] == null ? null : ((Date) record[12]));
                    row.setOrgNotaryAddress(record[25] == null ? null : ((String) record[25]));
                    row.setNameAdmin(record[26] == null ? null : (record[26].toString().trim()));

                    items.add(row);
                });
                page.setRowCount(count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("loi tai NotaryInfoDAO.searchFileNotary: " + e.getMessage());
        }

        page.setItems(items);

        //nếu có truyền fromDate và toDate thì cache lại kết quả tìm kiếm ( cache 1 tiếng )
        if (H.isTrue(fromDateRaw) && H.isTrue(toDateRaw)) {
            if ( redisEnable ) redisTemplate.opsForValue().set(keyCache, page, minutesCache, TimeUnit.MINUTES);
        }

        return page;
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOrganizationNotary(String fromDateRaw, String toDateRaw, String cityId, int pageNumber, int numberPerPage, String status, String aTypes, Integer addressTypeGet, Boolean getDetailDistrict) {
        PagingResult result = new PagingResult();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Reaport> items = new ArrayList<>();
        String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

        int offset = 0;
        if (pageNumber > 0) {
            offset = (pageNumber - 1) * numberPerPage;
        }

        result.setPageNumber(pageNumber);
        result.setNumberPerPage(numberPerPage);


        List<String> listStatus = new ArrayList<>();
        if (H.isTrue(status)) {
            String[] arrStatus = status.split(",");
            for (String s : arrStatus) {
                listStatus.add(s);
            }
        }

        List<String> orgHisTypeListOK = new ArrayList<>();
        if (H.isTrue(aTypes)) {
            String[] arrTypes = aTypes.split(",");
            for (String s : arrTypes) {
                orgHisTypeListOK.add(s);
            }
        }
        if (!H.isTrue(orgHisTypeListOK)) {
            orgHisTypeListOK.add("1");
            orgHisTypeListOK.add("2");
            orgHisTypeListOK.add("3");
            orgHisTypeListOK.add("4");
            orgHisTypeListOK.add("5");
            orgHisTypeListOK.add("6");
            orgHisTypeListOK.add("7");
        }

        try {
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);
//            String provinceCode = StringUtils.isBlank(cityId) ? null : cityId;
            List<String> provinceCodes = new ArrayList<>();
            if (H.isTrue(cityId)) {
                String[] arrCityId = cityId.split(",");
                for (String s : arrCityId) {
                    provinceCodes.add(s);
                }
            }
//            String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                provinceCodes = Arrays.asList(String.valueOf(userLogin.getAdministrationId()));
            }




            String whereClause = "";
            String whereClauseAType = "";
            String whereClauseAddress = "";

            if (H.isTrue(listStatus)) {
                whereClause = " AND onih.status in :status ";
            }
//            if (H.isTrue(orgHisTypeListOK)) {
//                whereClauseAType = " AND orgHisType in :orgHisType ";
//            }
            if (H.isTrue(provinceCodes)) {
//                provinceCode = cityId;
                whereClauseAddress = " AND PROVINCE_CODE in :provinCode ";
            }

            String init = "" +
                    "WITH \n" +
                    " rank_org_notary_action AS (\n" +
                    " SELECT ona.*, ROW_NUMBER() OVER (PARTITION BY org_notary_info_id,type ORDER BY id DESC) AS rn\n" +
                    " FROM org_notary_action ona WHERE ona.type = 1 \n" +
                    " )," +
                    " ranked_notary_reg_practice AS (\n" +
                    "    SELECT nrp.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_reg_practice nrp\n" +
                    "    WHERE nrp.active = :active AND nrp.status = :nrpStatus\n" + // Điều kiện active và status
                    "),  \n" +
                    // Lấy số công chứng viên có trong tổ chức
                    " count_notary_in_org AS (\n" +
                    "    SELECT org_notary_info_id, COUNT(*) AS count_notary\n" +
                    "    FROM notary_reg_practice\n" +
                    "    WHERE active = :active AND status = :nrpStatus\n" + // Điều kiện lọc theo active và status
                    "    GROUP BY org_notary_info_id\n" +
                    "),  \n" +
                    " distinct_province as (\n" +
                    " SELECT ";
//                    " PROVINCE_NAME " +
            if (getDetailDistrict) {
                init += " DISTRICT_NAME AS PROVINCE_NAME ";
            } else {
                init += " CASE \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE PROVINCE_NAME \n" +
                        "    END AS PROVINCE_NAME ";
            }
            init += " from DM_AREA where  1=1 \n" + whereClauseAddress + " \n";
            if (getDetailDistrict) {
                init += " group by DISTRICT_NAME ) \n";
            } else {
                init += " group by PROVINCE_NAME ) \n";
            }

            StringBuilder sql = new StringBuilder();

            sql.append("SELECT * FROM ( ");
            sql.append("SELECT  dka.PROVINCE_NAME, " +
                    " TO_NUMBER(decode(aa.pcchoatDong , null, 0, aa.pcchoatDong)) as pcchoatDong, " +
                    " TO_NUMBER(decode(aa.pccGiaither , null, 0, aa.pccGiaither)) as pccGiaither , " +
                    " TO_NUMBER(decode(aa.pccChuyenDoi , null, 0, aa.pccChuyenDoi)) as pccChuyenDoi, " +
                    " TO_NUMBER(decode(aa.vpccDaThanhLap , null, 0, aa.vpccDaThanhLap)) as vpccDaThanhLap, " +
                    " TO_NUMBER(decode(aa.vpccDangHoatDong , null, 0, aa.vpccDangHoatDong)) as vpccDangHoatDong, " +
                    " TO_NUMBER(decode(aa.vpccChanDut , null, 0, aa.vpccChanDut)) as vpccChanDut, " +
                    " row_number() over(order by dka.PROVINCE_NAME) AS R FROM distinct_province dka left join ( ");
            sql.append("SELECT FULL_NAME  ");

//            sql.append("TO_NUMBER(decode(pcchoatDong , null, 0, pcchoatDong)), ");
//            sql.append("TO_NUMBER(decode(pccGiaither , null, 0, pccGiaither)), ");
//            sql.append("TO_NUMBER(decode(pccChuyenDoi , null, 0, pccChuyenDoi)), ");
//            sql.append("TO_NUMBER(decode(vpccDaThanhLap , null, 0, vpccDaThanhLap)), ");
//            sql.append("TO_NUMBER(decode(vpccDangHoatDong , null, 0, vpccDangHoatDong)), ");
//            sql.append("TO_NUMBER(decode(vpccChanDut , null, 0, vpccChanDut)) ");

            if (orgHisTypeListOK.contains("2")) sql.append(", TO_NUMBER(decode(pcchoatDong , null, 0, pcchoatDong)) as pcchoatDong ");
            else sql.append(", 0 as pcchoatDong ");

            if (orgHisTypeListOK.contains("3")) sql.append(", TO_NUMBER(decode(pccGiaither , null, 0, pccGiaither)) as pccGiaither ");
            else sql.append(", 0 as pccGiaither ");

            if (orgHisTypeListOK.contains("4"))
                sql.append(", TO_NUMBER(decode(pccChuyenDoi , null, 0, pccChuyenDoi)) as pccChuyenDoi ");
            else sql.append(", 0 as pccChuyenDoi ");

            if (orgHisTypeListOK.contains("5"))
                sql.append(", TO_NUMBER(decode(vpccDaThanhLap , null, 0, vpccDaThanhLap)) as vpccDaThanhLap ");
            else sql.append(", 0 as vpccDaThanhLap ");

            if (orgHisTypeListOK.contains("6"))
                sql.append(", TO_NUMBER(decode(vpccDangHoatDong , null, 0, vpccDangHoatDong)) as vpccDangHoatDong ");
            else sql.append(", 0 as vpccDangHoatDong ");

            if (orgHisTypeListOK.contains("7")) sql.append(", TO_NUMBER(decode(vpccChanDut , null, 0, vpccChanDut)) as vpccChanDut ");
            else sql.append(", 0 as vpccChanDut ");


            sql.append("FROM  ( ");
            sql.append(" SELECT total, orgHisType, FULL_NAME FROM ( ");
            sql.append("SELECT COUNT(1) AS total, onih.orgHisType, ");
//                    " da.PROVINCE_NAME AS FULL_NAME ");
            if (getDetailDistrict) {
                sql.append(" da.DISTRICT_NAME AS FULL_NAME  ");
            } else {
                sql.append(" CASE \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        ELSE da.PROVINCE_NAME\n" +
                        "    END AS FULL_NAME ");
            }
            sql.append("FROM (SELECT status, ADDRESS_ID, ID, administration_id, CREATED_BY, NVL(last_update, gen_date) AS dateReport, type || '_' || status AS orgHisType ");
            sql.append("FROM org_notary_info WHERE active = 0 ");
            if (H.isTrue(fromDate)) {
                sql.append("AND gen_date >= :fromDate ");
            }
            // Thêm điều kiện TO_DATE nếu có
            if (H.isTrue(toDate)) {
                sql.append("AND gen_date <= :toDate ");
            }
            sql.append(") onih ");
            sql.append(" LEFT JOIN rank_org_notary_action ac on ac.org_notary_info_id= onih.id and ac.active=0 and ac.rn =1   ");
            sql.append(" LEFT JOIN dm_document doc on ac.document_id=doc.id and doc.active=0   ");
            sql.append(" LEFT JOIN dm_administration dam on dam.id=onih.administration_id   ");
            sql.append(" LEFT JOIN dm_area da ON da.id = onih.ADDRESS_ID    ");

//            sql.append(" INNER JOIN adm_users au ON au.USER_NAME = onih.CREATED_BY AND au.status = 1\n" +
//                    "    INNER JOIN dm_administration dam ON au.administration_id = dam.id AND dam.status = 1\n" +
//                    "    INNER JOIN dm_area da ON dam.address_id = da.id   ");

            // Thêm điều kiện PROVIN_CODE nếu có

            sql.append(" WHERE 1 = 1 ");
            if (H.isTrue(provinceCodes)) {
                sql.append("AND da.PROVINCE_CODE in :provinCode ");
            }

            // Thêm điều kiện P_TYPE_ADMIN nếu có
            if (typeAdm != null && !typeAdm.isEmpty()) {
                sql.append("AND dam.TYPE = :typeAdmin ");
            }

            sql.append(whereClause);

            if (getDetailDistrict) {
                sql.append("GROUP BY onih.orgHisType, da.DISTRICT_NAME ");
            } else sql.append("GROUP BY onih.orgHisType, da.PROVINCE_NAME ");
            sql.append(")  where 1=1 " + whereClauseAType + " ) ");
            sql.append("PIVOT (SUM(total) FOR orgHisType IN ");
            sql.append("('1_0' AS pcchoatDong, '1_2' AS pccGiaither, '1_18' AS pccChuyenDoi, ");
            sql.append("'2_12' AS vpccDaThanhLap, '2_0' AS vpccDangHoatDong, ");
            sql.append("'2_3' AS vpccChanDut, '2_1' AS vpccChoThanhLap )) ");
            sql.append(") aa ON aa.FULL_NAME = dka.PROVINCE_NAME order by dka.PROVINCE_NAME ");
            sql.append(") ");


            // Tạo câu truy vấn
            Query queryCount = entityManager.createNativeQuery(init + sql.toString());
            if (numberPerPage > 0) {
                sql.append("WHERE r > :offset AND r <= (:offset + :limit) ");
            }
            Query query = entityManager.createNativeQuery(init + sql.toString());

            query.setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            queryCount.setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

            query.setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE);
            queryCount.setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE);
            // Set giá trị cho các tham số truy vấn
            if (H.isTrue(fromDate)) {
                query.setParameter("fromDate", fromDate);
                queryCount.setParameter("fromDate", fromDate);
            }

            if (H.isTrue(toDate)) {
                query.setParameter("toDate", toDate);
                queryCount.setParameter("toDate", toDate);
            }

            if (H.isTrue(provinceCodes)) {
                query.setParameter("provinCode", provinceCodes);
                queryCount.setParameter("provinCode", provinceCodes);
            }

            if (typeAdm != null && !typeAdm.isEmpty()) {
                query.setParameter("typeAdmin", typeAdm);
                queryCount.setParameter("typeAdmin", typeAdm);
            }

            if (H.isTrue(listStatus)) {
                query.setParameter("status", listStatus);
                queryCount.setParameter("status", listStatus);
            }

//            if (H.isTrue(orgHisTypeListOK)) {
//                query.setParameter("orgHisType", orgHisTypeListOK);
//                queryCount.setParameter("orgHisType", orgHisTypeListOK);
//            }


            List<Object[]> count = queryCount.getResultList();

            Reaport total = new Reaport();
            total.setCol_1("Tổng số");

            count.stream().forEach((record) -> {
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


                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
                total.setCol_6(total.getCol_6() == null ? reaport.getCol_6() : String.valueOf((Long.parseLong(reaport.getCol_6()) + Long.parseLong(total.getCol_6()))));
                total.setCol_7(total.getCol_7() == null ? reaport.getCol_7() : String.valueOf((Long.parseLong(reaport.getCol_7()) + Long.parseLong(total.getCol_7()))));
                total.setCol_8(total.getCol_8() == null ? reaport.getCol_8() : String.valueOf((Long.parseLong(reaport.getCol_8()) + Long.parseLong(total.getCol_8()))));

            });

            if (count != null && count.size() > 0) {
                result.setRowCount(count.size());


                query.setParameter("offset", offset);
                query.setParameter("limit", numberPerPage);

                List<Object[]> postComments = query.getResultList();

                postComments.stream().forEach((record) -> {
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

                    items.add(reaport);
                });
                items.add(0, total);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setItems(items);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationOrganizationNotary(String fromDateRaw, String toDateRaw, String cityId, String type, int pageNumber, int numberPerPage, String aTypes, boolean getDetailDistrict) {

        int offset = 0;
        if (pageNumber > 0) {
            offset = (pageNumber - 1) * numberPerPage;
        }

        PagingResult result = new PagingResult();
        result.setPageNumber(pageNumber);
        result.setNumberPerPage(numberPerPage);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Reaport> items = new ArrayList<>();

        try {
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);
//            String provinCode = null;
            String whereAddress = "";
            List<String> provinCodes = new ArrayList<>();

            String typeAdm = Constants.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


            if (H.isTrue(cityId)) {
//                provinCode = cityId;
                String[] arrCityId = cityId.split(",");
                for (String s : arrCityId) {
                    provinCodes.add(s);
                }

                if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                    provinCodes = Arrays.asList(String.valueOf(userLogin.getAdministrationId()));
                }

                whereAddress = " AND PROVINCE_CODE in :provinCode ";
            }

//            StoredProcedureQuery query = entityManager
//                    .createStoredProcedureQuery("TK_THHD_TCHNCC")
//                    .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("PROVIN_CODE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_TYPE_ADMIN", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_OFFSET", Integer.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_NUMBER", Integer.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("prc", Class.class, ParameterMode.REF_CURSOR);
//
//            query.setParameter("P_FROM_DATE", UtilsDate.date2str(fromDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(fromDate, "yyyyMMdd"))
//                    .setParameter("P_TO_DATE", UtilsDate.date2str(toDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(toDate, "yyyyMMdd"))
//                    .setParameter("PROVIN_CODE", provinceCode == null ? "" : provinceCode)
//                    .setParameter("P_OFFSET", 0)
//                    .setParameter("P_NUMBER", 0)
//                    .setParameter("P_TYPE_ADMIN", typeAdm);
            List<String> actiontypeOK = new ArrayList<>();

            String whereClause = "";

            if (H.isTrue(type)) {
                String[] arrType = type.split(",");
                for (String s : arrType) {
                    actiontypeOK.add(s);

                }

            }
            if (!H.isTrue(actiontypeOK)) {
                actiontypeOK = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
            }

            String initQuery = "WITH distinct_province as (\n" +
                    "SELECT ";
//                    " PROVINCE_NAME " +
            if (getDetailDistrict) {
                initQuery += " DISTRICT_NAME AS PROVINCE_NAME ";
            } else {
                initQuery += " CASE \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE PROVINCE_NAME \n" +
                        "    END AS PROVINCE_NAME ";
            }
            initQuery += " from DM_AREA where  1=1 \n" + whereAddress + " \n";
            if (getDetailDistrict) {
                initQuery += " group by DISTRICT_NAME ) \n";
            } else {
                initQuery += " group by PROVINCE_NAME ) \n";
            }


            StringBuilder sql = new StringBuilder();

            sql.append("SELECT * FROM ( ");
            sql.append("SELECT aa.*, " +
                    " dka.PROVINCE_NAME, " +
                    " row_number() OVER (ORDER BY admFullName) AS R FROM   distinct_province dka left join ( ");
            sql.append("SELECT admFullName ");
            if (actiontypeOK.contains("2")) sql.append(", TO_NUMBER(decode(thanhLap , null, 0, thanhLap)) ");
            else sql.append(", 0 as thanhLap ");

            if (actiontypeOK.contains("3")) sql.append(", TO_NUMBER(decode(giaithe , null, 0, giaithe)) ");
            else sql.append(", 0 as giaithe ");

            if (actiontypeOK.contains("4")) sql.append(", TO_NUMBER(decode(chuyenDoi , null, 0, chuyenDoi)) ");
            else sql.append(", 0 as chuyenDoi ");

            if (actiontypeOK.contains("5")) sql.append(", TO_NUMBER(decode(vpccThanhLap , null, 0, vpccThanhLap)) ");
            else sql.append(", 0 as vpccThanhLap ");

            if (actiontypeOK.contains("6"))
                sql.append(", TO_NUMBER(decode(vpccTCThanhLap , null, 0, vpccTCThanhLap)) ");
            else sql.append(", 0 as vpccTCThanhLap ");

            if (actiontypeOK.contains("7"))
                sql.append(", TO_NUMBER(decode(vpccTHThanhLap , null, 0, vpccTHThanhLap)) ");
            else sql.append(", 0 as vpccTHThanhLap ");

            if (actiontypeOK.contains("8")) sql.append(", TO_NUMBER(decode(vpccHD , null, 0, vpccHD)) ");
            else sql.append(", 0 as vpccHD ");

            if (actiontypeOK.contains("9")) sql.append(", TO_NUMBER(decode(vpccTCHD , null, 0, vpccTCHD)) ");
            else sql.append(", 0 as vpccTCHD ");

            if (actiontypeOK.contains("10")) sql.append(", TO_NUMBER(decode(vpccTHHD , null, 0, vpccTHHD)) ");
            else sql.append(", 0 as vpccTHHD ");

            if (actiontypeOK.contains("11")) sql.append(", TO_NUMBER(decode(dcHopNhat , null, 0, dcHopNhat)) ");
            else sql.append(", 0 as dcHopNhat ");

            if (actiontypeOK.contains("12")) sql.append(", TO_NUMBER(decode(hopNhat , null, 0, hopNhat)) ");
            else sql.append(", 0 as hopNhat ");

            if (actiontypeOK.contains("13")) sql.append(", TO_NUMBER(decode(biSapNhap , null, 0, biSapNhap)) ");
            else sql.append(", 0 as biSapNhap ");

            if (actiontypeOK.contains("14")) sql.append(", TO_NUMBER(decode(sapNhap , null, 0, sapNhap)) ");
            else sql.append(", 0 as sapNhap ");

            if (actiontypeOK.contains("15")) sql.append(", TO_NUMBER(decode(chuyenNhuong , null, 0, chuyenNhuong)) ");
            else sql.append(", 0 as chuyenNhuong ");

            if (actiontypeOK.contains("16")) sql.append(", TO_NUMBER(decode(TDNDHD , null, 0, TDNDHD)) ");
            else sql.append(", 0 as TDNDHD ");

            if (actiontypeOK.contains("17"))
                sql.append(", TO_NUMBER(decode(viPham , null, 0, viPham)) ");
            else sql.append(", 0 as viPham ");

            if (actiontypeOK.contains("18")) sql.append(", TO_NUMBER(decode(chamDut , null, 0, chamDut)) ");
            else sql.append(", 0 as chamDut ");


            sql.append(" FROM ( ");

            sql.append(" SELECT " +
                    "  total,\n" +
                    "     actiontype,\n" +
                    "     admFullName " +
                    " from  (  ");
            // Truy vấn chính đầu tiên (ACT)
            sql.append("SELECT COUNT(1) AS total, 'ACT_' || oni.type || '_' || DECODE(xxx.type, 15, 13, xxx.type) AS actiontype, ");
            if (getDetailDistrict) {
                sql.append(" da.DISTRICT_NAME AS admFullName ");
            } else {
                sql.append("" +
//                    " da.PROVINCE_NAME AS admFullName " +
                        " CASE \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(da.PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE da.PROVINCE_NAME \n" +
                        "    END AS admFullName ");
            }
            sql.append("FROM org_notary_action xxx ");
            sql.append("INNER JOIN org_notary_info oni ON xxx.org_notary_info_id = oni.id AND oni.active = 0 AND xxx.active = 0 ");
            sql.append("AND xxx.id IN (SELECT MAX(onamax.id) FROM org_notary_action onamax WHERE onamax.org_notary_info_id = oni.id ");
            sql.append("AND onamax.active = 0 GROUP BY onamax.type) ");

            // Thêm điều kiện FROM_DATE và TO_DATE nếu có
            if (H.isTrue(fromDate)) {
                sql.append("AND xxx.gen_date >= :fromDate ");
            }
            if (H.isTrue(toDate)) {
                sql.append("AND xxx.gen_date <= :toDate ");
            }

            sql.append("INNER JOIN adm_users au ON au.USER_NAME = xxx.CREATED_BY AND au.status = 1 ");
            sql.append("INNER JOIN dm_administration dam ON au.administration_id = dam.id AND dam.status = 1 ");
            sql.append("INNER JOIN dm_area da ON dam.address_id = da.id ");

            // Thêm điều kiện PROVIN_CODE nếu có
            if (H.isTrue(provinCodes) && provinCodes.size() > 0) {
                sql.append("AND da.PROVINCE_CODE in :provinCode and dam.TYPE = :typeAdmin ");
            }
            if (getDetailDistrict)
                sql.append("GROUP BY 'ACT_' || oni.type || '_' || DECODE(xxx.type, 15, 13, xxx.type), da.DISTRICT_NAME ");
            else
                sql.append("GROUP BY 'ACT_' || oni.type || '_' || DECODE(xxx.type, 15, 13, xxx.type), da.PROVINCE_NAME ");

            // UNION truy vấn thứ hai (TRF)
            sql.append("UNION ALL SELECT COUNT(1) AS total, 'TRF_' || oni.type || '_' || xxx.type || '_' || ontd.org_notary_type AS actiontype, ");
            if (getDetailDistrict) {
                sql.append(" da.DISTRICT_NAME AS admFullName ");
            } else {
                sql.append("" +
//                    " da.PROVINCE_NAME AS admFullName " +
                        " CASE \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(da.PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE da.PROVINCE_NAME \n" +
                        "    END AS admFullName ");
            }
            sql.append(" FROM org_notary_info oni ");
            sql.append("INNER JOIN org_notary_transfer_detail ontd ON oni.id = ontd.org_notary_info_id AND ontd.active = 0 ");
            sql.append("INNER JOIN org_notary_transfer xxx ON xxx.id = ontd.org_notary_transfer_id AND xxx.active = 0 ");

            // Thêm điều kiện FROM_DATE và TO_DATE cho TRF
            if (H.isTrue(fromDate)) {
                sql.append("AND xxx.gen_date >= :fromDate ");
            }
            if (H.isTrue(toDate)) {
                sql.append("AND xxx.gen_date <= :toDate ");
            }

            sql.append("INNER JOIN adm_users au ON au.USER_NAME = xxx.CREATED_BY AND au.status = 1 ");
            sql.append("INNER JOIN dm_administration dam ON au.administration_id = dam.id AND dam.status = 1 ");
            sql.append("INNER JOIN dm_area da ON dam.address_id = da.id ");

            // Thêm điều kiện PROVIN_CODE nếu có
            if (H.isTrue(provinCodes) && provinCodes.size() > 0) {
                sql.append("AND da.PROVINCE_CODE in :provinCode and dam.TYPE = :typeAdmin ");
            }
            sql.append("WHERE oni.active = 0 ");
            if (getDetailDistrict)
                sql.append("GROUP BY 'TRF_' || oni.type || '_' || xxx.type || '_' || ontd.org_notary_type, da.DISTRICT_NAME ");
            else
                sql.append("GROUP BY 'TRF_' || oni.type || '_' || xxx.type || '_' || ontd.org_notary_type, da.PROVINCE_NAME ");

            // UNION truy vấn thứ ba (ORG_PEN)
            sql.append("UNION ALL SELECT COUNT(1) AS total, 'ORG_PEN' AS actiontype, ");
//                    " da.PROVINCE_NAME AS admFullName " +
            if (getDetailDistrict) {
                sql.append(" da.DISTRICT_NAME AS admFullName ");
            } else {
                sql.append(" CASE \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(da.PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE da.PROVINCE_NAME \n" +
                        "    END AS admFullName " +
                        " ");
            }
            sql.append("FROM org_notary_info oni INNER JOIN org_notary_penalize xxx ON oni.id = xxx.org_notary_id ");
            sql.append("AND oni.active = 0 AND xxx.active = 0 AND xxx.id IN (\n" +
                    "                SELECT\n" +
                    "                    MAX(onpmax.id)\n" +
                    "                FROM\n" +
                    "                    org_notary_penalize onpmax\n" +
                    "                WHERE\n" +
                    "                    xxx.active = 0\n" +
                    "                    AND   oni.id = onpmax.org_notary_id\n" +
                    "            ) ");

            // Thêm điều kiện FROM_DATE và TO_DATE cho ORG_PEN
            if (H.isTrue(fromDate)) {
                sql.append("AND xxx.gen_date >= :fromDate ");
            }
            if (H.isTrue(toDate)) {
                sql.append("AND xxx.gen_date <= :toDate ");
            }

            sql.append("INNER JOIN adm_users au ON au.USER_NAME = xxx.CREATED_BY AND au.status = 1 ");
            sql.append("INNER JOIN dm_administration dam ON au.administration_id = dam.id AND dam.status = 1 ");
            sql.append("INNER JOIN dm_area da ON dam.address_id = da.id ");

            // Thêm điều kiện PROVIN_CODE nếu có
            if (H.isTrue(provinCodes) && provinCodes.size() > 0) {
                sql.append("AND da.PROVINCE_CODE in :provinCode and dam.TYPE = :typeAdmin ");
            }

            if (getDetailDistrict) sql.append("GROUP BY da.DISTRICT_NAME ");
            else sql.append("GROUP BY da.PROVINCE_NAME ");

            // Pivot tổng hợp dữ liệu
            sql.append(" )  where 1=1 " + whereClause + " ) ");
            sql.append(" PIVOT (SUM(total) FOR actiontype IN ('ACT_1_1' AS thanhLap, 'ACT_1_3' AS giaithe, 'TRF_1_4_4' AS chuyenDoi, ");
            sql.append("'ACT_2_0' AS vpccDNThanhLap, 'ACT_2_1' AS vpccThanhLap, 'ACT_2_14' AS vpccTCThanhLap, 'ACT_2_8' AS vpccTHThanhLap, ");
            sql.append("'ACT_2_16' AS vpccDNHD, 'ACT_2_9' AS vpccHD, 'ACT_2_10' AS vpccTCHD, 'ACT_2_11' AS vpccTHHD, ");
            sql.append("'TRF_2_2_6' AS hopNhat, 'TRF_2_2_7' AS dcHopNhat, 'TRF_2_3_1' AS sapNhap, 'TRF_2_3_2' AS biSapNhap, ");
            sql.append("'ACT_2_7' AS chuyenNhuong, 'ACT_2_13' AS TDNDHD, 'ORG_PEN' AS viPham, 'ACT_2_4' AS chamDut)) ");
            sql.append(") aa on aa.admFullName = dka.PROVINCE_NAME ORDER BY dka.PROVINCE_NAME ");
            sql.append(") ");

            // Thêm điều kiện OFFSET và LIMIT nếu có
            if (numberPerPage > 0) {
                sql.append("WHERE R > :offset AND R <= (:offset + :limit) ");
            }

            // Tạo câu truy vấn
            Query query = entityManager.createNativeQuery(initQuery + sql.toString());


            // Set giá trị cho các tham số
            if (H.isTrue(fromDate)) {
                query.setParameter("fromDate", fromDate);
            }

            if (H.isTrue(toDate)) {
                query.setParameter("toDate", toDate);
            }

            if (H.isTrue(provinCodes)) {
                query.setParameter("provinCode", provinCodes);
                query.setParameter("typeAdmin", typeAdm);
            }


            query.setParameter("offset", 0);
            query.setParameter("limit", 1000);

            List<Object[]> count = query.getResultList();

            Reaport total = new Reaport();
            total.setCol_1("Tổng số");

            count.stream().forEach((record) -> {
                Reaport reaport = new Reaport();

                int i = 0;
                reaport.setCol_1(record[i] == null ? "" : record[i].toString());
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
                reaport.setCol_13(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_14(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_15(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_16(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_17(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_18(record[i] == null ? "0" : record[i].toString());
                i++;
                if (H.isTrue(record[i])) {
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                    i++;
                }
//                reaport.setCol_20(record[i] == null ? "0" : record[i].toString());
//                i++;

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
                total.setCol_13(total.getCol_13() == null ? reaport.getCol_13() : String.valueOf((Long.parseLong(reaport.getCol_13()) + Long.parseLong(total.getCol_13()))));
                total.setCol_14(total.getCol_14() == null ? reaport.getCol_14() : String.valueOf((Long.parseLong(reaport.getCol_14()) + Long.parseLong(total.getCol_14()))));
                total.setCol_15(total.getCol_15() == null ? reaport.getCol_15() : String.valueOf((Long.parseLong(reaport.getCol_15()) + Long.parseLong(total.getCol_15()))));
                total.setCol_16(total.getCol_16() == null ? reaport.getCol_16() : String.valueOf((Long.parseLong(reaport.getCol_16()) + Long.parseLong(total.getCol_16()))));
                total.setCol_17(total.getCol_17() == null ? reaport.getCol_17() : String.valueOf((Long.parseLong(reaport.getCol_17()) + Long.parseLong(total.getCol_17()))));
                total.setCol_18(total.getCol_18() == null ? reaport.getCol_18() : String.valueOf((Long.parseLong(reaport.getCol_18()) + Long.parseLong(total.getCol_18()))));
//                total.setCol_19(total.getCol_19() == null ? reaport.getCol_19() : String.valueOf((Long.parseLong(reaport.getCol_19()) + Long.parseLong(total.getCol_19()))));
//                total.setCol_20(total.getCol_20() == null ? reaport.getCol_20() : String.valueOf((Long.parseLong(reaport.getCol_20()) + Long.parseLong(total.getCol_20()))));

            });

            if (count != null && count.size() > 0) {
                result.setRowCount(count.size());

//                query = entityManager
//                        .createStoredProcedureQuery("TK_THHD_TCHNCC")
//                        .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("PROVIN_CODE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_TYPE_ADMIN", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_OFFSET", Integer.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_NUMBER", Integer.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("prc", Class.class, ParameterMode.REF_CURSOR);
//
//                query.setParameter("P_FROM_DATE", UtilsDate.date2str(fromDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(fromDate, "yyyyMMdd"))
//                        .setParameter("P_TO_DATE", UtilsDate.date2str(toDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(toDate, "yyyyMMdd"))
//                        .setParameter("PROVIN_CODE", provinceCode == null ? "" : provinceCode)
//                        .setParameter("P_OFFSET", offset)
//                        .setParameter("P_NUMBER", offset + numberPerPage + 1)
//                        .setParameter("P_TYPE_ADMIN", typeAdm);

                query.setParameter("offset", offset);
                query.setParameter("limit", numberPerPage);

                List<Object[]> postComments = query.getResultList();

                postComments.stream().forEach((record) -> {
                    Reaport reaport = new Reaport();

                    int i = 0;
                    reaport.setCol_1(record[i] == null ? "" : record[i].toString());
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
                    reaport.setCol_13(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_14(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_15(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_16(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_17(record[i] == null ? "0" : record[i].toString());
                    i++;
                    reaport.setCol_18(record[i] == null ? "0" : record[i].toString());
                    i++;
                    if (H.isTrue(record[i])) {
                        reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                        i++;
                    }
//                    reaport.setCol_19(record[i] == null ? "0" : record[i].toString());
//                    i++;
//                    reaport.setCol_20(record[i] == null ? "0" : record[i].toString());
//                    i++;

                    items.add(reaport);
                });
                items.add(0, total);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setItems(items);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationSuggestAppoint(String fromDateRaw, String toDateRaw, String cityId, String type, int pageNumber, int numberPerPage, boolean getDetailDistrict) {
        int offset = 0;
        if (pageNumber > 0) {
            offset = (pageNumber - 1) * numberPerPage;
        }
        PagingResult result = new PagingResult();
        result.setPageNumber(pageNumber);
        result.setNumberPerPage(numberPerPage);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<Reaport> items = new ArrayList<>();

        try {
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);
//            String provinceCode = null;
            List<String> provinCodes = new ArrayList<>();
            String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

            if (H.isTrue(cityId)) {
//                provinceCode = cityId;
                String[] arrCity = cityId.split(",");
                for (String s : arrCity) {
                    provinCodes.add(s);
                }
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                provinCodes = Collections.singletonList(userLogin.getAdministrationId().toString());
            }

//            StoredProcedureQuery query = entityManager
//                    .createStoredProcedureQuery("TK_THBN_CCV_BK")
//                    .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("PROVIN_CODE", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_TYPE_ADMIN", String.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_OFFSET", Integer.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("P_NUMBER", Integer.class, ParameterMode.IN)
//                    .registerStoredProcedureParameter("prc", Class.class, ParameterMode.REF_CURSOR);
//
//            query.setParameter("P_FROM_DATE", UtilsDate.date2str(fromDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(fromDate, "yyyyMMdd"))
//                    .setParameter("P_TO_DATE", UtilsDate.date2str(toDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(toDate, "yyyyMMdd"))
//                    .setParameter("PROVIN_CODE", provinceCode == null ? "" : provinceCode)
//                    .setParameter("P_OFFSET", 0)
//                    .setParameter("P_NUMBER", 0)
//                    .setParameter("P_TYPE_ADMIN", typeAdm);

            String whereAddress = "";
            String whereProvince = "";
            String whereFromDate = "";
            String whereToDate = "";
            String whereRownum = "";
            String whereType = "";

            /*
            Tiêu chí:" +
                    "1: Số lg người tập sự, 2: số lg người đề nghị bổ nhiệm ccv, 3:miễn nhiệm ccv, 4: đề nghị bổ nhiệm lại ccv, 5: số lg bổ nhiệm ccv, 6: số lg miễn nhiệm ccv, 7: bổ nhiểm lại ccv
             */
            List<String> actiontypeOK = new ArrayList<>();
            if (H.isTrue(type)) {
                String[] arrType = type.split(",");
                for (String s : arrType) {
                    if (s.equals("2")) {
                        actiontypeOK.add("tapsu");
                    }
                    if (s.equals("3")) {
                        actiontypeOK.add("choBN");
                    }
                    if (s.equals("4")) {
                        actiontypeOK.add("choMN");
                    }
                    if (s.equals("5")) {
                        actiontypeOK.add("choBNL");
                    }
                    if (s.equals("6")) {
                        actiontypeOK.add("daBN");
                    }
                    if (s.equals("7")) {
                        actiontypeOK.add("daMN");
                    }
                    if (s.equals("8")) {
                        actiontypeOK.add("daBNL");
                    }
                    if (s.equals("9")) {
                        actiontypeOK.add("tuchoiBN");
                    }
                    if (s.equals("10")) {
                        actiontypeOK.add("tuChoiBNL");
                    }
                    if (s.equals("11")) {
                        actiontypeOK.add("tuChoiMN");
                    }
                    if (actiontypeOK.size() > 0) {
                        whereType = " AND aka.strStatus IN :strStatus ";
                    }
                }
            }

            // Build dynamic WHERE clauses based on input parameters
            if (H.isTrue(provinCodes) && provinCodes.size() > 0) {
                whereProvince = " AND da.PROVINCE_CODE in :provinceCode AND dam.TYPE = :typeAdmin ";
                whereAddress = " AND PROVINCE_CODE in :provinceCode ";
            }

            if (H.isTrue(fromDate)) {
                whereFromDate = " AND nih.dateReport >= :fromDate ";
            }

            if (H.isTrue(toDate)) {
                whereToDate = " AND nih.dateReport <= :toDate ";
            }

            whereRownum = " WHERE r > :offset AND r < :number ";

            String initQuery = "WITH distinct_province as (\n" +
                    "SELECT ";
//                    " PROVINCE_NAME " +
            if (getDetailDistrict) {
                initQuery += " DISTRICT_NAME AS PROVINCE_NAME ";
            } else {
                initQuery += " CASE \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE PROVINCE_NAME \n" +
                        "    END AS PROVINCE_NAME ";
            }
            initQuery += " from DM_AREA where  1=1 \n" + whereAddress + " \n";
            if (getDetailDistrict) {
                initQuery += " group by DISTRICT_NAME ) \n";
            } else {
                initQuery += " group by PROVINCE_NAME ) \n";
            }

            String sql = "SELECT * FROM (" +
                    "SELECT aa.*, dka.PROVINCE_NAME,  ROW_NUMBER() OVER(ORDER BY dka.PROVINCE_NAME) AS R FROM distinct_province dka left join (" +
                    "SELECT FULL_NAME, tapsu, choBN, choMN, choBNL, daBN, daMN, daBNL, tuChoiBNL, tuChoiMN, tuchoiBN " +
                    "FROM (SELECT * FROM (" +
                    " SELECT total, strStatus, FULL_NAME from ( " +
                    "SELECT COUNT(1) AS total, " +
                    "CASE " +
                    "WHEN nih.status IN (1, 2) THEN 'tapsu' " +
                    "WHEN nih.status IN (5, 6) THEN 'choBN' " +
                    "WHEN nih.status IN (16, 21) THEN 'choMN' " +
                    "WHEN nih.status IN (17, 19) THEN 'choBNL' " +
                    "WHEN nih.status = 7 THEN 'daBN' " +
                    "WHEN nih.status = 11 THEN 'daMN' " +
                    "WHEN nih.status = 10 THEN 'daBNL' " +
                    "WHEN nih.status = 14 THEN 'tuchoiBN' " +
                    "WHEN nih.status = 20 THEN 'tuChoiBNL' " +
                    "WHEN nih.status = 18 THEN 'tuChoiMN' " +
                    "END AS strStatus, " +
                    "nih.FULL_NAME AS FULL_NAME " +
                    "FROM (SELECT DISTINCT nih.ID, nih.status, ";
//                    " da.PROVINCE_NAME AS FULL_NAME " +

//                    " CASE \n" +
//                    "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
//                    "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
//                    "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
//                    "        ELSE da.PROVINCE_NAME\n" +
//                    "    END AS FULL_NAME " +

            if (getDetailDistrict) {
                sql += " da.DISTRICT_NAME AS FULL_NAME ";
            } else {
                sql += " CASE \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(da.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(da.PROVINCE_NAME, 'Thành phố ', '')\n" +
                        "        WHEN da.PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(da.PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                        "        ELSE da.PROVINCE_NAME \n" +
                        "    END AS FULL_NAME ";
            }

            sql +=  "FROM (select status, CREATED_BY, nvl(last_update, gen_date) as dateReport, UPDATED_BY, ID  from NOTARY_INFO_HIS where active = 0)  nih " +
                    " INNER JOIN adm_users au ON (au.USER_NAME = nih.CREATED_BY OR au.USER_NAME = nih.UPDATED_BY) AND au.status = 1 " +
                    whereFromDate +
                    whereToDate +
                    " INNER JOIN dm_administration dam ON au.administration_id = dam.id AND dam.status = 1 " +
                    " INNER JOIN dm_area da ON dam.address_id = da.id " +
                    whereProvince +
                    ") nih " +
                    "GROUP BY CASE " +
                    "WHEN nih.status IN (1, 2) THEN 'tapsu' " +
                    "WHEN nih.status IN (5, 6) THEN 'choBN' " +
                    "WHEN nih.status IN (16, 21) THEN 'choMN' " +
                    "WHEN nih.status IN (17, 19) THEN 'choBNL' " +
                    "WHEN nih.status = 7 THEN 'daBN' " +
                    "WHEN nih.status = 11 THEN 'daMN' " +
                    "WHEN nih.status = 10 THEN 'daBNL' " +
                    "WHEN nih.status = 14 THEN 'tuchoiBN' " +
                    "WHEN nih.status = 20 THEN 'tuChoiBNL' " +
                    "WHEN nih.status = 18 THEN 'tuChoiMN' " +
                    "END, nih.FULL_NAME) aka where 1=1" + whereType + " \n" + " ) " +
                    "PIVOT (SUM(total) FOR strStatus IN ('tapsu' as tapsu, 'choBN' as choBN, 'choMN' as choMN, 'choBNL' as choBNL, 'daBN' as daBN, 'daMN' as daMN, 'daBNL' as daBNL, 'tuchoiBN' as tuchoiBN, 'tuChoiBNL' as tuChoiBNL, 'tuChoiMN' as tuChoiMN))) " +
                    ") aa on aa.FULL_NAME = dka.PROVINCE_NAME ORDER BY dka.PROVINCE_NAME " +
                    ")" + whereRownum;

            Query query = entityManager.createNativeQuery(initQuery + sql);

            if (H.isTrue(provinCodes) && provinCodes.size() > 0) {
                query.setParameter("provinceCode", provinCodes);
                query.setParameter("typeAdmin", typeAdm);
            }

            if (H.isTrue(fromDate)) {
                query.setParameter("fromDate", fromDate);
            }

            if (H.isTrue(toDate)) {
                query.setParameter("toDate", toDate);
            }

            if (H.isTrue(actiontypeOK)) {
                query.setParameter("strStatus", actiontypeOK);
            }
            query.setParameter("offset", 0);
            query.setParameter("number", 1000);

            List<Object[]> count = query.getResultList();

            Reaport total = new Reaport();
            total.setCol_1("Tổng số");

            count.stream().forEach((record) -> {
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

                if (H.isTrue(record[11])) {
                    reaport.setCol_1(record[11] == null ? "0" : record[11].toString());
                }

                total.setCol_2(total.getCol_2() == null ? reaport.getCol_2() : String.valueOf((Long.parseLong(reaport.getCol_2()) + Long.parseLong(total.getCol_2()))));
                total.setCol_3(total.getCol_3() == null ? reaport.getCol_3() : String.valueOf((Long.parseLong(reaport.getCol_3()) + Long.parseLong(total.getCol_3()))));
                total.setCol_4(total.getCol_4() == null ? reaport.getCol_4() : String.valueOf((Long.parseLong(reaport.getCol_4()) + Long.parseLong(total.getCol_4()))));
                total.setCol_5(total.getCol_5() == null ? reaport.getCol_5() : String.valueOf((Long.parseLong(reaport.getCol_5()) + Long.parseLong(total.getCol_5()))));
                total.setCol_6(total.getCol_6() == null ? reaport.getCol_6() : String.valueOf((Long.parseLong(reaport.getCol_6()) + Long.parseLong(total.getCol_6()))));
                total.setCol_7(total.getCol_7() == null ? reaport.getCol_7() : String.valueOf((Long.parseLong(reaport.getCol_7()) + Long.parseLong(total.getCol_7()))));
                total.setCol_8(total.getCol_8() == null ? reaport.getCol_8() : String.valueOf((Long.parseLong(reaport.getCol_8()) + Long.parseLong(total.getCol_8()))));

            });

            if (count != null && count.size() > 0) {
                result.setRowCount(count.size());

//                query = entityManager
//                        .createStoredProcedureQuery("TK_THBN_CCV_BK")
//                        .registerStoredProcedureParameter("P_FROM_DATE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_TO_DATE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("PROVIN_CODE", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_TYPE_ADMIN", String.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_OFFSET", Integer.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("P_NUMBER", Integer.class, ParameterMode.IN)
//                        .registerStoredProcedureParameter("prc", Class.class, ParameterMode.REF_CURSOR);
//
//                query.setParameter("P_FROM_DATE", UtilsDate.date2str(fromDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(fromDate, "yyyyMMdd"))
//                        .setParameter("P_TO_DATE", UtilsDate.date2str(toDate, "yyyyMMdd").equals("") ? "" : UtilsDate.date2str(toDate, "yyyyMMdd"))
//                        .setParameter("PROVIN_CODE", provinceCode == null ? "" : provinceCode)
//                        .setParameter("P_OFFSET", offset)
//                        .setParameter("P_NUMBER", offset + numberPerPage + 1)
//                        .setParameter("P_TYPE_ADMIN", typeAdm);

                if (H.isTrue(numberPerPage) && H.isTrue(offset)) {
                    query.setParameter("offset", offset);
                    query.setParameter("number", offset + numberPerPage + 1);
                }

                List<Object[]> postComments = query.getResultList();

                postComments.stream().forEach((record) -> {
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
//                    reaport.setCol_9(record[i] == null ? "0" : record[i].toString());
//                    i++;
//                    reaport.setCol_10(record[i] == null ? "0" : record[i].toString());
//                    i++;
//                    reaport.setCol_11(record[i] == null ? "0" : record[i].toString());
//                    i++;

                    if (H.isTrue(record[11])) {
                        reaport.setCol_1(record[11] == null ? "0" : record[11].toString());
                    }

                    items.add(reaport);
                });
                items.add(0, total);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setItems(items);

        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));
    }

    @Override
    public HashMap<String, Object> reportA1(HashMap<String, Object> data, Long cityCode, Date fromDate, Date toDate) {
        try {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long adminisId = userLogin.getAdministrationId();
            String whereClause1 = "";
            String whereClause2 = "";
            if (H.isTrue(fromDate)) {
                whereClause1 += " AND doc.date_sign >= :fromDate ";
                whereClause2 += " AND date_sign >= :fromDate ";
            }
            if (H.isTrue(toDate)) {
                whereClause1 += " AND doc.date_sign <= :toDate ";
                whereClause2 += " AND date_sign <= :toDate ";
            }
            if (H.isTrue(cityCode)) {
//                whereClause1 += " AND dma.province_code = :cityCode ";
                adminisId = cityCode;
            }

            String sql = "" +
                    "WITH ranked_probationary AS (\n" +
                    "    SELECT proinfo.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM probationary_info proinfo\n" +
                    "    WHERE proinfo.active = 0\n" +
                    "),\n" +
                    "ranked_notary_request AS (\n" +
                    "    SELECT nre.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id, REQUEST_TYPE ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre\n" +
                    "    WHERE nre.active = 0\n" +
                    "),\n" +
                    "ranked_notary_appoint AS (\n" +
                    "    SELECT app.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id, type_appoint ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_appoint app\n" +
                    "    WHERE app.active = 0\n" +
                    "),\n" +
                    "ranked_notary_reapppointed AS (\n" +
                    "    SELECT reapp.*, ROW_NUMBER() OVER (PARTITION BY type_reappoint, notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_reapppointed reapp\n" +
                    "    WHERE reapp.active = 0 \n" +
                    "),\n" +
                    "ranked_notary_reg_practice AS (\n" +
                    "    SELECT nrp.*, ROW_NUMBER() OVER (PARTITION BY notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM notary_reg_practice nrp\n" +
                    "    WHERE nrp.active = 0 \n" +
                    "),\n" +
                    "ranked_NOTARY_REQUEST4 AS (\n" +
                    "    SELECT nre4.*, ROW_NUMBER() OVER (PARTITION BY REQUEST_TYPE, notary_info_id ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre4\n" +
                    "    WHERE nre4.active = 0 and nre4.REQUEST_TYPE = 4\n" +
                    "),\n" +
                    "ranked_NOTARY_REQUEST2_3 AS (\n" +
                    "    SELECT nre2_3.*, ROW_NUMBER() OVER (PARTITION BY REQUEST_TYPE, notary_info_id  ORDER BY id DESC) AS rn\n" +
                    "    FROM NOTARY_REQUEST nre2_3\n" +
                    "    WHERE nre2_3.active = 0 and nre2_3.REQUEST_TYPE in (2,3)\n" +
                    ") ,\n" +
                    " rank_org_notary_action AS (\n" +
                    " SELECT ona.*, ROW_NUMBER() OVER (PARTITION BY org_notary_info_id,type ORDER BY id DESC) AS rn\n" +
                    " FROM org_notary_action ona WHERE ona.type = 1 \n" +
                    " )\n" +
                    " \n" +
                    " select * from (\n" +
                    " \n" +
                    "SELECT COUNT(*)\n" +
                    "FROM org_notary_info org         \n" +
                    "     LEFT JOIN notary_info info ON org.notary_id_office_chief = info.id     \n" +
                    "     LEFT JOIN rank_org_notary_action ac ON ac.org_notary_info_id = org.id AND ac.active = 0 AND ac.rn = 1\n" +
                    "     LEFT JOIN dm_document doc ON ac.document_id = doc.id AND doc.active = 0      \n" +
                    "     LEFT JOIN dm_area dma ON dma.ID = org.ADDRESS_ID          \n" +
                    "     LEFT JOIN dm_administration dm ON dm.id = org.administration_id\n" +
                    "WHERE 1=1 " + whereClause1 + "\n" +
                    " AND org.active = 0 " +
                    " and org.ADMINISTRATION_ID in (SELECT stat.id from DM_ADMINISTRATION stat START WITH stat.ID=:adminisId connect by PRIOR stat.ID = stat.PARENT_ID) \n" +
                    "\n" +
                    " UNION ALL\n" +
                    " SELECT count(*) FROM    ( SELECT  DISTINCT  aa.* FROM    (\n" +
                    "   SELECT\n" +
                    "    info.id,\n" +
                    "    info.status,\n" +
                    "    info.name,\n" +
                    "    '' as birth_day,\n" +
                    "    info.id_no,\n" +
                    "    DECODE( redoc.dispatch_code, NULL, doc.dispatch_code, redoc.dispatch_code ) AS dispatch_code,\n" +
                    "    DECODE( redoc.date_sign, NULL,doc.date_sign, redoc.date_sign ) AS date_sign,\n" +
                    "    nrp.number_cad,\n" +
                    "    oni.name AS orgname,\n" +
                    "    info.CREATED_BY,\n" +
                    "    info.UPDATED_BY,\n" +
                    "    info.GEN_DATE,\n" +
                    "    info.LAST_UPDATE,\n" +
                    "    decode( nrp.CREATED_BY, NULL, decode( nre.CREATED_BY, NULL, proinfo.CREATED_BY, nre.CREATED_BY ), nrp.CREATED_BY ) AS userCreate,\n" +
                    "    decode( nrp.ORG_NOTARY_INFO_ID, NULL, proinfo.ORG_NOTARY_INFO_ID, nrp.ORG_NOTARY_INFO_ID ) AS orgNotaryId,\n" +
                    "    nrp.CREATED_BY AS nrpcre,\n" +
                    "    proinfo.CREATED_BY AS procre,\n" +
                    "    nre.CREATED_BY AS nrecre,\n" +
                    "    app.CREATED_BY AS appcre,\n" +
                    "    reapp.CREATED_BY AS reappcre,\n" +
                    "    nre4.CREATED_BY AS nre4cre,\n" +
                    "    nre2_3.CREATED_BY nre2_3cre,\n" +
                    "    nre.REQUEST_TYPE AS request_type_cho_bn,-- chờ bổ nhiệm\n" +
                    "    nre4.REQUEST_TYPE AS request_type_cho_bnl,-- chờ bổ nhiệm lại\n" +
                    "    nre2_3.REQUEST_TYPE AS request_type_cho_mn, --chờ miễn nhiệm\n" +
                    "     DECODE(oni.address_id, NULL, oni.address, oni.address||' - '||dm.commune_name||' - '|| dm.district_name||' - '|| dm.province_name) as ADDRESS         \n" +
                    "   FROM\n" +
                    "    notary_info info\n" +
                    "    LEFT JOIN ranked_probationary proinfo ON info.id = proinfo.notary_info_id AND proinfo.rn = 1\n" +
                    "    LEFT JOIN org_notary_info org ON proinfo.org_notary_info_id = org.id  \n" +
                    "    LEFT JOIN ranked_notary_request nre ON info.id = nre.notary_info_id AND nre.REQUEST_TYPE = 1 AND nre.rn = 1\n" +
                    "    LEFT JOIN ranked_notary_appoint app ON info.id = app.notary_info_id AND app.type_appoint = 1 AND app.rn = 1\n" +
                    "    LEFT JOIN dm_document doc ON app.document_id = doc.id \n" +
                    "    AND doc.active = 0\n" +
                    "    LEFT JOIN ranked_notary_reapppointed reapp ON info.id = reapp.notary_info_id AND reapp.type_reappoint = 1 AND reapp.rn = 1\n" +
                    "    LEFT JOIN dm_document redoc ON reapp.document_id = redoc.id \n" +
                    "    AND redoc.active = 0\n" +
                    "    LEFT JOIN ranked_notary_reg_practice nrp ON info.id = nrp.notary_info_id AND nrp.rn = 1\n" +
                    "    LEFT JOIN org_notary_info oni ON nrp.org_notary_info_id = oni.id \n" +
                    "    AND oni.active = 0\n" +
                    "    left join dm_area dm on dm.id = oni.address_id \n" +
                    "     LEFT JOIN ranked_NOTARY_REQUEST4 nre4 ON info.id = nre4.notary_info_id AND nre4.rn = 1\n" +
                    "    LEFT JOIN ranked_NOTARY_REQUEST2_3 nre2_3 on info.id = nre2_3.notary_info_id AND app.rn = 1\n" +
                    "   WHERE 1=1 " +
                    "    AND info.ACTIVE = 0 \n" +
                    "   ) aa     \n" +
                    "    INNER JOIN adm_users ureq ON  1=1 \n" +
                    "    and (   ureq.user_name=aa.nrpcre \n" +
                    "            or ureq.user_name=aa.procre \n" +
                    "            or ureq.user_name=aa.nrecre \n" +
                    "            or ureq.user_name=aa.appcre \n" +
                    "            or ureq.user_name=aa.reappcre \n" +
                    "            or ureq.user_name=aa.nre4cre \n" +
                    "            or ureq.user_name=aa.nre2_3cre \n" +
                    "            or ureq.user_name=aa.CREATED_BY \n" +
                    "            or ureq.user_name=aa.UPDATED_BY \n" +
                    "        ) --ureq.user_name = aa.userCreate    \n" +
                    " AND ureq.administration_id IN ( SELECT stat.id FROM dm_administration stat START WITH stat.id = :adminisId CONNECT BY PRIOR stat.id = stat.parent_id) \n" +
                    "   \n" +
                    "    where  1=1      \n" + whereClause2 + "\n" +
                    ")\n" +
                    ")";

            String sqlTopcities = "" +
                    "WITH ranked_administrations AS (\n" +
                    "    SELECT \n" +
                    "        dma.name,  \n" +
                    "        COUNT(oni.ID) AS count,\n" +
                    "        ROW_NUMBER() OVER (ORDER BY COUNT(oni.ID) DESC) AS rn\n" +
                    "    FROM \n" +
                    "        ORG_NOTARY_INFO oni \n" +
                    "    LEFT JOIN \n" +
                    "        DM_ADMINISTRATION dma ON dma.id = oni.ADMINISTRATION_ID\n" +
                    "    GROUP BY \n" +
                    "        dma.name\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    CASE \n" +
                    "        WHEN rn <= 5 THEN name \n" +
                    "        ELSE 'Khác' \n" +
                    "    END AS administration_name,\n" +
                    "    SUM(count) AS total_count\n" +
                    "FROM \n" +
                    "    ranked_administrations\n" +
                    "GROUP BY \n" +
                    "    CASE \n" +
                    "        WHEN rn <= 5 THEN name \n" +
                    "        ELSE 'Khác' \n" +
                    "    END\n" +
                    "ORDER BY \n" +
                    "    total_count DESC \n";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("adminisId", adminisId);
            if (H.isTrue(fromDate)) {
                query.setParameter("fromDate", fromDate);
            }
            if (H.isTrue(toDate)) {
                query.setParameter("toDate", toDate);
            }
            if (H.isTrue(cityCode)) {
                query.setParameter("cityCode", cityCode);
            }

            Query queryTopCities = entityManager.createNativeQuery(sqlTopcities);

            List<Object[]> count = query.getResultList();
            if (count != null && count.size() > 0) {
                //Convert bigdecimal to long
                data.put("tk_tccc_ds_tccc", count.get(0) == null ? 0 : Long.parseLong(String.valueOf(count.get(0))));
                data.put("tk_tccc_ds_cgv", count.get(1) == null ? 0 : Long.parseLong(String.valueOf(count.get(1))));
            }
            List<Object[]> topCities = queryTopCities.getResultList();
            if (topCities != null && topCities.size() > 0) {
                List<HashMap<String, Object>> topCitiesData = new ArrayList<>();
                for (Object[] city : topCities) {
                    HashMap<String, Object> cityData = new HashMap<>();
                    cityData.put("administration_name", city[0]);
                    cityData.put("total_count", city[1]);
                    topCitiesData.add(cityData);
                }
                data.put("tk_tccc_top_cities", topCitiesData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportOrganizationNotary(String fromDate, String toDate, String cityId, String status, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String aTypes, Boolean getDetailDistrict) {
        try {
            PagingResult data = reportOrganizationNotary(fromDate, toDate, cityId, 1, 1000000, status, aTypes, null, getDetailDistrict).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> headers = new ArrayList<>();

            //title of report = "Báo cáo số liệu tổ chức hành nghề công chứng"
            //column = TT	Địa danh hành chính	Số PCC đang hoạt động	Số PCC giải thể	Số PCC chuyển đổi	Số VPCC đã thành lập	Số VPCC đang hoạt động	Số VPCC chấm dứt hoạt động.
            headers.add("TT");
            headers.add("Địa danh hành chính");
            headers.add("Số PCC đang hoạt động");
            headers.add("Số PCC giải thể");
            headers.add("Số PCC chuyển đổi");
            headers.add("Số VPCC đã thành lập");
            headers.add("Số VPCC đang hoạt động");
            headers.add("Số VPCC chấm dứt hoạt động");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Reaport item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getCol_1());
                row.add(item.getCol_2());
                row.add(item.getCol_3());
                row.add(item.getCol_4());
                row.add(item.getCol_5());
                row.add(item.getCol_6());
                row.add(item.getCol_7());
                dataExport.add(row);
            }

            String fileName = "BaoCaoSoLieuToChucHanNghiepCongChung.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo số liệu tổ chức hành nghề công chứng";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 0, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationOrganizationNotary(String fromDate, String toDate, String cityId, String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String aTypes) {
        try {
            PagingResult data = reportOperationOrganizationNotary(fromDate, toDate, cityId, type, 1, 1000000, aTypes, false).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> headers = new ArrayList<>();

            //title of report = "Tình hình hoạt động TC HNCC"
            //column = STT	Địa danh hành chính	Số PCC thành lập	"Số PCC bị
            //giải thể"	Số PCC được chuyển đổi	Số VPCC đươc cho phép thành lập	Số VPCC bị từ chối cho phép thành lập	Số VPCC bị thu hồi QĐ cho phép thành lập	Số VPCC được cấp Giấy ĐKHĐ	Số VPCC bị từ chối cấp Giấy ĐKHĐ	Số VPCC bị thu hồi Giấy ĐKHĐ	Số VPCC được hợp nhất	Số VPCC hợp nhất	Số VPCC bị sáp nhập	Số VPCC nhận sáp nhập	Số VPCC được chuyển nhượng	Số VPCC thay đổi nội dung ĐKHĐ	Số Tổ chức HNCC bị xử lý vi phạm	Số VPCC chấm dứt hoạt động
            //
            headers.add("STT");
            headers.add("Địa danh hành chính");
            headers.add("Số PCC thành lập");
            headers.add("Số PCC bị giải thể");
            headers.add("Số PCC được chuyển đổi");
            headers.add("Số VPCC đươc cho phép thành lập");
            headers.add("Số VPCC bị từ chối cho phép thành lập");
            headers.add("Số VPCC bị thu hồi QĐ cho phép thành lập");
            headers.add("Số VPCC được cấp Giấy ĐKHĐ");
            headers.add("Số VPCC bị từ chối cấp Giấy ĐKHĐ");
            headers.add("Số VPCC bị thu hồi Giấy ĐKHĐ");
            headers.add("Số VPCC được hợp nhất");
            headers.add("Số VPCC hợp nhất");
            headers.add("Số VPCC bị sáp nhập");
            headers.add("Số VPCC nhận sáp nhập");
            headers.add("Số VPCC được chuyển nhượng");
            headers.add("Số VPCC thay đổi nội dung ĐKHĐ");
            headers.add("Số Tổ chức HNCC bị xử lý vi phạm");
            headers.add("Số VPCC chấm dứt hoạt động");


            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Reaport item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getCol_1());
                row.add(item.getCol_2());
                row.add(item.getCol_3());
                row.add(item.getCol_4());
                row.add(item.getCol_5());
                row.add(item.getCol_6());
                row.add(item.getCol_7());
                row.add(item.getCol_8());
                row.add(item.getCol_9());
                row.add(item.getCol_10());
                row.add(item.getCol_11());
                row.add(item.getCol_12());
                row.add(item.getCol_13());
                row.add(item.getCol_14());
                row.add(item.getCol_15());
                row.add(item.getCol_16());
                row.add(item.getCol_17());
                row.add(item.getCol_18());
                dataExport.add(row);
            }

            String fileName = "BaoCaoSoLieuHoatDongToChucHanNghiepCongChung.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo số liệu hoạt động tổ chức hành nghề công chứng";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationSuggestAppoint(String fromDate, String toDate, String cityId, String type, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            PagingResult data = reportOperationSuggestAppoint(fromDate, toDate, cityId, type, 1, 1000000, false).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> headers = new ArrayList<>();

            //title of report = "Tình hình hoạt động TC HNCC"
            //column = TT	Địa danh hành chính	Số lượng Người tập sự	Số lượng Người đề nghị bổ nhiệm CCV	"Số lượng Người đề nghị
            //miễn nhiệm CCV"	Số lượng Người đề nghị bổ nhiệm lại CCV	"Số lượng
            //bổ nhiệm CCV"	"Số lượng
            //miễn nhiệm CCV"	"Số lượng
            //bổ nhiệm lại CCV"
            //
            headers.add("TT");
            headers.add("Địa danh hành chính");
            headers.add("Số lượng Người tập sự");
            headers.add("Số lượng Người đề nghị bổ nhiệm CCV");
            headers.add("Số lượng Người đề nghị miễn nhiệm CCV");
            headers.add("Số lượng Người đề nghị bổ nhiệm lại CCV");
            headers.add("Số lượng bổ nhiệm CCV");
            headers.add("Số lượng miễn nhiệm CCV");
            headers.add("Số lượng bổ nhiệm lại CCV");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Reaport item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getCol_1());
                row.add(item.getCol_2());
                row.add(item.getCol_3());
                row.add(item.getCol_4());
                row.add(item.getCol_5());
                row.add(item.getCol_6());
                row.add(item.getCol_7());
                row.add(item.getCol_8());
                dataExport.add(row);
            }

            String fileName = "BaoCaoSoLieuHoatDongDeXuatBoNhiem.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo số liệu hoạt động đề xuất bổ nhiệm";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolation(String fromDateRaw, String toDateRaw, String cityId, int pageNumber, int numberPerPage) {
        int offset = 0;
        if (pageNumber > 0) {
            offset = (pageNumber - 1) * numberPerPage;
        }
        PagingResult result = new PagingResult();
        try {
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);

//            String provinceCode = null;
            List<String> provinceCodes = new ArrayList<>();
            String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

            if (H.isTrue(cityId)) {
//                provinceCode = cityId;
                String[] cityIds = cityId.split(",");
                for (String id : cityIds) {
                    provinceCodes.add(id);
                }
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                provinceCodes = Collections.singletonList(userLogin.getAdministrationId().toString());
            }
            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(provinceCodes)) {
                whereClause += " AND org.ADMINISTRATION_ID in :provinceCode";
                whereClauseAddress += " AND PROVINCE_CODE in :provinceCode";
            }
            if (fromDate != null) {
                whereClause += " AND doc.DATE_SIGN >= :fromDate";
            }
            if (toDate != null) {
                whereClause += " AND doc.DATE_SIGN <= :toDate";
            }


            String init = "" +
                    "WITH " +
                    " distinct_province as (\n" +
                    "SELECT " +
                    " CASE \n" +
                    "        WHEN PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(PROVINCE_NAME, 'Tỉnh ', '') \n" +
                    "        WHEN PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(PROVINCE_NAME, 'Thành Phố ', '')\n" +
                    "        WHEN PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(PROVINCE_NAME, 'Thành phố ', '')\n" +
                    "        WHEN PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE PROVINCE_NAME \n" +
                    "    END AS PROVINCE_NAME " +
                    " from DM_AREA where  1=1 \n" + whereClauseAddress + " \n" +
                    "group by PROVINCE_NAME \n" +
                    "), " +
                    " report_a7 as (\n" +
                    "SELECT\n" +
                    "    pena.LEVER_PENALIZE as level_,\n" +
                    "    CASE WHEN\n" +
                    "                  da.PROVINCE_NAME LIKE 'Tỉnh %' THEN\n" +
                    "                        REPLACE ( da.PROVINCE_NAME, 'Tỉnh ', '' ) \n" +
                    "                        WHEN da.PROVINCE_NAME LIKE 'Thành Phố %' THEN\n" +
                    "                        REPLACE ( da.PROVINCE_NAME, 'Thành Phố ', '' ) \n" +
                    "                        WHEN da.PROVINCE_NAME LIKE 'Thành phố %' THEN\n" +
                    "                        REPLACE ( da.PROVINCE_NAME, 'Thành phố ', '' ) \n" +
                    "                        WHEN da.PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN\n" +
                    "                        REPLACE ( da.PROVINCE_NAME, 'Sở Tư Pháp ', '' ) ELSE da.PROVINCE_NAME \n" +
                    "                  END AS province  \n" +
                    "FROM\n" +
                    "    NOTARY_PENALIZE pena\n" +
                    "    INNER JOIN ORG_NOTARY_INFO org ON pena.ORG_NOTARY_ID = org.ID\n" +
                    "    LEFT JOIN dm_administration dam ON dam.id = org.administration_id\n" +
                    "   INNER JOIN dm_area da ON dam.address_id = da.id \n" +
                    "    INNER JOIN DM_DOCUMENT doc ON pena.DOCUMENT_ID = doc.ID\n" +
//                    "     INNER JOIN dm_area da ON org.address_id = da.id \n" +
                    "    INNER JOIN NOTARY_INFO info ON pena.NOTARY_INFO_ID = info.ID\n" +
                    "    INNER JOIN NOTARY_REG_PRACTICE prac ON info.ID = prac.NOTARY_INFO_ID \n" +
                    "    AND prac.ID IN ( SELECT max( ID ) FROM NOTARY_REG_PRACTICE WHERE NOTARY_INFO_ID = info.ID AND active = 0 GROUP BY NOTARY_INFO_ID ) \n" +
                    "WHERE\n" +
                    "    pena.ACTIVE = 0 \n" + whereClause +
                    "    AND org.ACTIVE = 0 \n" +
                    "    AND doc.ACTIVE = 0 \n" +
                    "    AND info.ACTIVE = 0 \n" +
                    "    AND prac.ACTIVE = 0 \n" +
                    "    AND org.status = 0 \n" +
                    "    AND org.ADMINISTRATION_ID IN ( SELECT stat.id FROM DM_ADMINISTRATION stat START WITH stat.ID = :idAdminisLogin CONNECT BY PRIOR stat.ID = stat.PARENT_ID )\n" +
                    "    ),\n" +
                    "    summary_report AS (\n" +
                    "    select \n" +
                    "     rp.province,\n" +
                    "     rp.level_,\n" +
                    "     count(1) as total\n" +
                    "     from report_a7 rp \n" +
                    "     GROUP BY  rp.province, rp.level_\n" +
                    ") ";

            String sqlQuery = "" +
                    "    select \n" +
                    "    dka.PROVINCE_NAME as \"col1\",\n" +
                    "    SUM(CASE WHEN level_ = 1 THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN level_ = 2 THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN level_ = 3 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "    from distinct_province dka left join summary_report sm on dka.PROVINCE_NAME = sm.province \n" +
                    "    GROUP BY dka.PROVINCE_NAME order by dka.PROVINCE_NAME ";

            String sqlTotal = "" +
                    " select \n" +
                    "    'Tổng số' as \"col1\",\n" +
                    "    SUM(CASE WHEN level_ = 1 THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN level_ = 2 THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN level_ = 3 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "    from distinct_province dka left join summary_report sm on dka.PROVINCE_NAME = sm.province order by dka.PROVINCE_NAME ";

            String sqlRowCount = "select count(*) from (" + sqlQuery + ")";
            sqlQuery = UtilData.paginationOracle(sqlQuery, offset, numberPerPage);
            Query query = entityManager.createNativeQuery(init + sqlQuery);
            Query queryTotal = entityManager.createNativeQuery(init + sqlTotal);
            Query queryRowCount = entityManager.createNativeQuery(init + sqlRowCount);

            query.setParameter("idAdminisLogin", userLogin.getAdministrationId());
            queryTotal.setParameter("idAdminisLogin", userLogin.getAdministrationId());
            queryRowCount.setParameter("idAdminisLogin", userLogin.getAdministrationId());

            if (H.isTrue(provinceCodes)) {
                query.setParameter("provinceCode", provinceCodes);
                queryTotal.setParameter("provinceCode", provinceCodes);
                queryRowCount.setParameter("provinceCode", provinceCodes);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryRowCount.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryRowCount.setParameter("toDate", toDate);
            }

            List<Object[]> dataList = query.getResultList();
            List<Object[]> totalList = queryTotal.getResultList();
            List<Reaport> items = new ArrayList<>();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
//            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
//                rowcount.getAndIncrement();
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString().trim());
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
                    reaport.setCol_1(record[i] == null ? "0" : record[i].toString().trim());
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
            int count = ((Number) queryRowCount.getSingleResult()).intValue();
            result.setRowCount(count);

            result.setItems(items);
//            result.setRowCount(rowcount.get());

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationViolation(String fromDate, String toDate, String cityId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            PagingResult data = reportOperationViolation(fromDate, toDate, cityId, 1, 1000000).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> headers = new ArrayList<>();
            //title = Báo cáo xử lý vi phạm của Công chứng viên
            //header TT	Địa danh hành chính	Kỷ luật		Xử lý vi phạm hành chính		Truy cứu trách nhiệm hình sự
            //
            headers.add("TT");
            headers.add("Địa danh hành chính");
            headers.add("Kỷ luật");
            headers.add("Xử lý vi phạm hành chính");
            headers.add("Truy cứu trách nhiệm hình sự");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Reaport item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getCol_1());
                row.add(item.getCol_2());
                row.add(item.getCol_3());
                row.add(item.getCol_4());
                dataExport.add(row);
            }

            String fileName = "BaoCaoXuLyViPhamCongChungVien.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo xử lý vi phạm của Công chứng viên";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> reportOperationViolationOrganization(String fromDateRaw, String toDateRaw, String cityId, int pageNumber, int numberPerPage) {
        int offset = 0;
        if (pageNumber > 0) {
            offset = (pageNumber - 1) * numberPerPage;
        }
        PagingResult result = new PagingResult();
        try {
            result.setPageNumber(pageNumber);
            result.setNumberPerPage(numberPerPage);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = StringUtils.isBlank(fromDateRaw) ? null : format.parse(fromDateRaw);
            Date toDate = StringUtils.isBlank(toDateRaw) ? null : format.parse(toDateRaw);

//            String provinceCode = null;
            List<String> provinceCodes = new ArrayList<>();
            String typeAdm = ConstantsTccc.TYPE_DMADMINISTRATION.SO_TU_PHAP.toString();

            if (H.isTrue(cityId)) {
//                provinceCode = cityId;
                String[] cityIds = cityId.split(",");
                for (String id : cityIds) {
                    provinceCodes.add(id);
                }
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                provinceCodes = Collections.singletonList(userLogin.getAdministrationId().toString());
            }
            String whereClause = "";
            String whereClauseAddress = "";
            if (H.isTrue(provinceCodes)) {
                whereClause += " AND info.ADMINISTRATION_ID in :provinceCode";
                whereClauseAddress += " AND PROVINCE_CODE in :provinceCode";
            }
            if (fromDate != null) {
                whereClause += " AND doc.DATE_SIGN >= :fromDate";
            }
            if (toDate != null) {
                whereClause += " AND doc.DATE_SIGN <= :toDate";
            }


            String init = "" +
                    "WITH " +
                    " distinct_province as (\n" +
                    "SELECT " +
                    " CASE \n" +
                    "        WHEN PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(PROVINCE_NAME, 'Tỉnh ', '') \n" +
                    "        WHEN PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(PROVINCE_NAME, 'Thành Phố ', '')\n" +
                    "        WHEN PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(PROVINCE_NAME, 'Thành phố ', '')\n" +
                    "        WHEN PROVINCE_NAME LIKE 'Sở Tư Pháp %' THEN REPLACE(PROVINCE_NAME, 'Sở Tư Pháp ', '')\n" +
                    "        ELSE PROVINCE_NAME \n" +
                    "    END AS PROVINCE_NAME " +
                    " from DM_AREA where  1=1 \n" + whereClauseAddress + " \n" +
                    "group by PROVINCE_NAME \n" +
                    "), " +
                    " report_a8 as (\n" +
                    "SELECT\n" +
                    "    TRIM(dm.name) AS province,\n" +
                    "    onp.LEVER_PENALIZE as level_\n" +
                    "FROM\n" +
                    "    ORG_NOTARY_INFO info\n" +
                    "    LEFT JOIN dm_administration dm ON dm.id = info.administration_id\n" +
                    "    JOIN ORG_NOTARY_PENALIZE onp ON onp.ORG_NOTARY_ID = info.ID\n" +
                    "    JOIN DM_DOCUMENT doc ON onp.DOCUMENT_ID = doc.ID\n" +
                    "    JOIN ADM_PARAMETER ap ON onp.ADMINISTRATION_ID_PENALTY = ap.ID \n" +
                    "WHERE\n" +
                    "    info.STATUS = 0 \n" + whereClause +
                    "    AND doc.TYPE = 17 \n" +
                    "    AND info.ACTIVE = 0\n" +
                    "    AND doc.ACTIVE = 0\n" +
                    "    AND onp.ACTIVE = 0\n" +
                    "    AND info.ADMINISTRATION_ID IN ( SELECT stat.id FROM DM_ADMINISTRATION stat START WITH stat.ID = :idAdminisLogin CONNECT BY PRIOR stat.ID = stat.PARENT_ID )\n" +
                    "    ),\n" +
                    "    summary_report AS (\n" +
                    "    select \n" +
                    "     rp.province,\n" +
                    "     rp.level_,\n" +
                    "     count(1) as total\n" +
                    "     from report_a8 rp \n" +
                    "     GROUP BY  rp.province, rp.level_\n" +
                    ") ";

            String sqlQuery = "" +
                    "    select \n" +
                    "    dka.PROVINCE_NAME as \"col1\",\n" +
                    "    SUM(CASE WHEN level_ = 1 THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN level_ = 2 THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN level_ = 3 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "    from distinct_province dka left join summary_report sm on dka.PROVINCE_NAME = sm.province \n" +
                    "    GROUP BY dka.PROVINCE_NAME order by dka.PROVINCE_NAME ";

            String sqlTotal = "" +
                    " select \n" +
                    "    'Tổng số' as \"col1\",\n" +
                    "    SUM(CASE WHEN level_ = 1 THEN total ELSE 0 END) AS \"col_2\",\n" +
                    "    SUM(CASE WHEN level_ = 2 THEN total ELSE 0 END) AS \"col_3\",\n" +
                    "    SUM(CASE WHEN level_ = 3 THEN total ELSE 0 END) AS \"col_4\"\n" +
                    "    from distinct_province dka left join summary_report sm on dka.PROVINCE_NAME = sm.province order by dka.PROVINCE_NAME ";

            String sqlRowCount = "select count(*) from (" + sqlQuery + ")";
            sqlQuery = UtilData.paginationOracle(sqlQuery, offset, numberPerPage);
            Query query = entityManager.createNativeQuery(init + sqlQuery);
            Query queryTotal = entityManager.createNativeQuery(init + sqlTotal);
            Query queryRowCount = entityManager.createNativeQuery(init + sqlRowCount);

            query.setParameter("idAdminisLogin", userLogin.getAdministrationId());
            queryTotal.setParameter("idAdminisLogin", userLogin.getAdministrationId());
            queryRowCount.setParameter("idAdminisLogin", userLogin.getAdministrationId());

            if (H.isTrue(provinceCodes)) {
                query.setParameter("provinceCode", provinceCodes);
                queryTotal.setParameter("provinceCode", provinceCodes);
                queryRowCount.setParameter("provinceCode", provinceCodes);
            }
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryRowCount.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryRowCount.setParameter("toDate", toDate);
            }

            List<Object[]> dataList = query.getResultList();
            List<Object[]> totalList = queryTotal.getResultList();
            List<Reaport> items = new ArrayList<>();
            Reaport total = new Reaport();
            total.setCol_1("Tổng số");
//            AtomicInteger rowcount = new AtomicInteger();
            totalList.stream().forEach((record) -> {
//                rowcount.getAndIncrement();
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

            result.setItems(items);
            int count = ((Number) queryRowCount.getSingleResult()).intValue();
            result.setRowCount(count);
//            result.setRowCount(rowcount.get());

            return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", result));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportExcelReportOperationViolationOrganization(String fromDate, String toDate, String cityId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            PagingResult data = reportOperationViolationOrganization(fromDate, toDate, cityId, 1, 1000000).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> headers = new ArrayList<>();
            //title = Báo cáo xử lý vi phạm của Tổ chức Hành nghề Công chứng
            //header TT	Địa danh hành chính	Kỷ luật		Xử lý vi phạm hành chính		Truy cứu trách nhiệm hình sự
            //
            headers.add("TT");
            headers.add("Địa danh hành chính");
            headers.add("Kỷ luật");
            headers.add("Xử lý vi phạm hành chính");
            headers.add("Truy cứu trách nhiệm hình sự");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Reaport item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getCol_1());
                row.add(item.getCol_2());
                row.add(item.getCol_3());
                row.add(item.getCol_4());
                dataExport.add(row);
            }

            String fileName = "BaoCaoXuLyViPhamToChucCongChung.xlsx";
            String sheetName = "Sheet1";
            String title = "Báo cáo xử lý vi phạm của Tổ chức Hành nghề Công chứng";
            String subTitle = "";
            if (H.isTrue(fromDate) || H.isTrue(toDate)) {
                if (!H.isTrue(fromDate)) fromDate = "";
                if (!H.isTrue(toDate)) toDate = "";
                subTitle = "Từ ngày " + fromDate + " đến ngày " + toDate;
            }
            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> getTcccByMap(Integer type) {
        try {
            List<HashMap<String, Object>> data = new ArrayList<>();
            if (!H.isTrue(type)) type = 1;

            if (type.intValue() == 1) {
                PagingResult listAddressHasData = reportOrganizationNotary(null, null, null, 1, 1000000, null, null, null, false).getBody().getData();
                List<Reaport> items = listAddressHasData.getItems();
                for (Reaport item : items) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", item.getCol_1());
                    Long value = (H.isTrue(item.getCol_2()) ? Long.parseLong(item.getCol_2()) : 0L) + (H.isTrue(item.getCol_6()) ? Long.parseLong(item.getCol_6()) : 0L);
                    map.put("value", value);
                    data.add(map);
                }
            } else if (type.intValue() == 2) {
                List<Reaport> items = getDataCCVMap();
                for (Reaport item : items) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", item.getCol_1());
                    map.put("value", item.getCol_2());
                    data.add(map);
                }
            }

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Reaport> getDataCCVMap() {
        try {
            String sql = "" +
                    "WITH ranked_probationary AS (\n" +
                    "    SELECT\n" +
                    "        proinfo.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY notary_info_id ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        probationary_info proinfo \n" +
                    "    WHERE\n" +
                    "        proinfo.active = 0 \n" +
                    "    ),\n" +
                    "    ranked_notary_request AS (\n" +
                    "    SELECT\n" +
                    "        nre.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY notary_info_id, REQUEST_TYPE ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        NOTARY_REQUEST nre \n" +
                    "    WHERE\n" +
                    "        nre.active = 0 \n" +
                    "    ),\n" +
                    "    ranked_notary_appoint AS (\n" +
                    "    SELECT\n" +
                    "        app.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY notary_info_id, type_appoint ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        notary_appoint app \n" +
                    "    WHERE\n" +
                    "        app.active = 0 \n" +
                    "    ),\n" +
                    "    ranked_notary_reapppointed AS (\n" +
                    "    SELECT\n" +
                    "        reapp.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY type_reappoint, notary_info_id ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        notary_reapppointed reapp \n" +
                    "    WHERE\n" +
                    "        reapp.active = 0 \n" +
                    "    ),\n" +
                    "    ranked_notary_reg_practice AS (\n" +
                    "    SELECT\n" +
                    "        nrp.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY notary_info_id ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        notary_reg_practice nrp \n" +
                    "    WHERE\n" +
                    "        nrp.active = 0 \n" +
                    "    ),\n" +
                    "    ranked_NOTARY_REQUEST4 AS (\n" +
                    "    SELECT\n" +
                    "        nre4.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY REQUEST_TYPE, notary_info_id ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        NOTARY_REQUEST nre4 \n" +
                    "    WHERE\n" +
                    "        nre4.active = 0 \n" +
                    "        AND nre4.REQUEST_TYPE = 4 \n" +
                    "    ),\n" +
                    "    ranked_NOTARY_REQUEST2_3 AS (\n" +
                    "    SELECT\n" +
                    "        nre2_3.*,\n" +
                    "        ROW_NUMBER ( ) OVER ( PARTITION BY REQUEST_TYPE, notary_info_id ORDER BY id DESC ) AS rn \n" +
                    "    FROM\n" +
                    "        NOTARY_REQUEST nre2_3 \n" +
                    "    WHERE\n" +
                    "        nre2_3.active = 0 \n" +
                    "        AND nre2_3.REQUEST_TYPE IN ( 2, 3 ) \n" +
                    "    ) \n" +
                    "    -- Truy vấn chính\n" +
                    "    SELECT \n" +
                    "        NAME_ADMIN,\n" +
                    "        COUNT(1) AS total_count\n" +
                    "    FROM\n" +
                    "        (\n" +
                    "        SELECT DISTINCT\n" +
                    "            aa.*,\n" +
                    " CASE \n" +
                    "        WHEN dka.PROVINCE_NAME LIKE 'Tỉnh %' THEN REPLACE(dka.PROVINCE_NAME, 'Tỉnh ', '') \n" +
                    "        WHEN dka.PROVINCE_NAME LIKE 'Thành Phố %' THEN REPLACE(dka.PROVINCE_NAME, 'Thành Phố ', '')\n" +
                    "        WHEN dka.PROVINCE_NAME LIKE 'Thành phố %' THEN REPLACE(dka.PROVINCE_NAME, 'Thành phố ', '')\n" +
                    "        ELSE dka.PROVINCE_NAME \n" +
                    "    END AS NAME_ADMIN, " +
//                    "            dka.PROVINCE_NAME AS NAME_ADMIN,\n" +
                    "            ROW_NUMBER() OVER (PARTITION BY aa.id ORDER BY aa.NAME DESC) AS rownum_filter \n" +
                    "        FROM\n" +
                    "            (\n" +
                    "            SELECT\n" +
                    "                    info.id,\n" +
                    "                    info.status,\n" +
                    "                    info.name,\n" +
                    "                    '' AS birth_day,\n" +
                    "                    info.id_no,\n" +
                    "                    DECODE( redoc.dispatch_code, NULL, doc.dispatch_code, redoc.dispatch_code ) AS dispatch_code,\n" +
                    "                    DECODE( redoc.date_sign, NULL, doc.date_sign, redoc.date_sign ) AS date_sign,\n" +
                    "                    nrp.number_cad,\n" +
                    "                    oni.name AS orgname,\n" +
                    "                    info.CREATED_BY,\n" +
                    "                    info.UPDATED_BY,\n" +
                    "                    info.GEN_DATE,\n" +
                    "                    info.LAST_UPDATE,\n" +
                    "                    decode( nrp.CREATED_BY, NULL, decode( nre.CREATED_BY, NULL, proinfo.CREATED_BY, nre.CREATED_BY ), nrp.CREATED_BY ) AS userCreate,\n" +
                    "                    decode( nrp.ORG_NOTARY_INFO_ID, NULL, proinfo.ORG_NOTARY_INFO_ID, nrp.ORG_NOTARY_INFO_ID ) AS orgNotaryId,\n" +
                    "                    nrp.CREATED_BY AS nrpcre,\n" +
                    "                    proinfo.CREATED_BY AS procre,\n" +
                    "                    nre.CREATED_BY AS nrecre,\n" +
                    "                    app.CREATED_BY AS appcre,\n" +
                    "                    reapp.CREATED_BY AS reappcre,\n" +
                    "                    nre4.CREATED_BY AS nre4cre,\n" +
                    "                    nre2_3.CREATED_BY nre2_3cre,\n" +
                    "                    nre.REQUEST_TYPE AS request_type_cho_bn,-- chờ bổ nhiệm\n" +
                    "                    nre4.REQUEST_TYPE AS request_type_cho_bnl,-- chờ bổ nhiệm lại\n" +
                    "                    nre2_3.REQUEST_TYPE AS request_type_cho_mn,--chờ miễn nhiệm\n" +
                    "                    DECODE(\n" +
                    "                        oni.address_id,\n" +
                    "                        NULL,\n" +
                    "                        oni.address,\n" +
                    "                        oni.address || ' - ' || dm.commune_name || ' - ' || dm.district_name || ' - ' || dm.province_name \n" +
                    "                    ) AS ADDRESS \n" +
                    "                FROM\n" +
                    "          notary_info info\n" +
                    "          LEFT JOIN ranked_probationary proinfo ON info.id = proinfo.notary_info_id \n" +
                    "          AND proinfo.rn = 1\n" +
                    "          LEFT JOIN org_notary_info org ON proinfo.org_notary_info_id = org.id\n" +
                    "          LEFT JOIN ranked_notary_request nre ON info.id = nre.notary_info_id \n" +
                    "          AND nre.REQUEST_TYPE = 1 \n" +
                    "          AND nre.rn = 1\n" +
                    "          LEFT JOIN ranked_notary_appoint app ON info.id = app.notary_info_id \n" +
                    "          AND app.type_appoint = 1 \n" +
                    "          AND app.rn = 1\n" +
                    "          LEFT JOIN dm_document doc ON app.document_id = doc.id \n" +
                    "          AND doc.active = 0\n" +
                    "          LEFT JOIN ranked_notary_reapppointed reapp ON info.id = reapp.notary_info_id \n" +
                    "          AND reapp.type_reappoint = 1 \n" +
                    "          AND reapp.rn = 1\n" +
                    "          LEFT JOIN dm_document redoc ON reapp.document_id = redoc.id \n" +
                    "          AND redoc.active = 0\n" +
                    "          LEFT JOIN ranked_notary_reg_practice nrp ON info.id = nrp.notary_info_id \n" +
                    "          AND nrp.rn = 1\n" +
                    "          LEFT JOIN org_notary_info oni ON nrp.org_notary_info_id = oni.id \n" +
                    "          AND oni.active = 0\n" +
                    "          LEFT JOIN dm_area dm ON dm.id = oni.address_id\n" +
                    "          LEFT JOIN ranked_NOTARY_REQUEST4 nre4 ON info.id = nre4.notary_info_id \n" +
                    "          AND nre4.rn = 1\n" +
                    "          LEFT JOIN ranked_NOTARY_REQUEST2_3 nre2_3 ON info.id = nre2_3.notary_info_id \n" +
                    "          AND app.rn = 1 \n" +
                    "        WHERE\n" +
                    "          info.ACTIVE = 0 \n" +
                    "        ) aa\n" +
                    "        INNER JOIN adm_users ureq ON 1 = 1 \n" +
                    "        AND (\n" +
                    "          ureq.user_name = aa.nrpcre \n" +
                    "          OR ureq.user_name = aa.procre \n" +
                    "          OR ureq.user_name = aa.nrecre \n" +
                    "          OR ureq.user_name = aa.appcre \n" +
                    "          OR ureq.user_name = aa.reappcre \n" +
                    "          OR ureq.user_name = aa.nre4cre \n" +
                    "          OR ureq.user_name = aa.nre2_3cre \n" +
                    "          OR ureq.user_name = aa.CREATED_BY \n" +
                    "          OR ureq.user_name = aa.UPDATED_BY \n" +
                    "        ) --ureq.user_name = aa.userCreate\n" +
                    "        INNER JOIN DM_ADMINISTRATION adm ON adm.id = ureq.ADMINISTRATION_ID \n" +
                    "            AND ureq.administration_id IN (SELECT stat.id FROM dm_administration stat START WITH stat.id = 0 CONNECT BY PRIOR stat.id = stat.parent_id) \n " +
                    "            INNER JOIN dm_area dka ON dka.id = adm.address_id  " +
                    "        WHERE\n" +
                    "            1 = 1 \n" +
                    "            AND aa.status = 8 \n" +
//                    "        ORDER BY\n" +
//                    "            dka.P\n" +
                    "        ) pgn\n" +
                    "    WHERE\n" +
                    "        1 = 1 \n" +
                    "    GROUP BY \n" +
                    "        name_admin\n" +
                    "    ORDER BY \n" +
                    "        name_admin\n ";
            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> dataList = query.getResultList();
            List<Reaport> items = new ArrayList<>();
            for (Object[] record : dataList) {
                Reaport reaport = new Reaport();
                int i = 0;
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString());
                i++;
                reaport.setCol_2(record[i] == null ? "0" : record[i].toString());
                items.add(reaport);
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> tcccDashboardQuery(AccUser userLogin, Integer type, Long cityCode, String fromDateRaw, String toDateRaw) {
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

            //set up 4 date current year, last year


            data.put("dataMap", getTcccByMap(type).getBody().getData());
            data.put("HSCC_da_thuc_hien", loadHSCCDaThucHien(userLogin, type, cityCode, fromDateRaw, toDateRaw));
            data.put("VPCC_duoc_phep_tl", loadVPCCDuocPhepThanhLap(userLogin, type, cityCode, fromDateRaw, toDateRaw));
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP || H.isTrue(cityCode)) {
                data.put("dataChartCity", getDataChartCity(cityCode, type, fromDateRaw, toDateRaw, userLogin));
            }
//            data = reportA1(data, cityCode, fromDate, toDate);
            PagingResult pageSearch = new PagingResult();
            pageSearch.setPageNumber(1);
            pageSearch.setNumberPerPage(10000000);
            PagingResult listNotaryOrg = list_search(null, cityCode, "0", pageSearch, fromDateRaw, toDateRaw);
            data.put("tk_tccc_ds_tccc", listNotaryOrg.getItems().size());
            PagingResult listCCV = list_ccv(null, cityCode, "8", pageSearch, fromDateRaw, toDateRaw);
            data.put("tk_tccc_ds_cgv", listCCV.getItems().size());

            data.put("tk_pie_th_bo_nhiem_ccv", loadDataPieChartForAppointCCV(cityCode, fromDateRaw, toDateRaw));
            data.put("tk_pie_loai_cong_chung", loadDataPieChartForLoaiCongChung(cityCode, fromDateRaw, toDateRaw));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", data), HttpStatus.OK);
    }

    private Object loadVPCCDuocPhepThanhLap(AccUser userLogin, Integer type, Long cityCode, String fromDateRaw, String toDateRaw) {
        try {
            String cityCodeStr = cityCode == null ? null : cityCode.toString();
            List<Reaport> dataList = reportOperationOrganizationNotary(fromDateRaw, toDateRaw, cityCodeStr, null, 1, 1000000, "5", false).getBody().getData().getItems();
            if (!H.isTrue(dataList)) return null;
            Reaport reaportTotal = dataList.get(0);
            return reaportTotal.getCol_5();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object loadHSCCDaThucHien(AccUser userLogin, Integer type, Long cityCode, String fromDateRaw, String toDateRaw) {
        PagingResult data = notaryActivityService.aggregateByStp(fromDateRaw, toDateRaw, String.valueOf(cityCode), 1, 10000000, null, null, null, false).getBody().getData();
        List<Reaport> dataList = data.getItems();
        if (!H.isTrue(dataList)) return null;
        Reaport rowTotal = dataList.get(0);
        return rowTotal.getCol_7();
    }

    @Override
    public Object getDataChartCity(Long cityCode, Integer type, String fromDateRaw, String toDateRaw, AccUser userLogin) {
        if (!H.isTrue(type)) type = 1;
        if (type.intValue() == 1) {
            //TCHNCC
            PagingResult dataCityDetail = reportOrganizationNotary(null, null, cityCode.toString(), 1, 1000000, null, null, 15, true).getBody().getData();
            List<Reaport> items = dataCityDetail.getItems();
            if (items.size() == 0) return null;
            //remove total row
            items.remove(0);
            List<HashMap<String, Object>> dataChartCity = new ArrayList<>();
            for (Reaport item : items) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", item.getCol_1());
                map.put("value", Long.valueOf(item.getCol_2()) + Long.valueOf(item.getCol_6()));
                dataChartCity.add(map);
            }
            return dataChartCity;
        } else if (type.intValue() == 2) {
            //CCV
        }

        return null;
    }

    public ResponseEntity<ApiResponseV1<?>> tcccDashboard(Long cityCode, String fromDateRaw, String toDateRaw, Integer type) {
        try {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //dùng redis cache kết quả
            String cityCodeStr = cityCode == null ? null : cityCode.toString();
            if (userLogin.getType().intValue() == Constants.TYPE_USER.SO_TU_PHAP) {
                cityCode = userLogin.getAdministrationId();
            }
            HashMap<String, Object> dataAll = new HashMap<>();
            String key = "tcccDashboardDataAll" + cityCodeStr + fromDateRaw + toDateRaw + userLogin.getId() + type;
            if ( redisEnable && redisTemplate.hasKey(key)) {
                dataAll = (HashMap<String, Object>) redisTemplate.opsForValue().get(key);
            } else {

                dataAll = (HashMap<String, Object>) tcccDashboardQuery(userLogin, type, cityCode, fromDateRaw, toDateRaw).getBody().getData();
                if ( redisEnable ) redisTemplate.opsForValue().set(key, dataAll, 10, TimeUnit.MINUTES);
            }

            //last year
            HashMap<String, Object> dataAllLastYear = new HashMap<>();
            String keyLastYear = "tcccDashboardDataAll" + cityCodeStr + fromDateRaw + toDateRaw + "LastYear" + userLogin.getId() + type;
            if ( redisEnable && redisTemplate.hasKey(keyLastYear)) {
                dataAllLastYear = (HashMap<String, Object>) redisTemplate.opsForValue().get(keyLastYear);
            } else {

                dataAllLastYear = (HashMap<String, Object>) tcccDashboardQuery(userLogin, type, cityCode, "01/01/" + (Calendar.getInstance().get(Calendar.YEAR) - 1), "31/12/" + (Calendar.getInstance().get(Calendar.YEAR) - 1)).getBody().getData();
                if ( redisEnable ) redisTemplate.opsForValue().set(keyLastYear, dataAllLastYear, 10, TimeUnit.MINUTES);
            }

            //current year
            HashMap<String, Object> dataAllCurrentYear = new HashMap<>();
            String keyCurrentYear = "tcccDashboardDataAll" + cityCodeStr + fromDateRaw + toDateRaw + "CurrentYear" + userLogin.getId() + type;
            if ( redisEnable && redisTemplate.hasKey(keyCurrentYear)) {
                dataAllCurrentYear = (HashMap<String, Object>) redisTemplate.opsForValue().get(keyCurrentYear);
            } else {
                dataAllCurrentYear = (HashMap<String, Object>) tcccDashboardQuery(userLogin, type, cityCode, "01/01/" + Calendar.getInstance().get(Calendar.YEAR), "31/12/" + Calendar.getInstance().get(Calendar.YEAR)).getBody().getData();
                if ( redisEnable ) redisTemplate.opsForValue().set(keyCurrentYear, dataAllCurrentYear, 10, TimeUnit.MINUTES);
            }

            //tính % tăng trưởng các giá trị. Lấy 2 số thập phân
            // tk_tccc_ds_tccc, tk_tccc_ds_cgv, HSCC_da_thuc_hien, VPCC_duoc_phep_tl
            Long tk_tccc_ds_tccc_current_year = Long.parseLong(dataAllCurrentYear.get("tk_tccc_ds_tccc").toString());
            Long tk_tccc_ds_tccc_last_year = Long.parseLong(dataAllLastYear.get("tk_tccc_ds_tccc").toString());
            Long tk_tccc_ds_cgv_current_year = Long.parseLong(dataAllCurrentYear.get("tk_tccc_ds_cgv").toString());
            Long tk_tccc_ds_cgv_last_year = Long.parseLong(dataAllLastYear.get("tk_tccc_ds_cgv").toString());
            Long HSCC_da_thuc_hien_current_year = Long.parseLong(dataAllCurrentYear.get("HSCC_da_thuc_hien").toString());
            Long HSCC_da_thuc_hien_last_year = Long.parseLong(dataAllLastYear.get("HSCC_da_thuc_hien").toString());
            Long VPCC_duoc_phep_tl_current_year = Long.parseLong(dataAllCurrentYear.get("VPCC_duoc_phep_tl").toString());
            Long VPCC_duoc_phep_tl_last_year = Long.parseLong(dataAllLastYear.get("VPCC_duoc_phep_tl").toString());
            String tk_tccc_ds_tccc_growth = String.valueOf(Math.round(((tk_tccc_ds_tccc_current_year - tk_tccc_ds_tccc_last_year) * 100.0 / tk_tccc_ds_tccc_last_year) * 100.0) / 100.0);
            String tk_tccc_ds_cgv_growth = String.valueOf(Math.round(((tk_tccc_ds_cgv_current_year - tk_tccc_ds_cgv_last_year) * 100.0 / tk_tccc_ds_cgv_last_year) * 100.0) / 100.0);
            String HSCC_da_thuc_hien_growth = String.valueOf(Math.round(((HSCC_da_thuc_hien_current_year - HSCC_da_thuc_hien_last_year) * 100.0 / HSCC_da_thuc_hien_last_year) * 100.0) / 100.0);
            String VPCC_duoc_phep_tl_growth = String.valueOf(Math.round(((VPCC_duoc_phep_tl_current_year - VPCC_duoc_phep_tl_last_year) * 100.0 / VPCC_duoc_phep_tl_last_year) * 100.0) / 100.0);

            //nếu năm trước = 0, năm sau có giá trị thì tăng trưởng = 100%
            // nếu năm trước có giá trị, năm sau = 0 thì tăng trưởng = -100%
            if (tk_tccc_ds_tccc_last_year == 0) {
                if (tk_tccc_ds_tccc_current_year > 0) {
                    tk_tccc_ds_tccc_growth = "100";
                } else {
                    tk_tccc_ds_tccc_growth = "0";
                }
            }
            if (tk_tccc_ds_cgv_last_year == 0) {
                if (tk_tccc_ds_cgv_current_year > 0) {
                    tk_tccc_ds_cgv_growth = "100";
                } else {
                    tk_tccc_ds_cgv_growth = "0";
                }
            }
            if (HSCC_da_thuc_hien_last_year == 0) {
                if (HSCC_da_thuc_hien_current_year > 0) {
                    HSCC_da_thuc_hien_growth = "100";
                } else {
                    HSCC_da_thuc_hien_growth = "0";
                }
            }
            if (VPCC_duoc_phep_tl_last_year == 0) {
                if (VPCC_duoc_phep_tl_current_year > 0) {
                    VPCC_duoc_phep_tl_growth = "100";
                } else {
                    VPCC_duoc_phep_tl_growth = "0";
                }
            }


            dataAll.put("tk_tccc_ds_tccc_growth", tk_tccc_ds_tccc_growth);
            dataAll.put("tk_tccc_ds_cgv_growth", tk_tccc_ds_cgv_growth);
            dataAll.put("HSCC_da_thuc_hien_growth", HSCC_da_thuc_hien_growth);
            dataAll.put("VPCC_duoc_phep_tl_growth", VPCC_duoc_phep_tl_growth);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", dataAll), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<FileOrgNotary>> detailOrganizationNotary(Long idOrgNotaryInfo) {
        FileOrgNotary file = new FileOrgNotary();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        // Lấy loại tổ chức
        Long typeOrg = checkOrgType(idOrgNotaryInfo);
        int number = 100;
        int numberHistory = 100;
        // Chung VPCC + PCC
        // Lấy ra thông tin vpcc + thông tin trưởng phòng
        OrgNotaryInfoView orgNotaryInfoView = new OrgNotaryInfoView();
        String hql = "select \n" +
                "    oni.STATUS, oni.TEL, oni.NAME as orgNotaryName, oni.FAX, oni.ADMINISTRATION_ID, oni.EMAIL as oniEmail, \n" +
                "    decode(oni.ADDRESS_ID,null,oni.ADDRESS,oni.ADDRESS||' - '||da3.COMMUNE_NAME||' - '||da3.DISTRICT_NAME||' - '||da3.PROVINCE_NAME) as ADDRESS, \n" +
                "    oni.WEBSITE,       \n" +
                "    ni.NAME as notaryName, ni.PHONE_NUMBER, ni.SEX, ni.EMAIL as niEmail, ni.BIRTH_DAY, ni.ID_NO, ni.ID_NO_DATE,   \n" +
                "    apa.VALUE as address_id_no,--ni.ADDRESS_ID_NO,   \n" +
                "    decode(ni.ADDRESS_RESIDENT_ID,null,ni.ADDRESS_RESIDENT,ni.ADDRESS_RESIDENT||' - '||da.COMMUNE_NAME||' - '||da.DISTRICT_NAME||' - '||da.PROVINCE_NAME) as ADDRESS_RESIDENT,\n" +
                "    decode(ni.ADDRESS_NOW_ID,null,ni.ADDRESS_NOW,ni.ADDRESS_NOW||' - '||da2.COMMUNE_NAME||' - '||da2.DISTRICT_NAME||' - '||da2.PROVINCE_NAME) as ADDRESS_NOW,\n" +
                "    nrp.NUMBER_CAD,oni.type,        \n" +
                "    decode(oni.administration_id,null,'',(SELECT dm.full_name from dm_administration dm WHERE dm.id = oni.administration_id )) as nameAdministration," +
                "    doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.DISPATCH_CODE         \n" +
                "FROM ORG_NOTARY_INFO oni        \n" +
                "    LEFT JOIN NOTARY_INFO ni ON oni.NOTARY_ID_OFFICE_CHIEF=ni.ID        \n" +
                "    LEFT JOIN NOTARY_REG_PRACTICE nrp ON nrp.NOTARY_INFO_ID=ni.ID and nrp.STATUS =:nrpStatus and nrp.ACTIVE=:active  \n" +
                "    LEFT JOIN notary_appoint apo on ni.id = apo.notary_info_id and apo.active=:active   \n" +
                "    LEFT JOIN dm_document doc on apo.document_id = doc.id and doc.active=:active    \n" +
                "    LEFT JOIN  DM_AREA da on da.ID=ni.ADDRESS_RESIDENT_ID \n" +
                "    LEFT JOIN  DM_AREA da2 on da2.ID=ni.address_now_id \n" +
                "    LEFT JOIN  DM_AREA da3 on da3.ID=oni.ADDRESS_id \n" +
                "    LEFT JOIN  ADM_PARAMETER apa on apa.ID=ni.address_id_no         \n" +
                "    \n" +
                "WHERE oni.ACTIVE=:active \n" +
                "    --and ni.ACTIVE=:active        \n" +
                "    and oni.id=:oniId   ";

        Query query = entityManager.createNativeQuery(hql)
                .setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)
                .setParameter("oniId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        db.stream().forEach((record) -> {
            orgNotaryInfoView.setStatusOrg(record[0] == null ? null : Long.parseLong(record[0].toString()));
            orgNotaryInfoView.setTel(record[1] == null ? null : ((String) record[1]));
            orgNotaryInfoView.setName(record[2] == null ? null : ((String) record[2]));
            orgNotaryInfoView.setFax(record[3] == null ? null : ((String) record[3]));
            orgNotaryInfoView.setAdministrationId(record[4] == null ? null : Long.parseLong(record[4].toString()));
            orgNotaryInfoView.setEmail(record[5] == null ? null : ((String) record[5]));
            orgNotaryInfoView.setAddress(record[6] == null ? null : ((String) record[6]));
            orgNotaryInfoView.setWebsite(record[7] == null ? null : ((String) record[7]));
            orgNotaryInfoView.setOfficeChiefName(record[8] == null ? null : ((String) record[8]));
            orgNotaryInfoView.setPhoneNumber(record[9] == null ? null : ((String) record[9]));
            orgNotaryInfoView.setSex(record[10] == null ? null : Long.parseLong(record[10].toString()));
            orgNotaryInfoView.setEmailNotary(record[11] == null ? null : ((String) record[11]));
            orgNotaryInfoView.setBirthDay(record[12] == null ? null : ((Date) record[12]));
            orgNotaryInfoView.setIdNo(record[13] == null ? null : ((String) record[13]));
            orgNotaryInfoView.setIdNoDate(record[14] == null ? null : ((Date) record[14]));
            orgNotaryInfoView.setAddressIdNo(record[15] == null ? null : ((String) record[15]));
            orgNotaryInfoView.setAddressResident(record[16] == null ? null : ((String) record[16]));
            orgNotaryInfoView.setAddressNow(record[17] == null ? null : ((String) record[17]));
            orgNotaryInfoView.setNumberCad(record[18] == null ? null : ((String) record[18]));
            orgNotaryInfoView.setType(record[19] == null ? null : Long.parseLong(record[19].toString()));
            orgNotaryInfoView.setAdminName(record[20] == null ? null : ((String) record[20]));
            orgNotaryInfoView.setDateSign(record[21] == null ? null : ((Date) record[21]));
            orgNotaryInfoView.setEffectiveDate(record[22] == null ? null : ((Date) record[22]));
            orgNotaryInfoView.setDispatchCode(record[23] == null ? null : ((String) record[23]));
        });
        // Lấy ra 1 list các thông tin xử phạt vpcc
        PagingResult pagePenalyze = getPagePenalize(idOrgNotaryInfo, 0, number);
        // Lấy ra 1 list các ccv đang hành nghề tại vpcc này
        PagingResult pageNotary = getPageNotary(idOrgNotaryInfo, 0, number);
        // Lấy ra 1 list các thông tin chuyển nhượng
        PagingResult pageTransferOffice = getTransferOffice(idOrgNotaryInfo, 0, number);
        // Lấy ra lịch sử tổ chức
        PagingResult pageHistory = getHistoryOrg(idOrgNotaryInfo, 0, numberHistory);
        file.setOrgNotaryInfoView(orgNotaryInfoView);
        file.setPageNotary(pageNotary);
        file.setPagePenalyze(pagePenalyze);
        file.setPageHistory(pageHistory);
        file.setPageTransferOffice(pageTransferOffice);
        // PCC
        if (typeOrg.equals(ConstantsTccc.TYPE_ORG_NOTARY.PHONG_CONG_CHUNG)) {
            // Lấy ra thông tin thành lập PCC
            OrgNotaryInfoView detailEstablishPCC = getDetailEstablishPCC(idOrgNotaryInfo);
            file.setDetailEstablishPCC(detailEstablishPCC);
            // Lấy ra thông tin chuyển đổi PCC
            OrgNotaryInfoView detailConversion = getDetailConversionPCC(idOrgNotaryInfo);
            file.setDetailConversion(detailConversion);
            // Lấy ra thông tin giải thể PCC
            OrgNotaryInfoView detailDissolution = getDetailDissolutionPCC(idOrgNotaryInfo);
            file.setDetailDissolution(detailDissolution);
        }
        // VPCC
        if (typeOrg.equals(ConstantsTccc.TYPE_ORG_NOTARY.VAN_PHONG_CONG_CHUNG)) {
            // Lấy ra 1 list các thông tin thay đổi nội dung hoạt động
            PagingResult pageCCARegistration = getPageChangeContentActiveRegistration(idOrgNotaryInfo, 0, number);
            file.setPageCCARegistration(pageCCARegistration);
            // Lấy ra 1 list các thông tin thành lập vpcc
            List<OrgNotaryInfoView> listEstablish = getPageEstablishVPCC(idOrgNotaryInfo);
            file.setListEstablish(listEstablish);
            // Lấy ra 1 list các thông tin đkhđ vpcc
            List<OrgNotaryInfoView> listRegis = getPageRegisVPCC(idOrgNotaryInfo);
            file.setListRegis(listRegis);
            // Lấy ra chi tiết thông tin hợp nhất vpcc
            OrgNotaryInfoView detailGroup = getDetailGroupVPCC(idOrgNotaryInfo);
            file.setDetailGroup(detailGroup);

            // Lấy ra chi tiết thông tin vpcc nhận hợp nhất
            OrgNotaryInfoView detailVPGroup = getGroupVPCC(idOrgNotaryInfo);
            file.setDetailVPGroup(detailVPGroup);
            // Lấy ra chi tiết thông tin sát nhập vpcc
            OrgNotaryInfoView detailMerger = getDetailMergerVPCC(idOrgNotaryInfo);
            file.setDetailMerger(detailMerger);

            // Lấy ra chi tiết thông tin sát nhập vpcc
            OrgNotaryInfoView detailVPMerger = getMergerVPCC(idOrgNotaryInfo);
            file.setDetailVPMerger(detailVPMerger);
            // Lấy ra chi tiết thông tin chấm dứt hoạt động vpcc
            OrgNotaryInfoView detailTermination = getDetailTerminationVPCC(idOrgNotaryInfo);
            file.setDetailTermination(detailTermination);

        }

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", file), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseV1<NotaryInfoView>> detailNotary(Long idNotary) {
        List<Object[]> db = new ArrayList<>();
        NotaryInfoView view = new NotaryInfoView();
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "SELECT\n" +
                "    info.name,\n" +
                "    info.birth_day,\n" +
                "    info.sex,\n" +
                "    info.id_no,\n" +
                "    info.id_no_date,\n" +
                "    apa.value as address_id_no,\n" +
                "    info.phone_number,\n" +
                "    info.email,\n" +
                "    info.address_resident,\n" +
                "    info.address_now,\n" +
                "    org.name AS orgname,\n" +
                "    org.address,\n" +
                "    info.id,\n" +
                "    info.status,\n" +
                "    info.address_resident_id,\n" +
                "    info.address_now_id,    \n" +
                "    decode(info.ADDRESS_RESIDENT_ID,null,info.ADDRESS_RESIDENT,info.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                "    decode(info.address_now_id,null,info.address_now,info.address_now||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2 FROM\n" +
                "    notary_info         info\n" +
                "    LEFT JOIN probationary_info   pro ON pro.notary_info_id = info.id     \n" +
                "    LEFT JOIN org_notary_info     org ON pro.org_notary_info_id = org.id      \n" +
                "    LEFT JOIN  DM_AREA dm on dm.ID=info.ADDRESS_RESIDENT_ID         \n" +
                "    LEFT JOIN  DM_AREA dm2 on dm2.ID=info.address_now_id      \n" +
                "    LEFT JOIN adm_parameter apa on apa.ID= info.address_id_no\n" +
                " WHERE\n" +
                "    info.id = :idnotary ";

        db = entityManager.createNativeQuery(sql).setParameter("idnotary", idNotary).getResultList();
        db.stream().forEach((record) -> {
            view.setNameNotaryInfo(record[0] == null ? null : ((String) record[0]));
            view.setBirthDay(record[1] == null ? null : ((Date) record[1]));
            view.setSex(record[2] == null ? null : Long.parseLong(record[2].toString()));
            view.setIdNo(record[3] == null ? null : ((String) record[3]));
            view.setIdNoDate(record[4] == null ? null : (Date) record[4]);
            view.setAddressIdNo(record[5] == null ? null : ((String) record[5]));
            view.setPhoneNumberNotaryInfo(record[6] == null ? null : (String) record[6]);
            view.setEmailNotaryInfo(record[7] == null ? null : (String) record[7]);
            view.setAddressResident(record[8] == null ? null : (String) record[8]);
            view.setAddressNow(record[9] == null ? null : (String) record[9]);
            view.setNameOrgNotaryInfo(record[10] == null ? null : ((String) record[10]));
            view.setAddress(record[11] == null ? null : (String) record[11]);
            view.setIdNotaryInfo(record[12] == null ? null : Long.parseLong(record[12].toString()));
            view.setStatusNotaryInfo(record[13] == null ? null : Long.parseLong(record[13].toString()));
            view.setAddressResidentId(record[14] == null ? null : Long.parseLong(record[14].toString()));
            view.setAddressNowId(record[15] == null ? null : Long.parseLong(record[15].toString()));
            view.setAddressResident(record[16] == null ? null : (String) record[16]);
            view.setAddressNow(record[17] == null ? null : (String) record[17]);
        });
        view.setPageAppoint(searchDetail(idNotary, 1, false, userLogin));
        view.setPageRegAndAuctionCard(searchDetail(idNotary, 5, false, userLogin));
        view.setPageProbationary(probationaryInfoDAO.searchDetail(idNotary, ConstantsTccc.STATUS_PROBATIONARYINFO.DANG_TAP_SU, 1, false, userLogin));


        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", view), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<TimeLineView>>> getTimeLineCCV(Long idNotary) {
        try {
            String sql = "SELECT * FROM (\n" +
                    "---------------------------Đăng ký tập sự HNCC--------------------------------------\n" +
                    "\n" +
                    "SELECT\n" +
                    "    'Đăng ký tập sự HNCC' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự: '||org.name as org_name,\n" +
                    "    'Thời gian bắt đầu tập sự: '||TO_CHAR(detail.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    null as date_end,\n" +
                    "    detail.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    TO_DATE('01/01/1000', 'dd/mm/yyyy') as gen_order \n" +
                    "FROM\n" +
                    "    probationary_info_detail   detail,\n" +
                    "    probationary_info          pro,\n" +
                    "    org_notary_info            org,\n" +
                    "    dm_document                doc,\n" +
                    "    dm_area                    dm,\n" +
                    "    notary_info info\n" +
                    "WHERE\n" +
                    "    detail.probationary_info_id = pro.id\n" +
                    "    AND detail.org_notary_info_id = org.id\n" +
                    "    AND pro.document_certificate_id = doc.id\n" +
                    "    and info.ID=pro.NOTARY_INFO_ID\n" +
                    "    AND ( dm.id = org.address_id OR org.address_id IS NULL )\n" +
                    "    AND detail.active = 0\n" +
                    "    AND org.active = 0\n" +
                    "    AND doc.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND detail.status = 1\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    AND detail.org_notary_info_to IS NULL\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Đăng ký tập sự HNCC' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Thời gian bắt đầu tập sự: '||TO_CHAR(pro.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    null as date_end,\n" +
                    "    pro.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    TO_DATE('01/01/1000', 'dd/mm/yyyy') as gen_order \n" +
                    "FROM\n" +
                    "    probationary_info   pro,\n" +
                    "    org_notary_info     org,\n" +
                    "    dm_document         doc,\n" +
                    "    dm_area             dm,\n" +
                    "    notary_info info\n" +
                    "WHERE\n" +
                    "    pro.org_notary_info_id = org.id\n" +
                    "    AND pro.document_certificate_id = doc.id\n" +
                    "    and info.ID=pro.NOTARY_INFO_ID\n" +
                    "    AND ( dm.id = org.address_id OR org.address_id IS NULL )\n" +
                    "    AND org.active = 0\n" +
                    "    AND doc.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND pro.status = 1\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    AND pro.org_notary_info_to IS NULL\n" +
                    "    \n" +
                    "---------------------------thay đổi CCV hướng dẫn --------------------------------------\n" +
                    "UNION\n" +
                    "select     \n" +
                    "    'Thay đổi Công chứng viên hướng dẫn' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Ngày thay đổi: '||TO_CHAR(detail.gen_date, 'DD/MM/YYYY') as gen_date,\n" +
                    "    null as date_end,\n" +
                    "    detail.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    detail.gen_date as gen_date\n" +
                    "from notary_info info \n" +
                    "    LEFT JOIN probationary_info pro on pro.notary_info_id=info.ID\n" +
                    "    LEFT JOIN probationary_info_detail detail on detail.PROBATIONARY_INFO_ID=pro.ID\n" +
                    "    LEFT JOIN notary_info tota on detail.NOTARY_TUTORIAL_ID=tota.ID\n" +
                    "    LEFT JOIN org_notary_info org on org.ID=pro.ORG_NOTARY_INFO_ID \n" +
                    "    \n" +
                    "WHERE info.active=0 \n" +
                    "    and pro.active=0 \n" +
                    "    and detail.active=0 \n" +
                    "    and detail.STATUS=26 \n" +
                    "    and info.ID=:idnotary \n" +
                    "\n" +
                    "---------------------------thay đổi nơi tập sự HNCC--------------------------------------\n" +
                    "UNION\n" +
                    "    \n" +
                    "SELECT\n" +
                    "    'Thay đổi nơi tập sự HNCC' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Thời gian bắt đầu tập sự: '||TO_CHAR(detail.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    null as date_end,\n" +
                    "    detail.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    detail.date_start as gen_order\n" +
                    "FROM\n" +
                    "    probationary_info_detail   detail,\n" +
                    "    probationary_info          pro,\n" +
                    "    org_notary_info            org\n" +
                    "WHERE\n" +
                    "    detail.probationary_info_id = pro.id\n" +
                    "    AND detail.org_notary_info_to = org.id\n" +
                    "    AND org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND detail.active = 0\n" +
                    "    AND detail.status IN ( 1, 15 )\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    AND detail.org_notary_info_to IS NOT NULL\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Thay đổi nơi tập sự HNCC' as ten_chinh,\n" +
                    "    'Sở tư pháp: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Thời gian bắt đầu tập sự: '||TO_CHAR(pro.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    null as date_end,\n" +
                    "    pro.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    pro.date_start as gen_order\n" +
                    "FROM\n" +
                    "    probationary_info   pro,\n" +
                    "    org_notary_info     org\n" +
                    "WHERE\n" +
                    "    pro.org_notary_info_to = org.id\n" +
                    "    AND org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND pro.status IN (1,15)\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    AND pro.org_notary_info_to IS NOT NULL\n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------tạm ngừng tập sự HNCC--------------------------------------\n" +
                    "UNION\n" +
                    "\n" +
                    "SELECT\n" +
                    "    'Tạm ngừng tập sự HNCC' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Thời gian tạm ngừng từ ngày: '||TO_CHAR(detail.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    'Thời gian tạm ngừng đến ngày: '||TO_CHAR(detail.date_end, 'DD/MM/YYYY') as date_end,\n" +
                    "    detail.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    detail.GEN_DATE as gen_order\n" +
                    "FROM\n" +
                    "    probationary_info_detail   detail,\n" +
                    "    probationary_info          pro,\n" +
                    "    org_notary_info            org\n" +
                    "WHERE\n" +
                    "    detail.probationary_info_id = pro.id\n" +
                    "    AND pro.org_notary_info_id = org.id\n" +
                    "    AND org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND detail.active = 0\n" +
                    "    AND detail.status = 2\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Tạm ngừng tập sự HNCC' as ten_chinh,\n" +
                    "    'Sở tư pháp: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    'Thời gian tạm ngừng từ ngày: '||TO_CHAR(pro.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    'Thời gian tạm ngừng đến ngày: '||TO_CHAR(pro.date_end, 'DD/MM/YYYY') as date_end,\n" +
                    "    pro.note as note,\n" +
                    "    null as DISPATCH_CODE,\n" +
                    "    null as DATE_SIGN,\n" +
                    "    null as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    pro.GEN_DATE as gen_order\n" +
                    "FROM\n" +
                    "    probationary_info   pro,\n" +
                    "    org_notary_info     org\n" +
                    "WHERE\n" +
                    "    pro.org_notary_info_id = org.id\n" +
                    "    AND org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND pro.status = 2\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "---------------------------chấm dứt - hoàn thành--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    decode(pro.status,   3,'Chấm dứt tập sự',   'Hoàn thành tập sự') as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    null as date_start,         \n" +
                    "    decode(pro.date_end , null,'', decode(pro.status,   3,'Chấm dứt tập sự từ ngày: '||TO_CHAR(pro.date_end, 'DD/MM/YYYY'),   'Hoàn thành tập sự từ ngày: '||TO_CHAR(pro.date_end, 'DD/MM/YYYY'))) as date_end,\n" +
                    "    pro.note as note,\n" +
                    "    decode(pro.status, 3,'Số quyết định Chấm dứt tập sự: '||doc.DISPATCH_CODE, '') as DISPATCH_CODE,\n" +
                    "    decode(pro.status, 3,decode(doc.DATE_SIGN ,null,'', 'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY')),'') as DATE_SIGN,\n" +
                    "    decode(pro.status, 3,decode(doc.EFFECTIVE_DATE ,null,'', 'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY')),'') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    pro.DATE_END as gen_order\n" +
                    "FROM\n" +
                    "    probationary_info   pro\n" +
                    "    INNER JOIN org_notary_info     org on pro.org_notary_info_id = org.id\n" +
                    "    LEFT JOIN dm_document doc          on pro.DOCUMENT_ID=doc.ID\n" +
                    "WHERE\n" +
                    "    org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND pro.status in (3,4)\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------đạt kết quả ts--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Đạt kết quả tập sự' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS administration,\n" +
                    "    --'Tổ chức HNCC nhận tập sự:'||org.name as org_name,\n" +
                    "    null as org_name,\n" +
                    "    --'Đạt kết quả tập sự từ ngày: '||TO_CHAR(pro.date_start, 'DD/MM/YYYY') as date_start,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    pro.note as note,\n" +
                    "    'Số giấy chứng nhận đạt kết quả: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày cấp: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    decode(doc.EFFECTIVE_DATE, null,'', 'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY')) as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    pro.DATE_END + interval '10' second as gen_order\n" +
                    "\n" +
                    "FROM\n" +
                    "    probationary_info   pro,\n" +
                    "    org_notary_info     org,\n" +
                    "    dm_document         doc\n" +
                    "WHERE\n" +
                    "    pro.org_notary_info_id = org.id\n" +
                    "    AND pro.document_id = doc.id\n" +
                    "    AND org.active = 0\n" +
                    "    AND pro.active = 0\n" +
                    "    AND doc.active = 0\n" +
                    "    AND pro.status = 4\n" +
                    "    AND pro.notary_info_id = :idnotary\n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------Bổ nhiệm--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT  \n" +
                    "    'Bổ nhiệm CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định bổ nhiệm: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "FROM        \n" +
                    "    notary_appoint   apo,        \n" +
                    "    dm_document      doc        \n" +
                    "WHERE        \n" +
                    "    apo.document_id = doc.id        \n" +
                    "    AND apo.type_appoint = 1        \n" +
                    "    AND apo.notary_info_id = :idnotary        \n" +
                    "    AND apo.active = 0        \n" +
                    "    AND doc.active = 0\n" +
                    "union\n" +
                    "SELECT \n" +
                    "    'Đề nghị bổ nhiệm' as ten_chinh, \n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=nrdoc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định đề nghị bổ nhiệm: '||nrdoc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(nrdoc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(nrdoc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    nrdoc.DATE_SIGN as gen_order\n" +
                    "FROM \n" +
                    "    notary_info info\n" +
                    "    INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n" +
                    "    INNER JOIN dm_document        nrdoc on nr.DOCUMENT_ID = nrdoc.id   \n" +
                    "WHERE \n" +
                    "    nr.notary_info_id = :idnotary  \n" +
                    "    --đề nghị bổ nhiệm\n" +
                    "    AND nr.REQUEST_TYPE in (1)   \n" +
                    "    AND nr.active = 0    \n" +
                    "    AND nrdoc.active = 0\n" +
                    "    \n" +
                    "    \n" +
                    "\n" +
                    "---------------------------từ chối Bổ nhiệm--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Từ chối bổ nhiệm CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định từ chối bổ nhiệm: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "        \n" +
                    "FROM        \n" +
                    "    notary_appoint    apo,      \n" +
                    "    dm_document       doc       \n" +
                    "WHERE       \n" +
                    "    apo.document_id = doc.id    \n" +
                    "    AND apo.type_appoint = 2   \n" +
                    "    AND apo.notary_info_id = :idnotary  \n" +
                    "    AND apo.active = 0    \n" +
                    "    AND doc.active = 0  \n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------miễn nhiệm--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    decode(dis.STATUS_DISMISSED , 1,'Miễn nhiệm','Từ chối miễn nhiệm') as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định miễn nhiệm: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "         \n" +
                    "FROM\n" +
                    "    notary_dismissed   dis,\n" +
                    "    dm_document        doc\n" +
                    "WHERE\n" +
                    "    dis.document_id = doc.id               \n" +
                    "    AND dis.notary_info_id = :idnotary     \n" +
                    "    AND dis.active = 0\n" +
                    "    AND doc.active = 0 \n" +
                    "    AND dis.STATUS_DISMISSED in (1,2)  \n" +
                    "UNION \n" +
                    "SELECT \n" +
                    "    'Đề nghị miễn nhiệm' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định Đề nghị miễn nhiệm: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "FROM \n" +
                    "    notary_info info\n" +
                    "    INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n" +
                    "    INNER JOIN dm_document        doc on nr.DOCUMENT_ID = doc.id   \n" +
                    "WHERE \n" +
                    "    nr.notary_info_id = :idnotary  \n" +
                    "    AND nr.REQUEST_TYPE in (2,3)   \n" +
                    "    AND nr.active = 0    \n" +
                    "    AND doc.active = 0 \n" +
                    "\n" +
                    "\n" +
                    "---------------------------Bổ nhiệm lại--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Bổ nhiệm lại CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định bổ nhiệm lại: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE ,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "   \n" +
                    "FROM        \n" +
                    "    notary_reapppointed   re,       \n" +
                    "    dm_document           doc       \n" +
                    "WHERE       \n" +
                    "    re.document_id = doc.id     \n" +
                    "    AND re.notary_info_id = :idnotary   \n" +
                    "    AND re.active = 0     \n" +
                    "    AND doc.active = 0       \n" +
                    "    AND re.TYPE_REAPPOINT in (1)   \n" +
                    "union \n" +
                    "SELECT \n" +
                    "    'Đề nghị bổ nhiệm lại CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định đề nghị bổ nhiệm lại: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE ,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order   \n" +
                    "            \n" +
                    "FROM \n" +
                    "    notary_info info\n" +
                    "    INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n" +
                    "    INNER JOIN dm_document        doc on nr.DOCUMENT_ID = doc.id   \n" +
                    "WHERE \n" +
                    "    nr.notary_info_id = :idnotary  \n" +
                    "    --đề nghị bổ nhiệm lại\n" +
                    "    AND nr.REQUEST_TYPE in (4)   \n" +
                    "    AND nr.active = 0    \n" +
                    "    AND doc.active = 0\n" +
                    "\n" +
                    "---------------------------từ chối Bổ nhiệm lại--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT        \n" +
                    "    'Từ chối bổ nhiệm lại CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định từ chối bổ nhiệm lại: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE  ,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "FROM       \n" +
                    "    notary_reapppointed   re,      \n" +
                    "    dm_document           doc      \n" +
                    "WHERE      \n" +
                    "    re.document_id = doc.id        \n" +
                    "    AND re.notary_info_id = :idnotary      \n" +
                    "    AND re.active = 0    \n" +
                    "    AND doc.active = 0   \n" +
                    "    AND re.TYPE_REAPPOINT in (2)\n" +
                    "\n" +
                    "\n" +
                    "---------------------------ĐK hành nghề và cấp thẻ ccv--------------------------------------\n" +
                    "UNION\n" +
                    "select    \n" +
                    "    'Đăng ký HNCC và cấp thẻ CCV' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=decode(doc.CREATED_BY, null,reg.CREATED_BY,doc.CREATED_BY)) ) AS administration,\n" +
                    "    'Tổ chức HNCC: '||org.NAME as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định HNCC: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    'Số thẻ CCV: '||reg.NUMBER_CAD as NUMBER_CAD,\n" +
                    "    --decode(reg.status, 1, 'Đang hành nghề', 2,'Tạm đình chỉ hành nghề', 4,'Chờ cấp thẻ', 5,'Từ chối cấp thẻ', 'Thu hồi thẻ') as tabs,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "        \n" +
                    "from NOTARY_REG_PRACTICE reg   \n" +
                    "    LEFT JOIN dm_document doc on reg.document_id = doc.id and doc.active=0 \n" +
                    "    LEFT JOIN org_notary_info org on reg.org_notary_info_id = org.id and org.active=0     \n" +
                    "where reg.NOTARY_INFO_ID=:idnotary \n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "---------------------------tạm đình chỉ--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT   \n" +
                    "    decode(work.type_supend,  1,'Tạm đình chỉ CCV',  'Hủy tạm đình chỉ') as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    decode(work.type_supend,  1,'Số quyết định tạm đình chỉ: '||doc.dispatch_code,  'Số quyết định hủy tạm đình chỉ: '||doc.dispatch_code) as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "          \n" +
                    "FROM        \n" +
                    "    notary_suspend_work   work        \n" +
                    "    LEFT JOIN org_notary_info       org ON work.org_notary_id = org.id  AND org.active = 0        \n" +
                    "    LEFT JOIN dm_document           doc ON work.document_id = doc.id  AND doc.active = 0        \n" +
                    "WHERE        \n" +
                    "    work.notary_info_id = :idnotary        \n" +
                    "    AND work.active = 0\n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------Cấp lại thẻ--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    decode(card.type_document,   1,'Đã cấp lại Thẻ',  3,'Từ chối cấp lại Thẻ', 'Chờ cấp lại Thẻ') as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định cấp lại thẻ: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    'Số thẻ CCV: '||prac.NUMBER_CAD as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "    \n" +
                    "FROM\n" +
                    "    notary_card           card\n" +
                    "    LEFT JOIN dm_document           doc ON card.document_id = doc.id\n" +
                    "                                 AND doc.active = 0\n" +
                    "    LEFT JOIN notary_reg_practice   prac ON card.notary_reg_practice_id = prac.id\n" +
                    "    LEFT JOIN org_notary_info       org ON prac.org_notary_info_id = org.id\n" +
                    "                                     AND org.active = 0\n" +
                    "WHERE\n" +
                    "    prac.NOTARY_INFO_ID = :idnotary\n" +
                    "    AND card.type_document IN ( 1, 3, 4)\n" +
                    "    \n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "---------------------------Thu hồi lại thẻ--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Đã thu hồi Thẻ' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định Thu hồi thẻ: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    'Số thẻ CCV: '||prac.NUMBER_CAD as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "FROM\n" +
                    "    notary_card           card,\n" +
                    "    org_notary_info       org,\n" +
                    "    dm_document           doc,\n" +
                    "    notary_reg_practice   prac\n" +
                    "WHERE\n" +
                    "    card.notary_reg_practice_id = prac.id\n" +
                    "    AND card.document_id = doc.id\n" +
                    "    AND prac.org_notary_info_id = org.id\n" +
                    "    AND prac.NOTARY_INFO_ID = :idnotary\n" +
                    "    AND card.type_document = 2\n" +
                    "    AND card.active = 0\n" +
                    "    AND doc.active = 0\n" +
                    "    AND prac.active = 0\n" +
                    "    AND card.type_document IN (2)\n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "    \n" +
                    "---------------------------xử phạt vi phạm--------------------------------------\n" +
                    "UNION\n" +
                    "SELECT\n" +
                    "    'Xử phạt vi phạm' as ten_chinh,\n" +
                    "    'Cơ quan quản lý: '||(SELECT full_name FROM dm_administration WHERE id = (SELECT us.administration_id from adm_users us WHERE us.user_name=doc.CREATED_BY) ) AS administration,\n" +
                    "    null as org_name,\n" +
                    "    null as date_start,\n" +
                    "    null as date_end,\n" +
                    "    null as note,\n" +
                    "    'Số quyết định xử phạt: '||doc.dispatch_code as DISPATCH_CODE,\n" +
                    "    'Ngày quyết định: '||TO_CHAR(doc.DATE_SIGN, 'DD/MM/YYYY') as DATE_SIGN,\n" +
                    "    'Ngày có hiệu lực: '||TO_CHAR(doc.EFFECTIVE_DATE, 'DD/MM/YYYY') as EFFECTIVE_DATE,\n" +
                    "    null as NUMBER_CAD,\n" +
                    "    null as tabs,\n" +
                    "    doc.DATE_SIGN as gen_order\n" +
                    "FROM\n" +
                    "    notary_penalize   pena,\n" +
                    "    dm_document       doc,\n" +
                    "    org_notary_info   org\n" +
                    "WHERE\n" +
                    "    pena.document_id = doc.id\n" +
                    "    AND pena.org_notary_id = org.id\n" +
                    "    AND pena.active = 0\n" +
                    "    AND doc.active = 0\n" +
                    "    AND pena.notary_info_id = :idnotary\n" +
                    "\n" +
                    ") bf ORDER by gen_order asc    \n";

            Query query = entityManager.createNativeQuery(sql).setParameter("idnotary", idNotary);
            List<Object[]> db = query.getResultList();
            List<TimeLineView> list = new ArrayList<>();
            db.stream().forEach((record) -> {
                TimeLineView row = new TimeLineView();
//                int i=0;
//                row.setTen_chinh(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setAdministration(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setOrg_name(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setDate_start(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setDate_end(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setNote(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setDispatch_code(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setDate_sign(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setEffective_date(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setNumber_cad(record[i] == null ? null : ((String) record[i]));          i++;
//                row.setTabs(record[i] == null ? null : (record[i].toString()));          i++;

                // quy về Ten_chinh và body. body sẽ là list các giá trị còn lại (nếu có: lấy đến số thẻ CCV)
                row.setTen_chinh(record[0] == null ? null : ((String) record[0]));
                List<String> body = new ArrayList<>();
                for (int j = 1; j < 10; j++) {
                    if (record[j] != null) {
                        body.add((String) record[j]);
                    }
                }
                row.setBody(body);


                list.add(row);
            });
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", list), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<Reaport>>> reportOrganizationNotaryChart(String fromDate, String toDate, String cityId, String status) {
        PagingResult<Reaport> allData = reportOrganizationNotary(null, null, cityId, 1, 1000, status, null, null, false).getBody().getData();
//        List<Reaport> list = new ArrayList<>();
        HashMap<Reaport, Long> map = new HashMap<>();
        for (Reaport item : allData.getItems()) {
            Long total = 0L;
            if (H.isTrue(item.getCol_2())) total += Long.parseLong(item.getCol_2());
            if (H.isTrue(item.getCol_3())) total += Long.parseLong(item.getCol_3());
            if (H.isTrue(item.getCol_4())) total += Long.parseLong(item.getCol_4());
            if (H.isTrue(item.getCol_5())) total += Long.parseLong(item.getCol_5());
            if (H.isTrue(item.getCol_6())) total += Long.parseLong(item.getCol_6());
            if (H.isTrue(item.getCol_7())) total += Long.parseLong(item.getCol_7());
            map.put(item, total);
        }

        //chỉ lấy ra top 10 ( tổng các cột vào rồi tính)
        List<Reaport> list = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", list), HttpStatus.OK);
    }


    public PagingResult searchDetail(Long idNotary, int tab, boolean count, AccUser user) {
        PagingResult result = new PagingResult();
        List<NotaryInfoView> items = new ArrayList<>();
        List<Object[]> db = new ArrayList<>();
        Long rowCount = 0L;
        try {
            //Thông tin bổ nhiệm
            if (tab == 1) {
                String hql = "SELECT        \n" +
                        "    doc.date_sign,        \n" +
                        "    doc.dispatch_code,        \n" +
                        "    doc.effective_date,        \n" +
                        "    doc.signer,        \n" +
                        "    doc.file_name,        \n" +
                        "    doc.link_file,        \n" +
                        "    apo.notary_info_id,        \n" +
                        "    apo.gen_date,        \n" +
                        "    apo.id as apoid,        \n" +
                        "    apo.TYPE_APPOINT as TYPE_APPOINT,\n" +
                        "    'Bổ nhiệm' as caseTexxt\n" +
                        "FROM        \n" +
                        "    notary_appoint   apo,        \n" +
                        "    dm_document      doc        \n" +
                        "WHERE        \n" +
                        "    apo.document_id = doc.id   \n" +
                        "    -- type bổ nhiệm \n" +
                        "    AND apo.type_appoint = 1        \n" +
                        "    AND apo.notary_info_id = :idNotary        \n" +
                        "    AND apo.active = :active        \n" +
                        "    AND doc.active = :active  \n" +
                        "    \n" +
                        "union \n" +
                        "\n" +
                        "SELECT \n" +
                        "    nrdoc.date_sign,   \n" +
                        "    nrdoc.dispatch_code,   \n" +
                        "    nrdoc.effective_date,  \n" +
                        "    nrdoc.signer,  \n" +
                        "    nrdoc.file_name,   \n" +
                        "    nrdoc.link_file,   \n" +
                        "    info.id, \n" +
                        "    nr.gen_date,\n" +
                        "    nr.ID as apoid,\n" +
                        "    -1 as TYPE_APPOINT,\n" +
                        "    'Đề nghị bổ nhiệm' as caseTexxt       \n" +
                        "            \n" +
                        "FROM \n" +
                        "    notary_info info\n" +
                        "    INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n" +
                        "    INNER JOIN dm_document        nrdoc on nr.DOCUMENT_ID = nrdoc.id   \n" +
                        "WHERE \n" +
                        "    nr.notary_info_id = :idNotary  \n" +
                        "    --đề nghị bổ nhiệm\n" +
                        "    AND nr.REQUEST_TYPE in (1)   \n" +
                        "    AND nr.active = :active    \n" +
                        "    AND nrdoc.active = :active ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql + " order by date_sign desc")
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setGenDate(record[7] == null ? null : ((Date) record[7]));
                        view.setIdAppoint(record[8] == null ? null : Long.parseLong(record[8].toString()));
                        view.setTypeAppoint(record[9] == null ? null : Long.parseLong(record[9].toString()));
                        view.setName(record[10] == null ? null : ((String) record[10]));
                        items.add(view);
                    });
                }
            }

            //Thông tin từ chối bổ nhiệm
            if (tab == 2) {
                String hql = "SELECT\n" +
                        "    doc.date_sign,  \n" +
                        "    doc.dispatch_code,  \n" +
                        "    DECODE(apo.reason, NULL, '',( SELECT pa.value FROM  adm_parameter pa WHERE pa.id = apo.reason )) AS reason,     \n" +
                        "    doc.signer,     \n" +
                        "    doc.file_name,      \n" +
                        "    doc.link_file,      \n" +
                        "    apo.notary_info_id,     \n" +
                        "    apo.id,      \n" +
                        "    apo.TYPE_APPOINT as TYPE_APPOINT        \n" +
                        "FROM        \n" +
                        "    notary_appoint    apo,      \n" +
                        "    dm_document       doc       \n" +
                        "WHERE       \n" +
                        "    apo.document_id = doc.id    \n" +
                        "    AND apo.type_appoint = :type    \n" +
                        "    AND apo.notary_info_id = :idNotary  \n" +
                        "    AND apo.active = :active    \n" +
                        "    AND doc.active = :active    ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("type", ConstantsTccc.TYPE_APPOINT.TU_CHOI_BO_NHIEM)
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("type", ConstantsTccc.TYPE_APPOINT.TU_CHOI_BO_NHIEM)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setReason(record[2] == null ? null : ((String) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setIdAppoint(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        view.setTypeAppoint(record[8] == null ? null : Long.parseLong(record[8].toString()));

                        items.add(view);
                    });
                }
            }

            //Miễn nhiệm
            if (tab == 3) {
                String hql = "SELECT\n"
                        + "            doc.date_sign,\n"
                        + "            doc.dispatch_code,\n"
                        + "            doc.effective_date,\n"
                        + "            doc.signer,\n"
                        + "            doc.file_name,\n"
                        + "            doc.link_file,\n"
                        + "            dis.notary_info_id,\n"
                        + "            dis.type_dismissed,\n"
                        + "            DECODE(dis.reason_id, NULL, '',( SELECT pa.value FROM adm_parameter pa WHERE pa.id = dis.reason_id )) AS reason,\n"
                        + "            dis.gen_date,\n"
                        + "            dis.reason AS note, \n"
                        + "            decode(dis.STATUS_DISMISSED , 1,'Miễn nhiệm','Từ chối miễn nhiệm') as caseTexxt,   \n "
                        + "            dis.id as disID, "
                        + "            dis.STATUS_DISMISSED as STATUS_DISMISSED "
                        + "        FROM\n"
                        + "            notary_dismissed   dis,\n"
                        + "            dm_document        doc\n"
                        + "        WHERE\n"
                        + "            dis.document_id = doc.id               \n"
                        + "            AND dis.notary_info_id = :idNotary     \n"
                        + "            AND dis.active = :active\n"
                        + "            AND doc.active = :active AND dis.STATUS_DISMISSED=1  \n"
                        + " UNION \n"
                        + " SELECT \n"
                        + "            nrdoc.date_sign,   \n"
                        + "            nrdoc.dispatch_code,   \n"
                        + "            nrdoc.effective_date,  \n"
                        + "            nrdoc.signer,  \n"
                        + "            nrdoc.file_name,   \n"
                        + "            nrdoc.link_file,   \n"
                        + "            info.id,   \n"
                        + "            DECODE(nr.REQUEST_TYPE ,2,1, 3,2, '' ) as type_dismissed,  \n"
                        + "            (SELECT pa.value FROM adm_parameter pa WHERE pa.id = nr.reason_id) AS reason,  \n"
                        + "            nr.gen_date,   \n"
                        + "            nr.reason AS note,  \n"
                        + "            'Đề nghị miễn nhiệm' as caseTexxt,        \n "
                        + "            nr.id as disID, "
                        + "            999 as STATUS_DISMISSED "
                        + "        FROM \n"
                        + "            notary_info info\n"
                        + "            INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n"
                        + "            INNER JOIN dm_document        nrdoc on nr.DOCUMENT_ID = nrdoc.id   \n"
                        + "        WHERE \n"
                        + "            nr.notary_info_id = :idNotary  \n"
                        + "            AND nr.REQUEST_TYPE in (2,3)   \n"
                        + "            AND nr.active = :active    \n"
                        + "            AND nrdoc.active = :active \n";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ") bf")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery("select bf.* FROM (" + hql + ") bf order by bf.date_sign desc ")
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setTypeDismissed(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        view.setReason(record[8] == null ? null : ((String) record[8]));
                        view.setGenDate(record[9] == null ? null : ((Date) record[9]));
                        view.setNote(record[10] == null ? null : ((String) record[10]));
                        view.setName(record[11] == null ? null : ((String) record[11]));
                        view.setIdDismissed(record[12] == null ? null : Long.parseLong(record[12].toString()));
                        view.setStatus(record[13] == null ? null : Long.parseLong(record[13].toString()));
                        items.add(view);
                    });
                }
            }

            //Từ chối Miễn nhiệm
            if (tab == 10) {
                String hql = "SELECT        \n" +
                        "    doc.date_sign,     \n" +
                        "    doc.dispatch_code,     \n" +
                        "    doc.effective_date,        \n" +
                        "    doc.signer,        \n" +
                        "    doc.file_name,     \n" +
                        "    doc.link_file,     \n" +
                        "    dis.notary_info_id,        \n" +
                        "    dis.type_dismissed,        \n" +
                        "    DECODE(dis.reason, NULL, '',( SELECT pa.value FROM adm_parameter pa WHERE pa.id = dis.reason )),        \n" +
                        "    dis.id as disID, " +
                        "    dis.STATUS_DISMISSED as STATUS_DISMISSED " +
                        "FROM       \n" +
                        "    notary_dismissed   dis,        \n" +
                        "    dm_document        doc     \n" +
                        "WHERE      \n" +
                        "    dis.document_id = doc.id       \n" +
                        "    AND dis.notary_info_id = :idNotary     \n" +
                        "    AND dis.status_dismissed = :statusDis      \n" +
                        "    AND dis.active = :active       \n" +
                        "    AND doc.active = :active ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("statusDis", ConstantsTccc.STATUS_DISMISSED.TU_CHOI_MIEN_NHIEM);
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql + " ORDER BY dis.ID desc")
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("statusDis", ConstantsTccc.STATUS_DISMISSED.TU_CHOI_MIEN_NHIEM);
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setTypeDismissed(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        view.setReason(record[8] == null ? null : ((String) record[8]));
                        view.setIdDismissed(record[9] == null ? null : Long.parseLong(record[9].toString()));
                        view.setStatus(record[10] == null ? null : Long.parseLong(record[10].toString()));
                        items.add(view);
                    });
                }
            }

            //Thông tin bổ nhiệm lại
            if (tab == 4) {
                String hql = "SELECT\n" +
                        "    doc.date_sign,      \n" +
                        "    doc.dispatch_code,      \n" +
                        "    doc.effective_date,     \n" +
                        "    doc.signer,     \n" +
                        "    doc.file_name,  \n" +
                        "    doc.link_file,      \n" +
                        "    re.notary_info_id,      \n" +
                        "    re.reason,      \n" +
                        "    re.gen_date,     \n" +
                        "    re.id as reId,     \n" +
                        "    re.TYPE_REAPPOINT,\n" +
                        "    'Bổ nhiệm lại' as caseTexxt \n" +
                        "FROM        \n" +
                        "    notary_reapppointed   re,       \n" +
                        "    dm_document           doc       \n" +
                        "WHERE       \n" +
                        "    re.document_id = doc.id     \n" +
                        "    AND re.notary_info_id = :idNotary   \n" +
                        "    AND re.active = :active     \n" +
                        "    AND doc.active = :active        \n" +
                        "    AND re.TYPE_REAPPOINT in (1)    \n" +
                        "union \n" +
                        "SELECT \n" +
                        "    doc.date_sign,   \n" +
                        "    doc.dispatch_code,   \n" +
                        "    doc.effective_date,  \n" +
                        "    doc.signer,  \n" +
                        "    doc.file_name,   \n" +
                        "    doc.link_file,   \n" +
                        "    info.id, \n" +
                        "    nr.REASON,\n" +
                        "    nr.gen_date,\n" +
                        "    nr.ID as reId,\n" +
                        "    -1 as TYPE_REAPPOINT,\n" +
                        "    'Đề nghị bổ nhiệm lại' as caseTexxt       \n" +
                        "            \n" +
                        "FROM \n" +
                        "    notary_info info\n" +
                        "    INNER JOIN notary_request     nr on nr.NOTARY_INFO_ID = info.id    \n" +
                        "    INNER JOIN dm_document        doc on nr.DOCUMENT_ID = doc.id   \n" +
                        "WHERE \n" +
                        "    nr.notary_info_id = :idNotary  \n" +
                        "    --đề nghị bổ nhiệm lại\n" +
                        "    AND nr.REQUEST_TYPE in (4)   \n" +
                        "    AND nr.active = :active    \n" +
                        "    AND doc.active = :active";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql + " order by date_sign desc ")
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setReason(record[7] == null ? null : ((String) record[7]));
                        view.setGenDate(record[8] == null ? null : ((Date) record[8]));
                        view.setIdReAppoint(record[9] == null ? null : Long.parseLong(record[9].toString()));
                        view.setTypeAppoint(record[10] == null ? null : Long.parseLong(record[10].toString()));
                        view.setName(record[11] == null ? null : ((String) record[11]));
                        items.add(view);
                    });
                }
            }

            //Thông tin từ chối bổ nhiệm lại
            if (tab == 12) {
                String hql = "SELECT        \n" +
                        "    doc.date_sign,     \n" +
                        "    doc.dispatch_code,     \n" +
                        "    doc.effective_date,        \n" +
                        "    doc.signer,        \n" +
                        "    doc.file_name,     \n" +
                        "    doc.link_file,     \n" +
                        "    re.notary_info_id,     \n" +
                        "    DECODE(re.reason, NULL, '',( SELECT pa.value FROM adm_parameter pa WHERE pa.id = re.reason )) AS reason,        \n" +
                        "    re.id as reId,     \n" +
                        "    re.TYPE_REAPPOINT     \n" +
                        "FROM       \n" +
                        "    notary_reapppointed   re,      \n" +
                        "    dm_document           doc      \n" +
                        "WHERE      \n" +
                        "    re.document_id = doc.id        \n" +
                        "    AND re.notary_info_id = :idNotary      \n" +
                        "    AND re.active = :active    \n" +
                        "    AND doc.active = :active   \n" +
                        "    AND re.TYPE_REAPPOINT in (" + ConstantsTccc.TYPE_REAPPOINT.TU_CHOI_BO_NHIEM_LAI + ")";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setIdNotaryInfo(record[6] == null ? null : Long.parseLong(record[6].toString()));
                        view.setReason(record[7] == null ? null : ((String) record[7]));
                        view.setIdReAppoint(record[8] == null ? null : Long.parseLong(record[8].toString()));
                        view.setTypeAppoint(record[9] == null ? null : Long.parseLong(record[9].toString()));
                        items.add(view);
                    });
                }
            }

            //đăng ký hành nghè công chứng và cấp thẻ
            if (tab == 5) {
                /*String hql = "select    \n"
                        + "decode(info.STATUS , 8,doc.DATE_SIGN, 24,doc.DATE_SIGN , 23, '', doc.DATE_SIGN ) as DATE_SIGN,              \n"
                        + "       decode(info.STATUS , 8,doc.DISPATCH_CODE, 24,  doc.DISPATCH_CODE ,23, '', doc.DISPATCH_CODE ) as DISPATCH_CODE,      \n"
                        + "       decode(info.STATUS , 8,doc.EFFECTIVE_DATE,24, doc.EFFECTIVE_DATE ,  23, '', doc.EFFECTIVE_DATE  ) as EFFECTIVE_DATE,     \n"
                        + "       decode(info.STATUS , 8,doc.SIGNER, 24,doc.SIGNER , 23, '', doc.SIGNER ) as SIGNER,                    \n"
                        + "       decode(info.STATUS , 8,doc.FILE_NAME, 24,doc.FILE_NAME , 23, '', doc.FILE_NAME ) as FILE_NAME,                    \n"
                        + "       decode(info.STATUS , 8,doc.LINK_FILE, 24,doc.LINK_FILE , 23, '', doc.LINK_FILE ) as LINK_FILE,                    \n"
                        + "       reg.NOTARY_INFO_ID , decode(info.STATUS , 8,reg.NUMBER_CAD, 24,'' , 23, '', reg.NUMBER_CAD ) as NUMBER_CAD,     \n"
                        + "       org.NAME as orgName,reg.GEN_DATE,reg.REASON,reg.status \n"
                        + "   from NOTARY_REG_PRACTICE reg \n"
                        + "       LEFT JOIN dm_document doc on reg.document_id = doc.id and doc.active=:active \n"
                        + "       LEFT JOIN org_notary_info org on reg.org_notary_info_id = org.id and org.active=:active \n"
                        + "       LEFT JOIN notary_info info on reg.notary_info_id = info.id and info.active=:active \n"
                        + "   where reg.NOTARY_INFO_ID=:idNotary and reg.status in (1,4,5) and reg.active=:active \n"
                        + "       and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) \n";*/

                String hql = "select    \n" +
                        "    doc.DATE_SIGN,     \n" +
                        "    doc.DISPATCH_CODE, \n" +
                        "    doc.EFFECTIVE_DATE, \n" +
                        "    doc.SIGNER,    \n" +
                        "    doc.FILE_NAME,     \n" +
                        "    doc.LINK_FILE,     \n" +
                        "    reg.NOTARY_INFO_ID,   \n" +
                        "    reg.NUMBER_CAD,    \n" +
                        "    org.NAME as orgName,   \n" +
                        "    reg.GEN_DATE,    \n" +
                        "    reg.REASON,         \n" +
                        "    reg.status,    \n" +
                        "    reg.ID as regID, dm.FULL_NAME    \n" +
                        "from NOTARY_REG_PRACTICE reg   \n" +
                        "    LEFT JOIN dm_document doc on reg.document_id = doc.id and doc.active=:active   \n" +
                        "    LEFT JOIN org_notary_info org on reg.org_notary_info_id = org.id and org.active=:active    \n" +
                        "    LEFT JOIN DM_ADMINISTRATION dm on org.ADMINISTRATION_ID = dm.id   \n" +
                        "where reg.NOTARY_INFO_ID=:idNotary     \n" +
                        "and reg.active=:active    \n" +
                        "    and :idAdminisLogin = :idAdminisLogin  ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql + " order by doc.DATE_SIGN desc ")
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();

                        int i = 0;

                        view.setDateSign(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setDispatchCode(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setEffectiveDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setSigner(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setFileName(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setLinkFile(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setIdNotaryInfo(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setNumberCad(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setNameOrgNotaryInfo(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setGenDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setReason(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setStatus_prac(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setIdNotaryRegPractice(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setNameAdmin(record[i] == null ? null : ((String) record[i]));
                        i++;

                        items.add(view);
                    });
                }
            }

            //Tạm đình chỉ HNCC
            if (tab == 6) {

                String hql = "SELECT        \n" +
                        "    doc.date_sign,        \n" +
                        "    doc.dispatch_code,        \n" +
                        "    doc.effective_date,        \n" +
                        "    doc.signer,        \n" +
                        "    doc.file_name,        \n" +
                        "    doc.link_file,        \n" +
                        "    DECODE(work.reason, NULL, '',(SELECT value FROM adm_parameter WHERE id = work.reason)) AS reason,        \n" +
                        "    work.notary_info_id,        \n" +
                        "    work.gen_date,        \n" +
                        "    work.id,        \n" +
                        "    work.type_supend,        \n" +
                        "    work.term_of_suspension        \n" +
                        "FROM        \n" +
                        "    notary_suspend_work   work        \n" +
                        "    LEFT JOIN org_notary_info       org ON work.org_notary_id = org.id        \n" +
                        "                                     AND org.active = :active        \n" +
                        "    LEFT JOIN dm_document           doc ON work.document_id = doc.id        \n" +
                        "                                 AND doc.active = :active        \n" +
                        "WHERE        \n" +
                        "    work.notary_info_id = :idNotary        \n" +
                        "    AND work.active = :active        \n" +
                        "    AND org.administration_id IN (SELECT stat.id FROM dm_administration stat START WITH stat.id = :idAdminisLogin  CONNECT BY PRIOR stat.id = stat.parent_id)        \n" +
                        "ORDER BY        \n" +
                        "    work.id DESC ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();

                        int i = 0;
                        view.setDateSign(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setDispatchCode(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setEffectiveDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setSigner(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setFileName(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setLinkFile(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setReason(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setIdNotaryInfo(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setGenDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setSupendId(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setTypeSupend(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setTermOfSuspension(record[i] == null ? null : ((Date) record[i]));
                        i++;

                        items.add(view);
                    });
                }
            }

            //hủy tạm đình chỉ
            if (tab == 13) {

                String hql = "SELECT\n" +
                        "    doc.date_sign,\n" +
                        "    doc.dispatch_code,\n" +
                        "    doc.effective_date,\n" +
                        "    doc.signer,\n" +
                        "    doc.file_name,\n" +
                        "    doc.link_file,\n" +
                        "    DECODE(work.reason, NULL, '',( SELECT  value FROM adm_parameter  WHERE id = work.reason )) AS reason,\n" +
                        "    work.notary_info_id,\n" +
                        "    work.gen_date,\n" +
                        "    work.id,        \n" +
                        "    work.type_supend        \n" +
                        "FROM\n" +
                        "    notary_suspend_work   work,\n" +
                        "    org_notary_info       org,\n" +
                        "    dm_document           doc,\n" +
                        "    notary_reg_practice   prac\n" +
                        "WHERE\n" +
                        "    work.org_notary_id = org.id\n" +
                        "    AND work.document_id = doc.id\n" +
                        "    AND work.notary_info_id = :idNotary \n" +
                        "    AND prac.id in ( SELECT MAX(id) FROM notary_reg_practice WHERE notary_info_id = :idNotary and active = 0)\n" +
                        "    AND doc.active = :active\n" +
                        "    AND doc.ACTIVE=:active and work.TYPE_SUPEND=" + ConstantsTccc.TYPE_SUPEND.HUY_TAM_DINH_CHI + ConstantsTccc.SPACE +
                        "    AND org.administration_id IN ( SELECT stat.id FROM dm_administration stat START WITH stat.id = :idAdminisLogin CONNECT BY PRIOR stat.id = stat.parent_id)\n";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setReason(record[6] == null ? null : ((String) record[6]));
                        view.setIdNotaryInfo(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        view.setGenDate(record[8] == null ? null : ((Date) record[8]));
                        view.setTypeSupend(record[9] == null ? null : Long.parseLong(record[9].toString()));

                        items.add(view);
                    });
                }
            }

            //Cấp lại thẻ CCV
            if (tab == 7) {

                String hql = "SELECT\n" +
                        "    doc.date_sign,\n" +
                        "    doc.dispatch_code,\n" +
                        "    doc.effective_date,\n" +
                        "    doc.signer,\n" +
                        "    doc.file_name,\n" +
                        "    doc.link_file,\n" +
                        "    DECODE(card.reason, NULL,( SELECT ap.value FROM adm_parameter ap  WHERE  ap.id = card.reason_id  ), card.reason) AS reason,\n" +
                        "    prac.NOTARY_INFO_ID,\n" +
                        "    card.gen_date,\n" +
                        "    card.id AS cardid,\n" +
                        "    card.type_document,\n" +
                        "    prac.number_cad\n" +
                        "FROM\n" +
                        "    notary_card           card\n" +
                        "    LEFT JOIN dm_document           doc ON card.document_id = doc.id\n" +
                        "                                 AND doc.active = :active\n" +
                        "    LEFT JOIN notary_reg_practice   prac ON card.notary_reg_practice_id = prac.id\n" +
                        "    LEFT JOIN org_notary_info       org ON prac.org_notary_info_id = org.id\n" +
                        "                                     AND org.active = :active\n" +
                        "WHERE\n" +
                        "    prac.NOTARY_INFO_ID = :idNotary\n" +
                        "    AND card.type_document IN ( 1, 3, 4)\n" +
                        "    AND org.administration_id IN ( SELECT stat.id FROM dm_administration stat  START WITH stat.id = :idAdminisLogin CONNECT BY  PRIOR stat.id = stat.parent_id) ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();

                        int i = 0;
                        view.setDateSign(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setDispatchCode(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setEffectiveDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setSigner(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setFileName(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setLinkFile(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setReason(record[i] == null ? null : ((String) record[i]));
                        i++;
                        view.setIdNotaryInfo(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setGenDate(record[i] == null ? null : ((Date) record[i]));
                        i++;
                        view.setIdNotaryCard(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setStatusNotaryCard(record[i] == null ? null : Long.parseLong(record[i].toString()));
                        i++;
                        view.setNumberCad(record[i] == null ? null : ((String) record[i]));
                        i++;

                        items.add(view);
                    });
                }
            }

            //xóa đkhn và thu hồi thẻ CCV
            if (tab == 8) {
                String hql = "SELECT\n" +
                        "    doc.date_sign,\n" +
                        "    doc.dispatch_code,\n" +
                        "    doc.effective_date,\n" +
                        "    doc.signer,\n" +
                        "    doc.file_name,\n" +
                        "    doc.link_file,\n" +
                        "    ( SELECT value FROM adm_parameter WHERE id = card.reason ) AS reason,\n" +
                        "    prac.NOTARY_INFO_ID,\n" +
                        "    card.gen_date,\n" +
                        "    card.id,    \n" +
                        "    card.type_document    \n" +
                        "FROM\n" +
                        "    notary_card           card,\n" +
                        "    org_notary_info       org,\n" +
                        "    dm_document           doc,\n" +
                        "    notary_reg_practice   prac\n" +
                        "WHERE\n" +
                        "    card.notary_reg_practice_id = prac.id\n" +
                        "    AND card.document_id = doc.id\n" +
                        "    AND prac.org_notary_info_id = org.id\n" +
                        "    AND prac.NOTARY_INFO_ID = :idNotary\n" +
                        "    AND card.type_document = :type\n" +
                        "    AND card.active = :active\n" +
                        "    AND doc.active = :active\n" +
                        "    AND prac.active = :active\n" +
                        "    AND card.type_document IN ( 2 )\n" +
                        "    AND org.administration_id IN (  SELECT stat.id FROM  dm_administration stat START WITH stat.id = :idAdminisLogin CONNECT BY PRIOR stat.id = stat.parent_id ) ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("type", ConstantsTccc.TYPE_NOTARY_CARD.THU_HOI_THE)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("type", ConstantsTccc.TYPE_NOTARY_CARD.THU_HOI_THE)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setReason(record[6] == null ? null : ((String) record[6]));
                        view.setIdNotaryInfo(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        view.setGenDate(record[8] == null ? null : ((Date) record[8]));
                        view.setIdNotaryCard(record[9] == null ? null : Long.parseLong(record[9].toString()));
                        view.setTypeDocument(record[10] == null ? null : Long.parseLong(record[10].toString()));
                        items.add(view);
                    });
                }
            }

            //Từ chối cấp lại thẻ
            if (tab == 11) {
                String hql = "SELECT\n" +
                        "    doc.date_sign,\n" +
                        "    doc.dispatch_code,\n" +
                        "    doc.effective_date,\n" +
                        "    doc.signer,\n" +
                        "    doc.file_name,\n" +
                        "    doc.link_file,\n" +
                        "    card.reason,\n" +
                        "    prac.NOTARY_INFO_ID,\n" +
                        "    card.id\n" +
                        "FROM\n" +
                        "    notary_card           card,\n" +
                        "    org_notary_info       org,\n" +
                        "    dm_document           doc,\n" +
                        "    notary_reg_practice   prac\n" +
                        "WHERE\n" +
                        "    card.notary_reg_practice_id = prac.id\n" +
                        "    AND card.document_id = doc.id\n" +
                        "    AND prac.org_notary_info_id = org.id\n" +
                        "    AND prac.NOTARY_INFO_ID = :idNotary\n" +
                        "    AND card.type_document = :type\n" +
                        "    AND card.active = :active\n" +
                        "    AND doc.active = :active\n" +
                        "    AND prac.active = :active\n" +
                        "    AND org.administration_id IN ( SELECT  stat.id FROM dm_administration stat  START WITH stat.id = :idAdminisLogin CONNECT BY  PRIOR stat.id = stat.parent_id )\n ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("type", ConstantsTccc.TYPE_NOTARY_CARD.TU_CHOI_CAP_LAI_THE)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("type", ConstantsTccc.TYPE_NOTARY_CARD.TU_CHOI_CAP_LAI_THE)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDateSign(record[0] == null ? null : ((Date) record[0]));
                        view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                        view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                        view.setSigner(record[3] == null ? null : ((String) record[3]));
                        view.setFileName(record[4] == null ? null : ((String) record[4]));
                        view.setLinkFile(record[5] == null ? null : ((String) record[5]));
                        view.setReason(record[6] == null ? null : ((String) record[6]));
                        view.setIdNotaryInfo(record[7] == null ? null : Long.parseLong(record[7].toString()));
                        items.add(view);
                    });
                }
            }

            //Xử phạt vi phạm
            if (tab == 9) {
                String hql = "SELECT\n" +
                        "    doc.dispatch_code,\n" +
                        "    doc.date_sign,\n" +
                        "    doc.signer,\n" +
                        "    doc.effective_date,\n" +
                        "    pena.lever_penalize,\n" +
                        "    pena.reason,\n" +
                        "    doc.file_name,\n" +
                        "    doc.link_file,\n" +
                        "    pena.notary_info_id,\n" +
                        "    pena.gen_date,\n" +
                        "    (SELECT value FROM adm_parameter WHERE id = pena.administration_id_penalty ) AS admin,\n" +
                        "    pena.type_penalize,\n" +
                        "    pena.money_penalty,\n" +
                        "    pena.additional_penalty,    \n" +
                        "    pena.id as penaId    \n" +
                        "FROM\n" +
                        "    notary_penalize   pena,\n" +
                        "    dm_document       doc,\n" +
                        "    org_notary_info   org\n" +
                        "WHERE\n" +
                        "    pena.document_id = doc.id\n" +
                        "    AND pena.org_notary_id = org.id\n" +
                        "    AND pena.active = :active\n" +
                        "    AND doc.active = :active\n" +
                        "    AND pena.notary_info_id = :idNotary\n" +
                        "    AND org.administration_id IN ( SELECT stat.id FROM dm_administration stat START WITH stat.id = :idAdminisLogin CONNECT BY PRIOR stat.id = stat.parent_id) ";

                Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                        .setParameter("idNotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                        .setParameter("idAdminisLogin", user.getAdministrationId());
                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());
                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("idNotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
                            .setParameter("idAdminisLogin", user.getAdministrationId());
                    db = query.getResultList();
                    db.stream().forEach((record) -> {
                        NotaryInfoView view = new NotaryInfoView();
                        view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                        view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                        view.setSigner(record[2] == null ? null : ((String) record[2]));
                        view.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                        view.setLeverPenalize(record[4] == null ? null : Long.parseLong(record[4].toString()));
                        view.setReason(record[5] == null ? null : ((String) record[5]));
                        view.setFileName(record[6] == null ? null : ((String) record[6]));
                        view.setLinkFile(record[7] == null ? null : ((String) record[7]));
                        view.setIdNotaryInfo(record[8] == null ? null : Long.parseLong(record[8].toString()));
                        view.setGenDate(record[9] == null ? null : ((Date) record[9]));
                        view.setValue(record[10] == null ? null : ((String) record[10]));
                        view.setTypePenalize(record[11] == null ? null : Long.parseLong(record[11].toString()));
                        view.setMoneyPenalty(record[12] == null ? null : Long.parseLong(record[12].toString()));
                        view.setAdditionalPenalty(record[13] == null ? null : Long.parseLong(record[13].toString()));
                        view.setSupendId(record[14] == null ? null : Long.parseLong(record[14].toString()));

                        items.add(view);
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Loi tai NotaryInfoDAO.searchDetail" + e.getMessage());
        }
        result.setRowCount(rowCount);
        result.setItems(items);
        return result;
    }


    // Lấy ra 1 list các thông tin xử phạt vpcc
    public PagingResult getPagePenalize(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult page = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        List<OrgNotaryInfoView> listPenalizeOrg = new ArrayList<>();

        String hql = "select onp.ID, doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, onp.lever_penalize, onp.REASON, doc.LINK_FILE, doc.FILE_NAME,"
                + "onp.TYPE_PENALIZE,onp.ADDITIONAL_PENALTY,decode(onp.administration_id_penalty,null,'',(SELECT p.value FROM adm_parameter p where p.id=onp.administration_id_penalty)) as administration_id_penalty,onp.money_penalty  "
                + "FROM DM_DOCUMENT doc "
                + "JOIN ORG_NOTARY_PENALIZE onp ON onp.DOCUMENT_ID=doc.ID "
                + "and onp.ACTIVE=:active and doc.ACTIVE=:active "
                + "and onp.ORG_NOTARY_ID=:orgId ";
        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("orgId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("orgId", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView penalizeOrg = new OrgNotaryInfoView();
                penalizeOrg.setPenalizeId(record[0] == null ? null : Long.parseLong(record[0].toString()));
                penalizeOrg.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                penalizeOrg.setDateSign(record[2] == null ? null : ((Date) record[2]));
                penalizeOrg.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                penalizeOrg.setSigner(record[4] == null ? null : ((String) record[4]));
                penalizeOrg.setLeverPenalize(record[5] == null ? null : Long.parseLong(record[5].toString()));
                penalizeOrg.setPenalizeReason(record[6] == null ? null : ((String) record[6]));
                penalizeOrg.setLinkFile(record[7] == null ? null : ((String) record[7]));
                penalizeOrg.setFileName(record[8] == null ? null : ((String) record[8]));
                penalizeOrg.setTypePenalize(record[9] == null ? null : Long.parseLong(record[9].toString()));
                penalizeOrg.setAdditionalPenalty(record[10] == null ? null : Long.parseLong(record[10].toString()));
                penalizeOrg.setAdministrationIdPenaltyStr(record[11] == null ? null : ((String) record[11]));
                penalizeOrg.setMoneyPenalty(record[12] == null ? null : Long.parseLong(record[12].toString()));
                listPenalizeOrg.add(penalizeOrg);
            });
        }
        page.setItems(listPenalizeOrg);
        page.setRowCount(rowCount);

        return page;
    }

    // Lấy ra 1 list bản ghi hoạt động của tổ chức
    public PagingResult getHistoryOrg(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult page = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        List<OrgNotaryInfoView> listAction = new ArrayList<>();
        String hql = "select * from ( "
                + "select decode(na.TYPE, 1,'THÀNH LẬP', 2,'ĐĂNG KÝ HOẠT ĐỘNG', 3,'GIẢI THỂ', 4,'CHẤM DỨT HOẠT ĐỘNG', 6,'Thay đổi nội dung hoạt động', 7,'Chuyển nhượng', 8,'Thu hồi QĐ cho phép thành lập', 9,'Cấp mới giấy đăng ký hoạt động', 10,'Từ chối cấp giấy đăng ký hoạt động', 11,'Thu hồi giấy đăng ký hoạt động', 12,'Thay đổi nội dung ĐKHĐ', 13,'Từ chối thay đổi nội dung ĐKHĐ' , 14,'Từ chối thành lập' , 15,'Cấp lại giấy đăng ký hoạt động', 'N/A' ) as ACT_NAME, "
                + "dcm.DISPATCH_CODE as DISPATCH_CODE, dcm.DATE_SIGN as DATE_SIGN, dcm.EFFECTIVE_DATE as EFFECTIVE_DATE, null as TYPE,  na.GEN_DATE  as GEN_DATE "
                + "from ORG_NOTARY_INFO ni , ORG_NOTARY_ACTION na, DM_DOCUMENT dcm where ni.ID = na.ORG_NOTARY_INFO_ID and na.DOCUMENT_ID = dcm.id "
                + "and ni.ACTIVE=:active and na.ACTIVE=:active and dcm.ACTIVE=:active and ni.id=:id "
                + "UNION ALL "
                + "select 'XỬ PHẠT VI PHẠM' as ACT_NAME, "
                + "dcm.DISPATCH_CODE as DISPATCH_CODE, dcm.DATE_SIGN as DATE_SIGN, dcm.EFFECTIVE_DATE as EFFECTIVE_DATE , LEVER_PENALIZE as TYPE, na.GEN_DATE  as GEN_DATE "
                + "from ORG_NOTARY_INFO ni , ORG_NOTARY_PENALIZE na, DM_DOCUMENT dcm where ni.ID = na.ORG_NOTARY_ID and na.DOCUMENT_ID = dcm.id "
                + "and ni.ACTIVE=:active and na.ACTIVE=:active and dcm.ACTIVE=:active and ni.id=:id "
                + "UNION ALL "
                + "select decode(na.TYPE, 2, 'HỢP NHẤT', 3, 'SÁP NHẬP', 4, 'CHUYỂN ĐỔI', 'N/A') as ACT_NAME, "
                + "dcm.DISPATCH_CODE as DISPATCH_CODE, dcm.DATE_SIGN as DATE_SIGN, dcm.EFFECTIVE_DATE as EFFECTIVE_DATE, null as TYPE,  na.GEN_DATE  as GEN_DATE "
                + "from ORG_NOTARY_INFO ni , ORG_NOTARY_TRANSFER na,ORG_NOTARY_TRANSFER_DETAIL ontd, DM_DOCUMENT dcm where ni.ID = ontd.ORG_NOTARY_INFO_ID and ontd.ORG_NOTARY_TRANSFER_ID = na.ID and na.DOCUMENT_ID = dcm.id "
                + "and ni.ACTIVE=:active and na.ACTIVE=:active and dcm.ACTIVE=:active and ontd.ACTIVE=:active and ni.id=:id "
                + ") order by GEN_DATE ";
        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("id", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
            ;
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView view = new OrgNotaryInfoView();
                view.setActionName(record[0] == null ? null : ((String) record[0]));
                view.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                view.setDateSign(record[2] == null ? null : ((Date) record[2]));
                view.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                view.setLeverPenalize(record[4] == null ? null : Long.parseLong(record[4].toString()));
                listAction.add(view);
            });
        }
        page.setItems(listAction);
        page.setRowCount(rowCount);

        return page;
    }

    // Lấy ra 1 list các ccv đang hành nghề tại vpcc này
    public PagingResult getPageNotary(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult page = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        List<NotaryInfoView> listNotaryOfOrg = new ArrayList<>();

        String hql = "SELECT\n" +
                "    ni.name,\n" +
                "    ni.birth_day,\n" +
                "    ni.id_no,\n" +
                "    ni.phone_number,\n" +
                "    decode(ni.ADDRESS_RESIDENT_ID,null,ni.ADDRESS_RESIDENT,ni.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as address_resident,\n" +
                "    doc.dispatch_code,\n" +
                "    doc.date_sign,\n" +
                "    nrp.number_cad\n" +
                "FROM\n" +
                "    notary_reg_practice   nrp\n" +
                "    LEFT JOIN notary_info           ni ON nrp.notary_info_id = ni.id\n" +
                "                                AND ni.active = :active\n" +
                "    LEFT JOIN notary_appoint        na ON na.notary_info_id = ni.id\n" +
                "                                   AND na.active = :active\n" +
                "                                   AND na.type_appoint = 1\n" +
                "                                   AND na.id IN (SELECT MAX(id) FROM notary_appoint GROUP BY notary_info_id)\n" +
                "    LEFT JOIN dm_document           doc ON na.document_id = doc.id\n" +
                "                                 AND doc.active = :active\n" +
                "    LEFT JOIN  DM_AREA dm on dm.ID=ni.ADDRESS_RESIDENT_ID         \n" +
                "WHERE\n" +
                "    nrp.org_notary_info_id = :orgId\n" +
                "    AND nrp.status = :nrpStatus\n" +
                "    AND nrp.active = :active ";

        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)
                .setParameter("orgId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)
                    .setParameter("orgId", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

            db = query.getResultList();
            db.stream().forEach((record) -> {
                NotaryInfoView notary = new NotaryInfoView();
                notary.setNameNotaryInfo(record[0] == null ? null : ((String) record[0]));
                notary.setBirthDay(record[1] == null ? null : ((Date) record[1]));
                notary.setIdNo(record[2] == null ? null : ((String) record[2]));
                notary.setPhoneNumberNotaryInfo(record[3] == null ? null : ((String) record[3]));
                notary.setAddressResident(record[4] == null ? null : ((String) record[4]));
                notary.setDispatchCode(record[5] == null ? null : ((String) record[5]));
                notary.setDateSign(record[6] == null ? null : ((Date) record[6]));
                notary.setNumberCad(record[7] == null ? null : ((String) record[7]));
                listNotaryOfOrg.add(notary);
            });
        }
        page.setItems(listNotaryOfOrg);
        page.setRowCount(rowCount);

        return page;
    }

    //Lấy chi tiết thông tin thành lập pcc --
    public OrgNotaryInfoView getDetailEstablishPCC(Long idOrgNotaryInfo) {
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "select doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, doc.LINK_FILE, doc.FILE_NAME, ona.NOTE "
                + "FROM DM_DOCUMENT doc "
                + "JOIN ORG_NOTARY_ACTION ona ON ona.DOCUMENT_ID=doc.ID "
                + "WHERE ona.ACTIVE=:active and doc.ACTIVE=:active "
                + "and ona.TYPE in (:onaType1, :onaType2, :onaType3) and ona.ORG_NOTARY_INFO_ID=:id";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("onaType1", ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_QD)
                .setParameter("onaType2", ConstantsTccc.TYPE_ORG_ACTION.THANH_LAP)
                .setParameter("onaType3", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THANH_LAP)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));
                view.setNote(record[6] == null ? null : ((String) record[6]));
            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    //Lấy chi tiết thông tin chuyển đổi vpcc --
    public OrgNotaryInfoView getDetailConversionPCC(Long idOrgNotaryInfo) {

        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "select doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, doc.LINK_FILE, doc.FILE_NAME, oni.NAME as orgName, "
                + "oni.ADDRESS, oni.TEL, oni.FAX, oni.EMAIL, oni.WEBSITE, ni.NAME as notaryName, ni.SEX, ni.ADDRESS_RESIDENT, ni.ADDRESS_NOW "
                + "FROM DM_DOCUMENT doc "
                + "JOIN ORG_NOTARY_TRANSFER ont ON ont.DOCUMENT_ID=doc.ID "
                + "JOIN ORG_NOTARY_TRANSFER_DETAIL ontd ON ontd.ORG_NOTARY_TRANSFER_ID=ont.ID "
                + "JOIN ORG_NOTARY_INFO oni ON ontd.ORG_NOTARY_INFO_ID=oni.ID "
                + "JOIN NOTARY_INFO ni ON oni.NOTARY_ID_OFFICE_CHIEF=ni.ID "
                + "WHERE ont.ACTIVE=:active and doc.ACTIVE=:active and ontd.ACTIVE=:active and oni.ACTIVE=:active and ni.ACTIVE=:active "
                + "and ont.ID in (select ontd2.ORG_NOTARY_TRANSFER_ID from ORG_NOTARY_TRANSFER_DETAIL ontd2 where ontd2.ORG_NOTARY_INFO_ID=:id and ontd2.ACTIVE=:active and ontd2.ORG_NOTARY_TYPE=:bichuyendoi ) "
                + "and ontd.ORG_NOTARY_TYPE=:vpchuyendoi";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("bichuyendoi", ConstantsTccc.LOAI_TCCC.TC_CHUYEN_DOI)
                .setParameter("vpchuyendoi", ConstantsTccc.LOAI_TCCC.TC_NHAN_CHUYEN_DOI)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {

                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));
                view.setName(record[6] == null ? null : ((String) record[6]));
                view.setAddress(record[7] == null ? null : ((String) record[7]));
                view.setTel(record[8] == null ? null : ((String) record[8]));
                view.setFax(record[9] == null ? null : ((String) record[9]));
                view.setEmail(record[10] == null ? null : ((String) record[10]));
                view.setWebsite(record[11] == null ? null : ((String) record[11]));
                view.setOfficeChiefName(record[12] == null ? null : ((String) record[12]));
                view.setSex(record[13] == null ? null : Long.parseLong(record[13].toString()));
                view.setAddressResident(record[14] == null ? null : ((String) record[14]));
                view.setAddressNow(record[15] == null ? null : ((String) record[15]));

            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    // Lấy ra 1 list các thông tin thành lập vpcc --
    public List<OrgNotaryInfoView> getPageEstablishVPCC(Long idOrgNotaryInfo) {

        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        List<OrgNotaryInfoView> listPageEstablish = new ArrayList<>();

        String hql = "select ona.ID, doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, doc.LINK_FILE, doc.FILE_NAME,ona.TYPE "
                + "FROM ORG_NOTARY_ACTION ona  "
                + "left JOIN DM_DOCUMENT doc ON ona.DOCUMENT_ID=doc.ID and doc.ACTIVE=:active "
                + "WHERE ona.TYPE in(:tuchoi,:dathanhlap,:thuhoi) "
                + "and ona.ACTIVE=:active  "
                + "and ona.ORG_NOTARY_INFO_ID=:orgId "
                + "AND ona.id IN ( SELECT MAX(id) FROM org_notary_action GROUP BY org_notary_info_id, type ) ";
        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("tuchoi", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THANH_LAP)
                .setParameter("dathanhlap", ConstantsTccc.TYPE_ORG_ACTION.THANH_LAP)
                .setParameter("thuhoi", ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_QD)
                .setParameter("orgId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            query = entityManager.createNativeQuery(hql)
                    .setParameter("tuchoi", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THANH_LAP)
                    .setParameter("dathanhlap", ConstantsTccc.TYPE_ORG_ACTION.THANH_LAP)
                    .setParameter("thuhoi", ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_QD)
                    .setParameter("orgId", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView InfoEstablish = new OrgNotaryInfoView();
                InfoEstablish.setIdOrgNotaryAction(record[0] == null ? null : Long.parseLong(record[0].toString()));
                InfoEstablish.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                InfoEstablish.setDateSign(record[2] == null ? null : ((Date) record[2]));
                InfoEstablish.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                InfoEstablish.setSigner(record[4] == null ? null : ((String) record[4]));
                InfoEstablish.setLinkFile(record[5] == null ? null : ((String) record[5]));
                InfoEstablish.setFileName(record[6] == null ? null : ((String) record[6]));
                InfoEstablish.setOnaType(record[7] == null ? null : Long.parseLong(record[7].toString()));
                listPageEstablish.add(InfoEstablish);
            });
        }
        return listPageEstablish;
    }

    //Lấy chi tiết thông tin chấm dứt hoạt động vpcc --
    public OrgNotaryInfoView getDetailTerminationVPCC(Long idOrgNotaryInfo) {
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "select doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, doc.LINK_FILE, doc.FILE_NAME, ona.NOTE "
                + "FROM DM_DOCUMENT doc "
                + "JOIN ORG_NOTARY_ACTION ona ON ona.DOCUMENT_ID=doc.ID "
                + "WHERE ona.ACTIVE=:active and doc.ACTIVE=:active "
                + "and ona.TYPE=:onaType and ona.ORG_NOTARY_INFO_ID=:id";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("onaType", ConstantsTccc.TYPE_ORG_ACTION.CHAM_DUT_HOAT_DONG)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));
                view.setNote(record[6] == null ? null : ((String) record[6]));
            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    //Lấy chi tiết thông tin vpcc nhận sáp nhập --
    public OrgNotaryInfoView getMergerVPCC(Long idOrgNotaryInfo) {
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "SELECT doc.dispatch_code, doc.date_sign, doc.effective_date,doc.signer, doc.link_file,doc.file_name   \n"
                + "FROM dm_document doc, org_notary_transfer tran, org_notary_transfer_detail de        \n"
                + "where doc.id=tran.document_id and tran.id = de.org_notary_transfer_id and de.org_notary_type=1       \n"
                + "and tran.type =3 and de.org_notary_info_id=:id and tran.active=0 and doc.active=0 and de.active=0        \n"
                + "and tran.id in (SELECT max(id) FROM org_notary_transfer GROUP BY type)       \n"
                + "and de.id in (SELECT max(id) FROM org_notary_transfer_detail GROUP BY org_notary_info_id,org_notary_type)";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));

            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    //Lấy chi tiết thông tin sát nhập vpcc --
    public OrgNotaryInfoView getDetailMergerVPCC(Long idOrgNotaryInfo) {
        try {
            Long rowCount = 0L;
            List<Object[]> db = new ArrayList<>();

            String hql = "SELECT\n" +
                    "    doc.dispatch_code,\n" +
                    "    doc.date_sign,\n" +
                    "    doc.effective_date,\n" +
                    "    doc.signer,\n" +
                    "    doc.link_file,\n" +
                    "    doc.file_name,\n" +
                    "    oni.name   AS orgname,\n" +
                    "    decode(oni.ADDRESS_ID,null,oni.ADDRESS,oni.ADDRESS||' - '||da3.COMMUNE_NAME||' - '||da3.DISTRICT_NAME||' - '||da3.PROVINCE_NAME) as ADDRESS,\n" +
                    "    oni.tel,\n" +
                    "    oni.fax,\n" +
                    "    oni.email,\n" +
                    "    oni.website,\n" +
                    "    ni.name    AS notaryname,\n" +
                    "    ni.sex,\n" +
                    "    DECODE(ni.address_resident_id, NULL, ni.address_resident, ni.address_resident || ' - ' || dm.commune_name || ' - ' || dm.district_name || ' - ' || dm.province_name) AS address_resident,\n" +
                    "    DECODE(ni.address_now_id, NULL, ni.address_now, ni.address_now || ' - ' || dm2.commune_name || ' - '|| dm2.district_name|| ' - '|| dm2.province_name) AS address_now\n" +
                    "FROM DM_DOCUMENT doc        \n" +
                    "    INNER JOIN ORG_NOTARY_TRANSFER ont ON ont.DOCUMENT_ID=doc.ID        \n" +
                    "    INNER JOIN ORG_NOTARY_TRANSFER_DETAIL ontd ON ontd.ORG_NOTARY_TRANSFER_ID=ont.ID        \n" +
                    "    INNER JOIN ORG_NOTARY_INFO oni ON ontd.ORG_NOTARY_INFO_ID=oni.ID        \n" +
                    "    LEFT JOIN NOTARY_INFO ni ON oni.NOTARY_ID_OFFICE_CHIEF=ni.ID and ni.ACTIVE=:active \n" +
                    "    LEFT JOIN  DM_AREA da3 on da3.ID=oni.ADDRESS_id\n" +
                    "    LEFT JOIN dm_area             dm on dm.id = ni.address_resident_id\n" +
                    "    LEFT JOIN dm_area             dm2 on dm2.id = ni.address_now_id\n" +
                    "WHERE \n" +
                    "    ont.ACTIVE=:active and doc.ACTIVE=:active and ontd.ACTIVE=:active and oni.ACTIVE=:active        \n" +
                    "    and ont.ID in (select ontd2.ORG_NOTARY_TRANSFER_ID from ORG_NOTARY_TRANSFER_DETAIL ontd2 where ontd2.ORG_NOTARY_INFO_ID=:id and ontd2.ACTIVE=:active and ontd2.ORG_NOTARY_TYPE=:bisatnhap )        \n" +
                    "    and ontd.ORG_NOTARY_TYPE=:vpsatnhap ";

            Query query = entityManager.createNativeQuery(hql)
                    .setParameter("id", idOrgNotaryInfo)
                    .setParameter("bisatnhap", ConstantsTccc.LOAI_TCCC.TC_SAP_NHAP)
                    .setParameter("vpsatnhap", ConstantsTccc.LOAI_TCCC.TC_DUOC_SAP_NHAP)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            db = query.getResultList();
            if (db.size() > 0 && db != null) {
                OrgNotaryInfoView view = new OrgNotaryInfoView();
                db.stream().forEach((record) -> {
                    view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                    view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                    view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                    view.setSigner(record[3] == null ? null : ((String) record[3]));
                    view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                    view.setFileName(record[5] == null ? null : ((String) record[5]));
                    view.setName(record[6] == null ? null : ((String) record[6]));
                    view.setAddress(record[7] == null ? null : ((String) record[7]));
                    view.setTel(record[8] == null ? null : ((String) record[8]));
                    view.setFax(record[9] == null ? null : ((String) record[9]));
                    view.setEmail(record[10] == null ? null : ((String) record[10]));
                    view.setWebsite(record[11] == null ? null : ((String) record[11]));
                    view.setOfficeChiefName(record[12] == null ? null : ((String) record[12]));
                    view.setSex(record[13] == null ? null : Long.parseLong(record[13].toString()));
                    view.setAddressResident(record[14] == null ? null : ((String) record[14]));
                    view.setAddressNow(record[15] == null ? null : ((String) record[15]));
                });
                return view;
            } else {
                OrgNotaryInfoView view = null;
                return view;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //Lấy chi tiết thông tin vpcc nhận hợp nhất --
    public OrgNotaryInfoView getGroupVPCC(Long idOrgNotaryInfo) {
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "SELECT doc.dispatch_code, doc.date_sign, doc.effective_date,doc.signer, doc.link_file,doc.file_name\n"
                + "FROM dm_document doc, org_notary_transfer tran, org_notary_transfer_detail de\n"
                + "where doc.id=tran.document_id and tran.id = de.org_notary_transfer_id and de.org_notary_type=6\n"
                + "and tran.type =2 and de.org_notary_info_id=:id and tran.active=0 and doc.active=0 and de.active=0\n"
                + "and tran.id in (SELECT max(id) FROM org_notary_transfer GROUP BY type)\n"
                + "and de.id in (SELECT max(id) FROM org_notary_transfer_detail GROUP BY org_notary_info_id,org_notary_type)";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));

            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    //Lấy chi tiết thông tin hợp nhất vpcc --
    public OrgNotaryInfoView getDetailGroupVPCC(Long idOrgNotaryInfo) {

        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "SELECT\n" +
                "    doc.dispatch_code,\n" +
                "    doc.date_sign,\n" +
                "    doc.effective_date,\n" +
                "    doc.signer,\n" +
                "    doc.link_file,\n" +
                "    doc.file_name,\n" +
                "    oni.name   AS orgname,\n" +
                "    decode(oni.ADDRESS_ID,null,oni.ADDRESS,oni.ADDRESS||' - '||da3.COMMUNE_NAME||' - '||da3.DISTRICT_NAME||' - '||da3.PROVINCE_NAME) as ADDRESS,\n" +
                "    oni.tel,\n" +
                "    oni.fax,\n" +
                "    oni.email,\n" +
                "    oni.website,\n" +
                "    ni.name    AS notaryname,\n" +
                "    ni.sex,\n" +
                "    DECODE(ni.address_resident_id, NULL, ni.address_resident, ni.address_resident || ' - ' || dm.commune_name || ' - ' || dm.district_name || ' - ' || dm.province_name) AS address_resident,\n" +
                "    DECODE(ni.address_now_id, NULL, ni.address_now, ni.address_now || ' - ' || dm2.commune_name || ' - '|| dm2.district_name|| ' - '|| dm2.province_name) AS address_now\n" +
                "FROM\n" +
                "    dm_document                  doc\n" +
                "    INNER JOIN org_notary_transfer          ont ON ont.document_id = doc.id\n" +
                "    INNER JOIN org_notary_transfer_detail   ontd ON ontd.org_notary_transfer_id = ont.id\n" +
                "    INNER JOIN org_notary_info              oni ON ontd.org_notary_info_id = oni.id\n" +
                "    LEFT JOIN notary_info                  ni ON oni.notary_id_office_chief = ni.id AND ni.active = :active\n" +
                "    LEFT JOIN  DM_AREA da3 on da3.ID=oni.ADDRESS_id\n" +
                "    LEFT JOIN dm_area             dm on dm.id = ni.address_resident_id\n" +
                "    LEFT JOIN dm_area             dm2 on dm2.id = ni.address_now_id\n" +
                "WHERE\n" +
                "    ont.active = :active\n" +
                "    AND doc.active = :active\n" +
                "    AND ontd.active = :active\n" +
                "    AND oni.active = :active\n" +
                "    AND ont.id IN ( SELECT ontd2.org_notary_transfer_id FROM org_notary_transfer_detail ontd2 WHERE ontd2.org_notary_info_id = :id AND ontd2.active = :active AND ontd2.org_notary_type = :bihopnhat )\n" +
                "    AND ontd.org_notary_type = :vpcchopnhat ";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("bihopnhat", ConstantsTccc.LOAI_TCCC.TC_DUOC_HOP_NHAT)
                .setParameter("vpcchopnhat", ConstantsTccc.LOAI_TCCC.TC_HOP_NHAT)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));
                view.setName(record[6] == null ? null : ((String) record[6]));
                view.setAddress(record[7] == null ? null : ((String) record[7]));
                view.setTel(record[8] == null ? null : ((String) record[8]));
                view.setFax(record[9] == null ? null : ((String) record[9]));
                view.setEmail(record[10] == null ? null : ((String) record[10]));
                view.setWebsite(record[11] == null ? null : ((String) record[11]));
                view.setOfficeChiefName(record[12] == null ? null : ((String) record[12]));
                view.setSex(record[13] == null ? null : Long.parseLong(record[13].toString()));
                view.setAddressResident(record[14] == null ? null : ((String) record[14]));
                view.setAddressNow(record[15] == null ? null : ((String) record[15]));
            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }

    }

    // Lấy ra 1 list ds đkhđ vpcc --
    public List<OrgNotaryInfoView> getPageRegisVPCC(Long idOrgNotaryInfo) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        List<OrgNotaryInfoView> listPageRegis = new ArrayList<>();

        String hql = "        SELECT\n" +
                "            ona.id,\n" +
                "            doc.dispatch_code,\n" +
                "            DECODE(ona.type, 13, TO_CHAR(ona.last_update, 'yyyyMMdd'), TO_CHAR(doc.date_sign, 'yyyyMMdd')) AS date_sign,\n" +
                "            doc.effective_date,\n" +
                "            doc.signer,\n" +
                "            doc.link_file,\n" +
                "            doc.file_name,\n" +
                "            ona.type\n" +
                "        FROM\n" +
                "            org_notary_action   ona\n" +
                "            LEFT JOIN dm_document   doc ON ona.document_id = doc.id\n" +
                "                AND doc.active = 0\n" +
                "        WHERE\n" +
                "            ona.type IN (:tuchoi,:capmoi,:thuhoi)      --,:caplai,:ghinhan \n" +
                "            AND ona.active = :active\n" +
                "            AND ona.org_notary_info_id = :orgid\n" +
                "            AND ona.id IN ( SELECT MAX(id) FROM org_notary_action GROUP BY org_notary_info_id, type)";

        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("tuchoi", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_DK_HOAT_DONG)
                .setParameter("capmoi", ConstantsTccc.TYPE_ORG_ACTION.CAP_GIAY_DK_HOAT_DONG)
                .setParameter("thuhoi", ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_DK_HOAT_DONG)
                //.setParameter("caplai", ConstantsTccc.TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG)
                //.setParameter("ghinhan", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG)
                .setParameter("orgid", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            query = entityManager.createNativeQuery(hql)
                    .setParameter("tuchoi", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_DK_HOAT_DONG)
                    .setParameter("capmoi", ConstantsTccc.TYPE_ORG_ACTION.CAP_GIAY_DK_HOAT_DONG)
                    .setParameter("thuhoi", ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_DK_HOAT_DONG)
                    //.setParameter("caplai", ConstantsTccc.TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG)
                    //.setParameter("ghinhan", ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG)
                    .setParameter("orgid", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
            db = query.getResultList();
            db.stream().forEach((record) -> {
                try {
                    OrgNotaryInfoView InfoRegis = new OrgNotaryInfoView();
                    InfoRegis.setIdOrgNotaryAction(record[0] == null ? null : Long.parseLong(record[0].toString()));
                    InfoRegis.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                    InfoRegis.setDateSign(record[2] == null ? null : dateFormat.parse((String) record[2]));
                    InfoRegis.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                    InfoRegis.setSigner(record[4] == null ? null : ((String) record[4]));
                    InfoRegis.setLinkFile(record[5] == null ? null : ((String) record[5]));
                    InfoRegis.setFileName(record[6] == null ? null : ((String) record[6]));
                    InfoRegis.setOnaType(record[7] == null ? null : Long.parseLong(record[7].toString()));
                    listPageRegis.add(InfoRegis);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            });
        }
        return listPageRegis;
    }

    //Lấy chi tiết thông tin giải thể pcc --
    public OrgNotaryInfoView getDetailDissolutionPCC(Long idOrgNotaryInfo) {
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        String hql = "select doc.DISPATCH_CODE, doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.SIGNER, doc.LINK_FILE, doc.FILE_NAME, ona.NOTE "
                + "FROM DM_DOCUMENT doc "
                + "JOIN ORG_NOTARY_ACTION ona ON ona.DOCUMENT_ID=doc.ID "
                + "WHERE ona.ACTIVE=:active and doc.ACTIVE=:active "
                + "and ona.TYPE=:onaType and ona.ORG_NOTARY_INFO_ID=:id";
        Query query = entityManager.createNativeQuery(hql)
                .setParameter("id", idOrgNotaryInfo)
                .setParameter("onaType", ConstantsTccc.TYPE_ORG_ACTION.GIAI_THE)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        if (db.size() > 0 && db != null) {
            OrgNotaryInfoView view = new OrgNotaryInfoView();
            db.stream().forEach((record) -> {
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setLinkFile(record[4] == null ? null : ((String) record[4]));
                view.setFileName(record[5] == null ? null : ((String) record[5]));
                view.setNote(record[6] == null ? null : ((String) record[6]));
            });
            return view;
        } else {
            OrgNotaryInfoView view = null;
            return view;
        }
    }

    // Lấy ra 1 list các thông tin thay đổi nội dung hoạt động
    public PagingResult getPageChangeContentActiveRegistration(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult page = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        List<OrgNotaryInfoView> listChangeAcRegisInfo = new ArrayList<>();

        String hql = "        SELECT\n" +
                "            ona.id,\n" +
                "            doc.dispatch_code,\n" +
                "            decode(ona.type,  13,ona.LAST_UPDATE,  15,doc.date_sign) as date_sign ,\n" +
                "            doc.effective_date,\n" +
                "            doc.signer,\n" +
                "            doc.link_file,\n" +
                "            doc.file_name,\n" +
                "            decode(ona.type,  13,'Ghi nhận thay đổi',  15,'Cấp lại giấy ĐKHĐ',    '') as caseText      \n" +
                "        FROM\n" +
                "            org_notary_action   ona\n" +
                "            LEFT JOIN dm_document         doc ON ona.document_id = doc.id\n" +
                "        WHERE\n" +
                "            ona.type IN (:onatype)\n" +
                "            AND ona.active = :active\n" +
                "            AND (doc.active = :active or doc.active is null) \n" +
                "            AND ona.org_notary_info_id = :orgid";

        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("onatype", Arrays.asList(new Long[]{ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG, ConstantsTccc.TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG}))
                .setParameter("orgid", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("onatype", Arrays.asList(new Long[]{ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG, ConstantsTccc.TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG}))
                    .setParameter("orgid", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
            ;
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView InfoChangeAcRegis = new OrgNotaryInfoView();
                InfoChangeAcRegis.setIdOrgNotaryAction(record[0] == null ? null : Long.parseLong(record[0].toString()));
                InfoChangeAcRegis.setDispatchCode(record[1] == null ? null : ((String) record[1]));
                InfoChangeAcRegis.setDateSign(record[2] == null ? null : ((Date) record[2]));
                InfoChangeAcRegis.setEffectiveDate(record[3] == null ? null : ((Date) record[3]));
                InfoChangeAcRegis.setSigner(record[4] == null ? null : ((String) record[4]));
                InfoChangeAcRegis.setLinkFile(record[5] == null ? null : ((String) record[5]));
                InfoChangeAcRegis.setFileName(record[6] == null ? null : ((String) record[6]));
                InfoChangeAcRegis.setActionName(record[7] == null ? null : ((String) record[7]));
                listChangeAcRegisInfo.add(InfoChangeAcRegis);
            });
        }
        page.setItems(listChangeAcRegisInfo);
        page.setRowCount(rowCount);
        return page;
    }

    // Lấy ra 1 list ds chuyển nhượng
    public PagingResult getTransferOffice(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult result = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();

        List<OrgNotaryInfoView> listTransferOffice = new ArrayList<>();
        String hql = "SELECT\n"
                + "            doc.dispatch_code,\n"
                + "            doc.date_sign,\n"
                + "            doc.effective_date,\n"
                + "            doc.signer,\n"
                + "            (select RTRIM(XMLAGG(XMLELEMENT(E,oni2.name,',').EXTRACT('//text()') ORDER BY oni2.name).GetClobVal(),',') from org_notary_action_detail ontd, notary_info oni2 where ontd.notary_info_id = oni2.id and ontd.org_notary_action_id = ac.id and ontd.active=0) as notaryName\n"
                + "        FROM\n"
                + "            org_notary_action ac, org_notary_action_detail detail, dm_document doc,\n"
                + "            org_notary_info org\n"
                + "            where org.id=:orgId and org.id=ac.org_notary_info_id and ac.document_id=doc.id and detail.org_notary_action_id=ac.id\n"
                + "            and ac.type=7 and ac.active=0 and detail.active=0 and doc.active=0 and ac.id in (SELECT max(id) FROM org_notary_action GROUP BY org_notary_info_id,type)\n"
                + "            and detail.id in (SELECT max(id) FROM org_notary_action_detail GROUP BY org_notary_action_id)\n";
//                + "            AND org.administration_id IN (SELECT stat.id FROM dm_administration stat START WITH stat.id = :idadminislogin CONNECT BY PRIOR stat.id = stat.parent_id)";

        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("orgId", idOrgNotaryInfo);
//                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.parseLong(count.toString());
        if (rowCount > 0L) {
            hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("orgId", idOrgNotaryInfo)
            //                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC)
            ;
            db = query.getResultList();
            db.stream().forEach((record) -> {
                OrgNotaryInfoView view = new OrgNotaryInfoView();
                view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                view.setEffectiveDate(record[2] == null ? null : ((Date) record[2]));
                view.setSigner(record[3] == null ? null : ((String) record[3]));
                view.setName(record[4] == null ? null : H.convertClobToString((java.sql.Clob) record[4]));

                listTransferOffice.add(view);
            });
        }
        result.setItems(listTransferOffice);
        result.setRowCount(rowCount);
        return result;
    }

    public Long checkOrgType(Long idOrg) {
        Long typeOrg = null;
        try {
            OrgNotaryInfo org = entityManager.find(OrgNotaryInfo.class, idOrg);
            typeOrg = org.getType();
        } catch (Exception e) {

        }
        return typeOrg;
    }

    private Object loadDataPieChartForLoaiCongChung(Long cityCode, String fromDateRaw, String toDateRaw) {
        PagingResult dataRaw = notaryActivityService.aggregateByStp(fromDateRaw, toDateRaw, String.valueOf(cityCode), 1, 1000000, null, null, null, false).getBody().getData();
        List<Reaport> items = dataRaw.getItems();
        if(!H.isTrue(items)) return null;
        Reaport reaportTotal = items.get(0);
        Long totalSum = Long.parseLong(reaportTotal.getCol_5()) + Long.parseLong(reaportTotal.getCol_6());
        String precent1 = String.valueOf(Math.round((Long.parseLong(reaportTotal.getCol_5()) * 100.0 / totalSum) * 100.0) / 100.0);
        String precent2 = String.valueOf(Math.round((Long.parseLong(reaportTotal.getCol_6()) * 100.0 / totalSum) * 100.0) / 100.0);


        List<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Công chứng hợp đồng, giao dịch");
        map.put("value", Arrays.asList(precent1, reaportTotal.getCol_5()));
        data.add(map);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("name", "Công chứng bản dịch và các việc khác");
        map1.put("value", Arrays.asList(precent2, reaportTotal.getCol_6()));
        data.add(map1);
        return data;
    }

    private Object loadDataPieChartForAppointCCV(Long cityCode, String fromDateRaw, String toDateRaw) {
        String cityCodeStr = cityCode == null ? null : cityCode.toString();
        PagingResult listAppoint = reportOperationSuggestAppoint(fromDateRaw, toDateRaw, cityCodeStr, null, 1, 1000000, false).getBody().getData();
        List<Reaport> itemsAppoint = listAppoint.getItems();
        Reaport reaportTotalAppoint = itemsAppoint.get(0);
        //làm trong đến 2 số thập phân
        Long totalSumAppoint = Long.parseLong(reaportTotalAppoint.getCol_2()) + Long.parseLong(reaportTotalAppoint.getCol_3()) + Long.parseLong(reaportTotalAppoint.getCol_4()) + Long.parseLong(reaportTotalAppoint.getCol_5()) + Long.parseLong(reaportTotalAppoint.getCol_6()) + Long.parseLong(reaportTotalAppoint.getCol_7()) + Long.parseLong(reaportTotalAppoint.getCol_8());
        String precentAppoint2 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_2()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint3 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_3()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint4 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_4()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint5 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_5()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint6 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_6()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint7 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_7()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        String precentAppoint8 = String.valueOf(Math.round((Long.parseLong(reaportTotalAppoint.getCol_8()) * 100.0 / totalSumAppoint) * 100.0) / 100.0);
        List<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", "Số lượng Người tập sự");
        map2.put("value", Arrays.asList(precentAppoint2, reaportTotalAppoint.getCol_2()));
        data.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("name", "Số lượng Người đề nghị bổ nhiệm CCV");
        map3.put("value", Arrays.asList(precentAppoint3, reaportTotalAppoint.getCol_3()));
        data.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("name", "Số lượng Người đề nghị miễn nhiệm CCV");
        map4.put("value", Arrays.asList(precentAppoint4, reaportTotalAppoint.getCol_4()));
        data.add(map4);
        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("name", "Số lượng Người đề nghị bổ nhiệm lại CCV");
        map5.put("value", Arrays.asList(precentAppoint5, reaportTotalAppoint.getCol_5()));
        data.add(map5);
        HashMap<String, Object> map6 = new HashMap<>();
        map6.put("name", "Số lượng bổ nhiệm CCV");
        map6.put("value", Arrays.asList(precentAppoint6, reaportTotalAppoint.getCol_6()));
        data.add(map6);
        HashMap<String, Object> map7 = new HashMap<>();
        map7.put("name", "Số lượng miễn nhiệm CCV");
        map7.put("value", Arrays.asList(precentAppoint7, reaportTotalAppoint.getCol_7()));
        data.add(map7);
        HashMap<String, Object> map8 = new HashMap<>();
        map8.put("name", "Số lượng bổ nhiệm lại CCV");
        map8.put("value", Arrays.asList(precentAppoint8, reaportTotalAppoint.getCol_8()));
        data.add(map8);
        return data;

    }


}
