package com.osp.bttp.dao.service.bttp.iml;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.CustomException;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.NotaryInfoCreateDto;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.entity.db4.LLawyer;
import com.osp.bttp.dao.model.mview.bttp.*;
import com.osp.bttp.dao.model.type.ActionType;
import com.osp.bttp.dao.model.type.ActorType;
import com.osp.bttp.dao.model.type.GroupType;
import com.osp.bttp.dao.repository.bttp.NotaryInfoRepository;
import com.osp.bttp.dao.repository.db3.AuctioneerRepository;
import com.osp.bttp.dao.repository.db4.LLawyerRepository;
import com.osp.bttp.dao.service.bttp.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class NotaryInfoServiceImpl implements NotaryInfoService {
    @Autowired
    private NotaryInfoRepository notaryInfoRepository;

    @Autowired
    private NotaryAppointService notaryAppointService;

    @Autowired
    private NotaryRegPracticeService notaryRegPracticeService;

    @Autowired
    private ProbationaryInfoService probationaryInfoService;

    @Autowired
    private NotarySuspendWorkService notarySuspendWorkService;

    @Autowired
    private NotaryPenalizeService notaryPenalizeService;

    @Autowired
    private LLawyerRepository lLawyerRepository;

    @Autowired
    private AuctioneerRepository auctioneerRepository;

    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private LogSystemService logSystemService;

    public final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public Long add(NotaryInfoCreateDto notaryInfoCreate) {
        NotaryInfo notaryInfo = new NotaryInfo();
        notaryInfo.setName(notaryInfoCreate.getName());
        notaryInfo.setActive(0L);
        if (StringUtils.hasText(notaryInfoCreate.getEmail())) {
            notaryInfo.setEmail(notaryInfoCreate.getEmail());
        }
        if (notaryInfoCreate.getSex() != null) {
            notaryInfo.setSex(notaryInfoCreate.getSex());
        }
        if (notaryInfoCreate.getBirthDay() != null) {
            notaryInfo.setBirthDay(notaryInfoCreate.getBirthDay());
        }
        if (StringUtils.hasText(notaryInfoCreate.getIdNo())) {
            notaryInfo.setIdNo(notaryInfoCreate.getIdNo());
        }
        if (notaryInfoCreate.getIdNoDate() != null) {
            notaryInfo.setIdNoDate(notaryInfoCreate.getIdNoDate());
        }
        if (StringUtils.hasText(notaryInfoCreate.getAddressIdNo())) {
            List<LLawyer> listDupLaw = lLawyerRepository.findAllByIdentityCardNumber(notaryInfoCreate.getIdNo());
            List<Auctioneer> listDupAuc = auctioneerRepository.findAllByIdCode(notaryInfoCreate.getIdNo());
            List<NotaryInfo> listDupNo = notaryInfoRepository.findAllByIdNo(notaryInfoCreate.getIdNo());
            if (!listDupLaw.isEmpty() || !listDupAuc.isEmpty() || !listDupNo.isEmpty()) {
                throw new CustomException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một {Đấu giá viên/Luật sư/Công chứng viên} khác");
            }
            notaryInfo.setAddressIdNo(notaryInfoCreate.getAddressIdNo());
        }
        if (StringUtils.hasText(notaryInfoCreate.getAddressResident())) {
            notaryInfo.setAddressResident(notaryInfoCreate.getAddressResident());
        }
        if (notaryInfoCreate.getAddressResidentId() != null) {
            notaryInfo.setAddressResidentId(notaryInfoCreate.getAddressResidentId());
        }
        if (StringUtils.hasText(notaryInfoCreate.getAddressNow())) {
            notaryInfo.setAddressNow(notaryInfoCreate.getAddressNow());
        }
        if (notaryInfoCreate.getAddressNowId() != null) {
            notaryInfo.setAddressNowId(notaryInfoCreate.getAddressNowId());
        }

        notaryInfo.setStatus(notaryInfoCreate.getStatus());

        if (StringUtils.hasText(notaryInfoCreate.getPhoneNumber())) {
            notaryInfo.setPhoneNumber(notaryInfoCreate.getPhoneNumber());
        }
        if (notaryInfoCreate.getAdministrationId() != null) {
            notaryInfo.setAdministrationId(notaryInfoCreate.getAdministrationId());
        }
        NotaryInfo notaryInfo1 = notaryInfoRepository.save(notaryInfo);
        logSystemService.saveLog(notaryInfo1.getName(), notaryInfo1.getId().toString(), ActionType.ADD, GroupType.NOTARY, ActorType.PER);
        return notaryInfo1.getId();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Long edit(Long id, NotaryInfoCreateDto notaryInfoNew) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(id);
        if (notaryInfo.isEmpty()) {
            throw new CustomException("Không tìm thấy công chứng viên id " + id);
        }
        NotaryInfo notaryInfoUpdate = notaryInfo.get();
        notaryInfoUpdate.setName(safeString(notaryInfoNew.getName()));
        notaryInfoUpdate.setEmail(safeString(notaryInfoNew.getEmail()));
        notaryInfoUpdate.setSex(safeLong(notaryInfoNew.getSex()));

        if (notaryInfoNew.getBirthDay() != null) {
            notaryInfoUpdate.setBirthDay(notaryInfoNew.getBirthDay());
        } else {
            notaryInfoUpdate.setBirthDay(null);
        }
        if (StringUtils.hasText(notaryInfoNew.getIdNo())) {
            List<LLawyer> listDupLaw = lLawyerRepository.findAllByIdentityCardNumber(notaryInfoNew.getIdNo());
            List<Auctioneer> listDupAuc = auctioneerRepository.findAllByIdCode(notaryInfoNew.getIdNo());
            List<NotaryInfo> listDupNo = notaryInfoRepository.findAllByIdNoAndIdNot(notaryInfoNew.getIdNo(), id);
            if (!listDupLaw.isEmpty() || !listDupAuc.isEmpty() || !listDupNo.isEmpty()) {
                throw new CustomException("Số CCCD/CMND/Hộ chiếu đã được sử dụng cho một {Đấu giá viên/Luật sư/Công chứng viên} khácCD");
            }
            notaryInfoUpdate.setIdNo(notaryInfoNew.getIdNo());
        }
        if (notaryInfoNew.getIdNoDate() != null) {
            notaryInfoUpdate.setIdNoDate(notaryInfoNew.getIdNoDate());
        } else {
            notaryInfoUpdate.setIdNoDate(null);
        }
        if (StringUtils.hasText(notaryInfoNew.getAddressIdNo())) {
            notaryInfoUpdate.setAddressIdNo(notaryInfoNew.getAddressIdNo());
        } else {
            notaryInfoUpdate.setAddressIdNo(null);
        }

        notaryInfoUpdate.setAddressResident(safeString(notaryInfoNew.getAddressResident()));
        if (notaryInfoNew.getAddressResidentId() != null) {
            notaryInfoUpdate.setAddressResidentId(notaryInfoNew.getAddressResidentId());
        } else {
            notaryInfoUpdate.setAddressResidentId(null);
        }

        notaryInfoUpdate.setAddressNow(safeString(notaryInfoNew.getAddressNow()));
        if (notaryInfoNew.getAddressNowId() != null) {
            notaryInfoUpdate.setAddressNowId(notaryInfoNew.getAddressNowId());
        } else {
            notaryInfoUpdate.setAddressNowId(null);
        }

        notaryInfoUpdate.setStatus(notaryInfoNew.getStatus());

        notaryInfoUpdate.setPhoneNumber(safeString(notaryInfoNew.getPhoneNumber()));
        if (notaryInfoNew.getAdministrationId() != null) {
            notaryInfoUpdate.setAdministrationId(notaryInfoNew.getAdministrationId());
        }
        NotaryInfo notaryInfo1 = notaryInfoRepository.save(notaryInfoUpdate);
        logSystemService.saveLog(notaryInfo1.getName(), notaryInfo1.getId().toString(), ActionType.EDIT, GroupType.NOTARY, ActorType.PER);
        return notaryInfo1.getId();

    }

    private String safeString(Object obj) {
        if (obj == null) return "";
        String str = obj.toString().trim();
        return str.isEmpty() ? "" : str;
    }

    private Long safeLong(Object obj) {
        return obj != null ? (Long) obj : 0L;
    }

    public PagingResult list_ccv(String name, Long orgId, Long orgCode, String status, PagingResult page, String fromDateRaw, String toDateRaw) {

        int offset = 0;
        int numberPerPage = page.getNumberPerPage();

        if (page.getPageNumber() > 0) {
            offset = (page.getPageNumber() - 1) * page.getNumberPerPage();
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
            String init = "" +
                    "WITH latest_notary_reg_practice AS (\n" +
                    "    SELECT notary_info_id, MAX(id) AS max_id\n" +
                    "    FROM notary_reg_practice\n" +
                    "    WHERE active = 0\n" +
                    "    GROUP BY notary_info_id\n" +
                    ")\n" +
                    "";
            String sql = "FROM    (\n" +
                    "   SELECT\n" +
                    "    info.id,\n" +
                    "    info.status,\n" +
                    "    info.name,\n" +
                    "    info.id_no,\n" +
                    "    nrp.number_cad,\n" +
                    "    oni.name  AS orgname,\n" +
                    "    info.CREATED_BY,\n" +
                    "    info.UPDATED_BY,\n" +
                    "    info.GEN_DATE,\n" +
                    "    info.LAST_UPDATE,\n" +
                    "    oni.address  AS ADDRESS, " +
                    "    info.ADMINISTRATION_ID, \n" +
                    "    oni.id AS orgId\n" +
                    "    \n" +
                    "   FROM\n" +
                    "    notary_info info\n" +
                    "    LEFT JOIN latest_notary_reg_practice l ON info.id = l.notary_info_id  \n " +
                    " left JOIN notary_reg_practice nrp ON nrp.id = l.max_id" +
                    "    LEFT JOIN dm_document reqdoc ON nrp.document_id = reqdoc.id AND reqdoc.active = 0 \n" +
                    "    LEFT JOIN org_notary_info oni ON nrp.org_notary_info_id = oni.id \n" +
                    "    AND oni.active = 0\n" +
                    "   WHERE\n" +
                    "    info.ACTIVE = 0 \n" +
                    "   ) aa     \n" +
                    "    inner join DM_ADMINISTRATION adm on adm.id = aa.ADMINISTRATION_ID \n" +
                    "  \n";
            if (H.isTrue(orgId)) {
                sql += "    AND aa.administration_id IN ( SELECT stat.id FROM dm_administration stat START WITH stat.id = :adminisId CONNECT BY PRIOR stat.id = stat.parent_id)      \n";
            }
            sql += "    where  1=1      \n";


//            if (H.isTrue(orgId) ) {
//                adminisId = Long.parseLong(searchObject.get("adminisId").toString().trim());
//            }
            if (H.isTrue(name)) {
                sql += "    and upper(aa.name) like upper(:fullName) or  upper(aa.number_cad) like '%' || upper(:fullName) || '%' or  upper(aa.id_no) like '%' || upper(:fullName) || '%'";
            }
            if (H.isTrue(status)) {
                sql += "    and aa.status = :status ";
            }
            if (H.isTrue(orgCode)) {
                sql += "    and aa.orgId = :orgCode ";
            }

            StringBuffer sqlColumn = new StringBuffer();
            sqlColumn.append(" SELECT * FROM ( SELECT  DISTINCT  aa.*, adm.NAME AS NAME_ADMIN, ROW_NUMBER() OVER (PARTITION BY aa.id ORDER BY aa.NAME desc) AS rownum_filter  ");
            sqlColumn.append(sql);
            sqlColumn.append(" order by adm.NAME desc ");
            sqlColumn.append(" ) WHERE rownum_filter = 1");
            sqlColumn.append(" ORDER BY LAST_UPDATE DESC  ");

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
            if (H.isTrue(orgCode)) {
                query.setParameter("orgCode", orgCode);
                queryCount.setParameter("orgCode", orgCode);
            }


            int count = ((Number) queryCount.getSingleResult()).intValue();

            if (count > 0L) {
                db = query.getResultList();
                db.stream().forEach((record) -> {
                    NotaryInfoView row = new NotaryInfoView();
                    row.setIdNotaryInfo(record[0] == null ? null : Long.parseLong(record[0].toString()));
                    row.setStatusNotaryInfo(record[1] == null ? null : Long.parseLong(record[1].toString()));
                    row.setNameNotaryInfo(record[2] == null ? null : ((String) record[2]));
                    row.setIdNo(record[3] == null ? null : ((String) record[3]));
                    row.setNumberCad(record[4] == null ? null : ((String) record[4]));
                    row.setNameOrgNotaryInfo(record[5] == null ? null : ((String) record[5]));
                    row.setCreatedBy(record[6] == null ? null : ((String) record[6]));
                    row.setUpdatedBy(record[7] == null ? null : ((String) record[7]));
                    row.setGenDate(record[8] == null ? null : ((Date) record[8]));
                    row.setLastUpdate(record[9] == null ? null : ((Date) record[9]));
                    row.setOrgNotaryAddress(record[10] == null ? null : ((String) record[10]));
                    row.setNameAdmin(record[13] == null ? null : (record[13].toString().trim()));

                    items.add(row);
                });
                page.setRowCount(count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("loi tai NotaryInfoDAO.searchFileNotary: " + e.getMessage());
        }

        page.setItems(items);

        return page;
    }


    public ResponseEntity<ApiResponseV1<NotaryInfoDetailView>> detailNotary(Long idNotary) {
        List<Object[]> db = new ArrayList<>();
        NotaryInfoDetailView view = new NotaryInfoDetailView();
        AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String sql = "SELECT\n" +
                "    info.name,\n" +
                "    info.birth_day,\n" +
                "    info.sex,\n" +
                "    info.id_no,\n" +
                "    info.id_no_date,\n" +
                "    info.address_id_no as address_no,\n" + // note
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
                "    wa.PROVINCE_CODE,\n" +
                "    wa2.PROVINCE_CODE,\n" +
                "    decode(info.ADDRESS_RESIDENT_ID,null,info.ADDRESS_RESIDENT,info.ADDRESS_RESIDENT) as add1, \n" +
                "    decode(info.address_now_id,null,info.address_now,info.address_now) as add2, " +
                "    info.ADMINISTRATION_ID\n" +
                "FROM\n" +
                "    notary_info         info\n" +
                "    LEFT JOIN probationary_info   pro ON pro.notary_info_id = info.id     \n" +
                "    LEFT JOIN org_notary_info     org ON pro.org_notary_info_id = org.id      \n" +
                "    LEFT JOIN  Ward wa on wa.id = info.ADDRESS_RESIDENT_ID         \n" +
                "    LEFT JOIN  Ward wa2 on wa2.id = info.address_now_id      \n" +
//                "    LEFT JOIN adm_parameter apa on apa.ID= info.address_id_no\n" +
                " WHERE\n" +
                "    info.id = :idnotary ";

        db = entityManager.createNativeQuery(sql).setParameter("idnotary", idNotary).getResultList();
        db.stream().forEach((record) -> {
            view.setName(record[0] == null ? null : ((String) record[0]));
            view.setBirthDay(record[1] == null ? null : ((Date) record[1]));
            view.setSex(record[2] == null ? null : Long.parseLong(record[2].toString()));
            view.setIdNo(record[3] == null ? null : ((String) record[3]));
            view.setIdNoDate(record[4] == null ? null : (Date) record[4]);
            view.setAddressIdNo(record[5] == null ? null : ((String) record[5]));
            view.setPhoneNumber(record[6] == null ? null : (String) record[6]);
            view.setEmail(record[7] == null ? null : (String) record[7]);
            view.setAddressResident(record[8] == null ? null : (String) record[8]);
            view.setAddressNow(record[9] == null ? null : (String) record[9]);
            view.setNameOrgNotaryInfo(record[10] == null ? null : ((String) record[10]));
            view.setAddress(record[11] == null ? null : (String) record[11]);
            view.setIdNotaryInfo(record[12] == null ? null : Long.parseLong(record[12].toString()));
            view.setStatus(record[13] == null ? null : Long.parseLong(record[13].toString()));
            view.setAddressResidentId(record[14] == null ? null : Long.parseLong(record[14].toString()));
            view.setAddressNowId(record[15] == null ? null : Long.parseLong(record[15].toString()));
            view.setCityResidentId(record[16] == null ? null : (String) record[16]);
            view.setCityNowId(record[17] == null ? null : (String) record[17]);
            view.setAddressResident(record[18] == null ? null : (String) record[18]);
            view.setAddressNow(record[19] == null ? null : (String) record[19]);
            view.setAdministrationId(record[20] == null ? null : Long.parseLong(record[20].toString()));

        });

        view.setNotaryProbationaryResponse(probationaryInfoService.getProbationaryInfoByIdNotary(idNotary));

        view.setNotaryAppointResponses(notaryAppointService.getProcessAppoints(idNotary));
        List<NotaryRegAndAuctionCardResponse> listNo = notaryRegPracticeService.getNotaryRegAndAuctionCard(idNotary);
        if (listNo != null && !listNo.isEmpty()) {
            view.setNotaryRegAndAuctionCardResponse(listNo.get(0));
        }
        List<NotarySuspendWorkResponse> workResponseList = notarySuspendWorkService.getSuspendWorkByNotary(idNotary);
        if (workResponseList != null && !workResponseList.isEmpty()) {
            view.setNotarySuspendWorkResponse(workResponseList.get(0));
        }

        view.setNotaryPenalizeResponse(notaryPenalizeService.getPenaltiesByNotary(idNotary));

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", view), HttpStatus.OK);
    }


    @Override
    public PaginationDto<NotaryChiefResponse> getListNotaryChiefCategory(String name, Long pageNo, Long pageSize) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0L;
        }
        if (pageSize == null || pageSize > 100) {
            pageSize = 50L;
        }
        Pageable pageable = PageRequest.of(Math.toIntExact(pageNo), Math.toIntExact(pageSize));
        Page<NotaryChiefResponse> page = null;
        page = notaryInfoRepository.findNotaryOfficeChiefs(name, pageable);
        return new PaginationDto<>(page.stream().toList(), page.getTotalElements(), page.getTotalPages());

    }

    @Override
    public void delete(Long id) {
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(id);
        if (notaryInfo.isPresent()) {
            Optional<NotaryInfo> checkRelationOther = notaryInfoRepository.checkDeleteCommonNotary(id);
            if (checkRelationOther.isPresent()) {
                throw new CustomException("Thông tin chung đang tham chiếu bảng khác !");
            }
            notaryInfoRepository.delete(notaryInfo.get());
        } else {
            throw new CustomException("Không tìm thấy thông tin chung!");
        }
    }

    @Override
    public String deleteNotaryInfo(Long id) {
        String message = "";
        StringBuilder builder = new StringBuilder(message);
        Optional<NotaryInfo> notaryInfo = notaryInfoRepository.findById(id);
        if (notaryInfo.isPresent()) {
            Optional<NotaryInfo> checkRelationOther = notaryInfoRepository.checkNotaryIsChief(id);
            if (checkRelationOther.isPresent()) {
                throw new CustomException("Công chứng viên đang làm đại diện !");
            }
            try {
                probationaryInfoService.deleteByNotaryId(id);
                builder.append("Xóa thành công thông tin tập sự");
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            try {
                notaryAppointService.deleteNotaryAppoint(id);
                builder.append("Xóa thành công thông tin bổ nhiệm/miễn nhiệm");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            try {
                notaryRegPracticeService.deleteByNotaryId(id);
                builder.append("Xóa thành công thông tin đăng ký hành nghề");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            try {
                notarySuspendWorkService.deleteByNotaryId(id);
                builder.append("Xóa thành công thông tin tạm đỉnh chỉ");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            try {
                notaryPenalizeService.deleteByNotaryId(id);
                builder.append("Xóa thành công thông tin xử phạt");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            notaryInfoRepository.delete(notaryInfo.get());
            builder.append("Xóa thành công thông tin chung");
        } else {
            throw new CustomException("Không tìm thấy thông tin chung!");
        }
        return builder.toString();
    }

    public ResponseEntity<ApiResponseV1<?>> exportExcelNotaryInfo(String name, Long orgId, String status, PagingResult page, String fromDateRaw, String toDateRaw, HttpServletResponse httpServletResponse) {
        try {
            PagingResult data = list_ccv(name, orgId, null, status, page, null, null);
            List<NotaryInfoView> items = data.getItems();
            List<String> headers = new ArrayList<>();
            headers.add("STT");
            headers.add("Trạng thái");
            headers.add("Họ và tên");
            headers.add("Năm sinh");
            headers.add("Số CMTND/ Hộ chiếu/ CCCD");
            headers.add("Số quyết định bổ nhiệm");
            headers.add("Ngày quyết định");
            headers.add("Số thẻ CCV");
            headers.add("Tổ chức HNCC");
            headers.add("Người tạo");
            headers.add("Ngày tạo");
            headers.add("Người cập nhật");
            headers.add("Ngày cập nhật");

            List<List<String>> dataExport = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                NotaryInfoView item = items.get(i);
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(i + 1));
                row.add(item.getStatusNotaryInfoStr());
                row.add(item.getNameNotaryInfo());
                row.add(item.getBirthDayStr_());
                row.add(item.getIdNo());
                row.add(item.getDispatchCode());
                row.add(item.getDateSignStr());
                row.add(item.getNumberCad());
                row.add(item.getNameOrgNotaryInfo());
                row.add(item.getCreatedBy());
                row.add(item.getStrGenDate());
                row.add(item.getUpdatedBy());
                row.add(item.getStrLastUpdate());
                dataExport.add(row);
            }

            String fileName = "Danh_sach_ho_so_cong_chung_vien.xlsx";
            String sheetName = "Sheet1";
            String title = "Danh sách hồ sơ công chứng viên";
            String subTitle = "Tổng số bản ghi : " + items.size();

            ExcelUtils.exportExcel(httpServletResponse, headers, dataExport, fileName, sheetName, title, subTitle);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", null), HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Có lỗi xảy ra", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
