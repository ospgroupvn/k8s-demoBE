package com.osp.bttp.dao.model.entity.db1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NOTARY_CARD")
public class NotaryCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="NOTARY_CARD_SEQ", sequenceName="NOTARY_CARD_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_CARD_SEQ")
    @Column(name = "ID")
    private Long id;
    @Size(max = 500)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "TYPE_DOCUMENT")
    private Long typeDocument;
    @Column(name = "ACTIVE")
    private Long active;
    @Size(max = 50)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "GEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;
    @Size(max = 50)
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "NOTARY_REG_PRACTICE_ID")
    private Long notaryRegPracticeId;
    @Column(name = "NOTARY_REQ")
    private Long notaryReq;
    @Column(name = "DATE_REQ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReq;
    @Column(name = "REASON_ID")
    private String reasonId;

    public NotaryCard() {
    }

    public NotaryCard(Long id) {
        this.id = id;
    }

    public Long getNotaryReq() {
        return notaryReq;
    }

    public void setNotaryReq(Long notaryReq) {
        this.notaryReq = notaryReq;
    }

    public Date getDateReq() {
        return dateReq;
    }

    public void setDateReq(Date dateReq) {
        this.dateReq = dateReq;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(Long typeDocument) {
        this.typeDocument = typeDocument;
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

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getNotaryRegPracticeId() {
        return notaryRegPracticeId;
    }

    public void setNotaryRegPracticeId(Long notaryRegPracticeId) {
        this.notaryRegPracticeId = notaryRegPracticeId;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }
}
