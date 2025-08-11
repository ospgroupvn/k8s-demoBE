package com.osp.bttp.dao.model.dto.response.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:18 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLOrganizationCSDLResponse {

    private Long id;

    @Schema(description = "Tên đơn vị", example = "Công ty TNHH ABC")
    private String orgName;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Tên đơn quốc gia (nước ngoài)", example = "Việt Nam")
    private String nationalName;

    @Schema(description = "Tên đại diện theo pháp luật", example = "Nguyễn Văn A")
    private String lawyerLegalRepresentativeName;

    @Schema(description = "Số giấy phép đăng ký hoạt động", example = "123456789")
    private String businessLicenseNumber;

    @Schema(description = "Số giấy phép thành lập", example = "123456789")
    private String registrationLicenseNumber;

    @Schema(description = "Số điện thoại", example = "0123456789")
    private String phone;

    @Schema(description = "Trạng thai. có thể truyền nhiều. 1 hoạt động, 0 tạm ngừng, 2 xóa", example = "1")
    private Integer status;

    @Schema(description = "Ngày cấp giấy phép kinh doanh")
    private Date businessLicenseIssueDate;

    @Schema(description = "Ngày cấp giấy phép đăng ký hoạt động")
    private Date registrationLicenseIssueDate;

    @Schema(description = "Ngày tạo")
    private Date genDate;

}
