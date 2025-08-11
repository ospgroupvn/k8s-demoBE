/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "NOTARY_PENALIZE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotaryPenalize extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="NOTARY_PENALIZE_SEQ", sequenceName="NOTARY_PENALIZE_SEQ",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTARY_PENALIZE_SEQ")
    private Long id;
    @Column(name = "ADMINISTRATION_ID_PENALTY")
    private Long administrationIdPenalty;
    @Column(name = "NOTARY_INFO_ID")
    private Long notaryInfoId;
    @Column(name = "ORG_NOTARY_ID")
    private Long orgNotaryId;
    @Column(name = "DOCUMENT_ID")
    private Long documentId;
    @Column(name = "TYPE_PENALIZE")
    private Long typePenalize;
    @Size(max = 2000)
    @Column(name = "REASON")
    private String reason;
    @Column(name = "LEVER_PENALIZE")
    private Long leverPenalize;
    @Column(name = "ADDITIONAL_PENALTY")
    private Long additionalPenalty;
    @Column(name = "MONEY_PENALTY")
    private Long moneyPenalty;
    @Column(name = "ACTIVE")
    private Long active;
}
