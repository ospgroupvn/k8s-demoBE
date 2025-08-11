package com.osp.bttp.endpoint.private_resource.dashboard_api;

import com.osp.bttp.common.contants.Constants;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.mview.LogSystemView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(DashBoardResource.DASHBOARD_RESOURCE)
public interface DashBoardResource {
    String DASHBOARD_RESOURCE = Constants.API_VERSION1 + "/dashboard";

    @GetMapping("/search-log")
    ResponseEntity<PaginationDto<LogSystemView>> searchLogSystem(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "groups", required = false) String groups,
            @RequestParam(name = "actor", required = false) String actor,
            @RequestParam(name = "input", required = false) String input);
}
