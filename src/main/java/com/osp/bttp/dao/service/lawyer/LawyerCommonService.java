package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.dao.model.mview.db1.Reaport;

import java.util.List;

/**
 * @author sangnk
 * @Created 17/03/2025 - 5:17 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LawyerCommonService {
    List<Reaport> getDataMap(Integer type, Long cityCode);
}
