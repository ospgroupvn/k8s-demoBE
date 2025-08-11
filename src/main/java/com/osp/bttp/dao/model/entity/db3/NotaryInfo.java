/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NOTARY_INFO")
@Data
public class NotaryInfo  extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_INFO_SEQ", sequenceName="NOTARY_INFO_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_INFO_SEQ")
    private Long id;
    @Size(max = 200)
    @Column(name = "NAME")
    private String name;
    @Column(name = "SEX")
    private Long sex;
    @Column(name = "BIRTH_DAY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    @Size(max = 15)
    @Column(name = "ID_NO")
    private String idNo;
    @Column(name = "ID_NO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idNoDate;
    @Size(max = 500)
    @Column(name = "ADDRESS_ID_NO")
    private String addressIdNo;
    @Size(max = 500)
    @Column(name = "ADDRESS_RESIDENT")
    private String addressResident;
    @Column(name = "ADDRESS_RESIDENT_ID")
    private Long addressResidentId;
    @Size(max = 500)
    @Column(name = "ADDRESS_NOW")
    private String addressNow;
    @Column(name = "ADDRESS_NOW_ID")
    private Long addressNowId;
    @Column(name = "STATUS")
    private Long status;
    @Size(max = 15)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;

    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;

    @Transient
    private String numberCad;
    @Transient
    private String typeNotaryInfo;
    
    @Transient
    private String nameAddressResident;
    @Transient
    private String nameAddressNow;
}
