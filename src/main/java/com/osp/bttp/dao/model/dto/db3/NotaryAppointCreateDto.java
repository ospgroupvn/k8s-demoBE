package com.osp.bttp.dao.model.dto.db3;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaryAppointCreateDto {
    @Max(3)
    @Min(1)
    private Long kind; // 1: bổ nhiệm , 2 : miễn nhiệm , 3: tái bổ nhiệm

    //document
    @NotNull
    private String dispatchCode; // số văn bản
    private Date dateSign;  // ngày đề nghị - ngày quyet dinh
    private String note;
    private String linkFile;
    private String fileName;
    private Long active;
    private String signer;

    // appoint
//    @Max(2)
//    @Min(1)
    private Long type; // 1: đề nghị , 2 quyết định

    @NotNull
    private Long notaryInfoId;

    private Long status ;

    private String reason;


    private Date effectiveDate; // ngày hiệu lực
}
