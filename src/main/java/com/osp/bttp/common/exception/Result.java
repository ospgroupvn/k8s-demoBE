package com.osp.bttp.common.exception;

public enum Result {

    SUCCESS(1, "Success"),
    SUCCESS2(2, "Success"),
    SUCCESS3(3, "Success"),
    SUCCESS4(4, "Success"),
    FAILED(150, "Failed"),
    UNAUTHORIZED(401, "Unauthorized"),
    TOKEN_EXPIRE_TIME(401, "Token expire time"),
    BAD_REQUEST(400, "Bad request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Api not found"),
    METHOD_NOT_ALLOW(405, "Method not allow"),
    DOC_EXPIRED(156, "Giấy tờ hết hạn sử dụng"),
    VERIFY_FAILED_2(112, "Không xác định được khuôn mặt!"),
    NO_CONTENT(204, "No content"),
    SERVER_ERROR(500, "Hệ thống đang bận"),
    PASSWORD_ERROR(501, "Tài khoản và mật khẩu không đúng, vui lòng kiểm tra lại!"),
    USER_NOT_EXIST(502, "Tài khoản và mật khẩu không đúng, vui lòng kiểm tra lại!"),
    RF_NOT_EXIST(516, "Mã ref không tồn tại, vui lòng kiểm tra lại!"),
    USER_LOCKED(510,
            "Tài khoản %s của bạn đã bị khoá, vui lòng liên hệ hotline 19XXXXX hoặc email xxx@osp.com.vn để được hỗ trợ!"),
    USER_LOCKED_APP(510,
            "Tài khoản đã bị khoá"),
    USER_UNCONFIRM(511, "Tài khoản chờ xác thực"),
    INVALID_PARAM_USER_NAME(10, "Username không hợp lệ"),
    USER_UNCONFIRMM(512, "Tài khoản chưa xác thực"),
    USER_UNKNOWN(503, "User unknown."),


    //------------------------------------------------------------------------
    INVALID_PARAM(10, "Tham số không hợp lệ"),
    LOGGED(11, "Đã đăng nhập"),
    NOT_LOGGED(12, "Chưa đăng nhập"),

    //----------------------------------MSISDN--------------------------------
    MSISDN_OR_SSID_INCORRET(30, "MSISDN hoặc SSID không chính xác"),
    CANT_LOCK_MSISDN(31, "Không thể khóa số"),
    OVER_NUMBER_LOCK_IN_DAY(32, "Quý khách đã vượt quá 10 lần click giữ số này trên ngày"),
    CANT_UNLOCK_MSISDN_IN_ANOTHER_SESSION(33, "Không thể hủy do thuê bao này ở một phiên giao dịch khác"),
    NOT_FOUND_MSISDN(34, "Không tìm thấy thuê bao"),

    //-----------------------------BOOKING ------------------------------------
    BOOKING_FAILED_PASSPORT(40, "CMND/Hộ chiếu này đã đăng ký giữ"),
    BOOKING_FAILED_NUMBER_PHONE(41, "Số điện thoại này đã đăng ký giữ"),
    BOOKING_FAILED_IP(42, "IP này đã đăng ký giữ"),
    BOOKING_FAILED(43, "Không thể giữ số"),
    BOOKING_ESIM_FAILED(44,
            "Chức năng eSim của app Chọn số đang được nâng cấp, xin quý khách đặt sim vật lý, xin cảm ơn!"),
    INVALID_PARAM_BOOKINGCODE(45, "Booking Code không hợp lệ"),
    INVALID_PARAM_PORTRAIT_IMG(46, "Link ảnh chân dung không hợp lệ"),
    INVALID_PARAM_FRONTPASSPOST_IMG(47, "Link ảnh mặt trưóc CMND không hợp lệ"),
    INVALID_PARAM_BACKSIDEPASSPOST_IMG(48, "Link ảnh mặt sau CMND không hợp lệ"),
    INVALID_PARAM_VIDEO_CALL_IMG(49, "Link ảnh mặt sau CMND không hợp lệ"),
    INVALID_PARAM_SIGN_IMG(50, "Ảnh chữ ký không hợp lệ"),
    INVALID_PARAM_SERISIM_IMG(51, "Ảnh seri sim không hợp lệ"),
    INVALID_PARAM_SERISIM_NUMBER(52,
            "Serial sim không trùng khớp với đơn hàng. Vui lòng kiểm tra lại thông tin serial"),

    //-----------------------------PACKAGE----------------------------------
    NOT_FOUND_RECORD(60, "Không tìm thấy bản ghi"),
    CONFIRM_OTP_FAILED(61, "Xác thực OTP thất bại"),
    GET_OTP_FAILED(62, "Lấy OTP thất bại"),

