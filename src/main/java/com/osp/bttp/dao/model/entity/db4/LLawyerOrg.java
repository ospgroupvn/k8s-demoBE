package com.osp.bttp.dao.model.entity.db4;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LAWYER_ORG")
@Data
public class LLawyerOrg extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lawyer_org_id", nullable = false)
    private Long lawyerOrgId;

    @Column(name = "lawyer_id", nullable = false)
    private Long lawyerId;

    @Column(name = "org_id",nullable = false)
    private Long orgId;

}
