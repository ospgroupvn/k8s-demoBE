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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author
 */
@Entity
@Table(name = "acc_authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACC_AUTHORITY_SEQ")
    @SequenceGenerator(sequenceName = "ACC_AUTHORITY_SEQ", allocationSize = 1, name = "ACC_AUTHORITY_SEQ")
    private long id;

    @Column(name = "AUTHORITY")
    @NotNull(message = "Authority Không được null")
    @NotEmpty(message = "Authority Không được để trống")
    private String authority;

    @Column(name = "FID")
    @NotNull(message = "Fid Không được null")
    private long fid;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_ID")
    private int orderId;

    @Column(name = "AUTH_KEY", nullable = false)
    @NotNull(message = "AuthKey Không được null")
    @NotEmpty(message = "AuthKey Không được để trống")
    private String authKey;

    //source
    @Column(name = "SOURCE")
    private String source;

    @Column(name = "HIDDEN")
    private String hidden;

    @Column(name = "UPDATE_BY_ID")
    private Long updateById;

    @Column(name = "CREATE_BY_ID")
    private Long createById;

    public Authority(String authKey) {
        this.authKey = authKey;
    }
}