    //-----------------------------User ------------------------------------
    INVALID_PARAM_PASSWORD(19, "Không đúng định dạng mật khẩu"),
    INVALID_PARAM_EMAIL(20, "Không đúng định dạng emil"),
    INVALID_PARAM_USERNAME(21, "Không đúng định dạng username"),
    INVALID_PARAM_MSISDN(22, "Không đúng định dạng số thuê bao"),
    //--------MSALE-QUEUE-----------
    //INVALID_PARAM_VIDEO_CALL(10, "Video call không hợp lệ"),

    //-----------------------------MSALE ------------------------------------
    NO_RESPONSE_RECEIVED(70, "Không nhận được phản hồi"),
    REQUEST_IS_EXITS(71, "Yêu cầu hòa mạng đã được gửi trước đó"),
    CONFIRM_SCAN_ESIM_FAIL_STATUS(72, "Trạng thái của yêu cầu hòa mạng không hợp lệ"),
    MAX_LIMIT_SEND_REQUEST_BY_ID_CARD(73,
            "Số CMND/CCCD/Hộ chiếu quý khách đăng ký đã được sử dụng 3 lần. Vui lòng thử lại!"),
    VIDEOCONFIRM_FAIL(74,
            "Gửi yêu cầu không thành công. Do xác minh video call thất bại! Vui lòng hoàn thành xác minh video call trước"),
    VIDEOCONFIRM_NOT_EXITS(75,
            "Không tìm thấy thông tin xác minh video call! Vui lòng hoàn thành xác minh video call trước"),

    //--------------    ---------------BOOKING ------------------------------
    INVALID_IMAGE(100, "Hình ảnh không hợp lệ hoặc có kích thước lớn hơn 5 mb"),
    NOT_FOUND_BOOKING(101, "Không tìm thấy Booking"),
    NOT_FOUND_GROUP(102, "Không tìm thấy nhóm sim"),
    MAX_LIMIT_BOOKING(103, "Giữ số không thành công. Bạn chỉ được giữ tối đa 30 số. Vui lòng thử lại!"),

    //----------------------------CHANGE_PASS -------------------------------
    INVALID_TYPE_CHANGE_PASS(23, "Loại thay đổi sai hoặc không tồn tại"),

    //----------------------------MOMO ------------------------------------
    NOT_FOUND_TRANS(80, "Không tìm thấy giao dịch"),
    MOMO_CREATE_TRANS_FAIL(81, "Đơn hàng đã tồn tại. Xin vui lòng tạo đơn hàng khác"),

    //------------------------------------------------------------------------
    //--------------------------PAYMENT---------------------------------------
    PAYMENT_CREATE_TRANS_FAIL(100, "Tạo giao dịch thanh toán thất bại!"),

    //Không tìm thấy yêu cầu thanh toán
    NOT_FOUND_PAYMENT(101, "Không tìm thấy yêu cầu thanh toán"),
    PAYMENT_APPROVED(102, "Yêu cầu thanh toán đã được duyệt"),

    //----------------------------MOBIFONE API VERIFY-------------------------
    VERIFY_INFO_IDENTIFICATION(401, "Xác thực thông tin gặp sự cố"),
    ORDER_UPDATE_FAIL_STATUS(90, "Chỉ cho phép Hủy đơn hàng khi trạng thái là chờ xử lý hoặc chờ lấy hàng"),


    //----------------------------Ekyc verify---------------------------------
    MAX_LIMIT_VERIFY(110, "Bạn đã dùng hết số lần xác thực. Vui lòng liên hệ 0931000666 để được hỗ trợ!"),
    WAIT(111,
            "Bạn đã sử dụng hết 3 lần xác thực phiên thứ nhất. Bạn còn 1 phiên xác thực duy nhất. Vui lòng thử lại sau 10p"),
    VERIFY_FAILED(112, "Phát hiện khuôn mặt khác nhau trong quá trình xác thực. Vui lòng thực hiện lại!"),

    ORDER_CODE_INVALID(113, "Mã đơn hàng không hợp lệ!"),
    CLIENT_GUID_INVALID(114, "Client GUID không hợp lệ!"),
    IMAGE_CMT_INVALID(115, "Ảnh CMT không không hợp lệ!"),
    IMAGE_LIVE_INVALID(116, "Ảnh chân dung không hợp lệ!"),
    BOOKING_CODE_INVALID(117, "Mã phiếu giữ số không hợp lệ!"),
    SIM_TYPE_INVALID(118, "Loại sim không hợp lệ!"),
    COUNT_OCR_INVALID(119, "Đã quá 3 lần OCR"),
    COUNT_EKYC_INVALID(119, "Đã quá 3 lần EKYC"),

    //----------------------------ORDER---------------------------------------
    NOT_FOUND_ORDER(130, "Không tìm thấy đơn hàng!"),
    ORDER_ALREADY_PAID(184, "Đơn hàng đã được thanh toán"),


