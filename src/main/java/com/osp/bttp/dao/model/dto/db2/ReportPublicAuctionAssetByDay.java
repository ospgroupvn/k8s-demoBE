package com.osp.bttp.dao.model.dto.db2;

import com.osp.bttp.common.utils.H;
import com.osp.bttp.common.utils.UtilsDate;
import lombok.Data;

import java.util.Date;

/**
 * @author sangnk
 * @Created 28/10/2024 - 9:10 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class ReportPublicAuctionAssetByDay {
    private Date publishDate;
    private String publishDateStr;
    private Long countPerDay1;
    private Long countPerDay2;

    public String getPublishDateStr() {
        if (H.isTrue(publishDate)) {
            return UtilsDate.date2str(publishDate, "dd/MM/yyyy");
        }
        return "";
    }
}
