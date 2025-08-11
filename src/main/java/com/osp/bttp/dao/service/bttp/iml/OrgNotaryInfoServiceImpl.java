package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.contants.ConstantBttp;
import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.OrgNotaryInfoCreateDto;
import com.osp.bttp.dao.model.dto.db3.OrgNotaryInfoDetail;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.DmAdministration;
import com.osp.bttp.dao.model.entity.db3.NotaryInfo;
import com.osp.bttp.dao.model.entity.db3.OrgNotaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryInfoInOrg;
import com.osp.bttp.dao.model.mview.bttp.OrgCategoryInfo;
import com.osp.bttp.dao.model.mview.bttp.OrgNotaryDetailResponse;
import com.osp.bttp.dao.model.mview.bttp.OrgNotaryInfoView;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import com.osp.bttp.dao.repository.bttp.Dm2AdministrationRepository;
import com.osp.bttp.dao.repository.bttp.NotaryInfoRepository;
import com.osp.bttp.dao.repository.bttp.OrgNotaryInfoRepository;
import com.osp.bttp.dao.service.bttp.LogSystemService;
import com.osp.bttp.dao.service.bttp.OrgNotaryInfoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrgNotaryInfoServiceImpl implements OrgNotaryInfoService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private Dm2AdministrationRepository dmAdministrationRepository;

    @Autowired
    private OrgNotaryInfoRepository orgNotaryInfoRepository;

    @Autowired
    private LogSystemService logSystemService;

    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    public final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrgNotaryInfo add(OrgNotaryInfoCreateDto orgNotaryInfoCreate) {
        OrgNotaryInfo orgNotaryInfo = new OrgNotaryInfo();
        String name = orgNotaryInfoCreate.getName().trim();

        Long dmId = orgNotaryInfoCreate.getAdministrationId();
        Optional<DmAdministration> dmAdministration = dmAdministrationRepository.findById(dmId);
        if (dmAdministration.isEmpty()) {
            throw new CustomException("Sở tư pháp không tồn tại");
        }
        List<OrgNotaryInfo> orgNotaryInfoExitsName = orgNotaryInfoRepository.findByNameIgnoreCaseAndAdministrationId(name,dmId);
        if (orgNotaryInfoExitsName.size()>0) {
            throw new CustomException("Tên văn phòng công chứng đã tồn tại");
        }
        orgNotaryInfo.setName(name);
        Long notaryIdOfficeChief = orgNotaryInfoCreate.getNotaryIdOfficeChief();
        Optional<NotaryInfo> notaryInfoOfficeChief = notaryInfoRepository.findNotaryOfficeChief(notaryIdOfficeChief);
        if (notaryInfoOfficeChief.isEmpty()) {
            throw new CustomException("Người đại diện không hợp lệ");
        }
        orgNotaryInfo.setNotaryIdOfficeChief(notaryIdOfficeChief);
        orgNotaryInfo.setAdministrationId(dmId);
        if (StringUtils.hasText(orgNotaryInfoCreate.getAddress())) {
            orgNotaryInfo.setAddress(orgNotaryInfoCreate.getAddress());
        }
        if (StringUtils.hasText(orgNotaryInfoCreate.getTel())) {
            orgNotaryInfo.setTel(orgNotaryInfoCreate.getTel());
        }

        if (StringUtils.hasText(orgNotaryInfoCreate.getEmail())) {
            orgNotaryInfo.setEmail(orgNotaryInfoCreate.getEmail());
        }
        if (orgNotaryInfoCreate.getAddressId() != null) {
            orgNotaryInfo.setAddressId(orgNotaryInfoCreate.getAddressId());
        }
        orgNotaryInfo.setStatus(orgNotaryInfoCreate.getStatus());
        orgNotaryInfo.setActive(0L);
        OrgNotaryInfo orgNotaryInfo1 = orgNotaryInfoRepository.save(orgNotaryInfo);
        logSystemService.saveLog(orgNotaryInfo1.getName(), orgNotaryInfo1.getId().toString(), ActionType.ADD, GroupType.NOTARY, ActorType.ORG);
        return orgNotaryInfo1;
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrgNotaryInfo edit(Long id, OrgNotaryInfoCreateDto orgNotaryInfoCreate) {
        OrgNotaryInfo orgNotaryInfo = orgNotaryInfoRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy văn phòng công chứng với ID: " + id));
        // Sở Tư Pháp
        Long adminId = orgNotaryInfoCreate.getAdministrationId();
        if (adminId != null) {
            if (dmAdministrationRepository.findById(adminId).isEmpty()) {
                throw new CustomException("Sở Tư Pháp không tồn tại");
            }
            orgNotaryInfo.setAdministrationId(adminId);
        }
        // Kiểm tra trùng tên với văn phòng khác trong cùng sở
        String name = orgNotaryInfoCreate.getName().trim();
        orgNotaryInfo.setName(name);
//        if (StringUtils.hasText(name)) {
//            List<OrgNotaryInfo> existing = orgNotaryInfoRepository.findByNameIgnoreCaseAndAdministrationId(name,adminId);
//            if (existing.size()>0 && !existing.get(0).getId().equals(id)) {
//                throw new CustomException("Tên văn phòng công chứng đã tồn tại");
//            }
//            orgNotaryInfo.setName(name);
//        }

        // Người đại diện
        Long notaryIdOfficeChief = orgNotaryInfoCreate.getNotaryIdOfficeChief();
        Long notaryIdOfficeChiefCurrent = orgNotaryInfo.getNotaryIdOfficeChief();

        if (notaryIdOfficeChief != null && !Objects.equals(notaryIdOfficeChiefCurrent, notaryIdOfficeChief)) {
            Optional<NotaryInfo> notaryInfoOfficeChief = notaryInfoRepository.findNotaryOfficeChief(notaryIdOfficeChief);
            if (notaryInfoOfficeChief.isEmpty()) {
                throw new CustomException("Người đại diện không hợp lệ");
            }
            orgNotaryInfo.setNotaryIdOfficeChief(notaryIdOfficeChief);
        }

        // Các thông tin khác
        orgNotaryInfo.setAddress(safeString(orgNotaryInfoCreate.getAddress()));
        orgNotaryInfo.setTel(safeString(orgNotaryInfoCreate.getTel()));
        orgNotaryInfo.setEmail(safeString(orgNotaryInfoCreate.getEmail()));
        orgNotaryInfo.setAddressId(orgNotaryInfoCreate.getAddressId());
        orgNotaryInfo.setStatus(safeLong(orgNotaryInfo.getStatus()));
        OrgNotaryInfo orgNotaryInfo1 = orgNotaryInfoRepository.save(orgNotaryInfo);
        logSystemService.saveLog(orgNotaryInfo1.getName(), orgNotaryInfo1.getId().toString(), ActionType.EDIT, GroupType.NOTARY, ActorType.ORG);
        return orgNotaryInfo1;
    }

    private String safeString(Object obj) {
        if (obj == null) return "";
        String str = obj.toString().trim();
        return str.isEmpty() ? "" : str;
    }

    private Long safeLong(Object obj) {
        return obj != null ? (Long) obj : 0L;
    }

    public PagingResult list_search(String name, Long orgId, String status, PagingResult page, String fromDate, String toDate) {

        int offset = 0;
        int numberPerPage = page.getNumberPerPage();

        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
        }
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
            orgId = Long.parseLong(String.valueOf(userLogin.getAdministrationId()));
        } else if (H.isTrue(orgId)) {
            orgId = orgId;
        } else {
            orgId = 0L;
        }

        List<com.osp.bttp.dao.model.mview.bttp.OrgNotaryInfoView> items = new ArrayList<>();
        List<Object[]> db = new ArrayList<>();

        Date fromDateSign = null;
        Date toDateSign = null;


        String init = "" +
                "WITH \n" +
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

            String sql = "select org.id as ONI_ID,org.status,org.name as ONI_NAME,info.name nameChief,    \n" +
                    "     decode(org.ADDRESS_ID  , null, org.ADDRESS,  org.ADDRESS) as orgADDRESS,     \n" +
                    "     dm.FULL_NAME as administration_name, " +
                    "     org.ADMINISTRATION_ID " +
                    " \n" +
                    "FROM org_notary_info org         \n" +
                    "     LEFT JOIN notary_info info on org.notary_id_office_chief=info.id     \n" +
