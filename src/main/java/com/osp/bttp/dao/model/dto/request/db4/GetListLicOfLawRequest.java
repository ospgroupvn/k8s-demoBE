package com.osp.bttp.dao.model.dto.request.db4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetListLicOfLawRequest {
    Long idLaw;
    Integer isDomestic;
}
