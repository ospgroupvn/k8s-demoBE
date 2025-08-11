package com.osp.bttp.dao.model.mview;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

/**
 * @author sangnk
 * @Created 18/10/2024 - 2:13 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class AccUserView {
    @Schema(description = "Id tài khoàn" , example = "1")
    private Long id;

    @Schema(description = "Tên đăng nhập", example = "admin")
    private String username;

    @Schema(description = "Họ tên", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Loại người dùng. 45 Admin, 40 bộ tư pháp, 30 sở tư pháp", example = "1")
    private Integer type;

    @Schema(description = "Ngày sinh")
    private Date dateBirth;

    @Schema(description = "Số điện thoại", example = "0123456789")
    private String mobile;

    @Schema(description = "Email", example = "")
    private String email;

    @Schema(description = "Địa chỉ", example = "")
    private String address;

    private Integer provinceId;
    private Long administrationId;

    public AccUserView(Long id, String username, String fullName, Integer type, Date dateBirth, String mobile, String email, String address, Integer provinceId, Long administrationId) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.type = type;
        this.dateBirth = dateBirth;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.provinceId = provinceId;
        this.administrationId = administrationId;
    }
}
