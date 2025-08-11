package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.dao.model.entity.db4.LLawyerUpdateInfoHis;

import java.util.List;

/**
 * @author sangnk
 * @Created 17/03/2025 - 2:24 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerUpdateInfoHisService {
    List<LLawyerUpdateInfoHis> getAllByLawyerId(Long lawyerId);
}
