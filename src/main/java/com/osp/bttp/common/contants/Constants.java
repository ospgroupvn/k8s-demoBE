package com.osp.bttp.common.contants;

import com.osp.bttp.dao.model.mview.db1.DocumentType;
import com.osp.bttp.dao.model.mview.db1.NotaryStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: write you class description here
 *
 * @author
 */
public class Constants {

    public static final String API_VERSION1 = "/v1/api";

    public static final Integer ACCOUNT_OK = 1;

    public interface HEADER_FIELD {
        String AUTHORIZATION = "Authorization";
    }

    public static final String[] AUTH_WHITELIST = {// -- swagger ui
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v1/api/auth/login"
    };
    public static final String[] NO_TOKEN_WHITELIST = {
            "/swagger-ui",
            "/v3/api-docs",
//            "/v1/api/public",
            "/v1/api/auth/login",
            "/v1/api/auctioneer/public",
            "/v1/api/auction-organization/public"
    };

    public interface PARAMETER {
        String REDIS_KEY_AUTHORITY_ALL = "bttp_author_filter_all_";
    }

    public interface TYPE_USER {
        Integer ADMIN = 45;
        Integer BO_TU_PHAP = 40;
        Integer SO_TU_PHAP = 30;
        Integer DOAN_LUAT_SU = 10;
        Integer USER = 2;
    }

    public interface TYPE_DMADMINISTRATION {
        Integer SO_TU_PHAP = 2;
        Integer CUC = 1;
    }
    public interface CATEGORY_TYPE {
        String PROVINCE = "TP";
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

        public static final Long NOT_SCAN = 0L;//không quét
        public static final Long NHD_SCAN = 1L;//quét để dừng hoạt động tổ chức
        public static final Long HD_SCAN = 2L;//quét để cập nhật tổ chức
        public static final Long ADD_SCAN = 3L;//quét tổ chức thêm mới hoạt động
        public static final Long REP_SCAN = 4L;//quét cập nhật lại tổ chức sau khi cấp lại
        public static final Long ORG_STATUS_TNHD=15L; //tạm dựng hoạt động tổ chức và chi nhánh đi kèm
        public static final Long ORG_STATUS_TNHDCN=16L; //tạm dừng hoạt động chi nhánh

        public class STATUS {
            public static final Long ORG_STATUS_ACTIVE = 0L;
            public static final Long ORG_STATUS_NHĐ = 1L;
            public static final Long ORG_DEL = 3L;
        }
    }

    public static class IS_PUBLISH {

        public static final Long IS_PUBLISH_YES = 1L;
        public static final Long IS_PUBLISH_NO = 0L;
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
                Constants.PARAMETER_TYPE_DATA.MIEN_NHIEM,
                Constants.PARAMETER_TYPE_DATA.BI_MIEN_NHIEM,
                Constants.PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM,
                Constants.PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM,
                Constants.PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM,
                Constants.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM,
                Constants.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI,
                Constants.PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE,
                Constants.PARAMETER_TYPE_DATA.CAP_LAI_THE,
                Constants.PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE,
                Constants.PARAMETER_TYPE_DATA.GIAI_THE_PCC,
                Constants.PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG,
                Constants.PARAMETER_TYPE_DATA.TAM_DINH_CHI,
                Constants.PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI,
                Constants.PARAMETER_TYPE_DATA.THU_HOI_THANH_LAP_VPCC,
                Constants.PARAMETER_TYPE_DATA.DON_VI_XU_PHAT_VI_PHAM_CCV,
                Constants.PARAMETER_TYPE_DATA.XOA_ĐK_HNCC_THU_HOI_THE_CCV,
                Constants.PARAMETER_TYPE_DATA.NOI_CAP_CMND
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
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.MIEN_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.BI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.BI_MIEN_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.DUOC_MIEN_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.DE_NGHI_MIEN_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_MIEN_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_BO_NHIEM_LAI_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_CAP_THE_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.CAP_LAI_THE)) {
                typeStr = PARAMETER_TYPE_DATA.CAP_LAI_THE_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE)) {
                typeStr = PARAMETER_TYPE_DATA.TU_CHOI_CAP_LAI_THE_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.GIAI_THE_PCC)) {
                typeStr = PARAMETER_TYPE_DATA.GIAI_THE_PCC_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG)) {
                typeStr = PARAMETER_TYPE_DATA.CHAM_DUT_HOAT_DONG_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.TAM_DINH_CHI)) {
                typeStr = PARAMETER_TYPE_DATA.TAM_DINH_CHI_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI)) {
                typeStr = PARAMETER_TYPE_DATA.HUY_TAM_DINH_CHI_STR;
            } else if (type.equals(Constants.PARAMETER_TYPE_DATA.THU_HOI_THANH_LAP_VPCC)) {
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
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TAP_SU)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAP_SU_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TAM_NGUNG_TAP_SU)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAM_NGUNG_TAP_SU_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_DE_NGHI_MIEN_NHIEM_CCV)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_MIEN_NHIEM_CCV_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DE_NGHI_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_BO_NHIEM_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_MIEN_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_MIEN_NHIEM_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_MIEN_NHIEM)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_MIEN_NHIEM_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_LAI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_BO_NHIEM_LAI_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_DK_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DK_HANH_NGHE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_CAP_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_THE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_CAP_LAI_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CAP_LAI_THE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_CAP_LAI_THE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_LAI_THE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TAM_DINH_CHI_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TAM_DINH_CHI_HANH_NGHE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_HUY_TAM_DINH_CHI_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_HUY_TAM_DINH_CHI_HANH_NGHE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_XOA_DK_HANH_NGHE)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CV_XOA_DK_HANH_NGHE_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_CCV)) {
                nameTypeStr = LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_CCV_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_THANH_LAP_PCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THANH_LAP_PCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_QD_TL_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_QD_TL_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_THU_HOI_QUYET_DINH_TL)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THU_HOI_QUYET_DINH_TL_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TC_TL_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TC_TL_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_DANG_KY_HOAT_DONG_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_DANG_KY_HOAT_DONG_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_CAP_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CAP_GIAY_DKHD_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_TU_CHOI_CAP_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_TU_CHOI_CAP_GIAY_DKHD_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_THAY_DOI_NOI_DUNG_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THAY_DOI_NOI_DUNG_DKHD_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_GHI_NHAN_THAY_DOI)) {
                nameTypeStr = LOAI_CONG_VAN.CV_GHI_NHAN_THAY_DOI_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_THU_HOI_GIAY_DKHD)) {
                nameTypeStr = LOAI_CONG_VAN.CV_THU_HOI_GIAY_DKHD_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_CHUYEN_NHUONG_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CHUYEN_NHUONG_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_TCCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_XU_LY_VI_PHAM_TCCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_GIAI_THE_PCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_GIAI_THE_PCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_HOP_NHAT_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_HOP_NHAT_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_SAP_NHAP_VPCC)) {
                nameTypeStr = LOAI_CONG_VAN.CV_SAP_NHAP_VPCC_STR;
            } else if (nameType.equals(Constants.LOAI_CONG_VAN.CV_CHAM_DUT_HOAT_DONG)) {
                nameTypeStr = LOAI_CONG_VAN.CV_CHAM_DUT_HOAT_DONG_STR;
            } else if (nameType.equals(LOAI_CONG_VAN.CV_YEU_CAU_BX_HO_SO)) {
                nameTypeStr = LOAI_CONG_VAN.CV_YEU_CAU_BX_HO_SO_STR;
            }
            return nameTypeStr;

        }
    }
}
