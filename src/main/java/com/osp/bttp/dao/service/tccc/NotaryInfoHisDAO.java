package com.osp.bttp.dao.service.tccc;

import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.dao.model.entity.db1.*;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.mview.db1.NotaryInfoView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class NotaryInfoHisDAO {

    private Logger logger = LogManager.getLogger(NotaryInfoHisDAO.class);
    @PersistenceContext(unitName = "db1")
    private EntityManager entityManager;

    public Long add(NotaryInfoHis item) {
        entityManager.persist(item);
        entityManager.flush();
        return item.getId();
    }

    public Long edit(NotaryInfoHis item) {
        entityManager.merge(item);
        entityManager.flush();
        return item.getId();
    }

    public NotaryInfoHis getById(Long id) {
        NotaryInfoHis item = entityManager.find(NotaryInfoHis.class, id);
        return item;
    }

    public NotaryInfoHis getHisByOption(Long id, Long idObj, String typeAction, AccUser user) {
        try {
            String sql = "select his from NotaryInfoHis his where his.typeAction=:typeAction and his.objId=:objId and his.id=:id " +
                    "and his.idHis in (select max(idHis) from NotaryInfoHis where typeAction=:typeAction and objId=:objId and id=:id and active=0) ";
            NotaryInfoHis his = null;
            Query query = entityManager.createQuery(sql, NotaryInfoHis.class)
                .setParameter("objId", idObj)
                .setParameter("id", id)
                .setParameter("typeAction", typeAction);

            List<Object> objNotarys = query.getResultList();
            if (objNotarys != null && objNotarys.size()>0) {
                his = (NotaryInfoHis) objNotarys.get(0);
            }
            return his;
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            return null;
        }
    }

    public boolean deleteHis(Long id, Long idObj, String typeAction, AccUser user) {
        try {
            String sql = "select his from NotaryInfoHis his where his.typeAction=:typeAction and his.objId=:objId and his.id=:id " +
                    "and his.idHis in (select max(idHis) from NotaryInfoHis where typeAction=:typeAction and objId=:objId and id=:id and active=0) ";

            //xóa trong lịch sử
            Query query = entityManager.createQuery(sql, NotaryInfoHis.class)
                    .setParameter("objId", idObj)
                    .setParameter("id", id)
                    .setParameter("typeAction", typeAction);
            List<Object> objNotarys = query.getResultList();
            if (objNotarys != null && objNotarys.size()>0) {
                NotaryInfoHis his = (NotaryInfoHis) objNotarys.get(0);
                his.setLastUpdate(new Date());
                his.setUpdatedBy(user.getUsername());
                his.setActive(ConstantsTccc.ACTIVE.HET_HIEU_LUC);
                entityManager.merge(his);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            return false;
        }
    }

    //tất cả các trường
    public NotaryInfoHis formToBo(NotaryInfo form, NotaryInfoHis bo) {

        if (form.getId() != null && form.getId() != -1L) {
            bo.setId(form.getId());
        }
        if (form.getName() != null && !"".equals(form.getName())) {
            bo.setName(form.getName());
        }
        if (form.getSex() != null && form.getSex() != -1L) {
            bo.setSex(form.getSex());
        }
        if (form.getBirthDay() != null) {
            bo.setBirthDay(form.getBirthDay());
        }
        if (form.getIdNo() != null && !"".equals(form.getIdNo())) {
            bo.setIdNo(form.getIdNo());
        }
        if (form.getIdNoDate() != null) {
            bo.setIdNoDate(form.getIdNoDate());
        }
        if (form.getAddressIdNo() != null && !"".equals(form.getAddressIdNo())) {
            bo.setAddressIdNo(form.getAddressIdNo());
        }
        if (form.getAddressResident() != null && !"".equals(form.getAddressResident())) {
            bo.setAddressResident(form.getAddressResident());
        }
        if (form.getAddressResidentId() != null && form.getAddressResidentId() != -1L) {
            bo.setAddressResidentId(form.getAddressResidentId());
        }
        if (form.getAddressNow() != null && !"".equals(form.getAddressNow())) {
            bo.setAddressNow(form.getAddressNow());
        }
        if (form.getAddressNowId() != null && form.getAddressNowId() != -1L) {
            bo.setAddressNowId(form.getAddressNowId());
        }
        if (form.getStatus() != null && form.getStatus() != -1L) {
            bo.setStatus(form.getStatus());
        }
        if (form.getPhoneNumber() != null && !"".equals(form.getPhoneNumber())) {
            bo.setPhoneNumber(form.getPhoneNumber());
        }
        if (form.getEmail() != null && !"".equals(form.getEmail())) {
            bo.setEmail(form.getEmail());
        }
        if (form.getNote() != null && !"".equals(form.getNote())) {
            bo.setNote(form.getNote());
        }
        if (form.getActive() != null && form.getActive() != -1L) {
            bo.setActive(form.getActive());
        }
        if (form.getCreatedBy() != null && !"".equals(form.getCreatedBy())) {
            bo.setCreatedBy(form.getCreatedBy());
        }
        if (form.getUpdatedBy() != null && !"".equals(form.getUpdatedBy())) {
            bo.setUpdatedBy(form.getUpdatedBy());
        }
        if (form.getGenDate() != null) {
            bo.setGenDate(form.getGenDate());
        }
        if (form.getLastUpdate() != null) {
            bo.setLastUpdate(form.getLastUpdate());
        }

        return bo;
    }

    //duyệt ccv lịch sử
    public boolean setStatusByDatesign(Long idNotary, AdmUser user, boolean duyet) {
        try{
            NotaryInfo info = entityManager.find(NotaryInfo.class, idNotary);
            entityManager.flush();

            String sql = "select * from (\n" +
                    "\n" +
                    "    -- đang tập sự     \n" +
                    "    select doc.DATE_SIGN, 1 as status from notary_info info\n" +
                    "        LEFT JOIN probationary_info pro on pro.notary_info_id = info.id and pro.active=0 and pro.STATUS=1\n" +
                    "            and pro.id in (SELECT max(id) from probationary_info WHERE NOTARY_INFO_ID=info.ID and ACTIVE=0 GROUP BY NOTARY_INFO_ID) \n" +
                    "        LEFT JOIN dm_document doc on doc.ID=pro.DOCUMENT_CERTIFICATE_ID and doc.active=0\n" +
                    "    WHERE doc.DATE_SIGN is not null and info.active=0 and info.ID=:id\n" +
                    "    \n" +
                    "    UNION ALL \n" +
                    "    \n" +
                    "    -- chấm dứt or hoàn thành  \n" +
                    "    select doc.DATE_SIGN, decode(pro.STATUS , 3,3 ,25) as status from notary_info info  \n" +
                    "        LEFT JOIN probationary_info pro on pro.notary_info_id = info.id and pro.active=0 and pro.STATUS in (3,4)   \n" +
                    "            and pro.id in (SELECT max(id) from probationary_info WHERE NOTARY_INFO_ID=info.ID and ACTIVE=0 GROUP BY NOTARY_INFO_ID) \n" +
                    "        LEFT JOIN dm_document doc on doc.ID=pro.DOCUMENT_ID and doc.active=0   \n" +
                    "    WHERE doc.DATE_SIGN is not null and info.active=0 and info.ID=:id  \n" +
                    "    \n" +
                    "    UNION ALL \n" +
                    "    \n" +
                    "    --bổ nhiệm CCV       \n" +
                    "    select doc.DATE_SIGN, 7 as status from notary_info info\n" +
                    "        LEFT JOIN notary_appoint apo  on apo.NOTARY_INFO_ID = info.ID  and apo.ACTIVE=0 and apo.TYPE_APPOINT=1  \n" +
                    "             and apo.id in (SELECT max(id) from notary_appoint WHERE ACTIVE=0 and apo.TYPE_APPOINT=1 GROUP BY NOTARY_INFO_ID)  \n" +
                    "        LEFT JOIN dm_document  doc on apo.document_id = doc.id  and doc.ACTIVE=0\n" +
                    "    WHERE doc.DATE_SIGN is not null and info.active=0 and info.ID=:id\n" +
                    "    \n" +
                    "    UNION ALL \n" +
                    "    \n" +
                    "    --miễn nhiệm       \n" +
                    "    select doc.DATE_SIGN, 11 as status from notary_info info\n" +
                    "        LEFT JOIN notary_dismissed   dis on dis.notary_info_id = info.id AND dis.active = 0 \n" +
                    "        LEFT JOIN dm_document        doc on dis.document_id = doc.id AND doc.active = 0\n" +
                    "    WHERE doc.DATE_SIGN is not null and info.active=0 and info.ID=:id\n" +
                    "    \n" +
                    "    UNION ALL \n" +
                    "    \n" +
                    "    --bổ nhiệm lại     \n" +
                    "    select doc.DATE_SIGN, 10 as status from notary_info info\n" +
                    "        LEFT JOIN notary_reapppointed   re on re.notary_info_id=info.ID AND re.active = 0\n" +
                    "        LEFT JOIN dm_document           doc on re.document_id = doc.id  AND doc.active = 0\n" +
                    "    WHERE doc.DATE_SIGN is not null and info.active=0 and info.ID=:id\n" +
                    "    \n" +
                    "    union all\n" +
                    "    \n" +
                    "    --đăng ký hành nghề        \n" +
                    "    select doc.DATE_SIGN, 8 as status from notary_info info\n" +
                    "        LEFT JOIN NOTARY_REG_PRACTICE prac  on prac.NOTARY_INFO_ID=info.ID AND prac.active = 0 and prac.status=1\n" +
                    "        LEFT JOIN dm_document           doc on prac.DOCUMENT_ID = doc.ID  AND doc.active = 0\n" +
                    "    WHERE info.active=0 and info.ID=:id\n" +
                    "    \n" +
                    "    UNION all \n" +
                    "    \n" +
                    "    --tạm đình chỉ     \n" +
                    "     select doc.DATE_SIGN, 9 as status  FROM notary_suspend_work   work \n" +
                    "        LEFT JOIN notary_info  info on  work.notary_info_id = info.id AND info.active=0  \n" +
                    "        LEFT JOIN org_notary_info       org on work.org_notary_id = org.id AND org.active=0 \n" +
                    "        LEFT JOIN dm_document           doc on work.document_id = doc.id AND doc.active=0 \n" +
                    "        LEFT JOIN notary_reg_practice   prac on prac.notary_info_id = info.id \n" +
                    "     WHERE\n" +
                    "        doc.DATE_SIGN is not null and\n" +
                    "        info.id=:id                    \n" +
                    "        AND prac.status=2                    \n" +
                    "        AND work.active =0 \n" +
                    "        and prac.ID in (select max(ID) from NOTARY_REG_PRACTICE where NOTARY_INFO_ID=info.ID and ACTIVE=0 and STATUS=2 group by STATUS)  \n" +
                    "    \n" +
                    "    UNION all\n" +
                    "    \n" +
                    "    --cấp lại thẻ      \n" +
                    "    SELECT doc.DATE_SIGN, 8 as status FROM notary_info           info \n" +
                    "        LEFT JOIN notary_reg_practice   prac on prac.notary_info_id = info.id \n" +
                    "        LEFT JOIN notary_card card on card.notary_reg_practice_id = prac.id \n" +
                    "        LEFT JOIN dm_document doc on card.document_id = doc.id \n" +
                    "        \n" +
                    "    WHERE \n" +
                    "        doc.DATE_SIGN is not null and\n" +
                    "        info.active = 0 \n" +
                    "        AND doc.active = 0 \n" +
                    "        AND prac.active = 0 \n" +
                    "        AND card.type_document IN (1) \n" +
                    "        AND info.id = :id  \n" +
                    "    \n" +
                    "    UNION all\n" +
                    "    \n" +
                    "    --xóa đăng ký hành nghề        \n" +
                    "    SELECT doc.DATE_SIGN, 22 as status FROM notary_info           info\n" +
                    "        LEFT JOIN notary_reg_practice   prac on prac.notary_info_id = info.id\n" +
                    "        LEFT JOIN notary_card           card on card.notary_reg_practice_id = prac.id\n" +
                    "        LEFT JOIN dm_document           doc on card.document_id = doc.id\n" +
                    "        \n" +
                    "    WHERE \n" +
                    "        doc.DATE_SIGN is not null and\n" +
                    "        info.active = 0\n" +
                    "        AND doc.active = 0\n" +
                    "        AND prac.active = 0\n" +
                    "        AND prac.status = 6\n" +
                    "        AND card.type_document IN (2)\n" +
                    "        AND info.id = :id\n" +
                    "    \n" +
                    "    \n" +
                    ") bf";

            List<Object[]> db = entityManager.createNativeQuery(sql + " order by bf.date_sign desc nulls last ").setParameter("id", idNotary).setFirstResult(0).setMaxResults(1).getResultList();

            db.stream().forEach((record) -> {
                Long status = record[1] == null ? null : Long.parseLong(record[1].toString());
                if(status != null) {
                    info.setStatus(status);
                }
            });

            info.setUpdatedBy(user.getUsername());
            info.setLastUpdate(new Date());
            if(db.size() == 0){
                info.setStatus(ConstantsTccc.NOTARY_STATUS.CHO_BO_SUNG);
            }
            entityManager.merge(info);

            if(!checkStatusByDatesign(idNotary) && duyet){
                NotaryInfoHis his = formToBo(info, new NotaryInfoHis());
                his.setStatus(ConstantsTccc.NOTARY_STATUS.DA_BO_SUNG);
                his.setUpdatedBy(user.getUsername());
                his.setLastUpdate(new Date());
                entityManager.persist(his);
            }
            entityManager.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //check duyệt ccv lịch sử
    public boolean checkStatusByDatesign(Long idNotary){
        boolean check = false;
        try {
            String count = entityManager.createQuery("select count(hit) from NotaryInfoHis hit where hit.id=:id and hit.status=:status ")
                    .setParameter("id", idNotary).setParameter("status", ConstantsTccc.NOTARY_STATUS.DA_BO_SUNG).getSingleResult().toString();
            if(Integer.parseInt(count) > 0){
                check = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    //xóa duyệt ccv lịch sử
    public boolean deleteStatusByDatesign(Long idNotary){
        boolean check = false;
        try {
            if(checkStatusByDatesign(idNotary)){
                NotaryInfoHis his = entityManager.createQuery("select hit from NotaryInfoHis hit where hit.id=:id and hit.status=:status",NotaryInfoHis.class)
                        .setParameter("id", idNotary).setParameter("status", ConstantsTccc.NOTARY_STATUS.DA_BO_SUNG).getSingleResult();
                entityManager.remove(his);
                entityManager.flush();
                check = true;
            } else {
                check = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return check;
    }

    //lấy lịch sử
    public NotaryInfoView getNotaryHisByType(Long idObj, Long idNotary, int tab, Long status_prac) {
        NotaryInfoView view = new NotaryInfoView();
        try {
            if(tab == -1) {
                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1,    \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DK_TAP_SU;

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //bổ nhiệm ccv và từ chối bổ nhiệm
            if(tab == 1 || tab == 2) {
                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.TYPE_APPOINT.BO_NHIEM)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DA_BO_NHIEM;

                } else if(status_prac.equals(ConstantsTccc.TYPE_APPOINT.TU_CHOI_BO_NHIEM)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TU_CHOI_BO_NHIEM;

                } else if(status_prac.equals(-1L)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DE_NGHI_BO_NHIEM;

                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            // 3 (miễn nhiệm , đề nghị mn) và 10 từ chối miễn nhiệm
            if(tab == 3 || tab == 10) {
                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.STATUS_DISMISSED.MIEN_NHIEM)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DA_MIEN_NHIEM;

                } else if(status_prac.equals(ConstantsTccc.STATUS_DISMISSED.TU_CHOI_MIEN_NHIEM)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TU_CHOI_MIEN_NHIEM;

                } else if(status_prac.equals(999L)) {//custum đề nghị miễn nhiệm
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DE_NGHI_MIEN_NHIEM;
                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //đăng ký hành nghề
            if(tab==5) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DK_HNCC_VA_CAP_THE;

                } else if(status_prac.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.CHO_CAP_THE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.CHO_CAP_THE;

                } else if(status_prac.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.TU_CHOI_CAP_THE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TU_CHOI_CAP_THE;
                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //bổ nhiệm lại, từ chối bổ nhiệm lại
            if(tab==4 || tab == 12) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.TYPE_REAPPOINT.BO_NHIEM_LAI)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DA_BO_NHIEM_LAI;

                } else if(status_prac.equals(ConstantsTccc.TYPE_REAPPOINT.TU_CHOI_BO_NHIEM_LAI)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TU_CHOI_BO_NHIEM_LAI;

                } else if(status_prac.equals(-1L)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DE_NGHI_BO_NHIEM_LAI;

                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //tạm đình chỉ đăng ký hành nghề , hủy tạm đình chỉ
            if(tab==6 || tab == 13) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.TYPE_SUPEND.TAM_DINH_CHI)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TAM_DINH_CHI_HNCC;

                } else if(status_prac.equals(ConstantsTccc.TYPE_SUPEND.HUY_TAM_DINH_CHI)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.HUY_TAM_DINH_CHI_HNCC;

                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //Cấp lại thẻ CCV
            if(tab==7) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = "";

                if(status_prac.equals(ConstantsTccc.TYPE_NOTARY_CARD.CHO_CAP_LAI_THE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DE_NGHI_CAP_LAI_THE_CCV;

                } else if(status_prac.equals(ConstantsTccc.TYPE_NOTARY_CARD.CAP_LAI_THE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.DA_CAP_LAI_THE_CCV;

                } else if(status_prac.equals(ConstantsTccc.TYPE_NOTARY_CARD.TU_CHOI_CAP_LAI_THE)) {
                    type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.TU_CHOI_CAP_LAI_THE_CCV;
                }

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //xóa đkhn và thu hồi thẻ CCV
            if(tab==8) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.XOA_DK_HNCC_VA_THU_HOI_THE_CCV;

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

            //xử phạt ccv
            if(tab==9) {

                String sql = "select \n" +
                        "\n" +
                        "    h.ID_HIS,\n" +
                        "    h.ID,\n" +
                        "    h.NAME,\n" +
                        "    h.SEX,\n" +
                        "    h.BIRTH_DAY,\n" +
                        "    h.ID_NO,\n" +
                        "    apa.VALUE as ADDRESS_ID_NO,-- h.ADDRESS_ID_NO,     \n" +
                        "    decode(h.ADDRESS_RESIDENT_ID,null,h.ADDRESS_RESIDENT,h.ADDRESS_RESIDENT||' - '||dm.COMMUNE_NAME||' - '||dm.DISTRICT_NAME||' - '||dm.PROVINCE_NAME) as add1, \n" +
                        "    h.ADDRESS_RESIDENT_ID,\n" +
                        "    decode(h.ADDRESS_NOW_ID,null,h.ADDRESS_NOW,h.ADDRESS_NOW||' - '||dm2.COMMUNE_NAME||' - '||dm2.DISTRICT_NAME||' - '||dm2.PROVINCE_NAME) as add2, \n" +
                        "    h.ADDRESS_NOW_ID,\n" +
                        "    h.STATUS,\n" +
                        "    h.PHONE_NUMBER,\n" +
                        "    h.EMAIL,\n" +
                        "    h.NOTE,\n" +
                        "    h.ACTIVE,\n" +
                        "    h.ID_NO_DATE,\n" +
                        "    h.CREATED_BY,\n" +
                        "    h.UPDATED_BY,\n" +
                        "    h.GEN_DATE,\n" +
                        "    h.LAST_UPDATE,\n" +
                        "    h.TYPE_ACTION,\n" +
                        "    h.OBJ_ID\n" +
                        "\n" +
                        "from Notary_Info_His h \n" +
                        "   LEFT JOIN  DM_AREA dm on dm.ID=h.ADDRESS_RESIDENT_ID         \n" +
                        "   LEFT JOIN  DM_AREA dm2 on dm2.ID=h.address_now_id    \n" +
                        "   LEFT JOIN  ADM_PARAMETER apa on apa.ID=h.address_id_no         \n" +
                        "\n" +
                        "where h.id=:idNotary and h.TYPE_ACTION in (:type_ac) and h.OBJ_ID=:idObj and h.active=0 \n";

                String type_ac = ConstantsTccc.TYPE_ACTION_NOTARY_INFO_HIS.XU_PHAT_CCV;

                Object[] record = (Object[]) entityManager.createNativeQuery(sql)
                        .setParameter("idNotary",idNotary)
                        .setParameter("idObj",idObj)
                        .setParameter("type_ac", type_ac)
                        .getSingleResult();

                if(record != null && record.length > 0) {
                    int j=0;
                    view.setIdDetail(record[j] == null ? null : Long.parseLong(record[j].toString()));          j++;
                    view.setIdNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setNameNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setSex(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setBirthDay(record[j] == null ? null : ((Date) record[j]));      j++;
                    view.setIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressIdNo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResident(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressResidentId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setAddressNow(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setAddressNowId(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setStatus(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setPhoneNumberNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setEmailNotaryInfo(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setNote(record[j] == null ? null : ((String) record[j]));      j++;
                    view.setActiveNotaryInfo(record[j] == null ? null : Long.parseLong(record[j].toString()));      j++;
                    view.setIdNoDate(record[j] == null ? null : ((Date) record[j]));      j++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
