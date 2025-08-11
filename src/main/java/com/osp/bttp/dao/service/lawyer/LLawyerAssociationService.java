package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;

/**
 * @author sangnk
 * @Created 17/03/2025 - 8:37 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface LLawyerAssociationService {
    LLawyerAssociation findById(Long lawyerAssociationId);
}
