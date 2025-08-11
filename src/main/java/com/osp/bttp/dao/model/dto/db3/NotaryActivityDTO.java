package com.osp.bttp.dao.model.dto.db3;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author sangnk
 * @Created 25/10/2024 - 11:39 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class NotaryActivityDTO {
    private Long id;
    @Schema(description = "Id đơn vị quản lý")
    private Long administrationId;
    @Schema(description = "Năm báo cáo")
    @NotNull(message = "Năm báo cáo không được để trống")
    private Long reportYear;
    @Schema(description = "Tháng báo cáo")
    @NotNull(message = "Tháng báo cáo không được để trống")
    private Long reportMonth;
    @Schema(description = "Ngày tháng năm báo cáo")
    @NotNull(message = "Ngày tháng năm báo cáo không được để trống")
    private Date reportDate;
    @Schema(description = "Số công việc công chứng hợp đồng, giao dịch")
    @NotNull(message = "Số công việc công chứng hợp đồng, giao dịch không được để trống")
    private Long numNotaryContracts;
    @Schema(description = "Thù lao công chứng hợp đồng, giao dịch", example = "1000000")
    @NotNull(message = "Thù lao công chứng hợp đồng, giao dịch không được để trống")
    private Long notaryFeesContracts;
    @Schema(description = "Số công việc công chứng bản dịch và các loại khác", example = "100")
    private Long numOtherNotaryTasks;
    @Schema(description = "Thù lao công chứng bản dịch và các loại khác", example = "1000000")
    private Long notaryFeesOtherTasks;
    @Schema(description = "Tổng số công việc", example = "200")
    private Long totalTasks;
    @Schema(description = "Tổng thù lao", example = "2000000")
    private Long totalFees;
    @Schema(description = "Trích nộp ngân sách, thuế", example = "200000")
    private Long taxContribution;
    @Schema(description = "Số tổ chức hành nghề CC có báo cáo", example = "10")
    private Long numNotaryOfficesReporting;
    @Schema(description = "Số công chứng viên", example = "100")
    private Long numNotariesWorking;
    @Schema(description = "Tỉnh thành", example = "Hà Nội")
    private String province;
}
