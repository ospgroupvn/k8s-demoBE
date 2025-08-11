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

@Entity
@Table(name = "NOTARY_REG_PRACTICE")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotaryRegPractice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_REG_PRACTICE_SEQ", sequenceName="NOTARY_REG_PRACTICE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_REG_PRACTICE_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "NUMBER_CAD")
    private String numberCad;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "NOTARY_REQ")
    private Long notaryReq;
    @Column(name = "DATE_REQ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReq;
    @Column(name = "TYPE_NOTARY_INFO")
    private Long typeNotaryInfo;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotaryRegPractice)) {
            return false;
        }
        NotaryRegPractice other = (NotaryRegPractice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
