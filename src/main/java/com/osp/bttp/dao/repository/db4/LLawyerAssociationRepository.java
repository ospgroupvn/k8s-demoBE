package com.osp.bttp.dao.repository.db4;

import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author sangnk
 * @Created 17/03/2025 - 8:37 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerAssociationRepository extends BaseRepository<LLawyerAssociation>, JpaSpecificationExecutor<LLawyerAssociation> {
    LLawyerAssociation getByAssocNameContaining(String name);

}
