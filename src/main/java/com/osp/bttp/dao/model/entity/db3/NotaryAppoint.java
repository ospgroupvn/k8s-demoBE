/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "NOTARY_APPOINT")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryAppoint extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_APPOINT_SEQ", sequenceName="NOTARY_APPOINT_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_APPOINT_SEQ")
    private Long id;
    @Column(name = "PROBATIONARY_CODE")
    private Character probationaryCode;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "TYPE_APPOINT")
    private Long typeAppoint;
    @Column(name = "REASON")
    private String reason;
    @Column(name = "STATUS_APPOINT")
    private Long statusAppoint;
    @Column(name = "KIND")
    private Long kind;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaryAppoint)) {
            return false;
        }
        NotaryAppoint other = (NotaryAppoint) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
