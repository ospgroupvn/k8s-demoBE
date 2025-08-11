package com.osp.bttp.common.contants;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

/**
 * @author sangnk
 * @Created 14/03/2025 - 11:35 SA
 * @project = bttp
 * @_ Mô tả:
 */
public class ConstantsLawyer {
    public interface LICENSE_TYPE {
        Integer CCHN = 1;
        Integer THE_LS = 2;
        Integer GPHN = 3;
        Integer DKHD = 5;
        Integer GPTL = 6;
    }

    ;

    public interface LICENSE_STATUS {
        public interface CCHN {
            Integer DANG_HANH_NGHE_CCHN = 14;
            Integer DA_CAP_CCHN = 15;
            Integer THU_HOI_CCHN = 16;
            Integer GIA_HAN_CCHN = 17;
        }

        public interface THE_LS {
            //      Integer DANG_HANH_NGHE_THE_LS = 24;
            Integer DA_CAP_THE_LS = 25;
            Integer THU_HOI_THE_LS = 26;
            //         Integer GIA_HAN_THE_LS = 27;
        }

        public interface GPHN {
            //     Integer DANG_HANH_NGHE = 34;
            Integer DA_CAP = 35;
            Integer THU_HOI = 36;
            Integer GIA_HAN = 37;
        }
    }

    ;

    public interface TYPE_OWNER {
        Integer LAWYER = 1;
        Integer ORG = 2;
    }

    public interface ACTIVITY_STATUS {
        Integer ACTIVE = 4;
        Integer IN_ACTIVE = 6;
    }

    public interface ORG_TYPE {
        Integer CTY_CO_PHAN = 1;
    }

    public interface PRACTICE_FORM {
        Integer Thành_lập_tham_gia_thành_lập_TCHNLS = 40;
        Integer Hop_dong_lao_dong = 41;
        Integer other = 44;
    }
}
