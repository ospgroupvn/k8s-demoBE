package com.osp.bttp.dao.model.dto.response.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:26 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class GetListLLawyerResponse {
    private Long id;

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "ID tổ chức")
    private Long organizationId;

    @Schema(description = "Tên tổ chức")
    private String organizationName;

    @Schema(description = "giới tính. 1: Nam, 2: Nữ")
    private Integer gender;

    @Schema(description = "Số điện thoại")
    private String phone;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Số giấy phép CCHN")
    private String licenseCCHNNumber;

    @Schema(description = "Là nội địa. trong nước 1 hoặc ngoài nước 0 ")
    private Integer isDomestic;

    @Schema(description = "Ngày đăng ky.dinh dang ISO ")
    private Date registrationDate;

    @Schema(description = "Ngày cấp CCHN/GPHN ")
    private Date issueDate;

    @Schema(description = "Trạng thai.")
    private Integer status;

    @Schema(description = "Số thẻ luật sư")
    private String licenseLSNumber;

    @Schema(description = "Ngày sinh")
    private Date dateOfBirth;

    @Schema(description = "Địa chỉ")
    private String address;

    @Schema(description = "Quóc tịch")
    private String nationalName;


    @Column(name = "ACTIVITY_STATUS")
    @Schema(description = "Trạng thái hành nghề. 4 - đang hành nghề. 6 - Đã thu hồi CCHN/GPHN")
    private Integer activityStatus;

    @Schema(description = "Hình thức hành nghề.")
    private Integer practiceForm;

    public GetListLLawyerResponse(String orgName, String fullName, Date genDate, String soCCHn, String soTheLs, String address, Integer status, Date dateOfBirth, Long id, Integer practiceForm,Date issueDate,String nationalName) {
        this.organizationName = orgName;
        this.fullName = fullName;
        this.registrationDate = genDate;
        this.licenseCCHNNumber = soCCHn;
        this.licenseLSNumber = soTheLs;
        this.address = address;
        this.activityStatus = status;
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.practiceForm = practiceForm;
        this.issueDate = issueDate;
        this.nationalName = nationalName;
    }
}
