package com.osp.bttp.dao.model.validator;

import com.osp.bttp.common.contants.ConstantsLawyer;
import com.osp.bttp.dao.model.entity.db4.LLicense;

public class LLicValidator {
    public static String validatorLic(Integer isDomestic, LLicense license){
        StringBuilder erros = new StringBuilder();
        if(!license.getOwnerType().equals(ConstantsLawyer.TYPE_OWNER.LAWYER)){
            erros.append("Loại sở hữu không hợp lệ (1) \n");
        }
        if(isDomestic == 1){
            if(!license.getLicenseType().equals(ConstantsLawyer.LICENSE_TYPE.THE_LS)){
                erros.append("Loại thẻ k hợp lệ The_Ls (2) \n");
            }

        }else {
            if(!license.getLicenseType().equals(ConstantsLawyer.LICENSE_TYPE.GPHN)){
                erros.append("Loại thẻ k hợp lệ GPHN (3) \n");
            }

        }
        return erros.toString();
    }
    public static String validateDocument(Integer isDomestic, LLicense license) {
        StringBuilder erros = new StringBuilder();

        if(!license.getOwnerType().equals(ConstantsLawyer.TYPE_OWNER.ORG)){
            erros.append("Loại giấy phép phải của tổ chức (2) \n");
        }
        if (isDomestic == 1) {
            if (!license.getLicenseType().equals(ConstantsLawyer.LICENSE_TYPE.DKHD)) {
                erros.append("Loại giấy phép phải là DKHD(5) \n");
            }
        } else {
            if (!license.getLicenseType().equals(ConstantsLawyer.LICENSE_TYPE.GPTL)) {
                erros.append("Loại giấy phép phải là GPTL(6) \n");
            }
        }
        return erros.toString();
    }

}
