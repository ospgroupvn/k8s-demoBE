package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.entity.db4.LNationality;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:04 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface LNationalityRepository extends BaseRepository<LNationality>, JpaSpecificationExecutor<LNationality> {
}
