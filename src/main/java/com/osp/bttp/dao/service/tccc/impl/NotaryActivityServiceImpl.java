package com.osp.bttp.dao.service.tccc.impl;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.utils.ExcelUtils;
import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.StoreUtils;
import com.osp.bttp.common.utils.UtilData;
import com.osp.bttp.dao.model.dto.db3.NotaryActivityDTO;
import com.osp.bttp.dao.model.entity.db3.AccUser;
import com.osp.bttp.dao.model.entity.db3.TcccNotaryActivity;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.repository.db3.TcccNotaryActivityRepository;
import com.osp.bttp.dao.service.tccc.NotaryActivityService;
import com.osp.bttp.dao.service.tccc.TcccService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author sangnk
 * @Created 25/10/2024 - 1:52 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class NotaryActivityServiceImpl implements NotaryActivityService {
    @PersistenceContext(unitName = "db3")
    private EntityManager entityManager;

    @Autowired
    private TcccNotaryActivityRepository tcccNotaryActivityRepository;

    @Autowired
    private StoreUtils storeUtils;

    @Autowired
    private TcccService tcccService;

    public final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> search(String fromDateRaw, String toDateRaw, Long cityId, int pageNumber, int numberPerPage, String yearReport, String monthReport) {
        try {
            int offset = (pageNumber - 1) * numberPerPage;
            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                cityId = Long.parseLong(String.valueOf(userLogin.getAdministrationId()));
            } else if (H.isTrue(cityId)) {
                cityId = cityId;
            } else {
                cityId = 0L;
            }
            Date fromDate = null;
            Date toDate = null;
            List<String> monthReportList = new ArrayList<>();
            if (fromDateRaw != null) {
                fromDate = format.parse(fromDateRaw);
            }
            if (toDateRaw != null) {
                toDate = format.parse(toDateRaw);
            }


            String sql = "SELECT " +
                    " tc.ID as id, " +
                    " tc.ADMINISTRATION_ID as  administrationId, " +
                    " tc.REPORT_DATE as reportDate, " +
                    " tc.REPORT_YEAR as reportYear, " +
                    " tc.REPORT_MONTH as reportMonth, " +
                    " tc.NUM_NOTARY_CONTRACTS as numNotaryContracts, " +
                    " tc.NOTARY_FEES_CONTRACTS as notaryFeesContracts, " +
                    " tc.NUM_OTHER_NOTARY_TASKS as numOtherNotaryTasks, " +
                    " tc.NOTARY_FEES_OTHER_TASKS as notaryFeesOtherTasks, " +
                    " tc.TOTAL_TASKS as totalTasks, " +
                    " tc.TOTAL_FEES as totalFees, " +
                    " tc.TAX_CONTRIBUTION as taxContribution, " +
                    " tc.NUM_NOTARY_OFFICES_REPORTING as numNotaryOfficesReporting, " +
                    " tc.NUM_NOTARIES_WORKING as numNotariesWorking, " +
                    " dma.NAME as province FROM TCCC_NOTARY_ACTIVITY tc left join DM_ADMINISTRATION dma on dma.id = tc.ADMINISTRATION_ID " +
                    " WHERE 1 = 1 " +
                    " and tc.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =:idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) ";
            if (fromDate != null) {
                sql += " AND tc.REPORT_DATE >= :fromDate";
            }
            if (toDate != null) {
                sql += " AND tc.REPORT_DATE <= :toDate";
            }
//            if (cityId != null) {
//                sql += " AND tc.ADMINISTRATION_ID = :cityId";
//            }
            if (yearReport != null) {
                sql += " AND tc.REPORT_YEAR = :yearReport";
            }
            if (monthReport != null) {
                String[] monthArr = monthReport.split(",");
                for (String month : monthArr) {
                    monthReportList.add(month);
                }
                sql += " AND tc.REPORT_MONTH IN :monthReport";
            }
            sql += " ORDER BY dma.NAME ";
            String sqlTotal = sql;
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
            Query query = entityManager.createNativeQuery(sql);
            Query queryTotal = entityManager.createNativeQuery(" SELECT COUNT(*) FROM (" + sqlTotal + ")");
            query.setParameter("idAdminisLogin", cityId);
            queryTotal.setParameter("idAdminisLogin", cityId);
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
            }
            if (yearReport != null) {
                query.setParameter("yearReport", yearReport);
                queryTotal.setParameter("yearReport", yearReport);
            }
            if (monthReport != null) {
                query.setParameter("monthReport", monthReportList);
                queryTotal.setParameter("monthReport", monthReportList);
            }
            List<NotaryActivityDTO> list = new ArrayList<>();
            List<Object[]> objects = query.getResultList();
            for (Object[] obj : objects) {
                NotaryActivityDTO notaryActivityDTO = new NotaryActivityDTO();
                notaryActivityDTO.setId(Long.parseLong(obj[0] == null ? "0" : obj[0].toString()));
                notaryActivityDTO.setAdministrationId(obj[1] == null ? null : Long.parseLong(obj[1].toString()));
                notaryActivityDTO.setReportDate(obj[2] == null ? null : (Date) obj[2]);
                notaryActivityDTO.setReportYear(obj[3] == null ? null : Long.parseLong(obj[3].toString()));
                notaryActivityDTO.setReportMonth(obj[4] == null ? null : Long.parseLong(obj[4].toString()));
                notaryActivityDTO.setNumNotaryContracts(obj[5] == null ? null : Long.parseLong(obj[5].toString()));
                notaryActivityDTO.setNotaryFeesContracts(obj[6] == null ? null : Long.parseLong(obj[6].toString()));
                notaryActivityDTO.setNumOtherNotaryTasks(obj[7] == null ? null : Long.parseLong(obj[7].toString()));
                notaryActivityDTO.setNotaryFeesOtherTasks(obj[8] == null ? null : Long.parseLong(obj[8].toString()));
                notaryActivityDTO.setTotalTasks(obj[9] == null ? null : Long.parseLong(obj[9].toString()));
                notaryActivityDTO.setTotalFees(obj[10] == null ? null : Long.parseLong(obj[10].toString()));
                notaryActivityDTO.setTaxContribution(obj[11] == null ? null : Long.parseLong(obj[11].toString()));
                notaryActivityDTO.setNumNotaryOfficesReporting(obj[12] == null ? null : Long.parseLong(obj[12].toString()));
                notaryActivityDTO.setNumNotariesWorking(obj[13] == null ? null : Long.parseLong(obj[13].toString()));
                notaryActivityDTO.setProvince(obj[14].toString().trim());
                list.add(notaryActivityDTO);
            }
            Long total = Long.parseLong(queryTotal.getResultList().get(0).toString());
            PagingResult pagingResult = new PagingResult();
            pagingResult.setPageNumber(pageNumber);
            pagingResult.setNumberPerPage(numberPerPage);
            pagingResult.setRowCount(total);
            pagingResult.setItems(list);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<NotaryActivityDTO>> createNotaryActivity(NotaryActivityDTO notaryActivityDTO) {
        TcccNotaryActivity entity = new TcccNotaryActivity();
        // Map fields from DTO to Entity
        entity.setAdministrationId(notaryActivityDTO.getAdministrationId());
        entity.setReportYear(notaryActivityDTO.getReportYear());
        entity.setReportMonth(notaryActivityDTO.getReportMonth());
        entity.setReportDate(notaryActivityDTO.getReportDate());
        entity.setNumNotaryContracts(notaryActivityDTO.getNumNotaryContracts());
        entity.setNotaryFeesContracts(notaryActivityDTO.getNotaryFeesContracts());
        entity.setNumOtherNotaryTasks(notaryActivityDTO.getNumOtherNotaryTasks());
        entity.setNotaryFeesOtherTasks(notaryActivityDTO.getNotaryFeesOtherTasks());
        entity.setTotalTasks(notaryActivityDTO.getTotalTasks());
        entity.setTotalFees(notaryActivityDTO.getTotalFees());
        entity.setTaxContribution(notaryActivityDTO.getTaxContribution());
        entity.setNumNotaryOfficesReporting(notaryActivityDTO.getNumNotaryOfficesReporting());
        entity.setNumNotariesWorking(notaryActivityDTO.getNumNotariesWorking());

        TcccNotaryActivity savedEntity = storeUtils.save(tcccNotaryActivityRepository, entity); // Save entity to database
        NotaryActivityDTO res = mapToDTO(savedEntity); // Convert entity back to DTO

        return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", res), HttpStatus.OK);
    }

    @Override
    public List<NotaryActivityDTO> getActivitiesByMonth(int year, int month) {
        List<TcccNotaryActivity> activities = tcccNotaryActivityRepository.findByReportYearAndReportMonth(year, month);
        return activities.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public NotaryActivityDTO updateNotaryActivity(Long id, NotaryActivityDTO notaryActivityDTO) throws BadRequestException {
        TcccNotaryActivity entity = tcccNotaryActivityRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("NotaryActivity not found"));
        // Update fields
        entity.setAdministrationId(notaryActivityDTO.getAdministrationId());
        entity.setReportYear(notaryActivityDTO.getReportYear());
        entity.setReportMonth(notaryActivityDTO.getReportMonth());
        entity.setReportDate(notaryActivityDTO.getReportDate());
        entity.setNumNotaryContracts(notaryActivityDTO.getNumNotaryContracts());
        entity.setNotaryFeesContracts(notaryActivityDTO.getNotaryFeesContracts());
        entity.setNumOtherNotaryTasks(notaryActivityDTO.getNumOtherNotaryTasks());
        entity.setNotaryFeesOtherTasks(notaryActivityDTO.getNotaryFeesOtherTasks());
        entity.setTotalTasks(notaryActivityDTO.getTotalTasks());
        entity.setTotalFees(notaryActivityDTO.getTotalFees());
        entity.setTaxContribution(notaryActivityDTO.getTaxContribution());
        entity.setNumNotaryOfficesReporting(notaryActivityDTO.getNumNotaryOfficesReporting());
        entity.setNumNotariesWorking(notaryActivityDTO.getNumNotariesWorking());
        TcccNotaryActivity updatedEntity = storeUtils.update(tcccNotaryActivityRepository, entity);
        return mapToDTO(updatedEntity);
    }

    @Override
    public ResponseEntity<ApiResponseV1> deleteNotaryActivity(Long id) {
        try {
            tcccNotaryActivityRepository.deleteById(id);
            return new ResponseEntity<>(new ApiResponseV1(true, 1, "Thành công", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<PagingResult>> aggregateByStp(String fromDateRaw, String toDateRaw, String cityId, int pageNumber, int numberPerPage, String yearReport, String monthReport, String aTypes, boolean loadDetail) {
        int offset = 0;
        if (pageNumber > 1) {
            offset = (pageNumber - 1) * numberPerPage;
        }
        try {
            Date fromDate = null;
            Date toDate = null;
            List<Integer> monthReportList = new ArrayList<>();
            //get Current month
            Integer lastMonthReport = Calendar.getInstance().get(Calendar.MONTH) + 1;
            if (fromDateRaw != null) {
                fromDate = format.parse(fromDateRaw);
            }
            if (toDateRaw != null) {
                toDate = format.parse(toDateRaw);
            }
            List<String> provinceCodes = new ArrayList<>();
            if (H.isTrue(cityId)) {
                String[] arrCityId = cityId.split(",");
                for (String s : arrCityId) {
                    provinceCodes.add(s);
                }
            }

            AccUser userLogin = (AccUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userLogin.getType().equals(Constants.TYPE_USER.SO_TU_PHAP)) {
                provinceCodes = Arrays.asList(String.valueOf(userLogin.getAdministrationId()));
            }

            if (monthReport != null) {
                String[] monthArr = monthReport.split(",");
                for (String month : monthArr) {
                    monthReportList.add(Integer.valueOf(month));
                }
                monthReportList.stream().sorted();
                lastMonthReport = monthReportList.get(monthReportList.size() - 1);
            } else {
                monthReportList.addAll(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
            }




            //chỉ hiển thị các cột được chọn ( cột 1 là tên tỉnh thành, dữ liệu từ cột 2 )
            HashMap<Integer, Boolean> columnAvaliable = new HashMap<>();
            //init columnAvaliable col2 -> col15
            for (int i = 2; i <= 15; i++) {
                columnAvaliable.put(i, true);
            }
            if (aTypes != null) {
                for (int i = 2; i <= 15; i++) {
                    columnAvaliable.put(i, false);
                }
                String[] aTypeArr = aTypes.split(",");
                for (String aType : aTypeArr) {
                    columnAvaliable.put(Integer.valueOf(aType), true);
                }
            }

            String whereClauseClassSic = "  ";
            String whereClause = "  ";
            String whereClauseAddress = "  ";
            if (fromDate != null) {
                whereClause += " AND na.REPORT_DATE >= :fromDate \n ";
            }
            if (toDate != null) {
                whereClause += " AND na.REPORT_DATE <= :toDate \n ";
            }
            if (H.isTrue(provinceCodes) ) {
                whereClauseAddress = " AND dma.ID in :idAdminisLogin \n ";
                whereClause += " and na.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID  in :idAdminisLogin connect by PRIOR stat.ID = stat.PARENT_ID) \n ";
            }
            else {
                whereClause += " and na.ADMINISTRATION_ID in (SELECT stat.ID FROM DM_ADMINISTRATION stat START WITH stat.ID =0 connect by PRIOR stat.ID = stat.PARENT_ID) \n ";
            }
            if (yearReport != null) {
                whereClause += " AND na.REPORT_YEAR = :yearReport \n ";
            }

            String init = " WITH variables AS ( \n" +
                    "    SELECT \n" +
                    "        :lastMonthReport as lastMonthReport \n" +
                    "    FROM dual Where 1 IN :monthReport  \n" +
                    "), \n" +
                    " Latest_Month AS (\n" +
                    "    SELECT ADMINISTRATION_ID,\n" +
                    "           TO_CHAR(MAX(REPORT_DATE), 'YYYY-MM') AS latest_month_with_data\n" +
                    "    FROM TCCC_NOTARY_ACTIVITY\n" +
                    "    WHERE NUM_NOTARIES_WORKING > 0\n" +
                    "    GROUP BY ADMINISTRATION_ID\n" +
                    "),\n" +
                    "Latest_Month_Has_data as (\n" +
                    "SELECT yt.ADMINISTRATION_ID,\n" +
                    "       yt.ID,\n" +
                    "       yt.REPORT_YEAR,\n" +
                    "       yt.REPORT_MONTH,\n" +
                    "       yt.NUM_NOTARIES_WORKING,\n" +
                    "\t\t\t ROW_NUMBER() OVER (PARTITION BY yt.ADMINISTRATION_ID ORDER BY id DESC) AS rn\n" +
                    "FROM TCCC_NOTARY_ACTIVITY yt\n" +
                    "JOIN Latest_Month lm\n" +
                    "ON yt.ADMINISTRATION_ID = lm.ADMINISTRATION_ID\n" +
                    "   AND TO_CHAR(yt.REPORT_DATE, 'YYYY-MM') = lm.latest_month_with_data\n" +
                    "WHERE yt.NUM_NOTARIES_WORKING > 0\n" +
                    ") \n";
            StringBuilder selectedColumns = new StringBuilder("SELECT dma.NAME as cityName ");

            if (columnAvaliable.get(2))
                selectedColumns.append(", MAX(last_m.NUM_NOTARIES_WORKING) AS \"so_ccv_thuc_hien_trong_thang\" ");
            else selectedColumns.append(", 0 AS \"so_ccv_thuc_hien_trong_thang\" ");

            if (columnAvaliable.get(3))
                selectedColumns.append(", SUM(CASE WHEN na.REPORT_MONTH IN :monthReport THEN na.TOTAL_TASKS ELSE 0 END) AS \"so_cv_trong_ky\" ");
            else selectedColumns.append(", 0 AS \"so_cv_trong_ky\" ");

            if (columnAvaliable.get(4))
                selectedColumns.append(", SUM(CASE WHEN na.REPORT_MONTH = :lastMonthReport THEN na.TOTAL_TASKS ELSE 0 END) AS \"so_cv_uoc_tinh_cuoi_ky\" ");
            else selectedColumns.append(", 0 AS \"so_cv_uoc_tinh_cuoi_ky\" ");

            if (columnAvaliable.get(5))
                selectedColumns.append(", SUM(na.NUM_NOTARY_CONTRACTS) AS \"so_cv_cong_chung_hop_dong_giao_dich\" ");
            else selectedColumns.append(", 0 AS \"so_cv_cong_chung_hop_dong_giao_dich\" ");

            if (columnAvaliable.get(6))
                selectedColumns.append(", SUM(na.NUM_OTHER_NOTARY_TASKS) AS \"so_cv_cong_chung_ban_dich_va_loai_khac\" ");
            else selectedColumns.append(", 0 AS \"so_cv_cong_chung_ban_dich_va_loai_khac\" ");

            if (columnAvaliable.get(7))
                selectedColumns.append(", SUM(na.TOTAL_TASKS) AS \"tong_so_cong_viec\" ");
            else selectedColumns.append(", 0 AS \"tong_so_cong_viec\" ");

//            if (columnAvaliable.get(8))
//                selectedColumns.append(", SUM(na.TOTAL_FEES) AS \"tong_thu_lao\" ");
//            else selectedColumns.append(", 0 AS \"tong_thu_lao\" ");

            if (columnAvaliable.get(8))
                selectedColumns.append(", SUM(na.NOTARY_FEES_CONTRACTS) AS \"tong_thu_lao_cc\" ");
            else selectedColumns.append(", 0 AS \"tong_thu_lao_cc\" ");

            if (columnAvaliable.get(9))
                selectedColumns.append(", SUM(na.NOTARY_FEES_OTHER_TASKS) AS \"tong_so_phi_cc\" ");
            else selectedColumns.append(", 0 AS \"tong_so_phi_cc\" ");

            if (columnAvaliable.get(10))
                selectedColumns.append(", SUM(na.TAX_CONTRIBUTION) AS \"tong_nop_ngan_sach\" ");
            else selectedColumns.append(", 0 AS \"tong_nop_ngan_sach\" ");

            if (columnAvaliable.get(11))
                selectedColumns.append(", SUM(na.NUM_NOTARY_OFFICES_REPORTING) AS \"so_tchn_cc_co_bao_cao\" ");
            else selectedColumns.append(", 0 AS \"so_tchn_cc_co_bao_cao\" ");

            if (columnAvaliable.get(12))
                selectedColumns.append(", 0 AS \"so_tchn_cc_dk_hd\" ");
            else selectedColumns.append(", 0 AS \"so_tchn_cc_dk_hd\" ");

            if (columnAvaliable.get(13))
                selectedColumns.append(", 0 AS \"so_ccv_dk_hd\" ");
            else selectedColumns.append(", 0 AS \"so_ccv_dk_hd\" ");

            selectedColumns.append(", dma.ID as cityCode ");


//                    "SELECT \n" +
//                    "    dma.NAME ,  \n" +
//                    "    \n" +
//                    "    SUM(CASE WHEN na.REPORT_MONTH IN :monthReport THEN na.NUM_NOTARIES_WORKING ELSE 0 END) AS \"so_ccv_thuc_hien_trong_thang\",\n" +
//                    "    \n" +
//                    "    SUM(CASE WHEN na.REPORT_MONTH IN :monthReport THEN na.TOTAL_TASKS ELSE 0 END) AS \"so_cv_trong_ky\",\n" +
//                    "    \n" +
//                    "    SUM(CASE WHEN na.REPORT_MONTH = :lastMonthReport THEN na.TOTAL_TASKS ELSE 0 END) AS \"so_cv_uoc_tinh_cuoi_ky\",\n" +
//                    "    \n" +
//                    "    SUM(na.NUM_OTHER_NOTARY_TASKS) AS \"so_cv_cong_chung_hop_dong_giao_dich\", \n" +
//                    "    SUM(na.NUM_OTHER_NOTARY_TASKS) AS \"so_cv_cong_chung_ban_dich_va_loai_khac\", \n" +
//                    "    SUM(na.TOTAL_TASKS) AS \"tong_so_cong_viec\",\n" +
//                    "    SUM(na.TOTAL_FEES) AS \"tong_thu_lao\", \n" +
//                    "    SUM(na.NOTARY_FEES_CONTRACTS) AS \"tong_thu_lao_cc\", \n" +
//                    "    SUM(na.NOTARY_FEES_OTHER_TASKS) AS \"tong_so_phi_cc\", \n" +
//                    "    SUM(na.TAX_CONTRIBUTION) AS \"tong_nop_ngan_sach\",\n" +
//                    "    SUM(na.NUM_NOTARY_OFFICES_REPORTING) AS \"so_tchn_cc_co_bao_cao\",\n" +
//                    "\n" +
//                    "    0 AS \"so_tchn_cc_dk_hd\",\n" +
//                    "    0 AS \"so_ccv_dk_hd\", dma.ID as cityCode \n" +
//                    "\n" +
            String sql = selectedColumns.toString() + " FROM \n" +
                    "     DM_ADMINISTRATION dma " +
                    " left join TCCC_NOTARY_ACTIVITY na on na.ADMINISTRATION_ID = dma.id AND na.REPORT_MONTH BETWEEN 1 AND 12   \n" + whereClause +
                    " left join Latest_Month_Has_data last_m on na.ID = last_m.id   \n" +
                    " WHERE 1=1 AND dma.type = 2 \n" + whereClauseAddress +
                    "     \n" +
                    " GROUP BY \n" +
                    "    dma.NAME, dma.ID  ORDER BY dma.NAME \n";

            String sqlTotal = sql;
            String sqlCount = "SELECT COUNT(*) FROM (" + sql + ")";
            sql = UtilData.paginationOracle(sql, offset, numberPerPage);
            sqlTotal = init + sqlTotal;
            sqlCount = init + sqlCount;
            sql = init + sql;
            Query query = entityManager.createNativeQuery(sql);
            Query queryCount = entityManager.createNativeQuery(sqlCount);
            Query queryTotal = entityManager.createNativeQuery(sqlTotal);
            query.setParameter("monthReport", monthReportList);
            query.setParameter("lastMonthReport", lastMonthReport);
            queryTotal.setParameter("monthReport", monthReportList);
            queryTotal.setParameter("lastMonthReport", lastMonthReport);
            queryCount.setParameter("monthReport", monthReportList);
            queryCount.setParameter("lastMonthReport", lastMonthReport);
            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
                queryTotal.setParameter("fromDate", fromDate);
                queryCount.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
                queryTotal.setParameter("toDate", toDate);
                queryCount.setParameter("toDate", toDate);
            }
            if (H.isTrue(provinceCodes) ) {
                query.setParameter("idAdminisLogin", provinceCodes);
                queryTotal.setParameter("idAdminisLogin", provinceCodes);
                queryCount.setParameter("idAdminisLogin", provinceCodes);
            }
            if (yearReport != null) {
                query.setParameter("yearReport", yearReport);
                queryTotal.setParameter("yearReport", yearReport);
                queryCount.setParameter("yearReport", yearReport);
            }
            Long rowCount = Long.parseLong(queryCount.getResultList().get(0).toString());
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
                reaport.setCol_1(record[i] == null ? "0" : record[i].toString().trim());
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
//                reaport.setCol_15(record[i] == null ? "0" : record[i].toString());
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
//                total.setCol_15(total.getCol_15() == null ? reaport.getCol_15() : String.valueOf((Long.parseLong(reaport.getCol_15()) + Long.parseLong(total.getCol_15()))));

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
//                    reaport.setCol_15(record[i] == null ? "0" : record[i].toString());
//                    i++;


                    items.add(reaport);
                });
                items.add(0, total);
            }

            /*
             * xử lý col_13 và col14
             * col_12 : Số TCHN CC đăng ký hoạt động
             * col_13 : Số CCV đăng ký HNCC
             * Cần gọi sang bên khác lấy thông tin sau đó combine vào list
             */
            if(loadDetail) {
                Long total_col12 = 0L;
                Long total_col13 = 0L;
                for (int i = 1; i < items.size(); i++) {
                    Reaport currentReaport = items.get(i);
                    Long orgId = Long.parseLong(currentReaport.getCol_14());
                    if (!H.isTrue(orgId) || !H.isTrue(yearReport) || !H.isTrue(monthReport)) {
                        continue;
                    }


                    PagingResult pageSearch = new PagingResult();
                    pageSearch.setPageNumber(1);
                    pageSearch.setNumberPerPage(1000000);
                    for (Integer currMonth : monthReportList) {
                        //get first day of month and last day of month
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Integer.parseInt(yearReport));
                        calendar.set(Calendar.MONTH, currMonth - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        Date firstDayOfMonth = calendar.getTime();
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        Date lastDayOfMonth = calendar.getTime();
                        String fromDateTcHNcc = format.format(firstDayOfMonth);
                        String toDateTcHNcc = format.format(lastDayOfMonth);

                        /*
                         * Start get col_12
                         */
                        PagingResult pageTCHNCC = tcccService.list_search(null, orgId, null, pageSearch, fromDateTcHNcc, toDateTcHNcc);
                        if (H.isTrue(pageTCHNCC) && H.isTrue(pageTCHNCC.getItems())) {
                            List<Object[]> listCCV = pageTCHNCC.getItems();
                            total_col12 += listCCV.size();
                            currentReaport.setCol_12(String.valueOf(total_col12));
                        }

                        /*
                         * Start get col_13
                         */
                        PagingResult pageCCV = tcccService.list_ccv(null, orgId, null, pageSearch, fromDateTcHNcc, toDateTcHNcc);
                        if (H.isTrue(pageCCV) && H.isTrue(pageCCV.getItems())) {
                            List<Object[]> listCCV = pageCCV.getItems();
                            total_col13 += listCCV.size();
                            currentReaport.setCol_13(String.valueOf(total_col13));
                        }

                    }


                }
                if (total_col13 > 0) {
                    items.get(0).setCol_13(String.valueOf(total_col13));
                }
                if (total_col12 > 0) {
                    items.get(0).setCol_12(String.valueOf(total_col12));
                }
            }


            PagingResult pagingResult = new PagingResult();
            pagingResult.setPageNumber(pageNumber);
            pagingResult.setNumberPerPage(numberPerPage);
            pagingResult.setRowCount(rowCount);
            pagingResult.setItems(items);
            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", pagingResult), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ApiResponseV1<?>> exportAggregateByStp(String fromDate, String toDate, String cityId, String yearReport, String monthReport, HttpServletResponse response) {
        try {
            PagingResult data = aggregateByStp(fromDate, toDate, cityId, 1, 1000000, yearReport, monthReport, null, true).getBody().getData();
            List<Reaport> items = data.getItems();
            List<String> header = new ArrayList<>();
            //title = BÁO CÁO HOẠT ĐỘNG CÔNG CHỨNGBáo cáo THỐNG KÊ SỐ LƯỢNG TỔ CHỨC HÀNH NGHỀ ĐẤU GIÁ
            //header = STT
            //Tỉnh/Thành phố
            //Số CCV thực hiện (trong tháng báo cáo)
            //Tổng số việc ước tính (Trong kỳ)
            //Số ước tính của tháng cuối kỳ
            //Số công việc công chứng hợp đồng, giao dịch
            //Số công việc công chứng bản dịch và các loại khác
            //Tổng số công việc
            //Tổng thù lao công chứng
            //Tổng số phí công chứng
            //Số tiền nộp ngân sách của các tổ chức công chứng
            //Số TCHN CC có báo cáo
            //Số TCHN CC đăng ký hoạt động
            //Số CCV đăng ký HNCC
            header.add("STT");
            header.add("Tỉnh/Thành phố");
            header.add("Số CCV thực hiện (trong tháng báo cáo)");
            header.add("Tổng số việc ước tính (Trong kỳ)");
            header.add("Số ước tính của tháng cuối kỳ");
            header.add("Số công việc công chứng hợp đồng, giao dịch");
            header.add("Số công việc công chứng bản dịch và các loại khác");
            header.add("Tổng số công việc");
            header.add("Tổng thù lao công chứng");
            header.add("Tổng số phí công chứng");
            header.add("Số tiền nộp ngân sách của các tổ chức công chứng");
            header.add("Số TCHN CC có báo cáo");
            header.add("Số TCHN CC đăng ký hoạt động");
            header.add("Số CCV đăng ký HNCC");



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
                row.add(items.get(i).getCol_13());
                dataExport.add(row);
        }
            String fileName = "BcThongKehoatDong.xlsx";
            String sheetName = "Sheet1";
            String title = "BÁO CÁO HOẠT ĐỘNG CÔNG CHỨNG";
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

    private NotaryActivityDTO mapToDTO(TcccNotaryActivity entity) {
        NotaryActivityDTO dto = new NotaryActivityDTO();
        dto.setAdministrationId(entity.getAdministrationId());
        dto.setReportYear(entity.getReportYear());
        dto.setReportMonth(entity.getReportMonth());
        dto.setReportDate(entity.getReportDate());
        dto.setNumNotaryContracts(entity.getNumNotaryContracts());
        dto.setNotaryFeesContracts(entity.getNotaryFeesContracts());
        dto.setNumOtherNotaryTasks(entity.getNumOtherNotaryTasks());
        dto.setNotaryFeesOtherTasks(entity.getNotaryFeesOtherTasks());
        dto.setTotalTasks(entity.getTotalTasks());
        dto.setTotalFees(entity.getTotalFees());
        dto.setTaxContribution(entity.getTaxContribution());
        dto.setNumNotaryOfficesReporting(entity.getNumNotaryOfficesReporting());
        dto.setNumNotariesWorking(entity.getNumNotariesWorking());
        return dto;
    }
}