    //----------------------------OCR----------------------------------------
    CANT_READ_ID_NUMBER(150, "Không xác định được id!"),
    CANT_READ_DOC(151, "Không xác định được loại giấy tờ!"),
    TYPE_DOC_INVALID(152, "Loại giấy tờ không chính xác. Vui lòng kiểm tra lại"),
    ID_PASSPORT_INVALID(153, "Số CMND/CCCD không trùng khớp. Vui lòng kiểm tra lại!"),
    DOCUMENT_EXPIRED(154, "Giấy tờ hết hạn!"),

    //----------------------------REQUEST_PAYMENT---------------------------------------
    INVALID_PARAM_BANK(160, "Giấy tờ hết hạn!"),
    ACCOUNT_UNCONFIRM(161, "Vui lòng xác thực tài khoản để bắt đầu kinh doanh trên nền tảng CTV OSP"),
    NOT_ENOUGH_MINIMUM_LIMIT(162,
            "Số dư tài khoản của bạn chưa đủ giá trị tối thiểu 200.000 VNĐ để tạo yêu cầu rút tiền!"),
    OVER_NUMBER_LIMIT_CREATE(163,
            "Bạn không thể tạo yêu cầu thanh toán do giới hạn rút 1 lần/ngày. Vui lòng thử lại sau!"),
    LACK_BANK_INFOR(164,
            "Bạn chưa có thông tin ngân hàng nhận thanh toán. Vui lòng bổ sung thông tin để tạo yêu cầu rút tiền!"),

    //----------------------------ACCOUNT----------------------------------------------
    PARTNER_NOT_FOUND(165, "Không tìm thấy cộng tác viên"),
    MSISDN_IS_EXIST(2, "Số điện thoại đã tồn tại "),
    MSISDN_IS_NOT_EXIST(1, "Số điện thoại không tồn tại"),
    ACCOUNT_LOCKED(170, "Tài khoản đã bị khóa"),
    ACCOUNT_NOT_ACTIVE(187, "Tài khoản không hoạt động"),
    INVALID_TYPE_SOURCE(171, "Loại nguồn không hợp lệ"),

    APP_BOOKING_FAILED(53, "Không thể giữ số"),

    APP_PASS_OLD_INCORRECT(172, "Mật khẩu cũ không chính xác"),

    APP_CHANGE_PASS_FAIL(173, "Thay đổi mật khẩu không thành công"),

    APP_PASS_WORD_INCORRECT(174, "Mật khẩu không chính xác"),

    IPN_MOMO_INCORRECT(175, "IPN MoMo lỗi"),

    BOOKING_MOBIFONE_APP_ERROR(176, "Tạo phiếu giữ số MobiFone lỗi"),

    BOOKING_SAYMEE_APP_ERROR(177, "Tạo phiếu giữ số Saymee lỗi"),

    BOOKING_VINA_APP_ERROR(178, "Tạo đơn VinaPhone lỗi"),

    IPN_VNP_INCORRECT(179, "IPN VnPay lỗi"),

    UNLOCK_MSISDN_FAIL(180, "Hủy khóa số thất bại"),

    ORDER_MSISDN_FAIL(181, "Tạo đơn hàng thất bại"),

//    GET_OTP_PACKAGE_FAIL(182, "Đăng ký gói cước thất bại")

    //không đủ điều kiện để đăng ký gói cước
    NOT_ENOUGH_CONDITION(183, "Không đủ điều kiện để đăng ký gói cước"),
    PASSWORD_MATCH_NOW(173, "Mật khẩu mới không được trùng với mật khẩu hiện tại"),

    COUNT_INVALID_TOKEN(185, "Đã quá số lần gửi mã OTP. Vui lòng thử lại sau 23h59p59s!"),
    SEND_TOKEN_FAIL(186, "Gửi OTP thất bại"),

    INVALID_OTP(187, "Mã xác thực không đúng hoặc hết hạn"),

    INVALID_OTP_8(188, "Mã xác thực không đúng hoặc hết hạn. Bạn còn 2 lần nhập mã!"),

    INVALID_OTP_9(189, "Mã xác thực không đúng hoặc hết hạn. Bạn còn 1 lần nhập mã!"),

    INVALID_OTP_10(190, "Đã quá số lần nhập mã OTP. Vui lòng thử lại sau 23h59p59s!"),

    PHONENUMBER_NOT_SUPPORT (191, "Mạng di động không được hỗ trợ!"),

    IMAGE_BACK_CMT_INVALID(116, "Sai mặt của giấy tờ. Vui lòng kiểm tra lại!"),

    SEND_TOKEN_EIGHT_TIME (192, "Còn 2 lần gửi lại OTP"),
    UPDATE_NVBH_ERROR (193, "Thông tin không hợp lệ"),
    CHECK_REF_CODE_ERROR (194, "Mã giới thiệu không tồn tại"),

    NOT_CCCD(165, "Ảnh giấy tờ không hợp lệ. Vui lòng cung cấp ảnh CCCD!" );

    private int code;
    private String message;

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return (this.code == 1) || (this.code == 2) || (this.code == 3) || (this.code == 4);
    }

}
