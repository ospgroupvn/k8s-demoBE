package com.osp.bttp.dao.model.entity.db4;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "C_CATEGORY")
@Data
public class LCategory extends BaseModel {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CODE")
    private String code;
    @Column(name = "CAT_TYPE")
    private String catType;
    @Column(name = "CAT_LEVEL")
    private Long level;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column (name="STATUS")
    private Long status;
    @Column(name="DES")
    private String des;
}
