package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.dao.model.dto.db3.ProbationaryInfoCreateDto;
import com.osp.bttp.dao.model.entity.db3.ProbationaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryProbationaryResponse;

import java.util.List;

public interface ProbationaryInfoService {
    ProbationaryInfo add(Long idNotary, ProbationaryInfoCreateDto probationaryInfo);
    ProbationaryInfo edit(Long id, ProbationaryInfoCreateDto probationaryInfo);
    NotaryProbationaryResponse getProbationaryInfoByIdNotary(Long idNotary);
    void deleteByNotaryId(Long idNotary);
}
