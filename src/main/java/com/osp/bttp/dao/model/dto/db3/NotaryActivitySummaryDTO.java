package com.osp.bttp.dao.model.dto.db3;

import lombok.Data;

/**
 * @author sangnk
 * @Created 25/10/2024 - 2:59 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class NotaryActivitySummaryDTO {
    private Long administrationId;
    private Long so_ccv_thuc_hien_trong_thang;
    private Long so_cv_trong_ky;
    private Long so_cv_uoc_tinh_cuoi_ky;
    private Long so_cv_cong_chung_hop_dong_giao_dich;
    private Long so_cv_cong_chung_ban_dich_va_loai_khac;
    private Long totalTax;
    private Long totalNotaries;

}
