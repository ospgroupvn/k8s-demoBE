package com.osp.bttp.dao.model.entity.db4;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * @author sangnk
 * @Created 14/03/2025 - 8:37 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Entity
@Table(name = "LAWYER_ASSOCIATIONS")
@Data
public class LLawyerAssociation extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ASSOC_ID", nullable = false)
    private Long assocId;

    @Column(name = "ASSOC_NAME", nullable = false)
    private String assocName;

    private String address;

    private String phone;

    private String email;

    @Column(name = "ESTABLISHED_DATE")
    private Date establishedDate;

    private Integer status; // 0: inactive, 1: active, 2: deleted
}
