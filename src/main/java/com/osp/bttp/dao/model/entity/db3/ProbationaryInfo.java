package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PROBATIONARY_INFO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProbationaryInfo  extends BaseEntity implements Serializable {
    
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
    @Column(name = "DATE_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnd;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "ACTIVE")
    private Long active;
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


}
