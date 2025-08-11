package com.osp.bttp.common.contants;

import java.util.Objects;

/**
 * Created by Admin on 1/4/2018.
 */
public class ConstantsDGTS {

    //Danh sách biểu thức bất quy tắc
    public static final String REGEX_NUMBER = "^[0-9]*$";
    public static final String REGEX_SEARCH_NUMBER = "^[0-9*]*$";
    public static final String REGEX_TEXT_NUMBER = "^[a-zA-Z0-9]+$";
    public static final String REGEX_TEXT_USERNAME = "^[_a-zA-Z0-9]+$";
    public static final String REGEX_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:Feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String REGEX_PHONE = "^((\\+84)|0){1}[0-9]{9,10}$";
    public static final String REGEX_USERNAME = "^[a-z0-9/._-]{6,30}$";
    public static final int VBPL_TYPE = 1;
    public static final int CDDH_TYPE = 2;
    public static final String PASS_DEFAULT = "zaq1@BTP";
    public static final String PASS_RESTORE_OWNER = "dgts.2020";

    public class Log { //Log hệ thống

        public static final String system = "SYSTEM";
        public static final String user = "USER";
        public static final String category = "CATEGORY";
    }

    public static final Long IS_ACTIVE = 1L;
    public static final String SCHEMA_NAME = "BTP_DGTS";
//    public static final String SCHEMA_NAME = "BTP_DGTS_DEMO";

    public static class STATUS {

        public static final int ACTIVE = 1;
        public static final int INACTIVE = 0;
        public static final int BLOCK = 2;
        public static final int PENDING = 3;
    }

    public static class BELONG_TYPE {

        public static final Long THONG_BAO_LCTCDG = 0L;
        public static final Long THONG_BAO_VDG = 1L;
    }

    public static final Long IS_NOT_CONFIRMED = 0L;

    //HAS_FILE
    public static final Long HAS_FILE_YES = 1L;

    public static class Auctioneer {

        public static final Long AUC_TVHD = 4L;
        public static final Long AUC_PROBATIONARY = 0L; // TẬP SỰ VIÊN
        public static final Long AUC_AUCTIONEER = 1L;//ĐẤU GIÁ VIÊN
        public static final Long AUC_MANAGER = 2L;// GIÁM ĐỐC TRUNG TÂM
        public static final Long IS_DELETED = 3L;// Đã bị xóa

        public static final Long NOT_SCAN = 0L;//Không quét
        public static final Long MANAGER_SCAN = 1L;//chuyển về giám đốc
        public static final Long AUC_SCAN = 2L;//đưa về đấu giá viên

    }

    public static class Organization {

        public static final String ORG_ALL = "ALL";// danh sách tất cả tổ chức đấu giá
        public static final String ORG_ALL_HIS = "ALL_HIS";
        public static final Long ORG_CENTER_AUCTIONEER = 0L; // TRUNG TÂM TỔ CHỨC ĐẤU GIÁ
        public static final Long ORG_ENTERPRISE = 1L;//DOANH NGHIỆP TỔ CHỨC ĐẤU GIÁ TÀI SẢN
        public static final Long ORG_BRANCH = 11L;// CHI NHÁNH

        public static final Long ORG_OFFICE = 12L;//VĂN PHÒNG ĐẠI DIỆN
        public static final Long ORG_HN = 4L;//Doanh nghiệp được sinh ra từ quyết định hợp nhất
        public static final Long ORG_CONTEXT_STATUS_COOPERATION = 0L;//TỔ CHỨC KHÔNG PHỨC HỢP
        public static final Long ORG_CONTEXT_STATUS_TH = 5L;//TỔ CHỨC ĐÃ BỊ THU HỒI;
        public static final Long ORG_CONTEXT_STATUS_KHAC = 1L;// tổ chức bị dừng hoạt động
        public static final Long ORG_CONTEXT_STATUS_HN = 3L;// tổ chức hình thành do hợp nhất

