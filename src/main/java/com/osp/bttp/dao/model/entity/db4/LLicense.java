package com.osp.bttp.dao.model.entity.db4;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.Date;

/**
 * @author sangnk
 * @Created 14/03/2025 - 10:32 SA
 * @project = bttp
 * @_ Mô tả: lưu thẻ luật sư (cả các lần chỉnh sửa) và , dữ liệu thay đổi giấy phép hành nghề và đăng ký hoạt động
 */
@Entity
@Table(name = "LICENSES")
@Data
public class LLicense extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LICENSE_ID", nullable = false)
    private Long licenseId;

    @Column(name = "OWNER_ID", nullable = false)
    private Long ownerId;  // luật su or to chuc

    @Column(name = "OWNER_TYPE", nullable = false)
    private Integer ownerType; // 1 - "LAWYER" hoặc  2 - "ORGANIZATION"

    @Column(name = "LICENSE_TYPE", nullable = false)
    private Integer licenseType; // " 2 - THẺ LS", " 3 - GPHN", " 4 - GPTL", " 5 - ĐKHĐ"

    @Column(name = "LICENSE_NUMBER", nullable = false, unique = true)
    private String licenseNumber;  // chung cho 2 - THẺ LS", " 3 - GPHN", " 4 - GPTL", " 5 - ĐKHĐ"

    @Column(name = "DECISION_NUMBER")
    private String decisionNumber;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "REVOCATION_DATE") //Ngày thu hồi giấy phép (dành cho GPHN)
    private Date revocationDate;

    @Column(name = "PRACTICE_FORM") //Hình thức hành nghe
    private Integer practiceForm;

    //noi hành nghe
    @Column(name = "PRACTICE_PLACE") // địa điểm hành nghề
    private String practicePlace;

    @Column(name = "STATUS") // Trạng thái của thẻ/GPHN :Đã cấp , Thu hồi , Gia Hạn (chỉ nước ngoài và 5 năm 1 lần)
    private Integer status;

    @Column(name = "APPROVAL_DOCUMENT")
    private String approvalDocument;

    @Column(name = "CHANGE_CONTENT")
    private String changeContent;

    @Column(name = "TYPE_CHANGE")
    private String typeChange;

    @Column(name = "countChange") // lần gia hạn
    private Integer countChange;

}