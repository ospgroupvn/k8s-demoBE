package com.osp.bttp.dao.model.entity.db4;

/**
 * @author sangnk
 * @Created 13/03/2025 - 4:46 CH
 * @project = bttp
 * @_ Mô tả:
 */
import com.osp.bttp.dao.model.entity.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Organizations")
@Data
public class LOrganization extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Column(name = "org_type", nullable = false) // loại tổ chức hành nghề luật
    private Integer orgType;

    @Column(name = "address")
    private String address;

//    @Schema(description = "Tên đại diện theo pháp luật", example = "Nguyễn Văn A")
    @Column(name = "lawyer_legal_representative_id")
    private Long lawyerLegalRepresentativeId;

    private String phone;

    private String email;

    @Column(name = "is_domestic", length = 1)
    private Integer isDomestic;

    @Column(name = "business_license_number") // số giấy phép đăng ký hoạt động
    @Schema(description = "Số giấy phép đăng ký hoạt động", example = "123456789")
    private String businessLicenseNumber;

    @Column(name = "business_license_issue_date")
    private Date businessLicenseIssueDate;

    @Column(name = "registration_license_number") // mã số thành lập
    private String registrationLicenseNumber;

    @Column(name = "registration_license_issue_date")
    private Date registrationLicenseIssueDate;


    private Integer status;

    @Column(name = "LAWYER_ASSOCIATION_ID") // đoàn luật sư
    private Long lawyerAssociationId;

    @Column(name = "PROVINCE_ID")
    private Long provinceId;

    @Column(name = "WARD_ID")
    private Long wardId;

    @Column(name = "PARENT_ORG_ID") // = 0 nếu là tổ chức. > 0 nếu là chi nhánh
    private Long parentOrgId;


    @Column(name = "NATIONALITY_ID")
    private Long nationalityId;
}