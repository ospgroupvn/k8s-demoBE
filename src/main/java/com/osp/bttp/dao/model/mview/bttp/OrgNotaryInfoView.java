
package com.osp.bttp.dao.model.mview.bttp;


import com.osp.bttp.common.contants.ConstantsTccc;
import com.osp.bttp.common.utils.UtilsDate;
import com.osp.bttp.dao.model.mview.db1.NotaryInfoView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sang
 */

@Data
public class OrgNotaryInfoView {
    /*Org Notary*/
    private String name;
    private String adminName;
    private String address;
    private Long addressId;
    private Long idOrgNotaryInfo;
    private Long statusOrg;
    private Long administrationId;
    /*Org Notary*/
    
    /*NotaryOfficeChief*/
    private String officeChiefName;

    
}
