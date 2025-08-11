package com.osp.bttp.dao.model.dto.response.db4;

import com.osp.bttp.dao.model.entity.db4.LLicense;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author sangnk
 * @Created 15/03/2025 - 1:10 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class GetListLLicenseResponse extends LLicense {
    public GetListLLicenseResponse() {
    }
    public GetListLLicenseResponse(LLicense license) {
        BeanUtils.copyProperties(license, this);
    }

    public GetListLLicenseResponse(Long ownerId, Integer ownerType, Integer LicenseType, String liscenseNumber, String practicePlace, Integer practiceForm, Integer status) {
        super();
        this.setOwnerId(ownerId);
        this.setOwnerType(ownerType);
        this.setLicenseType(LicenseType);
        this.setLicenseNumber(liscenseNumber);
        this.setPracticeForm(practiceForm);
        this.setPracticePlace(practicePlace);
        this.setStatus(status);
    }
}
