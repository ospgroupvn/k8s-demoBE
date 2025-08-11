package com.osp.bttp.dao.model.dto.response.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:18 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class GetListLOrganizationResponse {
    private Long id;

    @Schema(description = "Tên tỉnh/TP", example = "Hà Nội")
    private String provinceName;

    @Schema(description = "Quốc gia", example = "Việt Nam")
    private String nationalName;

    @Schema(description = "Tên đơn vị", example = "Công ty TNHH ABC")
    private String orgName;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Số giấy phép đăng ký hoạt động", example = "123456789")
    private String businessLicenseNumber;

    @Schema(description = "Số giấy phép thành lập", example = "123456789")
    private String registrationLicenseNumber;

    @Schema(description = "Số điện thoại", example = "0123456789")
    private String phone;

    @Schema(description = "Email", example = "Email")
    private String email;

    @Schema(description = "Trong nước hay nước ngoài. trong nước 1 hoặc ngoài nước 0'", example = "1")
    private Integer isDomestic;

    @Schema(description = "Trạng thai. có thể truyền nhiều. 1 hoạt động, 0 tạm ngừng, 2 xóa", example = "1")
    private Integer status;

//    @Schema(description = "Số luật sư có trong tổ chức", example = "1")
//    private Integer lawyerNumber;

    @Schema(description = "Tên đại diện theo pháp luật", example = "Nguyễn Văn A")
    private String lawyerLegalRepresentativeName;

    @Column(name = "business_license_issue_date")
    @Schema(description = "Ngày cấp giấy phép kinh doanh")
    private Date businessLicenseIssueDate;

    @Column(name = "registration_license_issue_date")
    @Schema(description = "Ngày cấp giấy phép đăng ký hoạt động")
    private Date registrationLicenseIssueDate;

    public GetListLOrganizationResponse(String orgName, String address, String lawyerLegalRepresentativeName, String businessLicense, Integer status , Long id, String registrationNumber, Date businessLicenseIssueDate, Date registrationLicenseIssueDate,String provinceName,String nationalName) {
        this.orgName = orgName;
        this.address = address;
        this.lawyerLegalRepresentativeName = lawyerLegalRepresentativeName;
        this.businessLicenseNumber = businessLicense;
        this.status = status;
        this.id = id;
        this.registrationLicenseNumber = registrationNumber;
        this.businessLicenseIssueDate = businessLicenseIssueDate;
        this.registrationLicenseIssueDate = registrationLicenseIssueDate;
        this.provinceName = provinceName;
        this.nationalName = nationalName;
    }
}
