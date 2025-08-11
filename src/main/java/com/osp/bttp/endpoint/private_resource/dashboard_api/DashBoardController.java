package com.osp.bttp.endpoint.private_resource.dashboard_api;

import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.mview.LogSystemView;
import com.osp.bttp.dao.service.bttp.LogSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class DashBoardController implements DashBoardResource {
    @Autowired
    private LogSystemService logSystemService;

    public ResponseEntity<PaginationDto<LogSystemView>> searchLogSystem(
            Integer page,
            Integer size,
            String groups,
            String actor,
            String input) {
        PaginationDto<LogSystemView> logSystemViewPaginationDto = logSystemService.searchLogSystem(page, size, groups, actor, input);
        return new ResponseEntity<>(logSystemViewPaginationDto, HttpStatus.OK);
    }
}





