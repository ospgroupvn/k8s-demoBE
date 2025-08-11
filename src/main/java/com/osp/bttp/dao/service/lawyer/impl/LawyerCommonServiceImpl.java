package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.common.utils.H;
import com.osp.bttp.dao.model.mview.db1.Reaport;
import com.osp.bttp.dao.service.lawyer.LawyerCommonService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sangnk
 * @Created 17/03/2025 - 5:18 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
public class LawyerCommonServiceImpl implements LawyerCommonService {
    @PersistenceContext(unitName = "db4")
    private EntityManager entityManager;

    @Override
    public List<Reaport> getDataMap(Integer type, Long cityCode) {
        try {
            List<Reaport> dataMap = new ArrayList<>();

            // Xác định bảng và cột dựa trên type
            String tableName = "LAWYERS";
            String idColumn = "LAWYER_ID";
            if (type.intValue() == 2) {
                tableName = "ORGANIZATIONS";
                idColumn = "ORG_ID";
            }

            // Xác định điều kiện WHERE
            String whereClause = "";
            if (H.isTrue(cityCode)){
                if (cityCode < 100) {
                    whereClause = " AND p.CODE = " + cityCode;
                } else {
                    whereClause = " AND p.ID = '" + cityCode + "'";
                }
            }

            // Truy vấn native SQL
            StringBuilder sql = new StringBuilder(
                    "WITH ProvinceCount AS ( " +
                            "    SELECT p.ID, p.NAME AS province_name, " +
                            "           COUNT(t." + idColumn + ") AS count " +
                            "    FROM  C_CATEGORY p " +
                            "    LEFT JOIN " + tableName + " t ON p.ID = t.PROVINCE_ID " +
                            "    WHERE p.CAT_TYPE = 'TP' " + whereClause +
                            "    GROUP BY p.ID, p.NAME " +
                            "), " +
                            "TotalCount AS ( " +
                            "    SELECT COUNT(" + idColumn + ") AS total " +
                            "    FROM " + tableName + " " +
                            ") " +
                            "SELECT 'Tổng số' AS name, total AS value FROM TotalCount " +
                            "UNION ALL " +
                            "SELECT province_name AS name, count AS value FROM ProvinceCount " +
                            "WHERE province_name IS NOT NULL " +
                            "ORDER BY name"
            );

            Query query = entityManager.createNativeQuery(sql.toString());
            List<Object[]> resultList = query.getResultList();

            for (Object[] row : resultList) {
                String name = (String) row[0]; // Tên tỉnh/thành hoặc "Tổng số"
                Long value = H.isTrue(row[1]) ? ((Number) row[1]).longValue() : 0L;

                Reaport reaport = new Reaport();
                reaport.setCol_1(name);
                reaport.setCol_2(String.valueOf(value));
                dataMap.add(reaport);
            }

            return dataMap;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
