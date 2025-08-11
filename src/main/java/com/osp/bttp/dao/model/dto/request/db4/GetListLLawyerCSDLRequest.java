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
public class GetListLLawyerCSDLRequest {

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "ID tổ chức")
    private Long organizationId;

    @Schema(description = "ID đoàn luật sư")
    private Long assocId;

    @Schema(description = "giới tính. 1: Nam, 2: Nữ. co the truyen nhieu gia tri")
    private List<Integer> listGender;

    @Schema(description = "Số điện thoại")
    private String phone;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Số giấy phép")
    private String licenseNumber;

    @Schema(description = "Là nội địa. 1: Nội địa, 2: Nước ngoài. có thể truyền nhiều giá trị")
    private Integer isDomestic;

    @Schema(description = "Ngày đăng ky. tu ngay. dinh dang dd/MM/yyyy")
    private String registrationDateFrom;

    @Schema(description = "Ngày đăng ky. den ngay. dinh dang dd/MM/yyyy")
    private String registrationDateTo;

    @Schema(description = "Trạng thai.  có thể truyền nhiều giá trị")
    private List<Integer> listStatus;

    @Schema(description = "Tirnh thanh pho")
    private Integer provinceId;

    @Schema(description = "Số trang. bat dau tu 1", example = "1")
    @NotNull(message = "Số trang không được để trống")
    private Integer pageNumber;

    @Schema(description = "Số bản ghi tren 1 trang", example = "10")
    @NotNull(message = "Số bản ghi 1 trang không được để trống")
    private Integer numberPerPage;

}
