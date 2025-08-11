package com.osp.bttp.dao.model.mview.bttp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryAppointResponse {

    private Date dateSign; // ngày đề nghị || ngày quyết định
    private String dispatchCode;// số văn bản
    private Date effectiveDate; // ngày quyết định
    private String signer;
    private String fileName;
    private String linkFile;
    private Long notaryInfoId;
    private Long active;
    private String reason;
    private Long id;
    private Long type; // 1: đề nghị , 2 quyết định
    private Integer kind; // 1: bổ nhiệm, 2: miễn nhiệm, 3: bổ nhiệm lại
    private Long status;
    private Long documentId;
}
