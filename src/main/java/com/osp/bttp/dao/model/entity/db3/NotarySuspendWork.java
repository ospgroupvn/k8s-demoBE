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
@Table(name = "NOTARY_SUSPEND_WORK")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotarySuspendWork extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_SUSPEND_WORK_SEQ", sequenceName="NOTARY_SUSPEND_WORK_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_SUSPEND_WORK_SEQ")
    private Long id;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Size(max = 500)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "ORG_NOTARY_ID")
    private Long orgNotaryId;
    @Column(name = "TERM_OF_SUSPENSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date termOfSuspension;
    @Column(name = "TYPE_SUPEND")
    private Long typeSupend;
    @Column(name = "SUPEND_ID")
    private Long supendId;
    @Column(name = "DATE_NUMBER")
    private Long dateNumber;
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotarySuspendWork)) {
            return false;
        }
        NotarySuspendWork other = (NotarySuspendWork) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
