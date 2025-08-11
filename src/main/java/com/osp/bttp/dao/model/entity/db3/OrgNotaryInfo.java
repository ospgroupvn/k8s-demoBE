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
@Table(name = "ORG_NOTARY_INFO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrgNotaryInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="ORG_NOTARY_INFO_SEQ", sequenceName="ORG_NOTARY_INFO_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_NOTARY_INFO_SEQ")
    private Long id;
    @Size(max = 500)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 15)
    @Column(name = "TEL")
    private String tel;
    @Size(max = 15)
    @Column(name = "FAX")
    private String fax;
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 50)
    @Column(name = "WEBSITE")
    private String website;
    @Column(name = "NOTARY_ID_OFFICE_CHIEF")
    private Long notaryIdOfficeChief;
    @Column(name = "ACTIVE")
    private Long active;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "TYPE")
    private Long type;
    @Column(name = "ADDRESS_ID")
    private Long addressId;
    @Column(name = "ADMINISTRATION_ID")
    private Long administrationId;
    @Column(name = "STATUS_BEFOR")
    private String statusBefor;

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgNotaryInfo)) {
            return false;
        }
        OrgNotaryInfo other = (OrgNotaryInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
