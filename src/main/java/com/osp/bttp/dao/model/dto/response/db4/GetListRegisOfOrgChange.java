package com.osp.bttp.dao.model.dto.response.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListRegisOfOrgChange {
    private Long id;

    private String vbChange;

    private String typeChange;

    private Date dateChange;

    private String contentChange;

}
