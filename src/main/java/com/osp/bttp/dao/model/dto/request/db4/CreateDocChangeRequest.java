package com.osp.bttp.dao.model.dto.request.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocChangeRequest {
    private Long idLic;
    private String typeOrgChange;
    private String vbChange;
    private Date issueDate;
    private String changeContent;
    private Integer isDomestic;
    private Long ownerId;
    private Integer ownerType;
}
