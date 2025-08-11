package com.osp.bttp.dao.model.mview.bttp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryProbationaryResponse {
    private Long idOrg;

    private String orgName; // tên tổ chức

    private String address; // địa chỉ trụ sở

    // document
    @NotNull
    private Long documentId;
    private String dispatchCode;  // số giấy chứng nhận
    private String signer; // người ký
    private Date dateSign;  // ngày cấp

    private String note;
    private String linkFile;
    private String fileName;

    // probationaryInfo
    private Long idProbationary;

    private Date dateStart;  // ngày bắt đầu tập sự

    private Date dateEnd;  // ngày kết thúc

    private Long status;

    private Long active;

    private Long typeCertificate;

    private Long dateNumber;    // số tháng tập sự

    public NotaryProbationaryResponse(Long idOrg, String orgName, String address,
                                      Long documentId, String dispatchCode,String signer, Date dateSign, String fileName, String linkFile,
                                      Long idProbationary, Date dateStart, Date dateEnd,
                                      Long status, Long active, Long dateNumber,
                                      Long typeCertificate, String note) {
        this.idOrg = idOrg;
        this.orgName = orgName;
        this.address = address;
        this.documentId = documentId;
        this.dispatchCode = dispatchCode;
        this.signer = signer;
        this.dateSign = dateSign;
        this.fileName = fileName;
        this.linkFile = linkFile;
        this.idProbationary = idProbationary;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
        this.active = active;
        this.dateNumber = dateNumber;
        this.typeCertificate = typeCertificate;
        this.note = note;
    }

}
