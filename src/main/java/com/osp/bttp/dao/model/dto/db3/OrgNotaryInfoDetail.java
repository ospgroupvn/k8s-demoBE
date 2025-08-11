
package com.osp.bttp.dao.model.dto.db3;


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
public class OrgNotaryInfoDetail {
    
    /*Org Notary*/
    private String name;
    private String adminName;
    private String address;
    private Long addressId;
    private String tel;
    private String website;
    private String email;
    private String fax;
    private Long idOrgNotaryInfo;
    private List<Long> notaryIds;
    private Long orgNotaryStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dayActive;
    private String dayActiveStr;
    private Long statusOrg;
    @Schema(description = "Trạng thái tổ chức. 0 đang hoạt động, 1 chờ thành lập 2 giải thể, ...", example = "1")
    private String statusOrgStr;
    private Long administrationId;
    private List<NotaryInfoView> listNotary;
    /*Org Notary*/
    
    /*NotaryOfficeChief*/
    private Long notaryIdTrans;
    private String notaryNameTrans;
    private String addressResidentTrans;
    private String officeChiefName;
    private String addressResident;
    private String addressNow;
    private Long sex;
    private Long notaryIdOfficeChief;
    private String phoneNumber;
    private String emailNotary;
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    private String idNo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    private String addressIdNo;
    private String numberCad;
    /*NotaryOfficeChief*/
    
    /*Dm Document*/
    private Long documentId;
    private String establishment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEstablishment;
    private String dateEstablishmentStr;
    private String paperRegistration;
    @Schema(description = "Số quyết định cấp thẻ CCV/ Số văn bản thông báo")
    private String dispatchCode;
    @Schema(description = "Người ký")
    private String signer;
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Ngày ký")
    private Date dateSign;
    @Schema(description = "Ngày ký")
    private String dateSignStr;
    private String fileName;
    private String linkFile;
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Ngày hiệu lực")
    private Date effectiveDate;
    @Schema(description = "Ngày hiệu lực")
    private String effectiveDateStr;
    private Long documentType;
    /*Dm Document*/

    private Long type;

    private String cityId;

}
