package com.osp.bttp.dao.model.dto.request.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:18 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class GetListLOrganizationRequest {
    @Schema(description = "Tên đơn vị", example = "Công ty TNHH ABC")
    private String orgName;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Số điện thoại", example = "0123456789")
    private String phone;

    @Schema(description = "Email", example = "Email")
    private String email;

    @Schema(description = "Trong nước hay nước ngoài. 1: Trong nước, 2: Nước ngoài", example = "[1[")
    private List<Integer> listIsDomestic;

    @Schema(description = "Ngày đăng ký. từ ngày. định dạng dd/MM/yyyy", example = "01/01/2022")
    private String registrationDateFrom;

    @Schema(description = "Ngày đăng ký. đến ngày. định dạng dd/MM/yyyy", example = "01/01/2022")
    private String registrationDateTo;

    @Schema(description = "Trạng thai. có thể truyền nhiều. 1 hoạt động, 0 tạm ngừng, 2 xóa", example = "[1]")
    private List<Integer> listStatus;

    @Schema(description = "Id tỉnh thành")
    private Long provinceId;

    @Schema(description = "Id parent. trong trường hợp lấy danh sách con, chi nhánh, phòng ban")
    private Long parentId;

    @Schema(description = "Số trang. bat dau tu 1")
    @NotNull(message = "Số trang không được để trống")
    private Integer pageNumber;

    @Schema(description = "Số bản ghi tren 1 trang")
    @NotNull(message = "Số bản ghi 1 trang không được để trống")
    private Integer numberPerPage;
}
