package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_PROPERTY_FILES")
public class AuPropertyFiles {
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_PROPERTY_FILES_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Column(name = "ID")
    private Long  id;
    @Column(name = "FILE_ID")
    private Long  fileId;
    @Column(name = "PROPERTY_ID")
    private Long propertyId;
    @Column(name = "GEN_DATE")
    private Date genDate;

    public AuPropertyFiles(Long fileId, Long propertyId, Date genDate) {
        this.fileId = fileId;
        this.propertyId = propertyId;
        this.genDate = genDate;
    }

    public AuPropertyFiles() {
    }
    
        

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
