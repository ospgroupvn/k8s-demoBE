package com.osp.bttp.dao.model.dto.response.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sangnk
 * @Created 13/03/2025 - 5:26 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLLawyerCSDLResponse {

    private Long id;

    @Schema(description = "Tên đoàn luật sư")
    private String assocName;

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Ngày sinh")
    private Date dateOfBirth;

    @Schema(description = "Quốc tịch (nước ngoài)")
    private String nationalName;

    @Schema(description = "Số điện thoại")
    private String phone;

    @Schema(description = "Tên tổ chức")
    private String organizationName;

    @Schema(description = "Số giấy phép CCHN")
    private String licenseCCHNNumber;

    @Schema(description = "Số thẻ luật sư")
    private String licenseLSNumber;

    @Schema(description = "Ngày cấp GPHN (nước ngoài)" )
    private Date dateIssueGPHN;

    @Schema(description = "Hình thức hành nghề (nước ngoài)")
    private String typeWork;

    @Schema(description = "Trạng thai.")
    private Integer activeStatus;

    @Schema(description = "Ngày tạo")
    private Date genDate;

}
