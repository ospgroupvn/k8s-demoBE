package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_CHOICE_ORG_PROPERTY")
public class AuChoiceOrgProperty {
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_CHOICE_ORG_PROPERTY_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Column(name = "ID")
    private Long  id;
    @Column(name = "PROPERTY_ID")
    private Long  propertyId;
    @Column(name = "CHOICE_ORG_ID")
    private Long choiceOrgId;
    @Column(name = "GEN_DATE")
    private Date genDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getChoiceOrgId() {
        return choiceOrgId;
    }

    public void setChoiceOrgId(Long choiceOrgId) {
        this.choiceOrgId = choiceOrgId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
