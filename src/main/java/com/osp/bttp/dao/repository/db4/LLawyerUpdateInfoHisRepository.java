package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.entity.db4.LLawyerUpdateInfoHis;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sangnk
 * @Created 17/03/2025 - 2:24 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerUpdateInfoHisRepository extends BaseRepository<LLawyerUpdateInfoHis>, JpaSpecificationExecutor<LLawyerUpdateInfoHis> {
    @Query("SELECT l FROM LLawyerUpdateInfoHis l WHERE l.lawyerId = ?1")
    List<LLawyerUpdateInfoHis> getAllByLawyerId(Long lawyerId);
}
