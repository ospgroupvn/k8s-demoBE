package com.osp.bttp.dao.model.entity.db4;

/**
 * @author sangnk
 * @Created 13/03/2025 - 4:42 CH
 * @project = bttp
 * @_ Mô tả:
 */
import com.osp.bttp.dao.model.entity.base.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "LAWYERS")
@Data
public class LLawyer extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lawyer_id", nullable = false)
    private Long lawyerId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "identity_card_number")
    @NotNull(message = "CCCD không được để trống")
    private String identityCardNumber;

    @Column(length = 1)
    private Integer gender;

    private String phone;

    private String address;

    private String email;

    @Column(name = "is_domestic", length = 1)
    @NotNull(message = "IsDomestic không được để trống")
    private Integer isDomestic;

    @Column(name = "status", length = 1)
    private Integer status;

    @Column(name = "LAWYER_ASSOCIATION_ID") // đoàn luật sư
    private Long lawyerAssociationId;

    @Column(name = "PROVINCE_ID")
    private Long provinceId;

    @Column(name = "WARD_ID")
    private Long wardId;

    //quốc tịch
    @Column(name = "NATIONALITY_ID")
    private Long nationalityId;

    @Column(name = "CERTIFICATE_NUMBER")
    @Schema(description = "Số chứng chỉ, giấy phép hành nghề")
    private String certificateNumber;

    @Column(name = "DECISION_NUMBER")
    @Schema(description = "Số quyết định")
    private String decisionNumber;

    @Column(name = "ISSUE_DATE")
    @Schema(description = "Ngày cấp chứng chỉ, giấy phép hành nghề")
    private Date issueDate;

    @Column(name = "REVOKE_DECISION_NUMBER")
    @Schema(description = "Số quyết định thu hồi")
    private String revokeDecisionNumber;

    @Column(name = "REVOKE_DATE")
    @Schema(description = "Ngày thu hồi")
    private Date revokeDate;

    @Column(name = "ACTIVITY_STATUS")
    @Schema(description = "Trạng thái hành nghề. 4 - đang hành nghề. 6 - Đã thu hồi CCHN/GPHN")
    private Integer activityStatus;

    @Column(name = "YEAR_REPORT")
    @Schema(description = "Năm dùng cho mục đích báo cáo")
    private String yearReport;

    @Column(name = "note")
    @Schema(description = "Ghi chú")
    private String note;

    public LLawyer(){
        this.yearReport = String.valueOf(LocalDate.now().getYear());
    }

}