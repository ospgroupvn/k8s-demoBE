/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DM_DOCUMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DmDocument extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="DOCUMENT_SEQ", sequenceName="DOCUMENT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
    private Long id;
    @Column(name = "DISPATCH_CODE")
    private String dispatchCode;
    @Column(name = "DATE_SIGN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSign;
    @Column(name = "SIGNER")
    private String signer;
    @Column(name = "UNIT_SIGN")
    private String unitSign;
    @Column(name = "LINK_FILE")
    private String linkFile;
    @Column(name = "FILE_NAME")
    private String fileName;
    @Column(name = "NOTE")
    private String note;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Column(name = "ACTIVE")
    private Long active;

    @Column(name = "DATE_GRANT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateGrant;

}