        public static final Long ORG_ROOT_PARRENT = 0L;//MẶC ĐỊNH ĐẤU GIÁ CHA
        public static final Long ORG_CONGTYHOPDANH = 2L;
        public static final Long ORG_STATUS_ACTIVE = 0L;
        public static final Long ORG_STATUS_NHĐ = 1L;
        public static final Long ORG_DEL = 3L;
        public static final Long NOT_SCAN = 0L;//không quét
        public static final Long NHD_SCAN = 1L;//quét để dừng hoạt động tổ chức
        public static final Long HD_SCAN = 2L;//quét để cập nhật tổ chức
        public static final Long ADD_SCAN = 3L;//quét tổ chức thêm mới hoạt động
        public static final Long REP_SCAN = 4L;//quét cập nhật lại tổ chức sau khi cấp lại
        public static final Long ORG_STATUS_TNHD=15L; //tạm dựng hoạt động tổ chức và chi nhánh đi kèm
        public static final Long ORG_STATUS_TNHDCN=16L; //tạm dừng hoạt động chi nhánh

    }

    public static class OranizationHis {

        public static final Long ORG_UPDATE_THONG_THUONG = 0L;//UPDATE THÔNG THƯỜNG
        public static final Long ORG_CAP_LAI_GP = 1L;//CẤP LẠI GIẤY PHÉP
        public static final Long ORG_UPDATE_TTTT = 2L;//UPDATE LẠI TRẠNG THÁI THÔNG THƯỜNG
        public static final Long ORG_THUHOIGIAYPHEP = 3L;//THU HỒI GIẤY PHÉP ĐKHĐ
        public static final Long ORG_SATNHAP = 4L;//dừng hoạt động do sát nhập
        public static final Long ORG_HOPNHAT = 5L;//dừng hoạt động do hợp nhất
        public static final Long ORG_GIAITHE = 6L;//dừng hoạt động do giải thể
        public static final Long ORG_PHASAN = 7L;//dừng hoạt động do phá sản
        public static final Long ORG_THANHLAPCAPMOI = 8L;//thành lập/cấp mới giấy phép hoạt động
        public static final Long ORG_THAYDOINOIDUNG = 9L;//thay đổi nội dung hoạt động của tcđg
        public static final Long ORG_CHUYENDOITT_DN = 10L;//thay đổi nội dung hoạt động của tcđg
        public static final Long ORG_CAPGP_CN = 11L;
        public static final Long ORG_CAPGP_VPDD = 12L;
    }

    //has value file
    public static final String DOT3 = "...";
    public static final String ENTER = "\n";
    public static final String SPACE = " ";
    public static final String COLON = ":";
    public static final String MASK = "\"";
    public static final String PLUS = "\\+";
    public static final String MINUS = "\\-";
    public static final String SEMI_COLON = ";";
    public static final String UNIT_SEPARATOR = "_";
    public static final String PERCENT = "%";
    public static final String VERTICAL_LINE = "|";
    public static final String SHARP = "#";
    public static final String BULLET = "-";

    public static class PUBLISH_STATUS {

        ///status danh sach khong chua noi dung khong phu hop 
        public static final Long WAITING_PUBLISH_AUTO = 0L;//TU_DONG_DUYET_CONG_KHAI
        public static final Long WAITING_APPROVE_NOT_CONTAIN_SENSITIVE_CONTENT = 1L;//CHO DUYET CONG KHAI
        public static final Long UNPUBLISH_NOT_CONTAIN_SENSITIVE_CONTENT = 2L;//KHONG ĐC CONG KHAI | YEU CAU NHAP LAI 

        ///status danh sach co chua noi dung khong phu hop        
        public static final Long WAITING_APPROVE_CONTAIN_SENSITIVE_CONTENT = 5L;//CHO DUYET CONG KHAI
        public static final Long UNPUBLISH_CONTAIN_SENSITIVE_CONTENT = 6L;//KHONG ĐC CONG KHAI | YEU CAU NHAP LAI 

        //trang thai cong khai
        public static final Long DA_CONG_KHAI = 4L;
        public static final Long CHUA_CONG_KHAI = 11L;

        public static final Long DA_XOA = 20L;
    }

    public static class IS_CONFIRMED {

        public static final Long IS_CONFIRMED_YES = 1L;
        public static final Long IS_CONFIRMED_NO = 0L;
    }

    public static class IS_PUBLISH {

        public static final Long IS_PUBLISH_YES = 1L;
        public static final Long IS_PUBLISH_NO = 0L;
    }

    public static class HAS_FILE {

        public static final Long HAS_FILE_YES = 1L;
    }

    public static class AUCTIONEER_TYPE {

