package com.osp.bttp.dao.model.dto.request.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListLawyerInOrgRequest {
    @Schema(description = "Tổ chức")
    @NotNull(message = "Id tổ chức không được để trống")
    private Long organizationId;

    @Schema(description = "Trong nước / ngoài nước")
    private Integer isDominic;

    @Schema(description = "Số trang. bat dau tu 1")
    @NotNull(message = "Số trang không được để trống")
    private Integer pageNumber;

    @Schema(description = "Số bản ghi tren 1 trang")
    @NotNull(message = "Số bản ghi 1 trang không được để trống")
    private Integer numberPerPage;
}
