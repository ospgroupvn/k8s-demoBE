package com.osp.bttp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ConstantResponse {
    SUCCESS(0, true, "Thành công"),
    ERROR(1, false, "Hệ thống bận, xin vui lòng thử lại"),
    INVALID_PARAM(400, false, "Tham số không hợp lệ"),
    SERVER_ERROR(500, false, "Server Error"),
    UNAUTHORIZED(401, false, "Chưa xác thực"),
    FORBIDDEN(403, false, "Truy cập bị cấm"),
    NOT_FOUND(404, false, "Không tìm thấy"),

    //AUTHENCATION
    PASSWORD_NOT_CHANGED(10, true, "Mật khẩu chưa được đổi"),
    ACCOUNT_LOCKED(0, false, "Tài khoản đang bị tạm khóa!"),

    //PARTNER
    ACCOUNT_NOT_FOUND(20, false, "Tài khoản không tồn tại"),
    PASSWORD_NOT_MATH(21, false, "Mật khẩu không khớp"),
    MSISDN_IS_EXIST(22, false, "Tài khoản đã tồn tại"),
    REF_CODE_NOT_EXIST(23, false, "Mã giới thiệu không hợp lệ"),
    MOBILE_IS_EXIST(24, false, "Đã tồn tại số điện thoại!"),
    EMAIL_IS_EXIST(25, false, "Đã tồn tại email!"),
    NVBH_NOT_EXIST(29, false, "Địa bàn bạn chọn chưa có nhân viên hỗ trợ!"),

    //BOOKING
    MAX_LIMIT_BOOKING(40, false, "Số CMND/CCCD/Hộ chiếu đang thực hiện giữ 3 số thuê bao chưa xử lý!"),
    REQUEST_STATUS_ERROR(41, false, "Thất bại do yêu cầu đã thay đổi trạng thái, vui lòng kiểm tra lại!"),
    FILE_DATA_ERROR(42, false, "File import không hợp lệ!"),
    QUANTITY_NOT_ENOUGH(43, false, "Thất bại do số lượng xuất không đủ, vui lòng kiểm tra lại!"),
    IMPORT_NOT_SUCCESS(44,false, "Import file không thành công")

    ;
    private int errorCode;
    private boolean isSuccess;
    private String message;

    ConstantResponse(int errorCode, boolean isSuccess) {
        this.errorCode = errorCode;
        this.isSuccess = isSuccess;
    }

    public final int getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public final String getMessage() {
        return message;
    }
}