        public static final Long TAP_SU_VIEN = 0L;//Tạm thời dùng cho người phụ trách chưa đc cấp CCHN và thẻ ĐGV
        public static final Long DAU_GIA_VIEN = 1L;
        public static final Long DGV_PHU_TRACH = 2L;
        public static final Long DA_XOA = 3L;
        public static final Long THANH_VIEN_HOP_DANH = 4L;
    }

    public static class IS_PIC{
        public static final Long NO = 0L;
        public static final Long YES = 1L;
    }

    public static class PUBLISH_AUCTION_STATUS {

        public static final Long DA_XOA = 0L;
        public static final Long CHUA_CONG_KHAI = 11L;
        public static final Long CHO_BO_TU_PHAP_DUYET = 5L;
        public static final Long DA_CONG_KHAI = 4L;
        public static final Long YEU_CAU_CAP_NHAT_LAI = 8L;
        public static final Long CHO_CONG_KHAI_TU_DONG = 12L;
        public static final Long DA_CAP_NHAT_LAI = 13L;
        public static final Long DA_GO_DANG_TAI = 14L;
    }

    public static class ACTION_LOGS {

        public static final String XOA = "0";
        public static final String TAO_MOI = "1";
        public static final String SUA = "2";
        public static final String GO_CONG_KHAI_DANG = "3";
    }

    public static class AUCTION_RESULT_STATUS {

        public static final Long CHUA_HOAN_THANH_DAU_GIA = 0L;
        public static final Long DA_HOAN_THANH_DAU_GIA = 1L;
    }

    public static class SUBMIT_FILE {

        public static final Long CHUA_NOP_HO_SO = 0L;
        public static final Long DA_NOP_HO_SO = 1L;
    }

    public static class SEND_REQUEST {

        public static final Long CHUA_GUI_YEU_CAU = 0L;
        public static final Long DA_GUI_YEU_CAU = 1L;
    }

    public static final Long DELETE = 0L;
    public static final Long PUBLISH = 1L;
    public static final Long NOT_PUBLISH = 2L;

    public static class RESULT_PUBLISH_STATUS {

        public static final Long UNPUBLISH = 0L;
        public static final Long CHO_CONG_KHAI_AUTO = 0L;
        ///status danh sach khong chua noi dung khong phu hop
        public static final Long CHO_DUYET_CONG_KHAI = 5L;
        public static final Long YEU_CAU_CAP_NHAT_LAI = 8L;
        //        public static final Long REQUEST_TYPE_AGAIN_NOT_CONTAIN_SENSITIVE_CONTENT = 3L;
//
//        ///status danh sach co chua noi dung khong phu hop        
//        public static final Long PUBLISHED_APPROVED_CONTAIN_SENSITIVE_CONTENT = 6L;
//        public static final Long WAITING_APPROVE_CONTAIN_SENSITIVE_CONTENT = 7L;
//        public static final Long REQUEST_TYPE_AGAIN_CONTAIN_SENSITIVE_CONTENT = 8L;
        //update
        public static final Long CHO_CONG_KHAI = 11L;
        public static final Long CHO_CONG_KHAI_TU_DONG = 12L;
        public static final Long DA_CONG_KHAI = 4L;
        public static final Long DA_CAP_NHAT_LAI = 13L;
    }

    public static class AUCTIONEER_HIS_STATUS {

        public static final Long UPDATE_TT = 0L;
        public static final Long UPDATE_LICHSU_TUDONG = 0L;

    }

    public static class Category {

        public static final String ASSET_TYPE = "ASSET_TYPE";
        public static final String ORG_TYPE = "ORG_TYPE";
    }

    public static final Long AUCTION_TYPY = 0L;
    public static final Long AUCTION_STATUS = 11L;

    public static class REQ_PUBLISH_SOURCE {

        public static final Long NGUOI_CO_TAI_SAN = 0L;
        public static final Long BO_TU_PHAP = 1L;
    }

    public static class REQ_CONFIRM_SOURCE {

        public static final Long NGUOI_CO_TAI_SAN = 0L;
        public static final Long TCDG = 1L;
    }

    public static class SOURCE_LOG {

        public static final Long SYSTEM_AUTO = 0L;
        public static final Long ADMIN_USER = 1L;
    }

    public static class ACT_TYPE {

