package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 12/27/2017.
 */
@Entity
@Table(name = "ADM_AUTHORITIES")
@Data
public class Authority implements Serializable {

    private static final long serialVersionUID = 2894810169009008957L;
    @Id
    @SequenceGenerator(name = "ADM_AUTHORITIES_SEQ", sequenceName = "ADM_AUTHORITIES_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADM_AUTHORITIES_SEQ")
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "AUTHORITY", nullable = false)
    private String authority;

    @Column(name = "FID", nullable = false)
    private long fid;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_ID")
    private int orderId;

    @Column(name = "AUTH_KEY", nullable = false)
    private String authKey;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "GEN_DATE", nullable = false)
    private Date genDate;

    @Column(name = "LAST_UPDATED", nullable = false)
    private Date lastUpdated;

}
