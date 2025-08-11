package com.osp.bttp.common.contants;


import com.osp.bttp.dao.model.mview.db1.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantsTccc {

    /**
     * Common String
     */
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
    //Danh sách biểu thức bất quy tắc
    public static final String REGEX_NUMBER = "^[0-9]*$";
    public static final String REGEX_SEARCH_NUMBER = "^[0-9*]*$";
    public static final String REGEX_TEXT_NUMBER = "^[a-zA-Z0-9]+$";
    public static final String REGEX_TEXT_USERNAME = "^[_a-zA-Z0-9]+$";
    public static final String REGEX_DATE = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:Feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String REGEX_TEL = "\\+?[0-9]{10,12}";

    public class Log { //Log hệ thống

        public static final String system = "SYSTEM";
        public static final String user = "USER";
        public static final String category = "CATEGORY";
        public static final String DOCUMENT_NOTARY = "Hồ sơ công chứng viên";
        public static final String PROBATIONARY_INFO = "Tập sự hành nghề công chứng";
        public static final String REQUEST_NOTARY = "Đề nghị bổ nhiệm CCV";
        public static final String DK_HNCC_CAP_THE = "Đăng ký HNCC và cấp Thẻ CCV";
        public static final String XOA_DK_HNCC_THU_HOI_THE = "Xóa đăng ký HNCC và thu hồi Thẻ CCV";
        public static final String CAP_LAI_THE = "Cấp lại Thẻ CCV";
        public static final String DE_NGHI_MIEN_NHIEM_CCV = "Đề nghị miễn nhiệm CCV";
        public static final String DE_NGHI_BO_NHIEM_CCV = "Đề nghị bổ nhiệm lại CCV";
        public static final String DINH_CHI_HNCC = "Tạm đình chỉ HNCC";
        public static final String BO_NHIEM_CCV = "Bổ nhiệm CCV";
        public static final String MIEN_NHIEM_CCV = "Miễn nhiệm CCV";
        public static final String BO_NHIEM_LAI_CCV = "Bổ nhiệm lại CCV";
        public static final String XU_LY_VI_PHAM_CCV = "Xử lý vi phạm đối với CCV";
        public static final String TAO_LAP_DL_CCV_LICH_SU = "Tạo lập dữ liệu CCV lịch sử";
        public static final String UPDATE_DL_CCV_LICH_SU = "Bổ sung thông tin CCV lịch sử";
        public static final String THAY_DOI_NOI_DUNG_DK_HOAT_DONG = "Thay đổi nội dung đăng ký hoạt động VPCC";
        public static final String LIST_HO_SO_TCHNCC = "Danh sách hồ sơ Tổ chức HNCC";
        public static final String THANH_LAP_VPCC = "Thành lập VPCC";
        public static final String DK_HOAT_DONG_VPCC = "Đăng ký hoạt động VPCC";
        public static final String HOP_NHAT_VPCC = "Hợp nhất VPCC";
        public static final String SAP_NHAP_VPCC = "Sáp nhập VPCC";
        public static final String CHUYEN_NHUONG_VPCC = "Chuyển nhượng VPCC";
        public static final String THANH_LAP_PCC = "Thành lập PCC";
        public static final String CHUYEN_DOI_PCC = "Chuyển đổi PCC";
        public static final String GIAI_THE_PCC = "Giải thể PCC";
        public static final String XU_LY_VI_PHAM_TC_HNCC = "Thông tin xử lý vi phạm đối với Tổ chức HNCC";
        public static final String CHAM_DUT_HOAT_DONG_VPCC = "Chấm dứt hoạt động VPCC";
        public static final String TAO_LAP_DL_TCCC_LICH_SU = "Tạo lập dữ liệu TCCC lịch sử";
        public static final String DM_CATEGORY = "Danh mục biểu mẫu";
        public static final String DM_REASON = "Danh mục lý do";
        public static final String DM_AREA = "Đơn vị hành chính";
        public static final String DM_ADMINISTRATION = "Danh mục tổ chức";
    }

    public interface TYPE_DISMISSED {

        public static final String BI_MIEN_NHIEM_STR = "Bị miễn nhiệm";
        public static final Long BI_MIEN_NHIEM = 1L;
        public static final String DUOC_MIEN_NHIEM_STR = "Được miễn nhiệm";
        public static final Long DUOC_MIEN_NHIEM = 2L;
        public static final String BTP_TU_MIEN_NHIEM_STR = "BTP tự miễn nhiệm CCV";
        public static final Long BTP_TU_MIEN_NHIEM = 3L;

        public static final List<TypeDismissed> LST_TYPE_DISMISSED = new ArrayList() {
            {
                add(new TypeAppoint(BI_MIEN_NHIEM_STR, BI_MIEN_NHIEM));
                add(new TypeAppoint(DUOC_MIEN_NHIEM_STR, DUOC_MIEN_NHIEM));
                add(new TypeAppoint(BTP_TU_MIEN_NHIEM_STR, BTP_TU_MIEN_NHIEM));
            }
        };
        public static final List<TypeDismissed> LST_TYPE_DISMISSED_ = new ArrayList() {
            {
                add(new TypeDismissed(BI_MIEN_NHIEM_STR, BI_MIEN_NHIEM));
                add(new TypeDismissed(DUOC_MIEN_NHIEM_STR, DUOC_MIEN_NHIEM));
            }
        };

        public static String getStr(Long typeDismissed, String typeDismissedStr) {
            if (typeDismissed == null) {
                typeDismissedStr = null;
            } else if (typeDismissed.equals(ConstantsTccc.TYPE_DISMISSED.BI_MIEN_NHIEM)) {
                typeDismissedStr = ConstantsTccc.TYPE_DISMISSED.BI_MIEN_NHIEM_STR;
            } else if (typeDismissed.equals(ConstantsTccc.TYPE_DISMISSED.DUOC_MIEN_NHIEM)) {
                typeDismissedStr = ConstantsTccc.TYPE_DISMISSED.DUOC_MIEN_NHIEM_STR;
            } else if (typeDismissed.equals(ConstantsTccc.TYPE_DISMISSED.BTP_TU_MIEN_NHIEM)) {
                typeDismissedStr = ConstantsTccc.TYPE_DISMISSED.BTP_TU_MIEN_NHIEM_STR;
            }
            return typeDismissedStr;
        }
    }

    public interface TYPE_APPOINT {

        public static final String BO_NHIEM_STR = "Bổ nhiệm";
        public static final Long BO_NHIEM = 1L;
        public static final String TU_CHOI_BO_NHIEM_STR = "Từ chối bổ nhiệm";
        public static final Long TU_CHOI_BO_NHIEM = 2L;

        public static final List<TypeAppoint> LST_TYPE_APPOINT = new ArrayList() {
            {
                add(new TypeAppoint(BO_NHIEM_STR, BO_NHIEM));
                add(new TypeAppoint(TU_CHOI_BO_NHIEM_STR, TU_CHOI_BO_NHIEM));
            }
        };

        public static String getTypeAppoint(Long typeAppoint, String typeAppointStr) {
            if (typeAppoint == null) {
                typeAppointStr = null;
            } else if (typeAppoint.equals(ConstantsTccc.TYPE_APPOINT.BO_NHIEM)) {
                typeAppointStr = ConstantsTccc.TYPE_APPOINT.BO_NHIEM_STR;
            } else if (typeAppoint.equals(ConstantsTccc.TYPE_APPOINT.TU_CHOI_BO_NHIEM)) {
                typeAppointStr = ConstantsTccc.TYPE_APPOINT.TU_CHOI_BO_NHIEM_STR;
            }
            return typeAppointStr;
        }
    }

    public interface TYPE_TIEN_ICH {
        public static final String CHO_TIEP_NHAN_BO_NHIEM_STR = "Số Công chứng viên chờ tiếp nhận bổ nhiệm";
        public static final int CHO_TIEP_NHAN_BO_NHIEM = 0;
        public static final String CHO_TIEP_NHAN_BO_NHIEM_LAI_STR = "Số Công chứng viên chờ tiếp nhận bổ nhiệm lại";
        public static final int CHO_TIEP_NHAN_BO_NHIEM_LAI = 1;
        public static final String CHO_TIEP_NHAN_MIEN_NHIEM_STR = "Số Công chứng viên chờ tiếp nhận miễn nhiệm";
        public static final int CHO_TIEP_NHAN_MIEN_NHIEM = 2;
        public static final String CHO_BO_NHIEM_STR = "Số Công chứng viên chờ bổ nhiệm";
        public static final int CHO_BO_NHIEM = 3;
        public static final String CHO_BO_NHIEM_LAI_STR = "Số Công chứng viên chờ bổ nhiệm lại";
        public static final int CHO_BO_NHIEM_LAI = 4;
        public static final String CHO_MIEN_NHIEM_STR = "Số Công chứng viên chờ miễn nhiệm";
        public static final int CHO_MIEN_NHIEM = 5;
        public static final String BO_NHIEM_CHUA_DKHN_STR = "Số Công chứng viên đã bổ nhiệm 60 ngày trước nhưng chưa Đăng ký hành nghề";
        public static final int BO_NHIEM_CHUA_DKHN = 6;
        public static final String BO_NHIEM_LAI_CHUA_DKHN_STR = "Số Công chứng viên đã bổ nhiệm lại 60 ngày trước nhưng chưa Đăng ký hành nghề";
        public static final int BO_NHIEM_LAI_CHUA_DKHN = 7;
        public static final String XOA_DKHN_STR = "Số Công chứng viên cần xóa đăng ký HNCC và thu hồi Thẻ CCV";
        public static final int XOA_DKHN = 8;
        public static final String CCV_CAN_DKHN_STR = "Số Công chứng viên cần đăng ký HNCC và cấp Thẻ CCV";
        public static final int CCV_CAN_DKHN = 9;
        public static final String EXP_DINHCHI_STR = "Số Công chứng viên quá thời hạn tạm đình chỉ mà chưa xóa quyết định";
        public static final int EXP_DINHCHI = 10;
        public static final String MAX_NUMBER_PENALIZE_STR = "Số Công chứng viên bị xử phạt vi phạm hành chính quá 2 lần";
        public static final int MAX_NUMBER_PENALIZE = 11;
        public static final String NEED_ACTIVE_STR = "Số Văn phòng công chứng cần đăng ký hoạt động";
        public static final int NEED_ACTIVE = 12;

        public static final String THANH_LAP_CHUA_ĐKHD_STR = "Thành lập VPCC, chưa ĐKHĐ";
        public static final int THANH_LAP_CHUA_ĐKHD = 13;
        public static final String HOP_NHAT_CHUA_ĐKHD_STR = "Hợp nhất VPCC, chưa ĐKHĐ";
        public static final int HOP_NHAT_CHUA_ĐKHD = 14;
        public static final String SAP_NHAP_CHUA_TDND_STR = "Sáp nhập VPCC, chưa thay đổi nội dung ĐKHĐ";
        public static final int SAP_NHAP_CHUA_TDND = 15;
        public static final String CHUYEN_NHUONG_CHUA_TDND_STR = "Chuyển nhượng VPCC, chưa thay đổi nội dung ĐKHĐ";
        public static final int CHUYEN_NHUONG_CHUA_TDND = 16;
        public static final String VPCC_CAN_TDND_DKHD_STR = "Số Văn phòng công chứng cần thay đổi nội dung đăng ký hoạt động";
        public static final int VPCC_CAN_TDND_DKHD = 18;

        public static final String SO_NGUOI_HOAN_THANH_TAP_SU_STR = "Số Người tập sự hoàn thành tập sự";
        public static final int SO_NGUOI_HOAN_THANH_TAP_SU = 19;
        public static final String SO_NGUOI_DAT_KET_QUA_TAP_SU_STR = "Số Người tập sự đạt kết quả tập sự";
        public static final int SO_NGUOI_DAT_KET_QUA_TAP_SU = 20;

        public static final String SO_CCV_TU_CHOI_BO_NHIEM_STR = "Số Công chứng viên bị từ chối bổ nhiệm";
        public static final int SO_CCV_TU_CHOI_BO_NHIEM = 21;
        public static final String SO_CCV_TU_CHOI_MIEN_NHIEM_STR = "Số Công chứng viên bị từ chối miễn nhiệm";
        public static final int SO_CCV_TU_CHOI_MIEN_NHIEM = 22;
        public static final String SO_CCV_TU_CHOI_BO_NHIEM_LAI_STR = "Số Công chứng viên bị từ chối bổ nhiệm lại";
        public static final int SO_CCV_TU_CHOI_BO_NHIEM_LAI = 23;
        
        public static final List<ObjCombobox> LST_TYPE_TIEN_ICH_CCV_BTP = new ArrayList() {
            {
                add(new ObjCombobox(SO_NGUOI_DAT_KET_QUA_TAP_SU_STR, SO_NGUOI_DAT_KET_QUA_TAP_SU));
                add(new ObjCombobox(CHO_TIEP_NHAN_BO_NHIEM_STR, CHO_TIEP_NHAN_BO_NHIEM));
                add(new ObjCombobox(CHO_TIEP_NHAN_BO_NHIEM_LAI_STR, CHO_TIEP_NHAN_BO_NHIEM_LAI));
                add(new ObjCombobox(CHO_TIEP_NHAN_MIEN_NHIEM_STR, CHO_TIEP_NHAN_MIEN_NHIEM));
                add(new ObjCombobox(CHO_BO_NHIEM_STR, CHO_BO_NHIEM));
                add(new ObjCombobox(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new ObjCombobox(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_BO_NHIEM_STR, SO_CCV_TU_CHOI_BO_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_MIEN_NHIEM_STR, SO_CCV_TU_CHOI_MIEN_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_BO_NHIEM_LAI_STR, SO_CCV_TU_CHOI_BO_NHIEM_LAI));
            }
        };
        public static final List<ObjCombobox> LST_TYPE_TIEN_ICH_CCV_STP = new ArrayList() {
            {
                add(new ObjCombobox(SO_NGUOI_HOAN_THANH_TAP_SU_STR, SO_NGUOI_HOAN_THANH_TAP_SU));
                add(new ObjCombobox(CHO_BO_NHIEM_STR, CHO_BO_NHIEM));
                add(new ObjCombobox(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new ObjCombobox(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_BO_NHIEM_STR, SO_CCV_TU_CHOI_BO_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_MIEN_NHIEM_STR, SO_CCV_TU_CHOI_MIEN_NHIEM));
                add(new ObjCombobox(SO_CCV_TU_CHOI_BO_NHIEM_LAI_STR, SO_CCV_TU_CHOI_BO_NHIEM_LAI));
//                add(new ObjCombobox(BO_NHIEM_CHUA_DKHN_STR, BO_NHIEM_CHUA_DKHN));
//                add(new ObjCombobox(BO_NHIEM_LAI_CHUA_DKHN_STR, BO_NHIEM_LAI_CHUA_DKHN));
                add(new ObjCombobox(XOA_DKHN_STR, XOA_DKHN));
                add(new ObjCombobox(CCV_CAN_DKHN_STR, CCV_CAN_DKHN));
                add(new ObjCombobox(EXP_DINHCHI_STR, EXP_DINHCHI));
                add(new ObjCombobox(MAX_NUMBER_PENALIZE_STR, MAX_NUMBER_PENALIZE));
            }
        };
        public static final List<ObjCombobox> LST_TYPE_TIEN_ICH_TCHNCC_STP = new ArrayList() {
            {
                add(new ObjCombobox(NEED_ACTIVE_STR, NEED_ACTIVE));
                //add(new ObjCombobox(THANH_LAP_CHUA_ĐKHD_STR, THANH_LAP_CHUA_ĐKHD));
                //add(new ObjCombobox(HOP_NHAT_CHUA_ĐKHD_STR, HOP_NHAT_CHUA_ĐKHD));
                //add(new ObjCombobox(SAP_NHAP_CHUA_TDND_STR, SAP_NHAP_CHUA_TDND));
                //add(new ObjCombobox(CHUYEN_NHUONG_CHUA_TDND_STR, CHUYEN_NHUONG_CHUA_TDND));
                add(new ObjCombobox(VPCC_CAN_TDND_DKHD_STR, VPCC_CAN_TDND_DKHD));
            }
        };
    }

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

        public static final List<NotaryStatus> LST_STATUS_BO_SUNG_CCV_LICH_SU = new ArrayList() {
            {
                add(new NotaryStatus(CHO_BO_SUNG_STR, CHO_BO_SUNG));
                add(new NotaryStatus(DANG_TAP_SU_STR, DANG_TAP_SU));
                add(new NotaryStatus(CHAM_DUT_TAP_SU_STR, CHAM_DUT_TAP_SU));
                add(new NotaryStatus(HOAN_THANH_TAP_SU_STR, HOAN_THANH_TAP_SU));
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(TAM_DINH_CHI_HANH_NGHE_STR, TAM_DINH_CHI_HANH_NGHE));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
            }
        };

        public static final List<NotaryStatus> LST_NOTARY_STATUS = new ArrayList() {
            {
                add(new NotaryStatus(DANG_TAP_SU_STR, DANG_TAP_SU));
                add(new NotaryStatus(TAM_NGUNG_TAP_SU_STR, TAM_NGUNG_TAP_SU));
                add(new NotaryStatus(CHAM_DUT_TAP_SU_STR, CHAM_DUT_TAP_SU));
                add(new NotaryStatus(CHUYEN_TAP_SU_STR, CHUYEN_TAP_SU));
                add(new NotaryStatus(HOAN_THANH_TAP_SU_STR, HOAN_THANH_TAP_SU));
                add(new NotaryStatus(DAT_KQ_TAP_SU_STR, DAT_KQ_TAP_SU));
                add(new NotaryStatus(CHO_TIEP_NHAN_STR, CHO_TIEP_NHAN));
                add(new NotaryStatus(CHO_BO_NHIEM_STR, CHO_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_STR, TU_CHOI_BO_NHIEM));
                add(new NotaryStatus(CHO_TIEP_NHAN_MIEN_NHIEM_STR, CHO_TIEP_NHAN_MIEN_NHIEM));
                add(new NotaryStatus(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
                add(new NotaryStatus(CHO_TIEP_NHAN_BO_NHIEM_LAI_STR, CHO_TIEP_NHAN_BO_NHIEM_LAI));
                add(new NotaryStatus(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(TAM_DINH_CHI_HANH_NGHE_STR, TAM_DINH_CHI_HANH_NGHE));
                add(new NotaryStatus(CHO_CAP_THE_STR, CHO_CAP_THE));
                add(new NotaryStatus(CHO_BO_SUNG_STR, CHO_BO_SUNG));
                /*add(new NotaryStatus(DA_BO_SUNG_STR, DA_BO_SUNG));*/
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_TAP_SU = new ArrayList() {
            {
                add(new NotaryStatus(DANG_TAP_SU_STR, DANG_TAP_SU));
                add(new NotaryStatus(TAM_NGUNG_TAP_SU_STR, TAM_NGUNG_TAP_SU));
                add(new NotaryStatus(CHAM_DUT_TAP_SU_STR, CHAM_DUT_TAP_SU));
                add(new NotaryStatus(CHUYEN_TAP_SU_STR, CHUYEN_TAP_SU));
                add(new NotaryStatus(HOAN_THANH_TAP_SU_STR, HOAN_THANH_TAP_SU));
                add(new NotaryStatus(DAT_KQ_TAP_SU_STR, DAT_KQ_TAP_SU));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_DE_NGHI_BO_NHIEM = new ArrayList() {
            {
                add(new NotaryStatus(DAT_KQ_TAP_SU_STR, DAT_KQ_TAP_SU));
                add(new NotaryStatus(CHO_TIEP_NHAN_STR, CHO_TIEP_NHAN));
                add(new NotaryStatus(CHO_BO_NHIEM_STR, CHO_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_STR, TU_CHOI_BO_NHIEM));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_SEARCH_XOA_DK_HANH_NGHE = new ArrayList() {
            {
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_SEARCH_BN_BNL = new ArrayList() {
            {
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_BO_NHIEM = new ArrayList() {
            {
                add(new NotaryStatus(CHO_BO_NHIEM_STR, CHO_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_STR, TU_CHOI_BO_NHIEM));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_MIEN_NHIEM = new ArrayList() {
            {
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
                add(new NotaryStatus(CHO_TIEP_NHAN_MIEN_NHIEM_STR, CHO_TIEP_NHAN_MIEN_NHIEM));
                add(new NotaryStatus(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_MIEN_NHIEM_ = new ArrayList() {
            {
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
                add(new NotaryStatus(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_CHO_BO_NHIEM_LAI = new ArrayList() {
            {
                add(new NotaryStatus(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_ĐK_HANH_NGHE_CAP_THE_CCV = new ArrayList() {
            {
                add(new NotaryStatus(CHO_TIEP_NHAN_MIEN_NHIEM_STR, CHO_TIEP_NHAN_MIEN_NHIEM));
                add(new NotaryStatus(CHO_MIEN_NHIEM_STR, CHO_MIEN_NHIEM));
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
                add(new NotaryStatus(CHO_TIEP_NHAN_BO_NHIEM_LAI_STR, CHO_TIEP_NHAN_BO_NHIEM_LAI));
                add(new NotaryStatus(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
                add(new NotaryStatus(DANG_HANH_NGHE_STR, DANG_HANH_NGHE));
                add(new NotaryStatus(TAM_DINH_CHI_HANH_NGHE_STR, TAM_DINH_CHI_HANH_NGHE));
                add(new NotaryStatus(CHO_CAP_THE_STR, CHO_CAP_THE));
                add(new NotaryStatus(CHO_BO_SUNG_STR, CHO_BO_SUNG));
                /*add(new NotaryStatus(DA_BO_SUNG_STR, DA_BO_SUNG));*/
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_BO_NHIEM_LAI = new ArrayList() {
            {
                add(new NotaryStatus(DA_MIEN_NHIEM_STR, DA_MIEN_NHIEM));
                add(new NotaryStatus(CHO_TIEP_NHAN_BO_NHIEM_LAI_STR, CHO_TIEP_NHAN_BO_NHIEM_LAI));
                add(new NotaryStatus(CHO_BO_NHIEM_LAI_STR, CHO_BO_NHIEM_LAI));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
            }
        };

        public static final List<NotaryStatus> LST_STATUS_TAO_LAP_POPUP = new ArrayList() {
            {
                add(new NotaryStatus(DA_BO_NHIEM_STR, DA_BO_NHIEM));
                add(new NotaryStatus(DA_BO_NHIEM_LAI_STR, DA_BO_NHIEM_LAI));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
            }
        };

        public static final List<NotaryStatus> LST_NOTARY_STATUS_BTP = new ArrayList() {
            {
                add(new NotaryStatus(HOAN_THANH_TAP_SU_STR, HOAN_THANH_TAP_SU));
                add(new NotaryStatus(DAT_KQ_TAP_SU_STR, DAT_KQ_TAP_SU));
            }
        };

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            }
             else if (status.equals(NOTARY_STATUS.DANG_KY_TAP_SU)) {
                statusStr = NOTARY_STATUS.DANG_KY_TAP_SU_STR;}
            else if (status.equals(ConstantsTccc.NOTARY_STATUS.DANG_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DANG_TAP_SU_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TAM_NGUNG_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TAM_NGUNG_TAP_SU_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHAM_DUT_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHAM_DUT_TAP_SU_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.HOAN_THANH_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.HOAN_THANH_TAP_SU_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_BO_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_BO_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.DA_BO_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DA_BO_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.DANG_HANH_NGHE)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DANG_HANH_NGHE_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TAM_DINH_CHI_HANH_NGHE)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TAM_DINH_CHI_HANH_NGHE_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.DA_BO_NHIEM_LAI)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DA_BO_NHIEM_LAI_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.DA_MIEN_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DA_MIEN_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_BO_SUNG)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_BO_SUNG_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.DA_BO_SUNG)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DA_BO_SUNG_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TU_CHOI_BO_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TU_CHOI_BO_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHUYEN_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHUYEN_TAP_SU_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN_MIEN_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN_MIEN_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN_BO_NHIEM_LAI)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_TIEP_NHAN_BO_NHIEM_LAI_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TU_CHOI_MIEN_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TU_CHOI_MIEN_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_BO_NHIEM_LAI)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_BO_NHIEM_LAI_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TU_CHOI_BO_NHIEM_LAI)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TU_CHOI_BO_NHIEM_LAI_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_MIEN_NHIEM)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_MIEN_NHIEM_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.THU_HOI_THE)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.THU_HOI_THE_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.CHO_CAP_THE)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.CHO_CAP_THE_STR;
            } else if (status.equals(ConstantsTccc.NOTARY_STATUS.TU_CHOI_CAP_THE)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.TU_CHOI_CAP_THE_STR;
            } else if(status.equals(NOTARY_STATUS.DAT_KQ_TAP_SU)) {
                statusStr = ConstantsTccc.NOTARY_STATUS.DAT_KQ_TAP_SU_STR;
            }
            return statusStr;
        }
    }

    public interface ACTIVE {

        public static final Long HIEU_LUC = 0L;
        public static final String HIEU_LUC_STR = "Đang còn hiệu lực";
        public static final Long HET_HIEU_LUC = 1L;
        public static final String HET_HIEU_LUC_STR = "Hết hiệu lực";
        public static final Long TRUNG_GIAN = 2L;
        public static final String TRUNG_GIAN_STR = "Trung gian";

        public static String getStrNot2(Long s, String str) {
            if (s == null) {
                str = null;
            } else if (s.equals(ConstantsTccc.ACTIVE.HIEU_LUC)) {
                str = ACTIVE.HIEU_LUC_STR;
            } else if (s.equals(ConstantsTccc.ACTIVE.HET_HIEU_LUC)) {
                str = ACTIVE.HET_HIEU_LUC_STR;
            }
            return str;
        }

        public static String getStr(Long s, String str) {
            if (s == null) {
                str = null;
            } else if (s.equals(ConstantsTccc.ACTIVE.HIEU_LUC)) {
                str = ACTIVE.HIEU_LUC_STR;
            } else if (s.equals(ConstantsTccc.ACTIVE.HET_HIEU_LUC)) {
                str = ACTIVE.HET_HIEU_LUC_STR;
            } else if (s.equals(ConstantsTccc.ACTIVE.TRUNG_GIAN)) {
                str = ACTIVE.TRUNG_GIAN_STR;
            }
            return str;
        }
    }

    public interface STATUS_PROBATIONARYINFO {

        public static final String DANG_KY_TAP_SU_STR = "Đang ký tập sự";
        public static final Long DANG_KY_TAP_SU = 0L;

        public static final String DANG_TAP_SU_STR = "Đang tập sự";
        public static final Long DANG_TAP_SU = 1L;

        public static final String TAM_NGUNG_TAP_SU_STR = "Tạm ngừng tập sự";
        public static final Long TAM_NGUNG_TAP_SU = 2L;

        public static final String CHAM_DUT_TAP_SU_STR = "Chấm dứt tập sự";
        public static final Long CHAM_DUT_TAP_SU = 3L;

        public static final String HOAN_THANH_TAP_SU_STR = "Hoàn thành tập sự";
        public static final Long HOAN_THANH_TAP_SU = 4L;

        public static final String CHO_BO_SUNG_STR = "Chờ bổ sung";
        public static final Long CHO_BO_SUNG = 12L;

        public static final String DA_BO_SUNG_STR = "Đã bổ sung";
        public static final Long DA_BO_SUNG = 13L;

        public static final String CHUYEN_TAP_SU_STR = "Chuyển tập sự";
        public static final Long CHUYEN_TAP_SU = 15L;

        public static final String THAY_DOI_CCV_HUONG_DAN_STR = "Thay đổi CCV hướng dẫn";
        public static final Long THAY_DOI_CCV_HUONG_DAN = 26L;

    }

    public interface LOAI_CONG_VAN {

        public static final Long CV_TAP_SU = 1L;
        public static final String CV_TAP_SU_STR = "Công văn tập sự";
        public static final Long CV_BO_NHIEM = 2L;
        public static final String CV_BO_NHIEM_STR = "Công văn bổ nhiệm";
        public static final Long CV_MIEN_NHIEM = 3L;
        public static final String CV_MIEN_NHIEM_STR = "Công văn miễn nhiệm";
        public static final Long CV_BO_NHIEM_LAI = 4L;
        public static final String CV_BO_NHIEM_LAI_STR = "Công văn bổ nhiệm lại";
        public static final Long CV_DK_HANH_NGHE = 5L;
        public static final String CV_DK_HANH_NGHE_STR = "Công văn đăng ký hành nghề";
        public static final Long CV_TAM_DINH_CHI_HANH_NGHE = 6L;
        public static final String CV_TAM_DINH_CHI_HANH_NGHE_STR = "Công văn tạm đình chỉ hành nghề";
        public static final Long CV_CAP_LAI_THE = 7L;
        public static final String CV_CAP_LAI_THE_STR = "Công văn cấp lại thẻ CCV";
        public static final Long CV_THANH_LAP_PCC = 8L;
        public static final String CV_THANH_LAP_PCC_STR = "Công văn thành lập PCC, VPCC";
        public static final Long CV_TU_CHOI_BO_NHIEM = 9L;
        public static final String CV_TU_CHOI_BO_NHIEM_STR = "Từ chối bổ nhiệm";
        public static final Long CV_TAM_NGUNG_TAP_SU = 10L;
        public static final String CV_TAM_NGUNG_TAP_SU_STR = "Công văn tạm ngừng tập sự";
        public static final Long CV_XOA_DK_HANH_NGHE = 11L;
        public static final String CV_CV_XOA_DK_HANH_NGHE_STR = "Công văn Xóa đăng kí hành nghề và thu hồi thẻ CCV";
        public static final Long CV_CHAM_DUT_HOAT_DONG = 12L;
        public static final String CV_CHAM_DUT_HOAT_DONG_STR = "Công văn Chấm dứt hoạt động của TCHNCC";
        public static final Long CV_DANG_KY_HOAT_DONG_VPCC = 13L;
        public static final String CV_DANG_KY_HOAT_DONG_VPCC_STR = "Công văn Đăng ký hoạt động TCHNCC";
        public static final Long CV_CHUYEN_NHUONG_VPCC = 14L;
        public static final String CV_CHUYEN_NHUONG_VPCC_STR = "Công văn chuyển nhượng";
        public static final Long CV_THU_HOI_QUYET_DINH_TL = 15L;
        public static final String CV_THU_HOI_QUYET_DINH_TL_STR = "Công văn thu hồi quyết định thành lập TCHNCC";
        public static final Long CV_DE_NGHI_BO_NHIEM = 16L;
        public static final String CV_DE_NGHI_BO_NHIEM_STR = "Công văn đề nghị bổ nhiệm";
        public static final Long CV_XU_LY_VI_PHAM_TCCC = 17L;
        public static final String CV_XU_LY_VI_PHAM_TCCC_STR = "Công văn xử lý vi phạm TCHNCC";
        public static final Long CV_CAP_GIAY_DKHD = 18L;
        public static final String CV_CAP_GIAY_DKHD_STR = "Công văn Cấp giấy ĐKHĐ";
        public static final Long CV_TU_CHOI_CAP_GIAY_DKHD = 19L;
        public static final String CV_TU_CHOI_CAP_GIAY_DKHD_STR = "Công văn Từ chối Cấp giấy ĐKHĐ";
        public static final Long CV_DE_NGHI_MIEN_NHIEM_CCV = 20L;
        public static final String CV_DE_NGHI_MIEN_NHIEM_CCV_STR = "Công văn đề nghị miễn nhiệm CCV";
        public static final Long CV_DE_NGHI_BO_NHIEM_LAI = 16L;
        public static final String CV_DE_NGHI_BO_NHIEM_LAI_STR = "Công văn đề nghị bổ nhiệm lại";
        public static final Long CV_TU_CHOI_MIEN_NHIEM = 17L;
        public static final String CV_TU_CHOI_MIEN_NHIEM_STR = "Công văn từ chối miễn nhiệm";
        public static final Long CV_THAY_DOI_NOI_DUNG_DKHD = 21L;
        public static final String CV_THAY_DOI_NOI_DUNG_DKHD_STR = "Công văn thay đổi nội dung đăng ký hoạt động";
        public static final Long CV_TU_CHOI_BO_NHIEM_LAI = 22L;
        public static final String CV_TU_CHOI_BO_NHIEM_LAI_STR = "Công văn Từ chối bổ nhiệm lại";
        public static final Long CV_TU_CHOI_CAP_LAI_THE = 23L;
        public static final String CV_TU_CHOI_CAP_LAI_THE_STR = "Công văn Từ chối cấp lại Thẻ";
        public static final Long CV_GIAI_THE_PCC = 24L;
        public static final String CV_GIAI_THE_PCC_STR = "Công văn Giải thể PCC";
        public static final Long CV_HUY_TAM_DINH_CHI_HANH_NGHE = 25L;
        public static final String CV_HUY_TAM_DINH_CHI_HANH_NGHE_STR = "Công văn hủy tạm đình chỉ hành nghề";
        public static final Long CV_TU_CHOI_CAP_THE = 26L;
        public static final String CV_TU_CHOI_CAP_THE_STR = "Công văn Từ chối cấp Thẻ";
        public static final Long CV_HOP_NHAT_VPCC = 27L;
        public static final String CV_HOP_NHAT_VPCC_STR = "Công văn Hợp nhất Văn phòng công chứng";
        public static final Long CV_SAP_NHAP_VPCC = 28L;
        public static final String CV_SAP_NHAP_VPCC_STR = "Công văn Sáp nhập Văn phòng công chứng";
        public static final Long CV_QD_TL_VPCC = 29L;
        public static final String CV_QD_TL_VPCC_STR = "Công văn Quyết định thành lập VPCC";
        public static final Long CV_TC_TL_VPCC = 30L;
        public static final String CV_TC_TL_VPCC_STR = "Công văn Từ chối thành lập VPCC";
        public static final Long CV_XU_LY_VI_PHAM_CCV = 31L;
        public static final String CV_XU_LY_VI_PHAM_CCV_STR = "Công văn xử lý vi phạm CCV";
        public static final Long CV_THU_HOI_GIAY_DKHD = 32L;
        public static final String CV_THU_HOI_GIAY_DKHD_STR = "Công văn thu hồi giấy ĐKHĐ";
        public static final Long CV_GHI_NHAN_THAY_DOI = 32L;
        public static final String CV_GHI_NHAN_THAY_DOI_STR = "Công văn ghi nhận thay đổi nội dung ĐKHĐ";
        public static final Long CV_YEU_CAU_BX_HO_SO = 33L;
        public static final String CV_YEU_CAU_BX_HO_SO_STR = "Công văn yêu cầu bổ sung hồ sơ";

        public static final List<DocumentType> LST_DOCUMENT_TYPE = new ArrayList() {
            {
                add(new DocumentType(CV_TAP_SU_STR, CV_TAP_SU));
                add(new DocumentType(CV_TAM_NGUNG_TAP_SU_STR, CV_TAM_NGUNG_TAP_SU));
                add(new DocumentType(CV_DE_NGHI_BO_NHIEM_STR, CV_DE_NGHI_BO_NHIEM));
                add(new DocumentType(CV_DE_NGHI_MIEN_NHIEM_CCV_STR, CV_DE_NGHI_MIEN_NHIEM_CCV));
                add(new DocumentType(CV_DE_NGHI_BO_NHIEM_LAI_STR, CV_DE_NGHI_BO_NHIEM_LAI));
                add(new DocumentType(CV_BO_NHIEM_STR, CV_BO_NHIEM));
                add(new DocumentType(CV_TU_CHOI_BO_NHIEM_STR, CV_TU_CHOI_BO_NHIEM));
                add(new DocumentType(CV_MIEN_NHIEM_STR, CV_MIEN_NHIEM));
                add(new DocumentType(CV_TU_CHOI_MIEN_NHIEM_STR, CV_TU_CHOI_MIEN_NHIEM));
                add(new DocumentType(CV_BO_NHIEM_LAI_STR, CV_BO_NHIEM_LAI));
                add(new DocumentType(CV_TU_CHOI_BO_NHIEM_LAI_STR, CV_TU_CHOI_BO_NHIEM_LAI));
                add(new DocumentType(CV_DK_HANH_NGHE_STR, CV_DK_HANH_NGHE));
                add(new DocumentType(CV_TU_CHOI_CAP_THE_STR, CV_TU_CHOI_CAP_THE));
                add(new DocumentType(CV_CAP_LAI_THE_STR, CV_CAP_LAI_THE));
                add(new DocumentType(CV_TU_CHOI_CAP_LAI_THE_STR, CV_TU_CHOI_CAP_LAI_THE));
                add(new DocumentType(CV_TAM_DINH_CHI_HANH_NGHE_STR, CV_TAM_DINH_CHI_HANH_NGHE));
                add(new DocumentType(CV_HUY_TAM_DINH_CHI_HANH_NGHE_STR, CV_HUY_TAM_DINH_CHI_HANH_NGHE));
                add(new DocumentType(CV_CV_XOA_DK_HANH_NGHE_STR, CV_XOA_DK_HANH_NGHE));
                add(new DocumentType(CV_XU_LY_VI_PHAM_CCV_STR, CV_XU_LY_VI_PHAM_CCV));
                add(new DocumentType(CV_THANH_LAP_PCC_STR, CV_THANH_LAP_PCC));
                add(new DocumentType(CV_QD_TL_VPCC_STR, CV_QD_TL_VPCC));
                add(new DocumentType(CV_THU_HOI_QUYET_DINH_TL_STR, CV_THU_HOI_QUYET_DINH_TL));
                add(new DocumentType(CV_TC_TL_VPCC_STR, CV_TC_TL_VPCC));
                add(new DocumentType(CV_DANG_KY_HOAT_DONG_VPCC_STR, CV_DANG_KY_HOAT_DONG_VPCC));
                add(new DocumentType(CV_CAP_GIAY_DKHD_STR, CV_CAP_GIAY_DKHD));
                add(new DocumentType(CV_TU_CHOI_CAP_GIAY_DKHD_STR, CV_TU_CHOI_CAP_GIAY_DKHD));
                add(new DocumentType(CV_THAY_DOI_NOI_DUNG_DKHD_STR, CV_THAY_DOI_NOI_DUNG_DKHD));
                add(new DocumentType(CV_GHI_NHAN_THAY_DOI_STR, CV_GHI_NHAN_THAY_DOI));
                add(new DocumentType(CV_THU_HOI_GIAY_DKHD_STR, CV_THU_HOI_GIAY_DKHD));
                add(new DocumentType(CV_CHUYEN_NHUONG_VPCC_STR, CV_CHUYEN_NHUONG_VPCC));
                add(new DocumentType(CV_XU_LY_VI_PHAM_TCCC_STR, CV_XU_LY_VI_PHAM_TCCC));
                add(new DocumentType(CV_GIAI_THE_PCC_STR, CV_GIAI_THE_PCC));
                add(new DocumentType(CV_HOP_NHAT_VPCC_STR, CV_HOP_NHAT_VPCC));
                add(new DocumentType(CV_SAP_NHAP_VPCC_STR, CV_SAP_NHAP_VPCC));
                add(new DocumentType(CV_CHAM_DUT_HOAT_DONG_STR, CV_CHAM_DUT_HOAT_DONG));
                add(new DocumentType(CV_YEU_CAU_BX_HO_SO_STR, CV_YEU_CAU_BX_HO_SO));
            }

        };

        public static String getSelectDocument(Long nameType, String nameTypeStr) {
            if (nameType == null) {
                nameTypeStr = null;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TAP_SU)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAP_SU_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TAM_NGUNG_TAP_SU)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAM_NGUNG_TAP_SU_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_DE_NGHI_MIEN_NHIEM_CCV)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_MIEN_NHIEM_CCV_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_BO_NHIEM_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_MIEN_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_MIEN_NHIEM_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_MIEN_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_MIEN_NHIEM_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_DK_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DK_HANH_NGHE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_CAP_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_THE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_CAP_LAI_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CAP_LAI_THE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_CAP_LAI_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_LAI_THE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TAM_DINH_CHI_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAM_DINH_CHI_HANH_NGHE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_HUY_TAM_DINH_CHI_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_HUY_TAM_DINH_CHI_HANH_NGHE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_XOA_DK_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CV_XOA_DK_HANH_NGHE_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_CCV)) {
                nameTypeStr = LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_CCV_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_THANH_LAP_PCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THANH_LAP_PCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_QD_TL_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_QD_TL_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_THU_HOI_QUYET_DINH_TL)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THU_HOI_QUYET_DINH_TL_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TC_TL_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TC_TL_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_DANG_KY_HOAT_DONG_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DANG_KY_HOAT_DONG_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_CAP_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CAP_GIAY_DKHD_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_TU_CHOI_CAP_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_GIAY_DKHD_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_THAY_DOI_NOI_DUNG_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THAY_DOI_NOI_DUNG_DKHD_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_GHI_NHAN_THAY_DOI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_GHI_NHAN_THAY_DOI_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_THU_HOI_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THU_HOI_GIAY_DKHD_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_CHUYEN_NHUONG_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CHUYEN_NHUONG_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_TCCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_TCCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_GIAI_THE_PCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_GIAI_THE_PCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_HOP_NHAT_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_HOP_NHAT_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_SAP_NHAP_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_SAP_NHAP_VPCC_STR;
            } else if (nameType.equals(ConstantsTccc.LOAI_CONG_VAN.CV_CHAM_DUT_HOAT_DONG)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CHAM_DUT_HOAT_DONG_STR;
            } else if (nameType.equals(LOAI_CONG_VAN.CV_YEU_CAU_BX_HO_SO)) {
                nameTypeStr = LOAI_CONG_VAN.CV_YEU_CAU_BX_HO_SO_STR;
            }
            return nameTypeStr;

        }
    }

    public interface TYPE_ORG_ACTION {

        public static final Long CHO_THANH_LAP = 0L;
        public static final String CHO_THANH_LAP_STR = "Chờ thành lập";
        public static final Long THANH_LAP = 1L;
        public static final String THANH_LAP_STR = "Thành lập";
        public static final Long DANG_KY_HOAT_DONG = 2L;
        public static final String DANG_KY_HOAT_DONG_STR = "Đăng ký hoạt động";
        public static final Long GIAI_THE = 3L;
        public static final String GIAI_THE_STR = "Ngừng hoạt động";
        public static final Long CHAM_DUT_HOAT_DONG = 4L;
        public static final String CHAM_DUT_HOAT_DONG_STR = "Chấm dứt hoạt động";
        public static final Long THAY_DOI_NOI_DUNG_HOAT_DONG = 6L;
        public static final String THAY_DOI_NOI_DUNG_HOAT_DONG_STR = "Thay đổi nội dung hoạt động";
        public static final Long CHUYEN_NHUONG = 7L;
        public static final String CHUYEN_NHUONG_STR = "Chuyển nhượng VPCC";
        public static final Long THU_HOI_QD = 8L;
        public static final String THU_HOI_QD_STR = "Thu hồi QĐ cho phép thành lập";
        public static final Long CAP_GIAY_DK_HOAT_DONG = 9L;
        public static final String CAP_GIAY_DK_HOAT_DONG_STR = "Cấp mới giấy ĐKHĐ";
        public static final Long TU_CHOI_DK_HOAT_DONG = 10L;
        public static final String TU_CHOI_DK_HOAT_DONG_STR = "Từ chối cấp giấy ĐKHĐ";
        public static final Long THU_HOI_DK_HOAT_DONG = 11L;
        public static final String THU_HOI_DK_HOAT_DONG_STR = "Thu hồi giấy ĐKHĐ";

        public static final Long THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 12L;
        public static final String THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Câp lại Giấy ĐKHĐ";
        public static final Long TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 13L;
        public static final String TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Ghi nhận thay đổi";

        public static final Long TU_CHOI_THANH_LAP = 14L;
        public static final String TU_CHOI_THANH_LAP_STR = "Từ chối thành lập";
        public static final Long CAP_LAI_GIAY_DK_HOAT_DONG = 15L;
        public static final String CAP_LAI_GIAY_DK_HOAT_DONG_STR = "Cấp lại giấy ĐKHĐ";
        public static final Long CHO_CAP_GIAY_DK_HOAT_DONG = 16L;
        public static final String CHO_CAP_GIAY_DK_HOAT_DONG_STR = "Chờ cấp giấy ĐKHĐ";

        public static final List<NotaryStatus> LST_TYPE_ACTION_1 = new ArrayList() {
            {
                add(new NotaryStatus(CAP_GIAY_DK_HOAT_DONG_STR, CAP_GIAY_DK_HOAT_DONG));
                add(new NotaryStatus(TU_CHOI_DK_HOAT_DONG_STR, TU_CHOI_DK_HOAT_DONG));
            }
        };
        public static final List<NotaryStatus> LST_TYPE_ACTION_2 = new ArrayList() {
            {
                add(new NotaryStatus(CAP_LAI_GIAY_DK_HOAT_DONG_STR, CAP_LAI_GIAY_DK_HOAT_DONG));
                add(new NotaryStatus(TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR, TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG));
            }
        };

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CHO_THANH_LAP)) {
                statusStr = TYPE_ORG_ACTION.CHO_THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.THANH_LAP)) {
                statusStr = TYPE_ORG_ACTION.THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.DANG_KY_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.DANG_KY_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.GIAI_THE)) {
                statusStr = TYPE_ORG_ACTION.GIAI_THE_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CHAM_DUT_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.CHAM_DUT_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.THAY_DOI_NOI_DUNG_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.THAY_DOI_NOI_DUNG_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_QD)) {
                statusStr = TYPE_ORG_ACTION.THU_HOI_QD_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CHUYEN_NHUONG)) {
                statusStr = TYPE_ORG_ACTION.CHUYEN_NHUONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CAP_GIAY_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.CAP_GIAY_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.TU_CHOI_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.THU_HOI_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.THU_HOI_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.THAY_DOI_NOI_DUNG_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.TU_CHOI_THANH_LAP)) {
                statusStr = TYPE_ORG_ACTION.TU_CHOI_THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.CAP_LAI_GIAY_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_ACTION.CHO_CAP_GIAY_DK_HOAT_DONG)) {
                statusStr = TYPE_ORG_ACTION.CHO_CAP_GIAY_DK_HOAT_DONG_STR;
            }

            return statusStr;
        }
    }

    public interface LOAI_DU_LIEU {

        public static final Long HOP_NHAT = 2L;
        public static final String HOP_NHAT_STR = "Hợp nhất";
        public static final Long SAP_NHAP = 3L;
        public static final String SAP_NHAP_STR = "Sáp nhập";
        public static final Long CHUYEN_DOI = 4L;
        public static final String CHUYEN_DOI_STR = "Chuyển đổi";
        public static final Long THAY_DOI_NOI_DUNG_DKHD = 5L;
        public static final String THAY_DOI_NOI_DUNG_DKHD_STR = "Cấp lại Giấy ĐKHĐ";
        public static final Long TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 6L;
        public static final String TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Ghi nhận thay đổi";

        public static final List<NotaryStatus> LOAI_DU_LIEU_1 = new ArrayList() {
            {
                add(new NotaryStatus(THAY_DOI_NOI_DUNG_DKHD_STR, THAY_DOI_NOI_DUNG_DKHD));
                add(new NotaryStatus(TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR, TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG));
            }
        };

        public static String getType(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.LOAI_DU_LIEU.HOP_NHAT)) {
                typeStr = LOAI_DU_LIEU.HOP_NHAT_STR;
            } else if (type.equals(ConstantsTccc.LOAI_DU_LIEU.SAP_NHAP)) {
                typeStr = LOAI_DU_LIEU.SAP_NHAP_STR;
            } else if (type.equals(ConstantsTccc.LOAI_DU_LIEU.CHUYEN_DOI)) {
                typeStr = LOAI_DU_LIEU.CHUYEN_DOI_STR;
            } else if (type.equals(ConstantsTccc.LOAI_DU_LIEU.THAY_DOI_NOI_DUNG_DKHD)) {
                typeStr = LOAI_DU_LIEU.THAY_DOI_NOI_DUNG_DKHD_STR;
            } else if (type.equals(ConstantsTccc.LOAI_DU_LIEU.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG)) {
                typeStr = LOAI_DU_LIEU.TU_CHOI_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR;
            }

            return typeStr;
        }
    }

    public interface LOAI_TCCC {

        public static final Long TC_DUOC_SAP_NHAP = 1L;
        public static final String TC_DUOC_SAP_NHAP_STR = "Tổ chức được sáp nhập";
        public static final Long TC_SAP_NHAP = 2L;
        public static final String TC_SAP_NHAP_STR = "Tổ chức sáp nhập";
        public static final Long TC_MOI_THANH_LAP = 3L;
        public static final String TC_MOI_THANH_LAP_STR = "Tổ chức mới thành lập";
        public static final Long TC_CHUYEN_DOI = 4L;
        public static final String TC_CHUYEN_DOI_STR = "Tổ chức chuyển đổi";
        public static final Long TC_NHAN_CHUYEN_DOI = 5L;
        public static final String TC_NHAN_CHUYEN_DOI_STR = "Tổ chức nhận chuyển đổi";
        public static final Long TC_HOP_NHAT = 6L;
        public static final String TC_HOP_NHAT_STR = "Tổ chức hợp nhất";
        public static final Long TC_DUOC_HOP_NHAT = 7L;
        public static final String TC_DUOC_HOP_NHAT_STR = "Tổ chức được hợp nhất";

        public static final Long TC_TRUOC_THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 8L;
        public static final String TC_TRUOC_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Tổ chức trước thay đổi Nội dung ĐKHĐ";
        public static final Long TC_SAU_THAY_DOI_NOI_DUNG_DK_HOAT_DONG = 9L;
        public static final String TC_SAU_THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR = "Tổ chức sau thay đổi Nội dung ĐKHĐ";
    }

    public interface SEX {

        public static final Long NAM = 1L;
        public static final String NAM_STR = "Nam";
        public static final Long NU = 2L;
        public static final String NU_STR = "Nữ";

        public static final List<NotaryStatus> LST_SEX = new ArrayList() {
            {
                add(new NotaryStatus(NAM_STR, NAM));
                add(new NotaryStatus(NU_STR, NU));
            }
        };

        public static String getStr(Long sex, String sexStr) {
            if (sex == null) {
                sexStr = null;
            } else if (sex.equals(SEX.NAM)) {
                sexStr = SEX.NAM_STR;
            } else if (sex.equals(SEX.NU)) {
                sexStr = SEX.NU_STR;
            }
            return sexStr;
        }

        public static String getStrLogic(Long sex, String sexStr) {
            if (sex == null) {
                sexStr = null;
            } else if (sex.equals(SEX.NAM)) {
                sexStr = "Ông";
            } else if (sex.equals(SEX.NU)) {
                sexStr = "Bà";
            }
            return sexStr;
        }
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

        public static final List<NotaryStatus> ALL_STATUS_ORG = new ArrayList() {
            {

                add(new NotaryStatus(CHO_THANH_LAP_STR, CHO_THANH_LAP));
                add(new NotaryStatus(TU_CHOI_THANH_LAP_STR, TU_CHOI_THANH_LAP));
                add(new NotaryStatus(DA_THANH_LAP_STR, DA_THANH_LAP));
                add(new NotaryStatus(THU_HOI_QD_STR, THU_HOI_QD));
                add(new NotaryStatus(CAP_GIAY_DK_HOAT_DONG_STR, CAP_GIAY_DK_HOAT_DONG));
                add(new NotaryStatus(DANG_HOAT_DONG_STR, DANG_HOAT_DONG));
                add(new NotaryStatus(TU_CHOI_DK_HOAT_DONG_STR, TU_CHOI_DK_HOAT_DONG));
                add(new NotaryStatus(THU_HOI_DK_HOAT_DONG_STR, THU_HOI_DK_HOAT_DONG));
                add(new NotaryStatus(GIAI_THE_STR, GIAI_THE));
                add(new NotaryStatus(CHAM_DUT_STR, CHAM_DUT));
                add(new NotaryStatus(BI_HOP_NHAT_STR, BI_HOP_NHAT));
                add(new NotaryStatus(BI_CHUYEN_DOI_STR, BI_CHUYEN_DOI));
                add(new NotaryStatus(BI_SAT_NHAP_STR, BI_SAT_NHAP));

            }
        };

        public static final List<NotaryStatus> STATUS_ORG_DKHD = new ArrayList() {
            {
                add(new NotaryStatus(CAP_GIAY_DK_HOAT_DONG_STR, CAP_GIAY_DK_HOAT_DONG));
                add(new NotaryStatus(TU_CHOI_DK_HOAT_DONG_STR, TU_CHOI_DK_HOAT_DONG));
                add(new NotaryStatus(THU_HOI_DK_HOAT_DONG_STR, THU_HOI_DK_HOAT_DONG));
            }
        };

        public static final List<NotaryStatus> STATUS_ORG_BX_PCC = new ArrayList() {
            {
                add(new NotaryStatus(DANG_HOAT_DONG_STR, DANG_HOAT_DONG));
                add(new NotaryStatus(BI_CHUYEN_DOI_STR, BI_CHUYEN_DOI));
                add(new NotaryStatus(GIAI_THE_STR, GIAI_THE));
            }
        };

        public static final List<NotaryStatus> STATUS_ORG_BX_VPCC = new ArrayList() {
            {
                add(new NotaryStatus(DANG_HOAT_DONG_STR, DANG_HOAT_DONG));
                add(new NotaryStatus(DA_THANH_LAP_STR, DA_THANH_LAP));
                add(new NotaryStatus(THU_HOI_QD_STR, THU_HOI_QD));
                add(new NotaryStatus(CAP_GIAY_DK_HOAT_DONG_STR, CAP_GIAY_DK_HOAT_DONG));
                add(new NotaryStatus(BI_HOP_NHAT_STR, BI_HOP_NHAT));
                add(new NotaryStatus(BI_SAT_NHAP_STR, BI_SAT_NHAP));
                add(new NotaryStatus(CHAM_DUT_STR, CHAM_DUT));
            }
        };

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.DANG_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.DANG_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.CHO_THANH_LAP)) {
                statusStr = STATUS_ORG_NOTARY.CHO_THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.GIAI_THE)) {
                statusStr = STATUS_ORG_NOTARY.GIAI_THE_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.CHAM_DUT)) {
                statusStr = STATUS_ORG_NOTARY.CHAM_DUT_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.CHUA_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.CHUA_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.THU_HOI_QD)) {
                statusStr = STATUS_ORG_NOTARY.THU_HOI_QD_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.CAP_GIAY_DK_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.CAP_GIAY_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.TU_CHOI_DK_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.TU_CHOI_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.BI_HOP_NHAT)) {
                statusStr = STATUS_ORG_NOTARY.BI_HOP_NHAT_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.DA_THANH_LAP)) {
                statusStr = STATUS_ORG_NOTARY.DA_THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.TU_CHOI_THANH_LAP)) {
                statusStr = STATUS_ORG_NOTARY.TU_CHOI_THANH_LAP_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.THU_HOI_DK_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.THU_HOI_DK_HOAT_DONG_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.BI_CHUYEN_DOI)) {
                statusStr = STATUS_ORG_NOTARY.BI_CHUYEN_DOI_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.BI_SAT_NHAP)) {
                statusStr = STATUS_ORG_NOTARY.BI_SAT_NHAP_STR;
            } else if (status.equals(ConstantsTccc.STATUS_ORG_NOTARY.THAY_DOI_NOI_DUNG_DK_HOAT_DONG)) {
                statusStr = STATUS_ORG_NOTARY.THAY_DOI_NOI_DUNG_DK_HOAT_DONG_STR;
            } else if(status.equals(ConstantsTccc.STATUS_ORG_NOTARY.CHO_BO_SUNG)) {
                statusStr = STATUS_ORG_NOTARY.CHO_BO_SUNG_STR;
            }

            return statusStr;
        }

    }

    public interface TYPE_DMADMINISTRATION {

        public static final Long CUC = 1L;
        public static final String CUC_STR = "cục";
        public static final Long SO_TU_PHAP = 2L;
        public static final String SO_TU_PHAP_STR = "Sở tư pháp";
    }

    public interface NOTARY_TYPE_CERTIFICATE {

        public static final Long GIAY_CHUNG_NHAN_DAO_TAO = 1L;
        public static final String GIAY_CHUNG_NHAN_DAO_TAO_STR = "Giấy chứng nhận tham gia khóa đào tạo";
        public static final Long GIAY_CHUNG_NHAN_BOI_DUONG = 2L;
        public static final String GIAY_CHUNG_NHAN_BOI_DUONG_STR = "Giấy chứng nhận tham gia khóa bồi dưỡng";
    }

    public interface TYPE_ORG_NOTARY {

        public static final Long PHONG_CONG_CHUNG = 1L;
        public static final String PHONG_CONG_CHUNG_STR = "Phòng công chứng";
        public static final Long VAN_PHONG_CONG_CHUNG = 2L;
        public static final String VAN_PHONG_CONG_CHUNG_STR = "Văn phòng công chứng";

        public static final List<NotaryStatus> LST_TYPE_ORG_NOTARY = new ArrayList() {
            {
                add(new NotaryStatus(PHONG_CONG_CHUNG_STR, PHONG_CONG_CHUNG));
                add(new NotaryStatus(VAN_PHONG_CONG_CHUNG_STR, VAN_PHONG_CONG_CHUNG));
            }
        };

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_NOTARY.PHONG_CONG_CHUNG)) {
                statusStr = TYPE_ORG_NOTARY.PHONG_CONG_CHUNG_STR;
            } else if (status.equals(ConstantsTccc.TYPE_ORG_NOTARY.VAN_PHONG_CONG_CHUNG)) {
                statusStr = TYPE_ORG_NOTARY.VAN_PHONG_CONG_CHUNG_STR;
            }

            return statusStr;
        }
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

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            } else if (status.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.HANH_NGHE)) {
                statusStr = NOTARY_STATUS.DANG_HANH_NGHE_STR;
            } else if (status.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.TAM_DINH_CHI)) {
                statusStr = STATUS_NOTARY_REG_PRACTICE.TAM_DINH_CHI_STR;
            } else if (status.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.CHO_CAP_THE)) {
                statusStr = STATUS_NOTARY_REG_PRACTICE.CHO_CAP_THE_STR;
            } else if (status.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.TU_CHOI_CAP_THE)) {
                statusStr = STATUS_NOTARY_REG_PRACTICE.TU_CHOI_CAP_THE_STR;
            } else if (status.equals(ConstantsTccc.STATUS_NOTARY_REG_PRACTICE.THU_HOI_THE)) {
                statusStr = STATUS_NOTARY_REG_PRACTICE.THU_HOI_THE_STR;
            }
            return statusStr;
        }

        public static final List<NotaryStatus> LST_STATUS = new ArrayList() {
            {
                add(new NotaryStatus(HANH_NGHE_STR, HANH_NGHE));
                add(new NotaryStatus(TAM_DINH_CHI_STR, TAM_DINH_CHI));
                add(new NotaryStatus(THU_HOI_THE_STR, THU_HOI_THE));
            }
        };
    }

    public interface TYPE_NOTARY_CARD {

        public static final Long CAP_LAI_THE = 1L;
        public static final String CAP_LAI_THE_STR = "Đã cấp lại Thẻ";
        public static final Long THU_HOI_THE = 2L;
        public static final String THU_HOI_THE_STR = "Đã thu hồi Thẻ";
        public static final Long TU_CHOI_CAP_LAI_THE = 3L;
        public static final String TU_CHOI_CAP_LAI_THE_STR = "Từ chối cấp lại Thẻ";
        public static final Long CHO_CAP_LAI_THE = 4L;
        public static final String CHO_CAP_LAI_THE_STR = "Chờ cấp lại Thẻ";

        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_CARD.CAP_LAI_THE)) {
                typeStr = ConstantsTccc.TYPE_NOTARY_CARD.CAP_LAI_THE_STR;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_CARD.THU_HOI_THE)) {
                typeStr = TYPE_NOTARY_CARD.THU_HOI_THE_STR;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_CARD.TU_CHOI_CAP_LAI_THE)) {
                typeStr = TYPE_NOTARY_CARD.TU_CHOI_CAP_LAI_THE_STR;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_CARD.CHO_CAP_LAI_THE)) {
                typeStr = TYPE_NOTARY_CARD.CHO_CAP_LAI_THE_STR;
            }
            return typeStr;
        }

        public static final List<NotaryStatus> LST_TYPE_NOTARY_CARD = new ArrayList() {
            {
                add(new NotaryStatus(CAP_LAI_THE_STR, CAP_LAI_THE));
                add(new NotaryStatus(TU_CHOI_CAP_LAI_THE_STR, TU_CHOI_CAP_LAI_THE));

            }
        };

        public static final List<NotaryStatus> LST_TYPE_CAP_LAI_THE = new ArrayList() {
            {
                add(new NotaryStatus(CHO_CAP_LAI_THE_STR, CHO_CAP_LAI_THE));
                add(new NotaryStatus(CAP_LAI_THE_STR, CAP_LAI_THE));
                add(new NotaryStatus(TU_CHOI_CAP_LAI_THE_STR, TU_CHOI_CAP_LAI_THE));

            }
        };
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

        public static final Long TRY_CUU_TRACH_NHIEM_HINH_SU = 9L;
        public static final String TRY_CUU_TRACH_NHIEM_HINH_SU_STR = "TRY_CUU_TRACH_NHIEM_HINH_SU";

        public static final List<NotaryStatus> LST_TYPE_PENALIZE_2 = new ArrayList() {
            {
                add(new NotaryStatus(CANH_CAO_STR, CANH_CAO));
                add(new NotaryStatus(XU_PHAT_TIEN_STR, XU_PHAT_TIEN));
            }
        };

        public static final List<NotaryStatus> LST_TYPE_PENALIZE_1 = new ArrayList() {
            {
                add(new NotaryStatus(KHIEN_TRACH_STR, KHIEN_TRACH));
                add(new NotaryStatus(KY_LUAT_CANH_CAO_STR, KY_LUAT_CANH_CAO));
                add(new NotaryStatus(HA_BAC_LUONG_STR, HA_BAC_LUONG));
                add(new NotaryStatus(GIANG_CHUC_STR, GIANG_CHUC));
                add(new NotaryStatus(CACH_CHUC_STR, CACH_CHUC));
                add(new NotaryStatus(BUOC_THOI_VIEC_STR, BUOC_THOI_VIEC));
            }
        };

        public static final List<NotaryStatus> LST_TYPE_PENALIZE_ORG_1 = new ArrayList() {
            {
                add(new NotaryStatus(KHIEN_TRACH_STR, KHIEN_TRACH));
                add(new NotaryStatus(KY_LUAT_CANH_CAO_STR, KY_LUAT_CANH_CAO));
            }
        };

        public static final List<NotaryStatus> LST_TYPE_PENALIZE_ORG_2 = new ArrayList() {
            {
                add(new NotaryStatus(CANH_CAO_STR, CANH_CAO));
                add(new NotaryStatus(XU_PHAT_TIEN_STR, XU_PHAT_TIEN));
            }
        };

        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.CANH_CAO)) {
                typeStr = TYPE_PENALIZE.CANH_CAO_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.XU_PHAT_TIEN)) {
                typeStr = TYPE_PENALIZE.XU_PHAT_TIEN_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.KHIEN_TRACH)) {
                typeStr = TYPE_PENALIZE.KHIEN_TRACH_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.KY_LUAT_CANH_CAO)) {
                typeStr = TYPE_PENALIZE.KY_LUAT_CANH_CAO_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.HA_BAC_LUONG)) {
                typeStr = TYPE_PENALIZE.HA_BAC_LUONG_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.GIANG_CHUC)) {
                typeStr = TYPE_PENALIZE.GIANG_CHUC_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.CACH_CHUC)) {
                typeStr = TYPE_PENALIZE.CACH_CHUC_STR;
            } else if (type.equals(ConstantsTccc.TYPE_PENALIZE.BUOC_THOI_VIEC)) {
                typeStr = TYPE_PENALIZE.BUOC_THOI_VIEC_STR;
            }
            return typeStr;
        }
    }

    public interface LEVER_PENALIZE {

        public static final Long KY_LUAT = 1L;
        public static final String KY_LUAT_STR = "Kỷ luật";
        public static final Long XU_LY_VI_PHAM_HANH_CHINH = 2L;
        public static final String XU_LY_VI_PHAM_HANH_CHINH_STR = "Xử lý vi phạm hành chính";
        public static final Long TRACH_NHIEM_HINH_SU = 3L;
        public static final String TRACH_NHIEM_HINH_SU_STR = "Truy cứu trách nhiệm hình sự";

        public static final List<NotaryStatus> LST_LEVER_PENALIZE = new ArrayList() {
            {
                add(new NotaryStatus(KY_LUAT_STR, KY_LUAT));
                add(new NotaryStatus(XU_LY_VI_PHAM_HANH_CHINH_STR, XU_LY_VI_PHAM_HANH_CHINH));
                add(new NotaryStatus(TRACH_NHIEM_HINH_SU_STR, TRACH_NHIEM_HINH_SU));
            }
        };

        public static final List<NotaryStatus> LST_LEVER_PENALIZE_ORG = new ArrayList() {
            {
                add(new NotaryStatus(KY_LUAT_STR, KY_LUAT));
                add(new NotaryStatus(XU_LY_VI_PHAM_HANH_CHINH_STR, XU_LY_VI_PHAM_HANH_CHINH));
                add(new NotaryStatus(TRACH_NHIEM_HINH_SU_STR, TRACH_NHIEM_HINH_SU));
            }
        };

        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.LEVER_PENALIZE.KY_LUAT)) {
                typeStr = LEVER_PENALIZE.KY_LUAT_STR;
            } else if (type.equals(ConstantsTccc.LEVER_PENALIZE.XU_LY_VI_PHAM_HANH_CHINH)) {
                typeStr = LEVER_PENALIZE.XU_LY_VI_PHAM_HANH_CHINH_STR;
            } else if (type.equals(ConstantsTccc.LEVER_PENALIZE.TRACH_NHIEM_HINH_SU)) {
                typeStr = LEVER_PENALIZE.TRACH_NHIEM_HINH_SU_STR;
            }
            return typeStr;
        }
    }

    public interface ADDITIONAL_PENALTY {

        public static final Long XU_PHAT_BX_1 = 1L;
        public static final String XU_PHAT_BX_1_STR = "Tước quyền sử dụng Chứng chỉ hành nghề, Thẻ CCV";
        public static final Long XU_PHAT_BX_2 = 2L;
        public static final String XU_PHAT_BX_2_STR = "Tịch thu tang vật, phương tiện được sử dụng để vi phạm hành chính";
        public static final Long XU_PHAT_BX_3 = 3L;
        public static final String XU_PHAT_BX_3_STR = "Tước quyền sử dụng Giấy đăng ký hoạt động, Giấy phép thành lập";

        public static final List<NotaryStatus> LST_ADDITIONAL_PENALTY = new ArrayList() {
            {
                add(new NotaryStatus(XU_PHAT_BX_1_STR, XU_PHAT_BX_1));
                add(new NotaryStatus(XU_PHAT_BX_2_STR, XU_PHAT_BX_2));
            }
        };

        public static final List<NotaryStatus> LST_ADDITIONAL_PENALTY_ORG = new ArrayList() {
            {
                add(new NotaryStatus(XU_PHAT_BX_3_STR, XU_PHAT_BX_3));
                add(new NotaryStatus(XU_PHAT_BX_2_STR, XU_PHAT_BX_2));
            }
        };

        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.ADDITIONAL_PENALTY.XU_PHAT_BX_1)) {
                typeStr = ADDITIONAL_PENALTY.XU_PHAT_BX_1_STR;
            } else if (type.equals(ConstantsTccc.ADDITIONAL_PENALTY.XU_PHAT_BX_2)) {
                typeStr = ADDITIONAL_PENALTY.XU_PHAT_BX_2_STR;
            } else if (type.equals(ConstantsTccc.ADDITIONAL_PENALTY.XU_PHAT_BX_3)) {
                typeStr = ADDITIONAL_PENALTY.XU_PHAT_BX_3_STR;
            }
            return typeStr;
        }
    }

    public interface TYPE_NOTARY_ORG_ACTION {

        public static final Long HOP_DANH = 1L;
        public static final String HOP_DANH_STR = "Công chứng viên hợp danh";
        public static final Long HOP_DONG = 2L;
        public static final String HOP_DONG_STR = "Công chứng viên làm việc theo chế độ hợp đồng";
        public static final Long CCV = 3L;
        public static final String CCV_STR = "Công chứng viên";

        public static final List<NotaryStatus> LST_TYPE_NOTARY_ORG_ACTION = new ArrayList() {
            {
                add(new NotaryStatus(HOP_DANH_STR, HOP_DANH));
                add(new NotaryStatus(HOP_DONG_STR, HOP_DONG));
            }
        };

        public static final List<NotaryStatus> LST_TYPE_NOTARY_ORG_ACTION_PCC = new ArrayList() {
            {
                add(new NotaryStatus(CCV_STR, CCV));
            }
        };

        public static final List<NotaryStatus> LST_TYPE_NOTARY_ORG_ACTION_ALL = new ArrayList() {
            {
                add(new NotaryStatus(CCV_STR, CCV));
                add(new NotaryStatus(HOP_DANH_STR, HOP_DANH));
                add(new NotaryStatus(HOP_DONG_STR, HOP_DONG));
            }
        };

        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_ORG_ACTION.HOP_DANH)) {
                typeStr = TYPE_NOTARY_ORG_ACTION.HOP_DANH_STR;
            } else if (type.equals(ConstantsTccc.TYPE_NOTARY_ORG_ACTION.HOP_DONG)) {
                typeStr = TYPE_NOTARY_ORG_ACTION.HOP_DONG_STR;
            } else if (type.equals(TYPE_NOTARY_ORG_ACTION.CCV)) {
                typeStr = TYPE_NOTARY_ORG_ACTION.CCV_STR;
            }
            return typeStr;
        }
    }

    public interface REQUEST_TYPE {

        public static final Long DE_NGHI_BO_NHIEM = 1L;
        public static final String DE_NGHI_BO_NHIEM_STR = "Yêu cầu bổ nhiệm";

        public static final Long BI_MIEN_NHIEM = 2L;
        public static final String BI_MIEN_NHIEM_STR = "Bị miễn nhiệm";

        public static final Long DUOC_MIEN_NHIEM = 3L;
        public static final String DUOC_MIEN_NHIEM_STR = "Được miễn nhiệm";

        public static final Long DE_NGHI_BO_NHIEM_LAI = 4L;
        public static final String DE_NGHI_BO_NHIEM_LAI_STR = "Yêu cầu bổ nhiệm lại";

        public static final Long BTP_TU_MIEN_NHIEM = 5L;
        public static final String BTP_TU_MIEN_NHIEM_STR = "BTP tự miễn nhiệm CCV";

        public static final Long BTP_YEU_CAU_BO_SUNG_HO_SO = 6L;
        public static final String BTP_YEU_CAU_BO_SUNG_HO_SO_STR = "BTP yêu cầu bổ sung hồ sơ";

        public static final List<TypeDismissed> LST_TYPE_DISMISSED = new ArrayList() {
            {
                add(new TypeDismissed(BI_MIEN_NHIEM_STR, BI_MIEN_NHIEM));
                add(new TypeDismissed(DUOC_MIEN_NHIEM_STR, DUOC_MIEN_NHIEM));
            }
        };

        public static final List<TypeDismissed> LST_TYPE_DISMISSED_SEARCH = new ArrayList() {
            {
                add(new TypeDismissed(BI_MIEN_NHIEM_STR, BI_MIEN_NHIEM));
                add(new TypeDismissed(DUOC_MIEN_NHIEM_STR, DUOC_MIEN_NHIEM));
                add(new TypeAppoint(BTP_TU_MIEN_NHIEM_STR, BTP_TU_MIEN_NHIEM));
            }
        };

        public static String getStr(Long requestType, String requestTypeStr) {
            if (requestType == null) {
                requestTypeStr = null;
            } else if (requestType.equals(ConstantsTccc.REQUEST_TYPE.BI_MIEN_NHIEM)) {
                requestTypeStr = REQUEST_TYPE.BI_MIEN_NHIEM_STR;
            } else if (requestType.equals(ConstantsTccc.REQUEST_TYPE.DUOC_MIEN_NHIEM)) {
                requestTypeStr = REQUEST_TYPE.DUOC_MIEN_NHIEM_STR;
            } else if (requestType.equals(ConstantsTccc.REQUEST_TYPE.BTP_TU_MIEN_NHIEM)) {
                requestTypeStr = REQUEST_TYPE.BTP_TU_MIEN_NHIEM_STR;
            }
            return requestTypeStr;
        }

    }

    public interface PARAMETER_TYPE_DATA {

        public static final String MIEN_NHIEM_STR = "Lý do miễn nhiệm";
        public static final Long MIEN_NHIEM = 1L;
        public static final String BI_MIEN_NHIEM_STR = "Lý do bị miễn nhiệm";
        public static final Long BI_MIEN_NHIEM = 2L;
        public static final String DUOC_MIEN_NHIEM_STR = "Lý do được miễn nhiệm";
        public static final Long DUOC_MIEN_NHIEM = 3L;
        public static final String DE_NGHI_MIEN_NHIEM_STR = "Lý do đề nghị miễn nhiệm";
        public static final Long DE_NGHI_MIEN_NHIEM = 4L;
        public static final String TU_CHOI_MIEN_NHIEM_STR = "Lý do từ chối miễn nhiệm";
        public static final Long TU_CHOI_MIEN_NHIEM = 5L;
        public static final String TU_CHOI_BO_NHIEM_STR = "Lý do từ chối bổ nhiệm";
        public static final Long TU_CHOI_BO_NHIEM = 6L;
        public static final String TU_CHOI_BO_NHIEM_LAI_STR = "Lý do từ chối bổ nhiệm lại";
        public static final Long TU_CHOI_BO_NHIEM_LAI = 7L;
        public static final String TU_CHOI_CAP_THE_STR = "Lý do từ chối cấp thẻ";
        public static final Long TU_CHOI_CAP_THE = 8L;
        public static final String CAP_LAI_THE_STR = "Lý do cấp lại thẻ";
        public static final Long CAP_LAI_THE = 9L;
        public static final String TU_CHOI_CAP_LAI_THE_STR = "Lý do từ chối cấp lại thẻ";
        public static final Long TU_CHOI_CAP_LAI_THE = 10L;
        public static final String GIAI_THE_PCC_STR = "Lý do giải thể PCC";
        public static final Long GIAI_THE_PCC = 11L;
        public static final String CHAM_DUT_HOAT_DONG_STR = "Lý do chấm dứt hoạt động";
        public static final Long CHAM_DUT_HOAT_DONG = 12L;
        public static final String TAM_DINH_CHI_STR = "Lý do tạm đình chỉ hành nghề công chứng";
        public static final Long TAM_DINH_CHI = 13L;
        public static final String HUY_TAM_DINH_CHI_STR = "Lý do hủy tạm đình chỉ hành nghề công chứng";
        public static final Long HUY_TAM_DINH_CHI = 14L;
        public static final String THU_HOI_THANH_LAP_VPCC_STR = "Lý do thu hồi thành lập văn phòng công chứng";
        public static final Long THU_HOI_THANH_LAP_VPCC = 15L;
        public static final String DON_VI_XU_PHAT_VI_PHAM_CCV_STR = "Đơn vị xử phạt vi phạm CCV";
        public static final Long DON_VI_XU_PHAT_VI_PHAM_CCV = 16L;
        public static final String XOA_ĐK_HNCC_THU_HOI_THE_CCV_STR = "Lý do xóa đăng ký hncc và thu hồi thẻ ccv";
        public static final Long XOA_ĐK_HNCC_THU_HOI_THE_CCV = 17L;
        public static final String NOI_CAP_CMND_STR = "Nơi cấp số CMND/ Hộ chiếu/ CCCD";
        public static final Long NOI_CAP_CMND = 18L;

        /*lưu ý thêm loại lý do phải thêm vào list này*/
        List<Long> typeDatas = Arrays.asList(new Long[]{
            ConstantsTccc.PARAMETER_TYPE_DATA.MIEN_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.BI_MIEN_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM,
            ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI,
            ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE,
            ConstantsTccc.PARAMETER_TYPE_DATA.CAP_LAI_THE,
            ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE,
            ConstantsTccc.PARAMETER_TYPE_DATA.GIAI_THE_PCC,
            ConstantsTccc.PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG,
            ConstantsTccc.PARAMETER_TYPE_DATA.TAM_DINH_CHI,
            ConstantsTccc.PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI,
            ConstantsTccc.PARAMETER_TYPE_DATA.THU_HOI_THANH_LAP_VPCC,
            ConstantsTccc.PARAMETER_TYPE_DATA.DON_VI_XU_PHAT_VI_PHAM_CCV,
            ConstantsTccc.PARAMETER_TYPE_DATA.XOA_ĐK_HNCC_THU_HOI_THE_CCV,
            ConstantsTccc.PARAMETER_TYPE_DATA.NOI_CAP_CMND
        });
        /*lưu ý thêm loại lý do phải thêm vào list này*/
        public static final List<NotaryStatus> LST_PARAMETER_TYPE_DATA = new ArrayList() {
            {
                add(new NotaryStatus(MIEN_NHIEM_STR, MIEN_NHIEM));
                add(new NotaryStatus(BI_MIEN_NHIEM_STR, BI_MIEN_NHIEM));
                add(new NotaryStatus(DUOC_MIEN_NHIEM_STR, DUOC_MIEN_NHIEM));
                add(new NotaryStatus(DE_NGHI_MIEN_NHIEM_STR, DE_NGHI_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_STR, TU_CHOI_BO_NHIEM));
                add(new NotaryStatus(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
                add(new NotaryStatus(TU_CHOI_CAP_THE_STR, TU_CHOI_CAP_THE));
                add(new NotaryStatus(CAP_LAI_THE_STR, CAP_LAI_THE));
                add(new NotaryStatus(TU_CHOI_CAP_LAI_THE_STR, TU_CHOI_CAP_LAI_THE));
                add(new NotaryStatus(GIAI_THE_PCC_STR, GIAI_THE_PCC));
                add(new NotaryStatus(CHAM_DUT_HOAT_DONG_STR, CHAM_DUT_HOAT_DONG));
                add(new NotaryStatus(TAM_DINH_CHI_STR, TAM_DINH_CHI));
                add(new NotaryStatus(HUY_TAM_DINH_CHI_STR, HUY_TAM_DINH_CHI));
                add(new NotaryStatus(THU_HOI_THANH_LAP_VPCC_STR, THU_HOI_THANH_LAP_VPCC));
                add(new NotaryStatus(DON_VI_XU_PHAT_VI_PHAM_CCV_STR, DON_VI_XU_PHAT_VI_PHAM_CCV));
                add(new NotaryStatus(XOA_ĐK_HNCC_THU_HOI_THE_CCV_STR, XOA_ĐK_HNCC_THU_HOI_THE_CCV));
                add(new NotaryStatus(NOI_CAP_CMND_STR, NOI_CAP_CMND));
            }
        };

        /*lưu ý thêm loại lý do phải thêm vào str này*/
        public static String getStr(Long type, String typeStr) {
            if (type == null) {
                typeStr = null;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.MIEN_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.BI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.BI_MIEN_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.CAP_LAI_THE)) {
                typeStr = PARAMETER_TYPE_DATA.CAP_LAI_THE_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.GIAI_THE_PCC)) {
                typeStr = PARAMETER_TYPE_DATA.GIAI_THE_PCC_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG)) {
                typeStr = PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.TAM_DINH_CHI)) {
                typeStr = PARAMETER_TYPE_DATA.TAM_DINH_CHI_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI)) {
                typeStr = PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI_STR;
            } else if (type.equals(ConstantsTccc.PARAMETER_TYPE_DATA.THU_HOI_THANH_LAP_VPCC)) {
                typeStr = PARAMETER_TYPE_DATA.THU_HOI_THANH_LAP_VPCC_STR;
            } else if (type.equals(PARAMETER_TYPE_DATA.DON_VI_XU_PHAT_VI_PHAM_CCV)) {
                typeStr = PARAMETER_TYPE_DATA.DON_VI_XU_PHAT_VI_PHAM_CCV_STR;
            } else if (type.equals(PARAMETER_TYPE_DATA.XOA_ĐK_HNCC_THU_HOI_THE_CCV)) {
                typeStr = PARAMETER_TYPE_DATA.XOA_ĐK_HNCC_THU_HOI_THE_CCV_STR;
            } else if(type.equals(PARAMETER_TYPE_DATA.NOI_CAP_CMND)){
                typeStr = PARAMETER_TYPE_DATA.NOI_CAP_CMND_STR;
            }
            return typeStr;
        }
    }

    public interface TYPE_REAPPOINT {

        public static final String BO_NHIEM_LAI_STR = "Bổ nhiệm lại";
        public static final Long BO_NHIEM_LAI = 1L;
        public static final String TU_CHOI_BO_NHIEM_LAI_STR = "Từ chối bổ nhiệm lại";
        public static final Long TU_CHOI_BO_NHIEM_LAI = 2L;

        public static final List<TypeAppoint> LST_TYPE_REAPPOINT = new ArrayList() {
            {
                add(new TypeAppoint(BO_NHIEM_LAI_STR, BO_NHIEM_LAI));
                add(new TypeAppoint(TU_CHOI_BO_NHIEM_LAI_STR, TU_CHOI_BO_NHIEM_LAI));
            }
        };

        public static String getStr(Long typeAppoint, String typeAppointStr) {
            if (typeAppoint == null) {
                typeAppointStr = null;
            } else if (typeAppoint.equals(ConstantsTccc.TYPE_REAPPOINT.BO_NHIEM_LAI)) {
                typeAppointStr = ConstantsTccc.TYPE_REAPPOINT.BO_NHIEM_LAI_STR;
            } else if (typeAppoint.equals(ConstantsTccc.TYPE_REAPPOINT.TU_CHOI_BO_NHIEM_LAI)) {
                typeAppointStr = ConstantsTccc.TYPE_REAPPOINT.TU_CHOI_BO_NHIEM_LAI_STR;
            }
            return typeAppointStr;
        }
    }

    public interface STATUS_DISMISSED {

        public static final String MIEN_NHIEM_STR = "Miễn nhiệm";
        public static final Long MIEN_NHIEM = 1L;
        public static final String TU_CHOI_MIEN_NHIEM_STR = "Từ chối miễn nhiệm";
        public static final Long TU_CHOI_MIEN_NHIEM = 2L;

        public static final List<TypeAppoint> LST_STATUS_DISMISSED = new ArrayList() {
            {
                add(new TypeAppoint(MIEN_NHIEM_STR, MIEN_NHIEM));
                add(new TypeAppoint(TU_CHOI_MIEN_NHIEM_STR, TU_CHOI_MIEN_NHIEM));
            }
        };

        public static String getStr(Long typeAppoint, String typeAppointStr) {
            if (typeAppoint == null) {
                typeAppointStr = null;
            } else if (typeAppoint.equals(ConstantsTccc.STATUS_DISMISSED.MIEN_NHIEM)) {
                typeAppointStr = ConstantsTccc.STATUS_DISMISSED.MIEN_NHIEM_STR;
            } else if (typeAppoint.equals(ConstantsTccc.STATUS_DISMISSED.TU_CHOI_MIEN_NHIEM)) {
                typeAppointStr = ConstantsTccc.STATUS_DISMISSED.TU_CHOI_MIEN_NHIEM_STR;
            }
            return typeAppointStr;
        }
    }

    public interface TYPE_SUPEND {

        public static final Long TAM_DINH_CHI = 1L;
        public static final String TAM_DINH_CHI_STR = "Tạm đình chỉ";
        public static final Long HUY_TAM_DINH_CHI = 2L;
        public static final String HUY_TAM_DINH_CHI_STR = "Hủy tạm đình chỉ";

        public static String getStr(Long status, String statusStr) {
            if (status == null) {
                statusStr = null;
            } else if (status.equals(ConstantsTccc.TYPE_SUPEND.TAM_DINH_CHI)) {
                statusStr = TYPE_SUPEND.TAM_DINH_CHI_STR;
            } else if (status.equals(ConstantsTccc.TYPE_SUPEND.HUY_TAM_DINH_CHI)) {
                statusStr = TYPE_SUPEND.HUY_TAM_DINH_CHI_STR;
            }
            return statusStr;
        }
    }

    public interface DUYET {
        public static final Long CHUA_DUYET = 0L;
        public static final String CHUA_DUYET_STR = "Chưa duyệt";
        public static final Long DA_DUYET = 1L;
        public static final String DA_DUYET_STR = "Đã duyệt";

        public static final List<NotaryStatus> LST_DUYET = new ArrayList() {
            {
                add(new NotaryStatus(CHUA_DUYET_STR, CHUA_DUYET));
                add(new NotaryStatus(DA_DUYET_STR, DA_DUYET));
            }
        };
    }

    public interface EXPORT_WORD_KEY {

        /*lưu ý tạo khóa sắp xếp a - z không được trùng ví dụ : NAME với NAMECCV   --> lỗi replate NAME */

 /*ngày hiện tại*/
        public static final String DD_NOW = "DDNOW";
        public static final String MM_NOW = "MMNOW";
        public static final String YYYY_NOW = "YYYYNOW";

        /*ngày cấp CMND*/
        public static final String DD_NGAYCAP = "DD_NGAYCAP";
        public static final String MM_NGAYCAP = "MM_NGAYCAP";
        public static final String YYYY_NGAYCAP = "YYYY_NGAYCAP";

        /*ngày sinh CCV*/
        public static final String DDCCV = "DDCCV";
        public static final String MMCCV = "MMCCV";
        public static final String YYYYCCV = "YYYYCCV";

        /*ngày cấp văn bản*/
        public static final String DDVBCCV = "DDVBCCV";
        public static final String MMVBCCV = "MMVBCCV";
        public static final String YYYYVBCCV = "YYYYVBCCV";

        /*ngày cấp văn bản bổ nhiệm*/
        public static final String DDVBBN = "DDVBBN";
        public static final String MMVBBN = "MMVBBN";
        public static final String YYYYVBBN = "YYYYVBBN";


        /*A start*/
        public static final String ADDRESS_CCV = "ADDRESSCCV";/*địa chỉ CCV*/
        public static final String ADMINISTRATIONCCV = "ADMINISTRATIONCCV";/*Tổ chức ký văn bản*/

 /*C start*/
        public static final String CMND_CCV = "CMNDCCV";/*CMND CCV*/
        public static final String CAUSEQDVBCCV = "CAUSEQDVBCCV";/*lý do văn bản*/
        public static final String CONTENTNOTE_PCCVPCC = "CONTENTNOTE_PCCVPCC";/*bổ nhiệm PCC OR VPCC*/

 /*G start*/
        public static final String GIOITINH_CCV = "GIOITINHCCV";/*giới tính CCV*/

 /*M start*/
        public static final String MSISDN_CCV = "MSISDNCCV";/*số điện thoại CCV*/

 /*N start*/
        public static final String NGAYSINH_CCV = "NGAYSINHCCV";/*ngày sinh CCV*/
        public static final String NGAYCAP_CCV = "NGAYCAPCCV";/*ngày cấp CMND CCV*/
        public static final String NOICAP_CCV = "NOICAPCCV";/*nơi cấp CMND CCV*/
        public static final String NOWADDRESS_CCV = "NOWADDRESSMAPINGCCV";/*địa chỉ hiện tại CCV*/
        public static final String NGAYKYQDVB_CCV = "NGAYKYQDVBCCV";/*ngày ký quyết định văn bản CCV*/
        public static final String NAME_CCV = "NAMECCV";/*Tên CCV*/

 /*E start*/
        public static final String EMAIL_CCV = "EMAILCCV";/*email CCV*/

 /*S start*/
        public static final String SQDVB_CCV = "SQDVBCCV";/*số quyết định văn bản*/
        public static final String SQDVBBN = "SQDVBBN";/*số quyết định văn bản bổ nhiệm*/

 /*T start*/
        public static final String TOCHUC_CCV = "TOCHUCCCV";/*địa chỉ tổ chức dự kiến hành nghề CCV*/

 /*L start*/
        public static final String LYDOVANBAN = "LYDOVANBAN";

        public static final String HEADER_QD = "HEADER_QD";

        public static final String HEA_DD = "HEA_DD";
        public static final String HEA_MM = "HEA_MM";
        public static final String HEA_YY = "HEA_YY";

    }

    public interface TYPE_ACTION_NOTARY_INFO_HIS {

        /*list tương ứng*/
        public static final String DK_TAP_SU = "DK_TAP_SU";
        public static final String TAM_NGUNG_TAP_SU = "TAM_NGUNG_TAP_SU";
        public static final String CHAM_DUT_TAP_SU = "CHAM_DUT_TAP_SU";
        public static final String THAY_DOI_NOI_TAP_SU = "THAY_DOI_NOI_TAP_SU";
        public static final String TIEP_TUC_TAP_SU = "TIEP_TUC_TAP_SU";
        public static final String HOAN_THANH_TAP_SU = "HOAN_THANH_TAP_SU";
        public static final String DAT_KQ_TAP_SU = "DAT_KQ_TAP_SU";

        public static final String DA_BO_NHIEM = "DA_BO_NHIEM";
        public static final String TU_CHOI_BO_NHIEM = "TU_CHOI_BO_NHIEM";
        public static final String DA_MIEN_NHIEM = "DA_MIEN_NHIEM";
        public static final String TU_CHOI_MIEN_NHIEM = "TU_CHOI_MIEN_NHIEM";
        public static final String DA_BO_NHIEM_LAI = "DA_BO_NHIEM_LAI";
        public static final String TU_CHOI_BO_NHIEM_LAI = "TU_CHOI_BO_NHIEM_LAI";

        public static final String CHO_CAP_THE = "CHO_CAP_THE";
        public static final String TU_CHOI_CAP_THE = "TU_CHOI_CAP_THE";
        public static final String DK_HNCC_VA_CAP_THE = "DK_HNCC_VA_CAP_THE";

        public static final String TAM_DINH_CHI_HNCC = "TAM_DINH_CHI_HNCC";
        public static final String HUY_TAM_DINH_CHI_HNCC = "HUY_TAM_DINH_CHI_HNCC";

        public static final String XOA_DK_HNCC_VA_THU_HOI_THE_CCV = "XOA_DK_HNCC_VA_THU_HOI_THE_CCV";

        public static final String DE_NGHI_BO_NHIEM = "DE_NGHI_BO_NHIEM";
        public static final String DE_NGHI_BO_NHIEM_LAI = "DE_NGHI_BO_NHIEM_LAI";
        public static final String DE_NGHI_MIEN_NHIEM = "DE_NGHI_MIEN_NHIEM";

        public static final String DE_NGHI_CAP_LAI_THE_CCV = "DE_NGHI_CAP_LAI_THE_CCV";
        public static final String DA_CAP_LAI_THE_CCV = "DA_CAP_LAI_THE_CCV";
        public static final String TU_CHOI_CAP_LAI_THE_CCV = "TU_CHOI_CAP_LAI_THE_CCV";

        public static final String XU_PHAT_CCV = "XU_PHAT_CCV";
    }

    public interface TYPE_ACTION_ORG_NOTARY_INFO_HIS {

        /*PCC*/
        public static final String THANH_LAP_PCC = "THANH_LAP_PCC";

        /*VPCC*/
        public static final String CHO_THANH_LAP = "CHO_THANH_LAP";
        public static final String TU_CHOI_THANH_LAP = "TU_CHOI_THANH_LAP";
        public static final String DA_THANH_LAP = "DA_THANH_LAP";
        public static final String THU_HOI_THANH_LAP = "THU_HOI_THANH_LAP";
        /**/
        public static final String CHO_CAP_GIAY_DKHD = "CHO_CAP_GIAY_DKHD";
        public static final String CAP_MOI_GIAY_DK_HOAT_DONG = "CAP_MOI_GIAY_DK_HOAT_DONG";
        public static final String TU_CHOI_CAP_GIAY_DK_HOAT_DONG = "TU_CHOI_CAP_GIAY_DK_HOAT_DONG";
        public static final String THU_HOI_GIAY_DK_HOAT_DONG = "THU_HOI_GIAY_DK_HOAT_DONG";
        public static final String CAP_LAI_GIAY_DK_HOAT_DONG = "CAP_LAI_GIAY_DK_HOAT_DONG";
        public static final String GHI_NHAN_THAY_DOI = "GHI_NHAN_THAY_DOI";

        public static final String GIAI_THE_PCC = "GIAI_THE_PCC";
        public static final String CHAM_DUT_HOAT_DONG_VPCC = "CHAM_DUT_HOAT_DONG_VPCC";
        public static final String CHUYEN_DOI_PCC = "CHUYEN_DOI_PCC";
        public static final String CHUYEN_NHUONG_VPCC = "CHUYEN_NHUONG_VPCC";
        public static final String SAT_NHAP_VPCC = "SAT_NHAP_VPCC";
        public static final String HOP_NHAT_VPCC = "HOP_NHAT_VPCC";
        public static final String CHO_BO_SUNG = "CHO_BO_SUNG";
    }

}
