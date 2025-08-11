package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "C_CATEGORY")
@Data
public class Category extends BaseModel {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "C_CATEGORY_SEQ")
    @SequenceGenerator(sequenceName = "C_CATEGORY_SEQ", allocationSize = 1, name = "C_CATEGORY_SEQ")
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
