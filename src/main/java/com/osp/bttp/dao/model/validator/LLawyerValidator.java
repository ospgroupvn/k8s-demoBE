package com.osp.bttp.dao.model.validator;

import com.osp.bttp.dao.model.dto.response.db4.DetailLLawyerCSDLResponse;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class LLawyerValidator {
    public static String validateCommonField(DetailLLawyerCSDLResponse csdlResponse){
        StringBuilder listErrors = new StringBuilder();
        String address =  csdlResponse.getAddress();
        if(StringUtils.isBlank(address) || address.length()>100){
            listErrors.append("Địa chỉ không hợp lệ \n");
        }
        // Validate ngày sinh (phải nhỏ hơn ngày hiện tại)
        Date dateOfBirth = csdlResponse.getDateOfBirth();
        if (dateOfBirth == null || dateOfBirth.after(new Date())) {
            listErrors.append("Ngày sinh không hợp lệ (không được để trống và phải nhỏ hơn ngày hiện tại)\n");
        }

        // Validate số CMND/CCCD (chỉ chứa số và có độ dài hợp lệ)
        String identityCardNumber = csdlResponse.getIdentityCardNumber();
        if (StringUtils.isBlank(identityCardNumber) || !identityCardNumber.matches("\\d{9,12}")) {
            listErrors.append("Số CMND/CCCD không hợp lệ (phải là số và có từ 9 đến 12 chữ số)\n");
        }
        // Validate số điện thoại (chỉ chứa số và có độ dài 10-11)
        String phone = csdlResponse.getPhone();
        if (StringUtils.isBlank(phone) || !phone.matches("\\d{10,11}")) {
            listErrors.append("Số điện thoại không hợp lệ (phải là số và có từ 10 đến 11 chữ số)\n");
        }

        // Validate email (phải đúng định dạng)
        String email = csdlResponse.getEmail();
        if (StringUtils.isBlank(email) || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            listErrors.append("Email không hợp lệ (phải đúng định dạng email)\n");
        }
        return listErrors.toString();
    }
}
