package com.osp.bttp.dao.model.dto.request.db4.dashBoard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReportOrgActive {

    List<Long> provinceIds;

    List<Integer> isDomestics;

    Integer pageNum;

    Integer pageSize;
}
