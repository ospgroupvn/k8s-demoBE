package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PROBATIONARY_INFO")
public class ProbationaryInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @SequenceGenerator(name="PROBATIONARY_INFO_SEQ", sequenceName="PROBATIONARY_INFO_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROBATIONARY_INFO_SEQ")
    private Long id;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "DATE_START")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStart;
    @Column(name = "ORG_NOTARY_INFO_ID")
    private Long orgNotaryInfoId;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "DATE_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Size(max = 500)
    @Column(name = "NOTE")
    private String note;
    @Column(name = "TYPE_CERTIFICATE")
    private Long typeCertificate;
    @Column(name = "DATE_NUMBER")
    private Long dateNumber;
    @Column(name = "DOCUMENT_CERTIFICATE_ID")
    private Long documentCertificateId;
    @Column(name = "ORG_NOTARY_INFO_TO")
    private Long orgNotaryInfoTo;
    @Column(name = "NOTARY_TUTORIAL_ID")
    private Long notaryTutorialId;

    public ProbationaryInfo() {
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
    
    public ProbationaryInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getNotaryInfoId() {
        return notaryInfoId;
    }

    public void setNotaryInfoId(Long notaryInfoId) {
        this.notaryInfoId = notaryInfoId;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Long getOrgNotaryInfoId() {
        return orgNotaryInfoId;
    }

    public void setOrgNotaryInfoId(Long orgNotaryInfoId) {
        this.orgNotaryInfoId = orgNotaryInfoId;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTypeCertificate() {
        return typeCertificate;
    }

    public void setTypeCertificate(Long typeCertificate) {
        this.typeCertificate = typeCertificate;
    }

    public Long getDateNumber() {
        return dateNumber;
    }

    public void setDateNumber(Long dateNumber) {
        this.dateNumber = dateNumber;
    }

    public Long getDocumentCertificateId() {
        return documentCertificateId;
    }

    public void setDocumentCertificateId(Long documentCertificateId) {
        this.documentCertificateId = documentCertificateId;
    }

    public Long getOrgNotaryInfoTo() {
        return orgNotaryInfoTo;
    }

    public void setOrgNotaryInfoTo(Long orgNotaryInfoTo) {
        this.orgNotaryInfoTo = orgNotaryInfoTo;
    }

    public Long getNotaryTutorialId() {
        return notaryTutorialId;
    }

    public void setNotaryTutorialId(Long notaryTutorialId) {
        this.notaryTutorialId = notaryTutorialId;
    }
}
