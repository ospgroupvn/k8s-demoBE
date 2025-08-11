
package com.osp.bttp.dao.model.mview.bttp;


import com.osp.bttp.common.dto.PagingResult;
import com.osp.bttp.dao.model.dto.db3.OrgNotaryInfoDetail;
import com.osp.bttp.dao.model.mview.db1.OrgNotaryInfoView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgNotaryDetailResponse {
    
    private OrgNotaryInfoDetail orgNotaryInfoView;

    @Schema(description = "list các ccv đang hành nghề tại vpcc này")
    private PagingResult pageNotary;


}
