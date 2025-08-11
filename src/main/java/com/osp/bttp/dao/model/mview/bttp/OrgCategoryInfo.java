package com.osp.bttp.dao.model.mview.bttp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgCategoryInfo {

    private Long id;

    private String name;

    private Long dmAdministrationId;
}
