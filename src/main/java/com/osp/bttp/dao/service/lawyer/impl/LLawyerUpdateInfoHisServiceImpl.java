package com.osp.bttp.dao.service.lawyer.impl;

import com.osp.bttp.dao.model.entity.db4.LLawyerUpdateInfoHis;
import com.osp.bttp.dao.repository.db4.LLawyerAssociationRepository;
import com.osp.bttp.dao.repository.db4.LLawyerUpdateInfoHisRepository;
import com.osp.bttp.dao.service.lawyer.LLawyerUpdateInfoHisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sangnk
 * @Created 17/03/2025 - 2:25 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Service
@RequiredArgsConstructor
public class LLawyerUpdateInfoHisServiceImpl implements LLawyerUpdateInfoHisService {
    private final LLawyerUpdateInfoHisRepository lLawyerUpdateInfoHisRepository;
    @Override
    public List<LLawyerUpdateInfoHis> getAllByLawyerId(Long lawyerId) {
        return lLawyerUpdateInfoHisRepository.getAllByLawyerId(lawyerId);
    }
}