//                    "     LEFT JOIN dm_area dma on dma.ID=org.ADDRESS_ID          \n" +
                    "     LEFT JOIN dm_administration dm on dm.id=org.administration_id " +
                    "    LEFT JOIN ranked_notary_reg_practice nrp ON info.id = nrp.notary_info_id AND nrp.rn = 1\n" +
                    "    LEFT JOIN count_notary_in_org cno ON cno.org_notary_info_id = org.id\n" +
                    "where org.active=0 and 1=1 ";
            sql = init + " \n " + sql;
            sql += stringFilter.toString();
            sql += " order by org.LAST_UPDATE DESC ,nameChief , decode(org.status,0,1,2)  ";
            String sqlCount = "select count(*) from (" + sql + ")";
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
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
                row.setAdminName(record[5] == null ? null : ((String) record[5]).trim());
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

        return page;
    }

    public ResponseEntity<ApiResponseV1<OrgNotaryDetailResponse>> detailOrganizationNotary(Long idOrgNotaryInfo) {
        OrgNotaryDetailResponse file = new OrgNotaryDetailResponse();
        List<Object[]> db = new ArrayList<>();
        // Chung VPCC + PCC
        // Lấy ra thông tin vpcc + thông tin trưởng phòng
        OrgNotaryInfoDetail orgNotaryInfoView = new OrgNotaryInfoDetail();
        String hql = "select \n" +
                "    oni.STATUS, oni.TEL, oni.NAME as orgNotaryName, oni.FAX, oni.ADMINISTRATION_ID, oni.EMAIL as oniEmail, \n" +
                "    decode(oni.ADDRESS_ID,null,oni.ADDRESS,oni.ADDRESS) as ADDRESS, \n" +
                "    ni.id as idChief,       \n" +
                "    ni.NAME as notaryName, ni.PHONE_NUMBER, ni.SEX, ni.EMAIL as niEmail, ni.BIRTH_DAY, ni.ID_NO, ni.ID_NO_DATE,   \n" +
                "    oni.ADDRESS_ID as address_id, \n" +
                "    decode(ni.ADDRESS_RESIDENT_ID,null,ni.ADDRESS_RESIDENT,ni.ADDRESS_RESIDENT) as ADDRESS_RESIDENT,\n" +
                "    decode(ni.ADDRESS_NOW_ID,null,ni.ADDRESS_NOW,ni.ADDRESS_NOW) as ADDRESS_NOW,\n" +
                "    nrp.NUMBER_CAD as card, oni.type,        \n" +
                "    decode(oni.administration_id, null, '', dm.full_name) as nameAdministration,\n" +
                "    doc.DATE_SIGN, doc.EFFECTIVE_DATE, doc.DISPATCH_CODE , wd.PROVINCE_CODE \n" +
                "FROM ORG_NOTARY_INFO oni        \n" +
                "    LEFT JOIN NOTARY_INFO ni ON oni.NOTARY_ID_OFFICE_CHIEF = ni.ID        \n" +
                "    LEFT JOIN NOTARY_REG_PRACTICE nrp ON nrp.NOTARY_INFO_ID = ni.ID and nrp.STATUS = :nrpStatus and nrp.ACTIVE = :active  \n" +
                "    LEFT JOIN notary_appoint apo ON ni.id = apo.notary_info_id and apo.kind = 1 and  apo.active = :active   \n" +
                "    LEFT JOIN dm_document doc ON apo.document_id = doc.id and doc.active = :active    \n" +
                "    LEFT JOIN dm_administration dm ON dm.id = oni.administration_id \n" +  // <<== thêm JOIN này
                "    LEFT JOIN ward wd ON wd.id = oni.address_id \n" +  // <<== thêm JOIN này
                "WHERE oni.ACTIVE = :active \n" +
                "    and oni.id = :oniId";

        Query query = entityManager.createNativeQuery(hql)
                .setParameter("nrpStatus", ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)
                .setParameter("oniId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        db = query.getResultList();
        db.stream().forEach(record -> {
            orgNotaryInfoView.setStatusOrg(record[0] == null ? null : Long.parseLong(record[0].toString()));
            orgNotaryInfoView.setTel(record[1] == null ? null : ((String) record[1]));
            orgNotaryInfoView.setName(record[2] == null ? null : ((String) record[2]));
            orgNotaryInfoView.setFax(record[3] == null ? null : ((String) record[3]));
            orgNotaryInfoView.setAdministrationId(record[4] == null ? null : Long.parseLong(record[4].toString()));
            orgNotaryInfoView.setEmail(record[5] == null ? null : ((String) record[5]));
            orgNotaryInfoView.setAddress(record[6] == null ? null : ((String) record[6]));
            orgNotaryInfoView.setNotaryIdOfficeChief(record[7] == null ? null : Long.parseLong(record[7].toString()));
            orgNotaryInfoView.setOfficeChiefName(record[8] == null ? null : ((String) record[8]));
            orgNotaryInfoView.setPhoneNumber(record[9] == null ? null : ((String) record[9]));
            orgNotaryInfoView.setSex(record[10] == null ? null : Long.parseLong(record[10].toString()));
            orgNotaryInfoView.setEmailNotary(record[11] == null ? null : ((String) record[11]));
            orgNotaryInfoView.setBirthDay(record[12] == null ? null : ((Date) record[12]));
            orgNotaryInfoView.setIdNo(record[13] == null ? null : ((String) record[13]));
            orgNotaryInfoView.setIdNoDate(record[14] == null ? null : ((Date) record[14]));
            orgNotaryInfoView.setAddressIdNo(null);
            orgNotaryInfoView.setAddressResident(record[16] == null ? null : ((String) record[16]));
            orgNotaryInfoView.setNumberCad(record[18] == null ? null : ((String) record[18]));
            orgNotaryInfoView.setType(record[19] == null ? null : Long.parseLong(record[19].toString()));
            orgNotaryInfoView.setAdminName(record[20] == null ? null : ((String) record[20]));
            orgNotaryInfoView.setDateSign(record[21] == null ? null : ((Date) record[21]));
            orgNotaryInfoView.setEffectiveDate(record[22] == null ? null : ((Date) record[22]));
            orgNotaryInfoView.setDispatchCode(record[23] == null ? null : ((String) record[23]));
            orgNotaryInfoView.setAddressId(record[15] == null ? null : Long.parseLong(record[15].toString()));
            orgNotaryInfoView.setCityId(record[24] == null ? null : ((String) record[24]));
        });

        // Lấy ra 1 list các ccv đang hành nghề tại vpcc này
        PagingResult pageNotary = getPageNotary(idOrgNotaryInfo, 0, 10);

        file.setOrgNotaryInfoView(orgNotaryInfoView);
        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", file), HttpStatus.OK);
    }

    @Override
    public PagingResult<NotaryInfoInOrg> getPageNotary(Long idOrgNotaryInfo, int offset, int number) {
        PagingResult page = new PagingResult();
        Long rowCount = 0L;
        List<Object[]> db = new ArrayList<>();
        List<NotaryInfoInOrg> listNotaryOfOrg = new ArrayList<>();

        String hql = "SELECT\n" +
                "    ni.name,\n" +
                "    ni.birth_day,\n" +
                "    ni.id_no,\n" +
                "    ni.phone_number,\n" +
                "    decode(ni.ADDRESS_RESIDENT_ID,null,ni.ADDRESS_RESIDENT,ni.ADDRESS_RESIDENT) as address_resident,\n" +
                "    doc.dispatch_code,\n" +
                "    doc.date_sign,\n" +
                "    nrp.number_cad,\n" +
                "    ni.id\n" +
                "FROM\n" +
                "    notary_reg_practice   nrp\n" +
                "    LEFT JOIN notary_info           ni ON nrp.notary_info_id = ni.id\n" +
                "                                AND ni.active = :active\n" +
                "    LEFT JOIN dm_document           doc ON nrp.document_id = doc.id\n" +
                "                                 AND doc.active = :active\n" +
                "WHERE\n" +
                "    nrp.org_notary_info_id = :orgId\n" +
                "    AND nrp.status = :nrpStatus\n" +
                "    AND nrp.active = :active ";
//
        Query query = entityManager.createNativeQuery("select count(*) FROM (" + hql + ")")
                .setParameter("nrpStatus", ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE)
                .setParameter("orgId", idOrgNotaryInfo)
                .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);
        BigDecimal count = (BigDecimal) query.getSingleResult();
        rowCount = Long.valueOf((count.toString()));
        if (rowCount > 0L) {
            //  hql = UtilData.paginationOracle(hql, offset, number);
            query = entityManager.createNativeQuery(hql)
                    .setParameter("nrpStatus", ConstantBttp.NOTARY_STATUS.DANG_HANH_NGHE)
                    .setParameter("orgId", idOrgNotaryInfo)
                    .setParameter("active", ConstantsTccc.ACTIVE.HIEU_LUC);

            db = query.getResultList();
            db.stream().forEach((record) -> {
                NotaryInfoInOrg notary = new NotaryInfoInOrg();
                notary.setIdNotaryInfo((Long) record[8]);
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


    @Override
    public PaginationDto<OrgCategoryInfo> getListOrgCategory(Long adminId, String name, Long pageNo, Long pageSize) {
        Long orgId = null;
        if (adminId != null) {
            Optional<DmAdministration> dmOptional = dmAdministrationRepository.findById(adminId);
            if (dmOptional.isPresent()) {
                Long type = dmOptional.get().getType();
                if (type.equals(Constants.TYPE_DMADMINISTRATION.SO_TU_PHAP.longValue())) {
                    orgId = adminId;
                } else {
                    orgId = 0L;
                }
            }
        } else {
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                orgId = Long.parseLong(String.valueOf(userLogin.getAdministrationId()));
            }
            if (userLogin.getType().equals(Constants.TYPE_USER.ADMIN)) {
                orgId = 0L;
            }
        }
        if (pageNo == null || pageNo < 0) {
            pageNo = 0L;
        }
        if (pageSize == null || pageSize > 100) {
            pageSize = 100L;
        }
        Pageable pageable = PageRequest.of(Math.toIntExact(pageNo), Math.toIntExact(pageSize));
        Page<OrgNotaryInfo> page = null;
        if (orgId != null && orgId == 0L) {
            if (StringUtils.hasText(name)) {
                page = orgNotaryInfoRepository.findAllByNameContainingIgnoreCase(pageable, name);
            } else {
                page = orgNotaryInfoRepository.findAll(pageable);
            }
        } else {
            if (StringUtils.hasText(name)) {
                page = orgNotaryInfoRepository.findAllByAdministrationIdAndNameContainingIgnoreCase(pageable, orgId, name);
            } else {
                page = orgNotaryInfoRepository.findAllByAdministrationId(pageable, orgId);
            }
        }
        List<OrgCategoryInfo> listCategoryInfo = page.getContent().stream().map(o -> new OrgCategoryInfo(o.getId(), o.getName(), o.getAdministrationId())).collect(Collectors.toList());
        return new PaginationDto<>(listCategoryInfo, page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(Long type) {
        List<DmAdministration> list = entityManager.createQuery("SELECT dm from DmAdministration dm where dm.isActive = 0 and dm.type=:type").setParameter("type", type).getResultList();
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thành công", list));
    }

}
