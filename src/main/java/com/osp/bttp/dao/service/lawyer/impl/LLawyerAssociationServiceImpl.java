package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.dao.model.entity.db4.LLawyerAssociation;
import com.osp.bttp.dao.repository.db4.LLawyerAssociationRepository;
import com.osp.bttp.dao.service.lawyer.LLawyerAssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sangnk
 * @Created 17/03/2025 - 8:39 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Service
public class LLawyerAssociationServiceImpl implements LLawyerAssociationService {

    private final LLawyerAssociationRepository lLawyerAssociationRepository;

    public LLawyerAssociationServiceImpl(LLawyerAssociationRepository lLawyerAssociationRepository) {
        this.lLawyerAssociationRepository = lLawyerAssociationRepository;
    }

    @Override
    public LLawyerAssociation findById(Long lawyerId) {
        return lLawyerAssociationRepository.findById(lawyerId).orElse(null);
    }
}
