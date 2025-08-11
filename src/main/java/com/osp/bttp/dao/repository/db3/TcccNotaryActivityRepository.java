package com.osp.bttp.dao.repository.db3;

import com.osp.bttp.dao.model.entity.db3.TcccNotaryActivity;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sangnk
 * @Created 25/10/2024 - 11:37 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface TcccNotaryActivityRepository extends BaseRepository<TcccNotaryActivity>, JpaSpecificationExecutor<TcccNotaryActivity> {
    @Query(value = "SELECT * FROM tccc_notary_activity WHERE report_year = ?1 AND report_month = ?2", nativeQuery = true)
    List<TcccNotaryActivity> findByReportYearAndReportMonth(int year, int month);
}
