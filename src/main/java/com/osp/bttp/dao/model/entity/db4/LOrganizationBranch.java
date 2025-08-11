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
@Table(name = "Organization_Branch")
@Data
public class LOrganizationBranch extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "org_branch_id", nullable = false)
    private Long orgBranchId;

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Column(name = "address")
    private String address;

//    @Schema(description = "Tên đại diện theo pháp luật", example = "Nguyễn Văn A")
    @Column(name = "lawyer_legal_representative_id")
    private Long lawyerLegalRepresentativeId;

    private String phone;

    @Column(name = "business_license_number") // số giấy phép đăng ký hoạt động
    @Schema(description = "Số giấy phép đăng ký hoạt động", example = "123456789")
    private String businessLicenseNumber;

    @Column(name = "business_license_issue_date")
    private Date businessLicenseIssueDate;

}