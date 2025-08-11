package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Table(name = "DM_ADMINISTRATION")
public class DmAdministration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "DM_ADMINISTRATION_SEQ", sequenceName = "DM_ADMINISTRATION_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DM_ADMINISTRATION_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "IS_CENTER")
    private Long isCenter;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "TYPE")
    private Long type;
    @Size(max = 1000)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 20)
    @Column(name = "PHONE")
    private String phone;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 50)
    @Column(name = "WEBSITE")
    private String website;
    @Size(max = 20)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "UPDATED_BY")
    private Long updatedBy;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "CREATE_BY")
    private Long createBy;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @Transient
    private String createByStr;
    @Transient
    private String updatedByStr;

}