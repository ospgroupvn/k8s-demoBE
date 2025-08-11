package com.osp.bttp.dao.model.mview;

import jakarta.persistence.Column;
import lombok.Data;

/**
 * @author sangnk
 * @Created 09/10/2024 - 8:30 CH
 * @project = bttp
 * @_ Mô tả:
 */
@Data
public class BaseView {
    @Column(name = "R__")
    private int rownum;
}
