package com.osp.bttp.dao.model.entity.db4;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

/**
 * @author sangnk
 * @Created 14/03/2025 - 9:05 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Entity
@Table(name = "NATIONALITIES")
@Data
public class LNationality extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NATIONALITY_ID")
    private Long nationalityId;

    @Column(name = "NATIONALITY_NAME", nullable = false)
    private String nationalityName;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    private Integer status;
}