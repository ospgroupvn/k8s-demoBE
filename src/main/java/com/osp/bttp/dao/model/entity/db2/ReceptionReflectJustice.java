package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;

@Entity
@Table(name = "AIMS_RECEPTION_JUSTICE")
public class ReceptionReflectJustice {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_RECEPTION_JUSTICE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name="RECEPTION_REFLECT_ID")
    private int receptionReflectId;
    @Column(name = "DEP_OF_JUSTICE_ID")
    private int depOfJusticeId;
    public ReceptionReflectJustice(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReceptionReflectId() {
        return receptionReflectId;
    }

    public void setReceptionReflectId(int receptionReflectId) {
        this.receptionReflectId = receptionReflectId;
    }

    public int getDepOfJusticeId() {
        return depOfJusticeId;
    }

    public void setDepOfJusticeId(int depOfJusticeId) {
        this.depOfJusticeId = depOfJusticeId;
    }
}