        public static final Long UPDATE_THONG_THUONG = 0L;
        public static final Long CAP_MOI_CCHN = 1L;
        public static final Long CAP_LAI_CCHN = 2L;
        public static final Long THU_HOI_CCHN = 3L;
        public static final Long XOA_CCHN = 4L;
        public static final Long CAP_MOI_THE_DGV = 5L;
        public static final Long CAP_LAI_THE_DGV = 6L;
        public static final Long THU_HOI_THE_DGV = 7L;
        public static final Long XOA_THE_DGV = 8L;
        public static final Long UPDATE_TRANG_THAI_HOAT_DONG = 9L;
        public static final Long THOI_HANH_NGHE_TAI_TC = 10L;

        public static final Long THEM_MOI_NGUOI_PHU_TRACH = 15L;
        public static final Long NGUOI_PHU_TRACH_LAM_DAI_DIEN = 16L;

    }

    public static class AUCTIONEER_STATUS {

        public static final Long DANG_HOAT_DONG = 0L;
        public static final Long NGUNG_HOAT_DONG = 1L;

    }

    public static class SentitiveKeyWord_STATUS {

        public static final Long DANG_HOAT_DONG = 0L;
        public static final Long NGUNG_HOAT_DONG = 1L;
    }

    public static final Long BAN_TIN_CHA = 0L;

    public static class OBJECT_TYPE {

        public static final Long THONG_BAO_LUA_CHON = 1L;
        public static final Long THONG_BAO_KET_QUA_LUA_CHON = 2L;
        public static final Long THONG_BAO_VIEC_DAU_GIA = 3L;
        public static final Long THONG_BAO_KET_QUA_DAU_GIA = 4L;
    }

    public static class ACT_HIS_TYPE {

        public static final Long THONG_BAO_LUA_CHON = 1L;
        public static final Long THONG_BAO_KET_QUA_LUA_CHON = 2L;
        public static final Long THONG_BAO_VIEC_DAU_GIA = 3L;
        public static final Long THONG_BAO_KET_QUA_GIA = 4L;

    }

    public static class PUBLISH_SOURCE {

        public static final Long NGUOI_CO_TAI_SAN = 0L;
        public static final Long TO_CHUC_DAU_GIA = 1L;
        public static final Long BO_TU_PHAP = 2L;
        public static final Long HE_THONG = 3L;
    }

    public static class FILE_TYPE {

        public static final Long PDF = 0L;
        public static final Long IMAGE = 1L;
    }

    public static class FILE_PATH {

        public static final String THONG_BAO_KET_QUA_LUA_CHON = "file/ThongBaoKetQuaLuaChon/";
        public static final String FILE_SERVER = "/home/btpdgts/files_upload/";

    }

    public static class USER_TYPE {

        public static final Long BTP = 0L;
        public static final Long STP = 1L;
        public static final Long TT_TC = 2L;
        public static final Long CN = 3L;

    }

    public static class USER_ROVOKE {

        public static final Long IS_ROVOKE = 1L;
        public static final Long NOT_ROVOKE = 0L;
    }

    public static class VAN_BAN_PHAP_LUAT {

        public static final Long HIEN_PHAP = 210L;
        public static final Long LUAT = 211L;
        public static final Long BO_LUAT = 212L;
        public static final Long PHAP_LENH = 213L;
        public static final Long LENH = 214L;
        public static final Long QUYET_DINH = 215L;
        public static final Long NGHI_DINH = 216L;

    }

    public static final String LIKE_OPERATOR = "%";
    public static final String VAN_BAN_DIEU_HANH = "VBDH";
    public static final String CHI_DAO_DIEU_HANH = "CDDH";
    public static final String CONG_VAN = "CV";
    public static final String NGHIEP_VU = "NV";
    public static final String HOI_DAP_PHAP_LUAT = "HDPL";
    public static final String VAN_BAN_PHAP_LUAT = "VBPL";

    public static final String GIOI_THIEU_CONG_THONG_TIN_1 = "GT1";
    public static final String GIOI_THIEU_CONG_THONG_TIN_2 = "GT2";
    public static final String GIOI_THIEU_CONG_THONG_TIN_3 = "GT3";
    public static final String GIOI_THIEU_CONG_THONG_TIN_4 = "GT4";

    public static class APPROVATE_STATUS {

