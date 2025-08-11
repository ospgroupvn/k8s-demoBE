package com.osp.bttp.dao.model.dto.response.db4;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetListLawyerAssoc {
    private Long assocId;

    @Schema(description = "Tên tổ chức", example = "Văn phòng luật sư Trần Minh Vũ")
    private String assocName;
}
