package com.osp.bttp.dao.service.bttp;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.FileOrgNotary;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.OrgNotaryInfoCreateDto;
import com.osp.bttp.dao.model.entity.db3.DmAdministration;
import com.osp.bttp.dao.model.entity.db3.OrgNotaryInfo;
import com.osp.bttp.dao.model.mview.bttp.NotaryInfoInOrg;
import com.osp.bttp.dao.model.mview.bttp.OrgCategoryInfo;
import com.osp.bttp.dao.model.mview.bttp.OrgNotaryDetailResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrgNotaryInfoService {
     OrgNotaryInfo add(OrgNotaryInfoCreateDto orgNotaryInfo);
     OrgNotaryInfo edit(Long id, OrgNotaryInfoCreateDto orgNotaryInfoCreate);
     PagingResult list_search(String name, Long orgId, String status, PagingResult page, String fromDate, String toDate);
     ResponseEntity<ApiResponseV1<List<DmAdministration>>> getAdministrationByType(Long type);
     ResponseEntity<ApiResponseV1<OrgNotaryDetailResponse>> detailOrganizationNotary(Long idOrgNotaryInfo);
     PaginationDto<OrgCategoryInfo> getListOrgCategory(Long adminId,String name , Long pageNo, Long pageSize);

     PagingResult<NotaryInfoInOrg> getPageNotary(Long idOrgNotaryInfo, int offset, int number);
}
