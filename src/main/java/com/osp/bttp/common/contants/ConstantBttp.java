package com.osp.bttp.common.contants;

public class ConstantBttp {
    public interface NOTARY_STATUS {

        public static final String DANG_KY_TAP_SU_STR = "Đăng ký tập sự";
        public static final Long DANG_KY_TAP_SU = 0L;
        public static final String DANG_TAP_SU_STR = "Đang tập sự";
        public static final Long DANG_TAP_SU = 1L;
        public static final String TAM_NGUNG_TAP_SU_STR = "Tạm ngừng tập sự";
        public static final Long TAM_NGUNG_TAP_SU = 2L;
        public static final String CHAM_DUT_TAP_SU_STR = "Chấm dứt tập sự";
        public static final Long CHAM_DUT_TAP_SU = 3L;
        public static final String HOAN_THANH_TAP_SU_STR = "Hoàn thành tập sự";
        public static final Long HOAN_THANH_TAP_SU = 4L;
        public static final String CHO_TIEP_NHAN_STR = "Chờ tiếp nhận bổ nhiệm";
        public static final Long CHO_TIEP_NHAN = 5L;
        public static final String CHO_BO_NHIEM_STR = "Chờ bổ nhiệm";
        public static final Long CHO_BO_NHIEM = 6L;
        public static final String DA_BO_NHIEM_STR = "Đã bổ nhiệm";
        public static final Long DA_BO_NHIEM = 7L;
        public static final String DANG_HANH_NGHE_STR = "Đang hành nghề";
        public static final Long DANG_HANH_NGHE = 8L;
        public static final String TAM_DINH_CHI_HANH_NGHE_STR = "Tạm đình chỉ hành nghề";
        public static final Long TAM_DINH_CHI_HANH_NGHE = 9L;
        public static final String DA_BO_NHIEM_LAI_STR = "Đã bổ nhiệm lại";
        public static final Long DA_BO_NHIEM_LAI = 10L;
        public static final String DA_MIEN_NHIEM_STR = "Đã miễn nhiệm";
        public static final Long DA_MIEN_NHIEM = 11L;
        public static final String CHO_BO_SUNG_STR = "Chờ bổ sung";
        public static final Long CHO_BO_SUNG = 12L;
        public static final String DA_BO_SUNG_STR = "Đã bổ sung";
        public static final Long DA_BO_SUNG = 13L;
        public static final String TU_CHOI_BO_NHIEM_STR = "Từ chối bổ nhiệm";
        public static final Long TU_CHOI_BO_NHIEM = 14L;
        public static final String CHUYEN_TAP_SU_STR = "Đã thay đổi nơi tập sự";
        public static final Long CHUYEN_TAP_SU = 15L;
        public static final String CHO_TIEP_NHAN_MIEN_NHIEM_STR = "Chờ tiếp nhận miễn nhiệm";
        public static final Long CHO_TIEP_NHAN_MIEN_NHIEM = 16L;
        public static final String CHO_TIEP_NHAN_BO_NHIEM_LAI_STR = "Chờ tiếp nhận bổ nhiệm lại";
        public static final Long CHO_TIEP_NHAN_BO_NHIEM_LAI = 17L;
        public static final String TU_CHOI_MIEN_NHIEM_STR = "Từ chối miễn nhiệm";
        public static final Long TU_CHOI_MIEN_NHIEM = 18L;
        public static final String CHO_BO_NHIEM_LAI_STR = "Chờ bổ nhiệm lại";
        public static final Long CHO_BO_NHIEM_LAI = 19L;
        public static final String TU_CHOI_BO_NHIEM_LAI_STR = "Từ chối bổ nhiệm lại";
        public static final Long TU_CHOI_BO_NHIEM_LAI = 20L;
        public static final String CHO_MIEN_NHIEM_STR = "Chờ miễn nhiệm";
        public static final Long CHO_MIEN_NHIEM = 21L;
        public static final String THU_HOI_THE_STR = "Thu hồi thẻ";
        public static final Long THU_HOI_THE = 22L;
        public static final String CHO_CAP_THE_STR = "Chờ cấp thẻ";
        public static final Long CHO_CAP_THE = 23L;
        public static final String TU_CHOI_CAP_THE_STR = "Từ chối cấp thẻ";
        public static final Long TU_CHOI_CAP_THE = 24L;
        public static final String DAT_KQ_TAP_SU_STR = "Đạt kết quả tập sự";
        public static final Long DAT_KQ_TAP_SU = 25L;
    }
    public interface STATUS_NOTARY_REG_PRACTICE {
        public static final Long HANH_NGHE = 1L;
        public static final String HANH_NGHE_STR = "Đang hành nghề";
        public static final Long TAM_DINH_CHI = 2L;
        public static final String TAM_DINH_CHI_STR = "Tạm đình chỉ hành nghề";
        public static final Long CHO_CAP_THE = 4L;
        public static final String CHO_CAP_THE_STR = "Chờ cấp thẻ";
        public static final Long TU_CHOI_CAP_THE = 5L;
        public static final String TU_CHOI_CAP_THE_STR = "Từ chối cấp thẻ";
        public static final Long THU_HOI_THE = 6L;
        public static final String THU_HOI_THE_STR = "Thu hồi thẻ";
    }
    public interface TYPE_PENALIZE {

