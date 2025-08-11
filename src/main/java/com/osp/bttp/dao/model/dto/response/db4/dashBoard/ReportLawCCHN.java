package com.osp.bttp.dao.model.dto.response.db4.dashBoard;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportLawCCHN {
    private Long idLaw;

    private String fullName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(length = 1)
    private Integer gender;

    private String address;


    private Long provinceId;

    @Schema(description = "Số chứng chỉ, giấy phép hành nghề")
    private String certificateNumber;


    @Schema(description = "Số quyết định")
    private String decisionNumber;


    @Schema(description = "Ngày cấp chứng chỉ, giấy phép hành nghề")
    private Date issueDate;

    private Integer status;

}
