package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_ORG_HIS_FILES")
public class OrgHisFile {
    @Id
    @SequenceGenerator(name="AIMS_ORG_HIS_FILE_SEQ", sequenceName="AIMS_ORG_HIS_FILE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AIMS_ORG_HIS_FILE_SEQ")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "ORG_HIS_ID")
    private Long orgHisId;
    @Column(name = "FILE_ID")
    private Long fileId;
    @Column(name = "GEN_DATE")
    private Date genDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgHisId() {
        return orgHisId;
    }

    public void setOrgHisId(Long orgHisId) {
        this.orgHisId = orgHisId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }
}