        /*LEVER_PENALIZE = 2*/
        public static final Long CANH_CAO = 1L;
        public static final String CANH_CAO_STR = "Cảnh cáo";
        public static final Long XU_PHAT_TIEN = 2L;
        public static final String XU_PHAT_TIEN_STR = "Phạt tiền";
        public static final Long KHIEN_TRACH = 3L;
        /*LEVER_PENALIZE=1*/
        public static final String KHIEN_TRACH_STR = "Khiển trách";
        public static final Long KY_LUAT_CANH_CAO = 4L;
        public static final String KY_LUAT_CANH_CAO_STR = "Cảnh cáo";
        public static final Long HA_BAC_LUONG = 5L;
        public static final String HA_BAC_LUONG_STR = "Hạ bậc lương";
        public static final Long GIANG_CHUC = 6L;
        public static final String GIANG_CHUC_STR = "Giáng chức";
        public static final Long CACH_CHUC = 7L;
        public static final String CACH_CHUC_STR = "Cách chức";
        public static final Long BUOC_THOI_VIEC = 8L;
        public static final String BUOC_THOI_VIEC_STR = "Buộc thôi việc";
    }
    public interface LEVER_PENALIZE {

        public static final Long KY_LUAT = 1L;
        public static final String KY_LUAT_STR = "Kỷ luật";
        public static final Long XU_LY_VI_PHAM_HANH_CHINH = 2L;
        public static final String XU_LY_VI_PHAM_HANH_CHINH_STR = "Xử lý vi phạm hành chính";
        public static final Long TRACH_NHIEM_HINH_SU = 3L;
        public static final String TRACH_NHIEM_HINH_SU_STR = "Truy cứu trách nhiệm hình sự";
    }

    public interface ADDITIONAL_PENALTY {

        public static final Long XU_PHAT_BX_1 = 1L;
        public static final String XU_PHAT_BX_1_STR = "Tước quyền sử dụng Chứng chỉ hành nghề, Thẻ CCV";
        public static final Long XU_PHAT_BX_2 = 2L;
        public static final String XU_PHAT_BX_2_STR = "Tịch thu tang vật, phương tiện được sử dụng để vi phạm hành chính";
//        public static final Long XU_PHAT_BX_3 = 3L;
//        public static final String XU_PHAT_BX_3_STR = "Tước quyền sử dụng Giấy đăng ký hoạt động, Giấy phép thành lập";
    }
    // kind
    public interface  TYPE_APPOINT{
        public static final Long BO_NHIEM = 1L;
        public static final String BO_NHIEM_STR = "Bổ nhiệm";
        public static final Long MIEN_NHIEM = 2L;
        public static final String MIEN_nhiem_STR = "Miễn nhiệm";
        public static final Long BO_NHIEM_LAI = 3L;
        public static final String BO_NHIEM_LAI_STR = "Bổ nhiệm lại";
    }
    //type:
    public interface KIND_APPOINT{
        public static final Long DE_NGHI = 1L;
        public static final String DE_NGHI_STR = "Đề nghị";
        public static final Long QUYET_DINH = 2L;
        public static final String QUYET_DINH_STR = "Quyết định";
    }

    public interface STATUS_ORG_NOTARY {

        public static final Long DANG_HOAT_DONG = 0L;
        public static final String DANG_HOAT_DONG_STR = "Đang hoạt động";
        public static final Long CHO_THANH_LAP = 1L;
        public static final String CHO_THANH_LAP_STR = "Chờ thành lập";
        public static final Long GIAI_THE = 2L;
        public static final String GIAI_THE_STR = "Đã giải thể";
        public static final Long CHAM_DUT = 3L;
        public static final String CHAM_DUT_STR = "Chấm dứt hoạt động";
        public static final Long CHUA_HOAT_DONG = 4L;
        public static final String CHUA_HOAT_DONG_STR = "Chưa hoạt động";

        public static final Long THU_HOI_QD = 8L;
        public static final String THU_HOI_QD_STR = "Thu hồi QĐ cho phép thành lập";
        public static final Long CAP_GIAY_DK_HOAT_DONG = 9L;
        public static final String CAP_GIAY_DK_HOAT_DONG_STR = "Chờ cấp Giấy ĐKHĐ";
        public static final Long TU_CHOI_DK_HOAT_DONG = 10L;
        public static final String TU_CHOI_DK_HOAT_DONG_STR = "Từ chối cấp Giấy ĐKHĐ";
        public static final Long BI_HOP_NHAT = 11L;
        public static final String BI_HOP_NHAT_STR = "Đã hợp nhất";

        public static final Long DA_THANH_LAP = 12L;
        public static final String DA_THANH_LAP_STR = "Đã thành lập";
        public static final Long TU_CHOI_THANH_LAP = 13L;
        public static final String TU_CHOI_THANH_LAP_STR = "Từ chối thành lập";
        public static final Long THU_HOI_DK_HOAT_DONG = 16L;
        public static final String THU_HOI_DK_HOAT_DONG_STR = "Thu hồi Giấy ĐKHĐ";
        public static final Long THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 17L;
        public static final String THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Thay đổi nội dung ĐKHĐ";
        public static final Long BI_CHUYEN_DOI = 18L;
        public static final String BI_CHUYEN_DOI_STR = "Đã chuyển đổi";
        public static final Long BI_SAT_NHAP = 19L;
        public static final String BI_SAT_NHAP_STR = "Đã sáp nhập";

        public static final Long CHO_BO_SUNG = 99L;
        public static final String CHO_BO_SUNG_STR = "Chờ bổ sung";
    }
    }
