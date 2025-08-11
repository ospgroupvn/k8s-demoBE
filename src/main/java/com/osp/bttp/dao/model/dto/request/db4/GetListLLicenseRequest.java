package com.osp.bttp.dao.model.dto.request.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:26 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class GetListLLicenseRequest {
    @Schema(description = "Id người sở hữu", example = "1")
    @NotNull(message = "Id người sở hữu không được để trống")
    private Long ownerId;

    @Schema(description = "Loại người sở hữu. 1 - \"LAWYER\" hoặc  2 - \"ORGANIZATION\"", example = "1")
    @NotNull(message = "Loại người sở hữu không được để trống")
    private Integer ownerType;

    @Schema(description = "Loại giấy phép. \"1 - CCHN\", \" 2 - THẺ LS\", \" 3 - GPHN\", \" 4 - GPTL\", \" 5 - ĐKHĐ\"", example = "1")
    @NotNull(message = "Loại giấy phép không được để trống")
    private Integer licenseType;

    @Schema(description = "Số trang. bat dau tu 1", example = "1")
    @NotNull(message = "Số trang không được để trống")
    private Integer pageNumber;

    @Schema(description = "Số bản ghi tren 1 trang", example = "10")
    @NotNull(message = "Số bản ghi 1 trang không được để trống")
    private Integer numberPerPage;

}
