package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.common.contants.ConstantsDGTS;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.mview.db2.AuctioneerInfo;
import com.osp.bttp.dao.model.mview.db2.ListInfoDecision;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author sangnk
 * @Created 22/10/2024 - 3:47 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
public class AuctioneerDAO {
    @PersistenceContext(unitName = "db2")
    private EntityManager entityManager;


    private final List<?> cerType = Arrays.asList(
            ConstantsDGTS.ACT_TYPE.CAP_MOI_CCHN, ConstantsDGTS.ACT_TYPE.CAP_LAI_CCHN,
            ConstantsDGTS.ACT_TYPE.THU_HOI_CCHN
    );
    private final List<?> cardType = Arrays.asList(
            ConstantsDGTS.ACT_TYPE.CAP_MOI_THE_DGV, ConstantsDGTS.ACT_TYPE.CAP_LAI_THE_DGV,
            ConstantsDGTS.ACT_TYPE.THU_HOI_THE_DGV, ConstantsDGTS.ACT_TYPE.THOI_HANH_NGHE_TAI_TC
    );

    public Optional<AuctioneerInfo> getAuctioneerWithFullAddrByID(Long auctioneerID) {
        List<AuctioneerInfo> result = new ArrayList<>();

        String sql = "SELECT"
                + "	aa.*,addr.ADDR_FULL "
                + " FROM "
                + "	("
                + "	SELECT"
                + "		a.id," +
                " a.AUCTIONEER_TYPE, " +
                " a.GEN_DATE, " +
                " a.LAST_UPDATED," +
                " a.ID_CODE, " +
                " a.ID_TYPE, " +
                " a.FULLNAME," +
                " a.DOB, " +
                " a.SEX, " +
                " a.ADDR_PERMANENT, " +
                " a.ADDR_CURRENT, " +
                " a.TEL_NUMBER, " +
                " a.EMAIL, " +
                " a.CER_CODE, " +
                " a.CARD_CODE, " +
                " a.OTHER_INFO, " +
                " a.ID_DOI, " +
                " a.ID_POI, " +
                " a.CER_DOI, " +
                " a.CARD_POI, " +
                " a.CARD_DOI, " +
                " a.AUCTIONEER_STATUS, " +
                " a.IS_PUBLISH, " +
                " a.ADDR_DISTRICT_ID, " +
                " a.ADDR_CITY_ID," +
                " a.CER_STATUS," +
                " a.CARD_STATUS," +
                " a.WARNING," +
                " a.SCAN_TYPE," +
                " a.ORG_ID," +
                " a.USER_UPDATE," +
                " a.IS_PIC  "
                + "	FROM "
                + "AIMS_AUCTIONEER a "
                + "	WHERE"
                + "		a.id= :auctioneerID ) aa"
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
        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("auctioneerID", auctioneerID);
        try {
            List<Object[]> list = query.getResultList();
            for (Object[] obj : list) {
                AuctioneerInfo item = new AuctioneerInfo();
                item.setId(Long.parseLong(obj[0].toString()));
                item.setAuctioneerType(obj[1] != null ? Long.parseLong(obj[1].toString()) : null);
                item.setGenDate(obj[2] != null ? (Timestamp) obj[2] : null);
                item.setLastUpdated(obj[3] != null ? (Timestamp) obj[3] : null);
                item.setIdCode(obj[4] != null ? obj[4].toString() : null);
                item.setIdType(obj[5] != null ? obj[5].toString() : null);
                item.setFullname(obj[6] != null ? obj[6].toString() : null);
                item.setDob(obj[7] != null ?  obj[7].toString() : null);
                item.setSex(obj[8] != null ? Long.parseLong(obj[8].toString()) : null);
//                item.setAddrPermanent(obj[9] != null ? obj[9].toString() : null);
//                item.setAddrCurrent(obj[10] != null ? obj[10].toString() : null);
                item.setTelNumber(obj[11] != null ? obj[11].toString() : null);
                item.setEmail(obj[12] != null ? obj[12].toString() : null);
                item.setCerCode(obj[13] != null ? obj[13].toString() : null);
                item.setCardCode(obj[14] != null ? obj[14].toString() : null);
                item.setOtherInfo(obj[15] != null ? obj[15].toString() : null);
                item.setIdDOI(obj[16] != null ? (Timestamp) obj[16] : null);
                item.setIdPOI(obj[17] != null ? obj[17].toString() : null);
                item.setCerDoi(obj[18] != null ? obj[18].toString() : null);
                item.setCardPoi(obj[19] != null ? obj[19].toString() : null);
                item.setCardDoi(obj[20] != null ? obj[20].toString() : null);
                item.setAuctioneerStatus(obj[21] != null ? Long.parseLong(obj[21].toString()) : null);
                item.setIsPublish(obj[22] != null ? Long.parseLong(obj[22].toString()) : null
                );
//                item.setAddrDistrictId(obj[23] != null ? Long.parseLong(obj[23].toString()) : null);
//                item.setAddrCityId(obj[24] != null ? Long.parseLong(obj[24].toString()) : null);
                item.setCerStatus(obj[25] != null ? Long.parseLong(obj[25].toString()) : null);
                item.setCardStatus(obj[26] != null ? Long.parseLong(obj[26].toString()) : null);
                item.setWarning(obj[27] != null ? obj[27].toString() : null);
//                item.setScanType(obj[28] != null ? obj[28].toString() : null);
//                item.setOrgId(obj[29] != null ? Long.parseLong(obj[29].toString()) : null);
//                item.setUserUpdate(obj[30] != null ? obj[30].toString() : null);
//                item.setIsPic(obj[31] != null ? Long.parseLong(obj[31].toString()) : null);
                item.setAddrFull(obj[32] != null ? obj[32].toString() : null);

                result.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<PagingResult> getDetailInfo(PagingResult page, Long auctioneerID, String type) {
        String sql = "SELECT his.*,org.FULLNAME AS ORG_NAME from "
                + "(select " +
                " a.id, " +
                " a.AUCTIONEER_ID, " +
                " a.S_DATE," +
                " a.E_DATE," +
                " a.AUCTIONEER_TYPE," +
                " a.ORG_ID, " +
                " a.INFO," +
                " a.REWARD_INFO, " +
                " a.GEN_DATE," +
                " a.SOURCE_LOG," +
                " a.CREATED_BY," +
                " a.CER_CODE," +
                " a.CARD_CODE," +
                " a.ACT_TYPE," +
                " a.FILE_ID," +
                " a.ACT_DESC," +
                " a.NUMBER_OF_DECISION," +
                " a.DATE_OF_DECISION, " +
                " a.EFFECTIVE_DATE, " +
                " a.OTHER_INFO," +
                " a.SCAN_TYPE    " +
                "  from  AIMS_AUCTIONEER_HIS a where a.AUCTIONEER_ID = :autioneerID "
                + " and a.ACT_TYPE IN (:act_type) order by a.GEN_DATE desc) his "
                + " LEFT JOIN ( "
                + "SELECT "
                + "	b.id AS his_id, "
                + "	c.FULLNAME "
                + "FROM "
                + "	 AIMS_AUCTIONEER_HIS b, "
                + "	 AIMS_ORGANIZATION c "
                + "WHERE "
                + "	b.AUCTIONEER_ID = :autioneerID "
                + "	AND b.ORG_ID = c.ID ) org ON "
                + "his.id = org.his_id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("autioneerID", auctioneerID);
        if ("CERTIFICATE".equals(type)) {
            query.setParameter("act_type", cerType);
        } else if ("CARD".equals(type)) {
            query.setParameter("act_type", cardType);
        }
        try {
            List<Object[]> list = query.getResultList();
            List<ListInfoDecision> listInfo = new ArrayList<>();
            for (Object[] obj : list) {
                ListInfoDecision info = new ListInfoDecision();
                info.setId(Long.parseLong(obj[0].toString()));
                info.setAuctioneerID(Long.parseLong(obj[1].toString()));
//                info.setSDate(obj[2] != null ? obj[2].toString() : null);
//                info.setEDate(obj[3] != null ? obj[3].toString() : null);
                info.setAuctioneerType(obj[4] != null ? Long.parseLong(obj[4].toString()) : null);
                info.setOrgID(obj[5] != null ? Long.parseLong(obj[5].toString()) : null);
//                info.setInfo(obj[6] != null ? obj[6].toString() : null);
//                info.setRewardInfo(obj[7] != null ? obj[7].toString() : null);
//                info.setGenDate(obj[8] != null ? obj[8].toString() : null);
//                info.setSourceLog(obj[9] != null ? obj[9].toString() : null);
//                info.setCreatedBy(obj[10] != null ? obj[10].toString() : null);
                info.setCerCode(obj[11] != null ? obj[11].toString() : null);
                info.setCardCode(obj[12] != null ? obj[12].toString() : null);
                info.setActType(obj[13] != null ? Long.parseLong(obj[13].toString()) : null);
//                info.setFileID(obj[14] != null ? Long.parseLong(obj[14].toString()) : null);
//                info.setActDesc(obj[15] != null ? obj[15].toString() : null);
                info.setNumberOfDecision(obj[16] != null ? obj[16].toString() : null);
                info.setDateOfDecision(obj[17] != null ? (Timestamp) obj[17] : null);
                info.setEffectiveDate(obj[18] != null ? (Timestamp) obj[18] : null);
//                info.setOtherInfo(obj[19] != null ? obj[19].toString() : null);
//                info.setScanType(obj[20] != null ? obj[20].toString() : null);
                info.setOrgName(obj[21] != null ? obj[21].toString() : null);
                listInfo.add(info);
            }
            if (listInfo != null && listInfo.size() > 0) {
                page.setItems(listInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.ofNullable(page);
    }
}
