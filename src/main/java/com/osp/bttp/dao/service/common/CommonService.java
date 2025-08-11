package com.osp.bttp.dao.service.common;


import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.dao.model.dto.response.db4.GetListLawyerAssoc;
import com.osp.bttp.dao.model.dto.response.db4.GetListOrg;
import com.osp.bttp.dao.model.entity.db3.Category;
import com.osp.bttp.dao.model.entity.db4.LNationality;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author sangnk
 * @Created 10/10/2024 - 4:24 CH
 * @project = bttp
 * @_ Mô tả:
 */
public interface CommonService {
    List<Category> getAllProvince();

    ResponseEntity<ApiResponseV1<List<GetListLawyerAssoc>>> getAllLawyerAssoc();

    ResponseEntity<ApiResponseV1<List<GetListOrg>>> getAllOrg();

    public ResponseEntity<ApiResponseV1<List<LNationality>>> getAllNational() ;

    List<Category> getAllWardByProvinceId(Long id);
}
