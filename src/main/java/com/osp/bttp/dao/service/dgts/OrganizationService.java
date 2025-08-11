package com.osp.bttp.dao.service.dgts;

import com.osp.bttp.common.dto.ApiResponseV1;
import org.springframework.http.ResponseEntity;

/**
 * @author sangnk
 * @Created 10/10/2024 - 11:31 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface OrganizationService {
    ResponseEntity<ApiResponseV1<?>> getAllOrganization();
}
