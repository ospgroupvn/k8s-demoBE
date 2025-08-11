/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db3;

import com.osp.bttp.dao.model.entity.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "acc_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_GROUP_SEQ")
    @SequenceGenerator(sequenceName = "ACC_GROUP_SEQ", allocationSize = 1, name = "ACC_GROUP_SEQ")
    private Long id;

    @Column(name = "GROUP_NAME",nullable = false)
    private String groupName;

    @Column(name = "STATUS",nullable = false)
    private Integer status;

    @Column(name = "AUTHORITY",length = 1024)
    private String authority;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private Integer type;


    //source
    @Column(name = "SOURCE")
    private String source;

    @Column(name = "IS_DEFAULT")
    private Long isDefault;


}
