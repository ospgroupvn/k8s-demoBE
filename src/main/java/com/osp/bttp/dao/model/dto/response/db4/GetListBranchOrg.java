package com.osp.bttp.dao.model.dto.response.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class GetListBranchOrg {
    private Long id;

    private String fullName;

    private String address;

    private String sdt;

    private String repName;

    private String regisNumber;

    private Date issueDate;
}
