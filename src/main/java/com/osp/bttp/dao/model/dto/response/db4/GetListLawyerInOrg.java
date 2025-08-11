package com.osp.bttp.dao.model.dto.response.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLawyerInOrg {
    private Long id;

    private String fullName;

    private Date dob;

    private String CCHNNumber;

    private String GPHNNumber;

    private String licNumber;

    private Integer typeWork;

    private Integer status;
}
