package com.osp.bttp.dao.service.lawyer;

import com.osp.bttp.dao.model.entity.db4.LLicense;


/**
 * @author sangnk
 * @Created 15/03/2025 - 9:45 SA
 * @project = bttp
 * @_ Mô tả:
 */

public interface LLicenseChangeService {

    public void insertLicChange(LLicense license) ;
    public void deleteLicChange(Long idLicChange);

}
