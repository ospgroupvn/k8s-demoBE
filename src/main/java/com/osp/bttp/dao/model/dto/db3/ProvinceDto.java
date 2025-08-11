package com.osp.bttp.dao.model.dto.db3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceDto {

    private Integer id;

    private String provinceCode;

    private String name;

    private String code;
}