        public static final Long CHUA_XAC_MINH = 0L;
        public static final Long DA_XAC_MINH = 1L;
    }

    public static final String SYS_UPLOAD_FILE = "SYS_UPLOAD_FILE";
    public static final Integer TIN_TUC_NOI_BAT = 228;
    public static final String FOLDER_UPLOAD_NEWS = "/assets/publicv3/images";
    public static final String CAT_TYPE_NEWS = "NEWS";

    public static class DOJ_STATUS {

        public static final Long DA_XOA = 0L;
        public static final Long DA_CONG_KHAI = 1L;
        public static final Long CHUA_CONG_KHAI = 2L;
    }

    public static class EMAIL_TYPE {

        public static final Long DANG_KY = 0L;
        public static final Long LAY_LAI_MAT_KHAU = 1L;

    }

    public static class ROLE_DEFAULT {

        public static final String CN = "Nhóm quyền mặc định_CN";
        public static final String TT_TC = "Nhóm quyền mặc định_TC";
        public static final String STP = "Nhóm quyền mặc định _STP";
        public static final String BTP = "Nhóm quyền mặc định _BTP";

    }

    public static class USER_STATUS {

        public static final int LOCK = 0;
        public static final int ACTIVE = 1;
        public static final int DELETE = 2;
        public static final int REVOKE = 3;

    }
    public static class USER_SOURCE_BLOCK {

        public static final Long YES_SOURCE = 1L;//không khóa
        public static final Long NO_SOURCE = 0L;//khóa do dừng hoạt động

    }

    public static final String KEY_CHE_DO_TIEN_KIEM = "SYS_VALIDATE_OWNER";

    public static class SCAN_TYPE {

        public static final Long SCAN_NO = 0L;
        public static final Long SCAN_YES = 1L;
    }

    public static class TRAINEE_STATUS {

        public static final Long DANG_TS = 1L;
        public static final Long TAM_DUNG_TS = 2L;
        public static final Long HOAN_THANH_TS = 3L;
        public static final Long DANG_KY_KT_CHUA_CO_KQ = 4L;
        public static final Long CHUA_DAT_KQ = 5L;
        public static final Long DAT_KQ = 6L;
        public static final Long CHAM_DUT_TS = 7L;
        public static final Long CHUYEN_SANG_STP_KHAC = 8L;
        public static final Long XOA_TAP_SU = 9L;
        public static final Long TT_THAY_DOI_NOI_TS = 10L;
        public static final Long TT_TAM_NGUNG_TS = 11L;
        public static final Long XOA_QUA_TRINH = 13L;
        public static final Long DANG_KY_KT_DA_CO_KQ = 12L;
    }

    public static class PASSWORD_CHANGED {

        public static final Long CHANGED = 1L;
        public static final Long NOT_CHANGE = 0L;
    }

    public static class AUCTION_APPORVE_STATUS {

        public static final Long CHUA_XAC_MINH = 0L;
        public static final Long DA_XAC_MINH = 1L;

    }

    public static final String STATUS_SYNC = "STATUS_SYNC";

    public static class STATUS_SYNC_DATA {
        public static final String ON = "1";
        public static final String OFF = "0";
    }

    public static final Long BLOCK_AUCTION = 1L;
    public static final Long TYPE_USER_LOGS = 2L;

    public static String getStrActType(Long actType) {
        if (Objects.equals(actType, ACT_TYPE.CAP_MOI_CCHN)) {
            return "Đã cấp CCHN";
        }
        if (Objects.equals(actType, ACT_TYPE.CAP_LAI_CCHN)) {
            return "Đã cấp lại CCHN";
        }
        if (Objects.equals(actType, ACT_TYPE.THU_HOI_CCHN)) {
            return "Đã thu hồi CCHN";
        }
        if (Objects.equals(actType, ACT_TYPE.CAP_MOI_THE_DGV)) {
            return "Đã cấp Thẻ ĐGV";
        }
        if (Objects.equals(actType, ACT_TYPE.CAP_LAI_THE_DGV)) {
            return "Đã cấp lại Thẻ ĐGV";
        }
        if (Objects.equals(actType, ACT_TYPE.THU_HOI_THE_DGV)) {
            return "Đã thu hồi Thẻ ĐGV";
        }
        return "";
    }
}
