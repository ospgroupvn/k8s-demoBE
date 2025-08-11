package com.osp.bttp.dao.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author sangnk
 * @Created 09/10/2024 - 2:32 CH
 * @project = bttp
 * @_ Mô tả:
 */
public class CreateUserRequest {
    @Schema(description = "Tên đăng nhập", example = "sangnk")
    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @NotNull(message = "Tên đăng nhập không được null")
    private String username;

    @Schema(description = "Mật khẩu", example = "123456")
    @NotEmpty(message = "Mật khẩu không được để trống")
    @NotNull(message = "Mật khẩu không được null")
    private String password;

    @Schema(description = "Họ và tên", example = "Nguyễn Khắc Sang")
    @NotEmpty(message = "Họ và tên không được để trống")
    @NotNull(message = "Họ và tên không được null")
    private String fullName;

    @Schema(description = "Loại. 2 là người dùng, 45 là admin", example = "1")
    @NotNull(message = "type không được null")
    private Integer type;
}
