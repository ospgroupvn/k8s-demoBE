package com.osp.bttp.dao.service.tccc;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.db3.NotaryActivityDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author sangnk
 * @Created 25/10/2024 - 11:57 SA
 * @project = bttp
 * @_ Mô tả:
 */
public interface NotaryActivityService {
    ResponseEntity<ApiResponseV1<PagingResult>> search(String fromDate, String toDate, Long cityId, int pageNumber, int numberPerPage, String yearReport, String monthReport);

    ResponseEntity<ApiResponseV1<NotaryActivityDTO>> createNotaryActivity(NotaryActivityDTO notaryActivityDTO);

    List<NotaryActivityDTO> getActivitiesByMonth(int year, int month);

    NotaryActivityDTO updateNotaryActivity(Long id, NotaryActivityDTO notaryActivityDTO) throws BadRequestException;

    ResponseEntity<ApiResponseV1> deleteNotaryActivity(Long id);

    ResponseEntity<ApiResponseV1<PagingResult>> aggregateByStp(String fromDate, String toDate, String cityId, int pageNumber, int numberPerPage, String yearReport, String monthReport, String aTypes, boolean loadDetail);

    ResponseEntity<ApiResponseV1<?>> exportAggregateByStp(String fromDate, String toDate, String cityId, String yearReport, String monthReport, HttpServletResponse response);
}
