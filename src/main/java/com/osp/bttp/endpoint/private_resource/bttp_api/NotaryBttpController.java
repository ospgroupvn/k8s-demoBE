package com.osp.bttp.endpoint.private_resource.bttp_api;

import com.osp.bttp.common.dto.ApiResponseV1;
import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.PaginationDto;
import com.osp.bttp.dao.model.dto.db3.*;
import com.osp.bttp.dao.model.entity.db3.*;
import com.osp.bttp.dao.model.mview.bttp.NotaryChiefResponse;
import com.osp.bttp.dao.model.mview.bttp.NotaryInfoDetailView;
import com.osp.bttp.dao.service.bttp.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
@RequestMapping("/v1/api/private/bttp/notary")
//@Secured({ConstantAuthor.SYSTEM.system, ConstantAuthor.SYSTEM.report})
public class NotaryBttpController {

    @Autowired
    private NotaryInfoService notaryInfoService;

    @Autowired
    private ProbationaryInfoService probationaryInfoService;

    @Autowired
    private NotaryAppointService notaryAppointService;

    @Autowired
    private NotarySuspendWorkService notarySuspendWorkService;

    @Autowired
    private NotaryRegPracticeService notaryRegPracticeService;

    @Autowired
    private NotaryPenalizeService notaryPenalizeService;

