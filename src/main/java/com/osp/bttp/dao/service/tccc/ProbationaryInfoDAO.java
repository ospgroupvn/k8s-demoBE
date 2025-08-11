/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.service.tccc;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.CallAddNotary;
import com.osp.bttp.dao.model.entity.db1.*;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.ProbationaryInfoView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class ProbationaryInfoDAO {

    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;
    private Logger logger = LogManager.getLogger(ProbationaryInfoDAO.class);

    @Autowired
    NotaryInfoHisDAO hisDAO;

    public ProbationaryInfoDetail findDetailById(Long id) {
        ProbationaryInfoDetail probationaryInfo = entityManager.find(ProbationaryInfoDetail.class, id);
        return probationaryInfo;
    }

    public ProbationaryInfo findById(Long id) {
        ProbationaryInfo probationaryInfo = entityManager.find(ProbationaryInfo.class, id);
        return probationaryInfo;
    }

    // lấy thông tin đăng ký tập sự
    public ProbationaryInfo getById(Long id) {
        try {
            ProbationaryInfo probationaryInfo = entityManager.createQuery("SELECT pro FROM ProbationaryInfo pro where pro.id=:id and pro.status=1 and pro.active=0 ", ProbationaryInfo.class)
                    .setParameter("id", id).getSingleResult();
            return probationaryInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PagingResult searchDetail(Long idNotary, Long status, int tab, boolean count, AccUser user) {
        PagingResult result = new PagingResult();

        List<ProbationaryInfoView> items = new ArrayList<>();
        List<Object[]> db = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        Long rowCount = 0L;

        try {
            NotaryInfo info = entityManager.find(NotaryInfo.class, idNotary);

            //1 : đăng kí tập sự
            if (tab == 1) {

                String hql = "SELECT\n" +
                        "    doc.dispatch_code, doc.date_sign, org.name, detail.date_start, detail.date_end, pro.date_number, detail.created_by, detail.gen_date, detail.updated_by, detail.last_update, doc.file_name, doc.link_file, pro.notary_info_id, org.id AS orgid,\n" +
                        "    DECODE(org.address_id, NULL, org.address, org.address|| ' - '||dm.commune_name||' - '|| dm.district_name|| ' - '|| dm.province_name) AS orgaddress,     \n" +
                        "    (select NAME FROM notary_info WHERE id=pro.notary_tutorial_id) as notary_tutorial_id,           \n" +
                        "    pro.ID as proId, pro.status            \n" +
                        "FROM\n" +
                        "    probationary_info_detail   detail,\n" +
                        "    probationary_info          pro,\n" +
                        "    org_notary_info            org,\n" +
                        "    dm_document                doc,\n" +
                        "    dm_area                    dm\n" +
                        "WHERE\n" +
                        "    detail.probationary_info_id = pro.id\n" +
                        "    AND detail.org_notary_info_id = org.id\n" +
                        "    AND pro.document_certificate_id = doc.id\n" +
                        "    AND ( dm.id = org.address_id OR org.address_id IS NULL )   \n" +
                        "    AND detail.active = :active\n" +
                        "    AND org.active = :active\n" +
                        "    AND doc.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND detail.status = :status\n" +
                        "    AND pro.notary_info_id = :idnotary\n" +
                        "    AND detail.org_notary_info_to IS NULL\n" +
                        //"     and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) " +
                        "UNION\n" +
                        "SELECT\n" +
                        "    doc.dispatch_code, doc.date_sign, org.name, pro.date_start, pro.date_end, pro.date_number, pro.created_by, pro.gen_date, pro.updated_by, pro.last_update, doc.file_name, doc.link_file, pro.notary_info_id, org.id AS orgid,\n" +
                        "    DECODE(org.address_id, NULL, org.address, org.address||' - '||dm.commune_name||' - '|| dm.district_name||' - '|| dm.province_name) AS orgaddress,      \n" +
                        "    (select NAME FROM notary_info WHERE id=pro.notary_tutorial_id) as notary_tutorial_id,           \n" +
                        "    pro.ID as proId, pro.status            \n" +
                        "FROM\n" +
                        "    probationary_info   pro,\n" +
                        "    org_notary_info     org,\n" +
                        "    dm_document         doc,\n" +
                        "    dm_area             dm\n" +
                        "WHERE\n" +
                        "    pro.org_notary_info_id = org.id\n" +
                        "    AND pro.document_certificate_id = doc.id\n" +
                        "    AND ( dm.id = org.address_id\n" +
                        "          OR org.address_id IS NULL )\n" +
                        "    AND org.active = :active\n" +
                        "    AND doc.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND pro.status = :status\n" +
                        "    AND pro.notary_info_id = :idnotary\n" +
                        "    AND pro.org_notary_info_to IS NULL";
                //"     and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) ";

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        .setParameter("status", status)
                        .setParameter("idnotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                /*.setParameter("idAdminisLogin", user.getAdministrationId());*/

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L) {
                    /*trường hợp tiếp tục tập sự sẽ có 2 bản ghi trở lên*/
                    rowCount = 1L;

                    int offset = 0;
                    int size = 1;
                    hql = UtilData.paginationOracle(hql, offset, size);

                    if (!count) {
                        query = entityManager.createNativeQuery(hql)
                                .setParameter("status", status)
                                .setParameter("idnotary", idNotary)
                                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                                /*.setParameter("idAdminisLogin", user.getAdministrationId())*/
//                                .setFirstResult(0).setMaxResults(1);

                        db = query.getResultList();

                        db.stream().forEach((record) -> {
                            ProbationaryInfoView view = new ProbationaryInfoView();

                            view.setDispatchCode(record[0] == null ? null : ((String) record[0]));
                            view.setDateSign(record[1] == null ? null : ((Date) record[1]));
                            view.setNameOrgNotaryInfo(record[2] == null ? null : ((String) record[2]));
                            view.setDateStart(record[3] == null ? null : ((Date) record[3]));
                            view.setDateEnd(record[4] == null ? null : ((Date) record[4]));
                            view.setDateNumber(record[5] == null ? null : Long.parseLong((record[5]).toString()));
                            view.setCreatedBy(record[6] == null ? null : ((String) record[6]));
                            view.setGenDate(record[7] == null ? null : ((Date) record[7]));
                            view.setUpdatedBy(record[8] == null ? null : ((String) record[8]));
                            view.setLastUpdate(record[9] == null ? null : ((Date) record[9]));
                            view.setFileName(record[10] == null ? null : ((String) record[10]));
                            view.setLinkFile(record[11] == null ? null : ((String) record[11]));
                            view.setNotaryInfoId(record[12] == null ? null : Long.parseLong((record[12]).toString()));
                            view.setOrgNotaryInfoId(record[13] == null ? null : Long.parseLong((record[13]).toString()));
                            view.setAddress(record[14] == null ? null : ((String) record[14]));
                            view.setName_ntaryInfo(record[15] == null ? null : ((String) record[15]));
                            view.setId(record[16] == null ? null : Long.parseLong((record[16]).toString()));
                            view.setStatus(record[17] == null ? null : Long.parseLong((record[17]).toString()));

                            items.add(view);
                        });
                    }
                }
            }

            //2 : thay đổi nơi tập sự
            if (tab == 2) {

                String hql = "SELECT\n" +
                        "    org.name, detail.date_start, detail.date_end, pro.date_number, detail.created_by, detail.gen_date, detail.updated_by, detail.last_update, pro.notary_info_id, detail.note, org.id AS orgid,\n" +
                        "    ( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS admname\n" +
                        "FROM\n" +
                        "    probationary_info_detail   detail,\n" +
                        "    probationary_info          pro,\n" +
                        "    org_notary_info            org\n" +
                        "WHERE\n" +
                        "    detail.probationary_info_id = pro.id\n" +
                        "    AND detail.org_notary_info_to = org.id\n" +
                        "    AND org.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND detail.active = :active\n" +
                        "    AND detail.status IN (1,15)\n" +
                        "    AND pro.notary_info_id = :idnotary\n" +
                        "    AND detail.org_notary_info_to IS NOT NULL\n" +
                        //"     and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) " +
                        "UNION\n" +
                        "SELECT\n" +
                        "    org.name, pro.date_start, pro.date_end, pro.date_number, pro.created_by, pro.gen_date, pro.updated_by, pro.last_update, pro.notary_info_id, pro.note, org.id AS orgid, \n" +
                        "    ( SELECT full_name FROM dm_administration WHERE id = org.administration_id ) AS admname\n" +
                        "FROM\n" +
                        "    probationary_info   pro,\n" +
                        "    org_notary_info     org\n" +
                        "WHERE\n" +
                        "    pro.org_notary_info_to = org.id\n" +
                        "    AND org.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND pro.status IN (1,15)\n" +
                        "    AND pro.notary_info_id = :idnotary\n" +
                        "    AND pro.org_notary_info_to IS NOT NULL";
                //"     and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) "

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        /*.setParameter("status", status)*/
                        .setParameter("idnotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
//                        .setParameter("idAdminisLogin", user.getAdministrationId());

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql + " order by GEN_DATE desc ")
                            /*.setParameter("status", status)*/
                            .setParameter("idnotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
//                            .setParameter("idAdminisLogin", user.getAdministrationId());

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        ProbationaryInfoView view = new ProbationaryInfoView();

                        view.setNameOrgNotaryInfo(record[0] == null ? null : ((String) record[0]));
                        view.setDateStart(record[1] == null ? null : ((Date) record[1]));
                        view.setDateEnd(record[2] == null ? null : ((Date) record[2]));
                        view.setDateNumber(record[3] == null ? null : Long.parseLong((record[3]).toString()));
                        view.setCreatedBy(record[4] == null ? null : ((String) record[4]));
                        view.setGenDate(record[5] == null ? null : ((Date) record[5]));
                        view.setUpdatedBy(record[6] == null ? null : ((String) record[6]));
                        view.setLastUpdate(record[7] == null ? null : ((Date) record[7]));
                        view.setNotaryInfoId(record[8] == null ? null : Long.parseLong((record[8]).toString()));
                        view.setNote(record[9] == null ? null : ((String) record[9]));
                        view.setOrgNotaryInfoId(record[10] == null ? null : Long.parseLong((record[10]).toString()));
                        view.setNameAdmin(record[11] == null ? null : ((record[11]).toString()));

                        items.add(view);
                    });
                }
            }

            // 3: tạm ngừng
            if (tab == 3) {

                String hql = "SELECT\n" +
                        "    org.name, detail.date_start, detail.date_end, pro.date_number, detail.created_by, detail.gen_date, detail.updated_by, detail.last_update, pro.notary_info_id, detail.note\n" +
                        "FROM\n" +
                        "    probationary_info_detail   detail,\n" +
                        "    probationary_info          pro,\n" +
                        "    org_notary_info            org\n" +
                        "WHERE\n" +
                        "    detail.probationary_info_id = pro.id\n" +
                        "    AND pro.org_notary_info_id = org.id\n" +
                        "    AND org.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND detail.active = :active\n" +
                        "    AND detail.status = :status\n" +
                        "    AND pro.notary_info_id = :idnotary\n" +
                        //"     and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) " +
                        "UNION\n" +
                        "SELECT\n" +
                        "    org.name, pro.date_start, pro.date_end, pro.date_number, pro.created_by, pro.gen_date, pro.updated_by, pro.last_update, pro.notary_info_id, pro.note\n" +
                        "FROM\n" +
                        "    probationary_info   pro,\n" +
                        "    org_notary_info     org\n" +
                        "WHERE\n" +
                        "    pro.org_notary_info_id = org.id\n" +
                        "    AND org.active = :active\n" +
                        "    AND pro.active = :active\n" +
                        "    AND pro.status = :status\n" +
                        "    AND pro.notary_info_id = :idnotary";
                //"and org.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) "

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        .setParameter("status", status)
                        .setParameter("idnotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                //.setParameter("idAdminisLogin", user.getAdministrationId());

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("status", status)
                            .setParameter("idnotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
                    //.setParameter("idAdminisLogin", user.getAdministrationId());

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        ProbationaryInfoView view = new ProbationaryInfoView();

                        view.setNameOrgNotaryInfo(record[0] == null ? null : ((String) record[0]));
                        view.setDateStart(record[1] == null ? null : ((Date) record[1]));
                        view.setDateEnd(record[2] == null ? null : ((Date) record[2]));
                        view.setDateNumber(record[3] == null ? null : Long.parseLong((record[3]).toString()));
                        view.setCreatedBy(record[4] == null ? null : ((String) record[4]));
                        view.setGenDate(record[5] == null ? null : ((Date) record[5]));
                        view.setUpdatedBy(record[6] == null ? null : ((String) record[6]));
                        view.setLastUpdate(record[7] == null ? null : ((Date) record[7]));
                        view.setNotaryInfoId(record[8] == null ? null : Long.parseLong((record[8]).toString()));
                        view.setNote(record[9] == null ? null : ((String) record[9]));

                        items.add(view);
                    });
                }
            }

            // 4: chấm dứt , 5 : hoàn thành
            if (tab == 4 || tab == 5) {

                String hql =
                        "SELECT\n" +
                                "    org.name, pro.date_start, pro.date_end, pro.date_number, pro.created_by, pro.gen_date, pro.updated_by, pro.last_update, pro.notary_info_id, pro.note \n" +
                                "    ,doc.DISPATCH_CODE,doc.DATE_SIGN,doc.SIGNER,doc.UNIT_SIGN,doc.LINK_FILE,doc.FILE_NAME,doc.EFFECTIVE_DATE \n" +
                                "FROM\n" +
                                "    probationary_info   pro\n" +
                                "    INNER JOIN org_notary_info     org on pro.org_notary_info_id = org.id\n" +
                                "    LEFT JOIN dm_document doc          on pro.DOCUMENT_ID=doc.ID\n" +
                                "WHERE\n" +
                                "    org.active = :active\n" +
                                "    AND pro.active = :active\n" +
                                "    AND pro.status = :status\n" +
                                "    AND pro.notary_info_id = :idnotary ";

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        .setParameter("status", status)
                        .setParameter("idnotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("status", status)
                            .setParameter("idnotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        ProbationaryInfoView view = new ProbationaryInfoView();

                        int i=0;

                        view.setNameOrgNotaryInfo(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setDateStart(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setDateEnd(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setDateNumber(record[i] == null ? null : Long.parseLong((record[i]).toString()));      i++;
                        view.setCreatedBy(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setGenDate(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setUpdatedBy(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setLastUpdate(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setNotaryInfoId(record[i] == null ? null : Long.parseLong((record[i]).toString()));      i++;
                        view.setNote(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setDispatchCode(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setDateSign(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setSigner(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setUnitSign(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setLinkFile(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setFileName(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setEffectiveDate(record[i] == null ? null : ((Date) record[i]));      i++;

                        items.add(view);
                    });
                }
            }

            //thay đổi ccv hướng dẫn
            if(tab == 6) {
                String hql = "select " +
                        "    tota.name as name1,\n" +
                        "    org.name as name2,\n" +
                        "    detail.gen_date,\n" +
                        "    detail.note, org.ID " +
                        " from notary_info info \n" +
                        "\n" +
                        "    LEFT JOIN probationary_info pro on pro.notary_info_id=info.ID\n" +
                        "    LEFT JOIN probationary_info_detail detail on detail.PROBATIONARY_INFO_ID=pro.ID\n" +
                        "    LEFT JOIN notary_info tota on detail.NOTARY_TUTORIAL_ID=tota.ID\n" +
                        "    LEFT JOIN org_notary_info org on org.ID=detail.ORG_NOTARY_INFO_ID \n" +
                        "    \n" +
                        "WHERE info.active=0 and pro.active=0 and detail.active=0 and detail.STATUS=:status and info.ID=:idNotary ";

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        .setParameter("status", ConstantsTccc.STATUS_PROBATIONARYINFO.THAY_DOI_CCV_HUONG_DAN)
                        .setParameter("idNotary", idNotary);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("status", ConstantsTccc.STATUS_PROBATIONARYINFO.THAY_DOI_CCV_HUONG_DAN)
                            .setParameter("idNotary", idNotary);

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        ProbationaryInfoView view = new ProbationaryInfoView();

                        view.setName_ntaryInfo(record[0] == null ? null : ((String) record[0]));
                        view.setNameOrgNotaryInfo(record[1] == null ? null : ((String) record[1]));
                        view.setGenDate(record[2] == null ? null : ((Date) record[2]));
                        view.setNote(record[3] == null ? null : ((String) record[3]));
                        view.setOrgNotaryInfoId(record[4] == null ? null : Long.parseLong(record[4].toString()));

                        items.add(view);
                    });
                }
            }

            //đạt kết quả tập sự
            if (tab == 7) {

                String hql =
                        "SELECT\n" +
                                "    doc.dispatch_code, doc.date_sign, doc.file_name, doc.link_file, org.name, pro.date_start, pro.date_end, pro.date_number, pro.created_by, pro.gen_date, pro.updated_by, pro.last_update, pro.notary_info_id, pro.note\n" +
                                "FROM\n" +
                                "    probationary_info   pro,\n" +
                                "    org_notary_info     org,\n" +
                                "    dm_document         doc\n" +
                                "WHERE\n" +
                                "    pro.org_notary_info_id = org.id\n" +
                                "    AND pro.document_id = doc.id\n" +
                                "    AND org.active = :active\n" +
                                "    AND pro.active = :active\n" +
                                "    AND doc.active = :active\n" +
                                "    AND pro.status = :status\n" +
                                "    AND pro.notary_info_id = :idnotary";

                Query query = entityManager.createNativeQuery("select count(*) from (" + hql + ") bf ")
                        .setParameter("status", ConstantsTccc.NOTARY_STATUS.HOAN_THANH_TAP_SU)
                        .setParameter("idnotary", idNotary)
                        .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                BigDecimal count_ = (BigDecimal) query.getSingleResult();
                rowCount = Long.parseLong(count_.toString());

                if (rowCount > 0L && !count) {
                    query = entityManager.createNativeQuery(hql)
                            .setParameter("status", ConstantsTccc.NOTARY_STATUS.HOAN_THANH_TAP_SU)
                            .setParameter("idnotary", idNotary)
                            .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

                    db = query.getResultList();

                    db.stream().forEach((record) -> {
                        ProbationaryInfoView view = new ProbationaryInfoView();

                        int i=0;

                        view.setDispatchCode(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setDateSign(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setFileName(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setLinkFile(record[i] == null ? null : ((String) record[i]));      i++;

                        view.setNameOrgNotaryInfo(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setDateStart(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setDateEnd(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setDateNumber(record[i] == null ? null : Long.parseLong((record[i]).toString()));      i++;
                        view.setCreatedBy(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setGenDate(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setUpdatedBy(record[i] == null ? null : ((String) record[i]));      i++;
                        view.setLastUpdate(record[i] == null ? null : ((Date) record[i]));      i++;
                        view.setNotaryInfoId(record[i] == null ? null : Long.parseLong((record[i]).toString()));      i++;
                        view.setNote(record[i] == null ? null : ((String) record[i]));      i++;

                        items.add(view);
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Loi tai ProbationaryInfoDAO.searchDetail" + e.getMessage());
        }

        result.setRowCount(rowCount);
        result.setItems(items);

        return result;
    }


}
