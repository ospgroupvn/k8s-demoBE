package com.osp.bttp.dao.model.dto.response.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListOrg {
    private Long id;

    private String orgName;
}
