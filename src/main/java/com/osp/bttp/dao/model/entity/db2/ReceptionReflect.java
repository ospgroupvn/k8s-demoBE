/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "AIMS_RECEPTION_REFLECT")
public class ReceptionReflect {

    @Id
    @SequenceGenerator(name = "SEQ_GEN", sequenceName = "AIMS_RECEPTION_REFLECT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    @Column(name = "FULLNAME")
    private String fullname;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "ID_USER_PUBLISH")
    private Long idUserPublish;
    @Column(name = "IS_PUBLISH")
    private Long isPublish;
    @Column(name = "PUBLISH_TIME")
    private Timestamp publishTime;
    @Column(name = "OWNER_ID")
    private Long ownerID;
    @Transient
    private String depOfJusticeId;
    @Transient
    private String placeName;
    public ReceptionReflect() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getIdUserPublish() {
        return idUserPublish;
    }

    public void setIdUserPublish(Long idUserPublish) {
        this.idUserPublish = idUserPublish;
    }

    public Long getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Long isPublish) {
        this.isPublish = isPublish;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }

    public String getDepOfJusticeId() {
        return depOfJusticeId;
    }

    public void setDepOfJusticeId(String depOfJusticeId) {
        this.depOfJusticeId = depOfJusticeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