    /**
     * A3 - Thêm công chứng viên
     */
    @Operation(
            summary = "A3: Thêm công chứng viên",
            description = "API để thêm mới thông tin công chứng viên"
    )
    @PostMapping("")
    public ResponseEntity<ApiResponseV1<Long>> addNotary(
            @RequestBody @Valid
            @Parameter(description = "Thông tin công chứng viên cần thêm")
            NotaryInfoCreateDto notaryInfoDto) {
        Long id = notaryInfoService.add(notaryInfoDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thêm công chứng viên thành công", id));
    }

    /**
     * A4 - Cập nhật công chứng viên
     */
    @Operation(
            summary = "A4: Cập nhật công chứng viên",
            description = "API để cập nhật thông tin công chứng viên theo ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseV1<String>> editNotary(
            @PathVariable("id")
            @Parameter(description = "ID công chứng viên cần cập nhật")
            Long id,
            @RequestBody @Valid
            @Parameter(description = "Thông tin cập nhật công chứng viên")
            NotaryInfoCreateDto notaryInfoDto) {
        notaryInfoService.edit(id, notaryInfoDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Cập nhật công chứng viên thành công", null));

    }

    @Operation(
            summary = "B1: Thêm thông tin tập sự cho công chứng viên",
            description = "API để thêm mới thông tin tập sự cho công chứng viên theo ID"
    )
    @PostMapping("/probationary/{id}")
    public ResponseEntity<ApiResponseV1<ProbationaryInfo>> addProbationaryInfo(
            @PathVariable("id")
            @Parameter(description = "ID công chứng viên") Long idNotary,
            @RequestBody @Valid
            @Parameter(description = "Thông tin tập sự của công chứng viên")
            ProbationaryInfoCreateDto probationaryInfoDto) {
        ProbationaryInfo probationaryInfo = probationaryInfoService.add(idNotary, probationaryInfoDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thêm thông tin tập sự thành công", probationaryInfo));
    }


    @Operation(
            summary = "Cập nhật quyết định tập sự",
            description = "API dùng để cập nhật thông tin quyết định tập sự"
    )
    @PutMapping("/probationary/{id}")
    public ResponseEntity<ApiResponseV1<ProbationaryInfo>> editProbationaryInfo(
            @PathVariable("id") @Parameter(description = "ID của quyết định tập sự") Long id,
            @RequestBody @Valid
            @Parameter(description = "Thông tin cập nhật quyết định tập sự")
            ProbationaryInfoCreateDto probationaryInfoDto) {

        ProbationaryInfo probationaryInfo = probationaryInfoService.edit(id, probationaryInfoDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Cập nhật quyết định tập sự thành công", probationaryInfo));
    }


    @Operation(
            summary = "C1: Bổ nhiệm công chứng viên lần đầu",
            description = "API để thêm thông tin bổ nhiệm công chứng viên lần đầu"
    )
    @PostMapping("/appoint")
    public ResponseEntity<ApiResponseV1<NotaryAppoint>> addNotaryAppoint(
            @RequestBody @Valid
            @Parameter(description = "Thông tin bổ nhiệm công chứng viên")
            NotaryAppointCreateDto notaryAppointCreateDto) {
        NotaryAppoint notaryAppoint = notaryAppointService.addNotaryAppoint(notaryAppointCreateDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Bổ nhiệm thành công", notaryAppoint));
    }

    @Operation(
            summary = "C2: Cập nhật bổ nhiệm công chứng viên",
            description = "API để sửa thông tin bổ nhiệm công chứng viên"
    )
    @PutMapping("/appoint/{id}")
    public ResponseEntity<ApiResponseV1<NotaryAppoint>> editNotaryAppoint(
            @PathVariable("id") Long id,
            @RequestBody @Valid
            @Parameter(description = "Thông tin cập nhật bổ nhiệm công chứng viên")
            NotaryAppointCreateDto notaryAppointCreateDto) {
        NotaryAppoint notaryAppoint = notaryAppointService.editNotaryAppoint(id, notaryAppointCreateDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Cập nhật bổ nhiệm thành công", notaryAppoint));
    }

    @DeleteMapping("/appoint/{id}")
    public ResponseEntity<ApiResponseV1<String>> deleteAppoint(@PathVariable("id") Long id) {
        notaryAppointService.deleteNotaryAppoint(id);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Xóa bổ nhiệm/miễn nhiệm thành công", null));
    }

    @Operation(
            summary = "C4: Tạm đình chỉ công chứng viên",
            description = "API để thêm thông tin tạm đình chỉ công chứng viên đang hoạt động"
    )
    @PostMapping("/suspend")
    public ResponseEntity<ApiResponseV1<NotarySuspendWork>> suspendNotary(
            @RequestBody @Valid
            @Parameter(description = "Thông tin tạm đình chỉ công chứng viên")
            NotarySuspendWorkCreateDto dto) {
        NotarySuspendWork notarySuspendWork = notarySuspendWorkService.add(dto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Tạm đình chỉ thành công", notarySuspendWork));
    }

    @PutMapping("/suspend/{id}")
    public ResponseEntity<ApiResponseV1<NotarySuspendWork>> updateSuspend(
            @PathVariable Long id,
            @RequestBody @Valid NotarySuspendWorkCreateDto dto) {
        NotarySuspendWork notarySuspendWork = notarySuspendWorkService.edit(id, dto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Cập nhật tạm đình chỉ thành công", notarySuspendWork));

    }

    @DeleteMapping("/suspend/{id}")
    public ResponseEntity<ApiResponseV1<String>> deleteSuspend(@PathVariable("id") Long id) {
        notarySuspendWorkService.delete(id);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Xóa tạm đình chỉ thành công", null));
    }

    @Operation(
            summary = "C4: Đăng ký hành nghề công chứng viên",
            description = "API để thêm thông tin đăng ký hành nghề công chứng viên"
    )
    @PostMapping("/re-practice")
    public ResponseEntity<ApiResponseV1<NotaryRegPractice>> addNotaryRegPractice(
            @RequestBody @Valid
            @Parameter(description = "Thông tin đăng ký hành nghề")
            NotaryRegPracticeCreateDto notaryRegPracticeCreateDto
    ) {
        NotaryRegPractice notaryRegPractice = notaryRegPracticeService.add(notaryRegPracticeCreateDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Đăng ký hành nghề thành công", notaryRegPractice));
    }

    @Operation(
            summary = "C4: Chỉnh sửa thông tin đăng ký hành nghề công chứng viên",
            description = "API để chỉnh sửa thông tin đăng ký hành nghề công chứng viên"
    )
    @PutMapping("/re-practice/{id}")
    public ResponseEntity<ApiResponseV1<NotaryRegPractice>> editNotaryRegPractice(
            @PathVariable("id") Long id,
            @RequestBody @Valid
            @Parameter(description = "Thông tin đăng ký hành nghề cần chỉnh sửa")
            NotaryRegPracticeCreateDto notaryRegPracticeCreateDto
    ) {
        NotaryRegPractice notaryRegPractice = notaryRegPracticeService.edit(id, notaryRegPracticeCreateDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Chỉnh sửa đăng ký hành nghề thành công", notaryRegPractice));
    }

    @DeleteMapping("/re-practice/{id}")
    public ResponseEntity<ApiResponseV1<String>> deleteNotaryRegPractice(@PathVariable("id") Long id) {
        notaryRegPracticeService.delete(id);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Xóa đăng ký hành nghề thành công", null));
    }


    @Operation(
            summary = "C5: Kỷ luật công chứng viên",
            description = "API để thêm thông tin kỷ luật công chứng viên"
    )
    @PostMapping("/penalize")
    public ResponseEntity<ApiResponseV1<NotaryPenalize>> addNotaryPenalize(
            @RequestBody @Valid
            @Parameter(description = "Thông tin xử lý kỷ luật công chứng viên")
            NotaryPenalizeCreateDto notaryPenalizeCreateDto) {
        NotaryPenalize notaryPenalize = notaryPenalizeService.add(notaryPenalizeCreateDto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Thêm kỷ luật thành công", notaryPenalize));
    }

    @PutMapping("/penalize/{id}")
    public ResponseEntity<ApiResponseV1<NotaryPenalize>> updatePenalize(
            @PathVariable Long id,
            @RequestBody @Valid NotaryPenalizeCreateDto dto) {
        NotaryPenalize notaryPenalize = notaryPenalizeService.edit(id, dto);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Cập nhật xử phạt thành công", notaryPenalize));
    }

    @DeleteMapping("/penalize/{id}")
    public ResponseEntity<ApiResponseV1<String>> deletePenalize(@PathVariable("id") Long id) {
        notaryPenalizeService.delete(id);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 1, "Xóa xử phạt thành công", null));
    }

    @GetMapping("/ccv/search")
    public ResponseEntity<ApiResponseV1<PagingResult>> searchCcv(
            @RequestParam(value = "name", required = false) @Parameter(description = "Tên công chứng viên") String name,
            @RequestParam(value = "orgId", required = false) @Parameter(description = "Id sở tư pháp ( lấy từ api getAdministrationByType ) ") Long orgId,
            @RequestParam(value = "orgCode", required = false) @Parameter(description = "(Id tổ chức ) ") Long orgCode,
            @RequestParam(value = "fromDate", required = false) @Parameter(description = "Từ ngày ( dd/mm/yyyy )") String fromDate,
            @RequestParam(value = "toDate", required = false) @Parameter(description = "Đến ngày ( dd/mm/yyyy )") String toDate,
            @RequestParam(value = "status", required = false) @Parameter(description = "Trạng thái hoạt động") String status,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = false, defaultValue = "10") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage,
            HttpServletRequest request) {
        try {
            PagingResult page = new PagingResult();
            page.setPageNumber(pageNumber);
            page.setNumberPerPage(numberPerPage);
            page = notaryInfoService.list_ccv(name, orgId,orgCode, status, page, fromDate, toDate);

            return new ResponseEntity<>(new ApiResponseV1<>(true, 1, "Thành công", page), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Thất bại", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ccv/export")
    public ResponseEntity<ApiResponseV1<?>> exportCcv(
            @RequestParam(value = "name", required = false) @Parameter(description = "Tên công chứng viên") String name,
            @RequestParam(value = "orgId", required = false) @Parameter(description = "Id sở tư pháp ( lấy từ api getAdministrationByType ) ") Long orgId,
            @RequestParam(value = "status", required = false) @Parameter(description = "Trạng thái hoạt động") String status,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") @Parameter(description = "Số trang") int pageNumber,
            @RequestParam(value = "numberPerPage", required = false, defaultValue = "10000") @Parameter(description = "Số lượng bản ghi trên 1 trang") int numberPerPage,
            HttpServletResponse response) {

        PagingResult page = new PagingResult();
        page.setPageNumber(pageNumber);
        page.setNumberPerPage(numberPerPage);
        return notaryInfoService.exportExcelNotaryInfo(name, orgId, status, page, null, null, response);
    }


    //A3 - chi tiết công chứng viên
    @Operation(summary = "A3 : Chi tiết công chứng viên", description = "Chi tiết công chứng viên")
    @GetMapping("/detailNotary/{idNotaryInfo}")
    public ResponseEntity<ApiResponseV1<NotaryInfoDetailView>> detailNotary(
            @PathVariable("idNotaryInfo") @Parameter(description = "Id công chứng viên") Long id) {
        try {
            return notaryInfoService.detailNotary(id);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi hệ thống", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "A3 : Danh sách người đại diện", description = "Api lấy danh sách người đại diện")
    @GetMapping("/chief-category")
    public ResponseEntity<PaginationDto<NotaryChiefResponse>> getListNotaryChiefCategory(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "0") Long pageNo,
            @RequestParam(required = false, defaultValue = "50") Long pageSize
    ) {
        PaginationDto<NotaryChiefResponse> result = notaryInfoService.getListNotaryChiefCategory(name, pageNo, pageSize);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "A3 : Xóa thông tin chung công chứng viên")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommonNotary(@PathVariable("id") Long id) {
        notaryInfoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "A3 : Xóa thông tin chung và thông tin liên quan công chứng viên")
    @DeleteMapping("/all/{id}")
    public ResponseEntity<ApiResponseV1<String>> deleteInfoNotary(@PathVariable("id") Long id) {
        String message = notaryInfoService.deleteNotaryInfo(id);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 200, "Tải lên thành công", message));
    }

    @Operation(summary = "Upload file đính kèm vào tài liệu")
    @PostMapping("/document/upload/{id}")
    public ResponseEntity<ApiResponseV1<String>> uploadFileToDocument(
            @PathVariable("id") Long idDocument,
            @RequestParam("file") MultipartFile multipartFile) throws InternalException {
        String filePath = notaryAppointService.uploadFileToDocument(idDocument, multipartFile);
        return ResponseEntity.ok(new ApiResponseV1<>(true, 200, "Tải lên thành công", filePath));

    }

    @Operation(summary = "Tải xuống file theo đường dẫn")
    @GetMapping("/document/download")
    public ResponseEntity<?> downloadFile(
            @RequestParam("path") String pathFile,
            @RequestParam("fileName") String fileName) throws InternalException {
        return notaryAppointService.downloadFile(pathFile, fileName);

    }

    @Operation(summary = "Xóa file theo đường dẫn")
    @DeleteMapping("/document/delete")
    public ResponseEntity<ApiResponseV1<Boolean>> deleteFile(@RequestParam("path") String filePath) {
        try {
            boolean deleted = notaryAppointService.deleteFile(filePath);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponseV1<>(true, 200, "Xóa file thành công", true));
            } else {
                return new ResponseEntity<>(new ApiResponseV1<>(false, 400, "Không thể xóa file", false), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseV1<>(false, 500, "Lỗi khi xóa file", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}




