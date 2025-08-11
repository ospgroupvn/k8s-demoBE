package com.osp.bttp.dao.model.dto.db3;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProbationaryInfoCreateDto {

    private Long idOrg;

    // document
    @NotNull
    private String dispatchCode;

    private Date dateSign;

    private String note;
    private String linkFile;
    private String fileName;
    private String signer;
    // probationaryInfo
    private Date dateStart;

    private Date dateEnd;

    private Long status;

    private Long active;

    private Long typeCertificate;

    private Long dateNumber;

//    private Long documentCertificateId;

}
