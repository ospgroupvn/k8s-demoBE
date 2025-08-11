/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;


import jakarta.persistence.Column;
import lombok.Data;


import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author admin
 */
@Data
public class AuctioneerInfo {

    private Long id;
    private Long auctioneerType;
    private Timestamp genDate;
    private Timestamp lastUpdated;
    private String idCode;
    private String idType;
    private String fullname;
    private String dob;
    private Long sex;
    private String addPermanent;
    private String addCurrent;
    private String telNumber;
    private String email;
    private String cerCode;
    private String cerDoi;
    private String cardPoi;
    private String cardDoi;
    private String cardCode;
    private String otherInfo;
    private Date idDOI;
    private String idPOI;
    private Date cerDOI;

    @Column(name = "CARD_POI")
    private String cardPOI;

    @Column(name = "CARD_DOI")
    private Date cardDOI;

    @Column(name = "AUCTIONEER_STATUS")
    private Long auctioneerStatus;

    @Column(name = "IS_PUBLISH")
    private Long isPublish;

    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;

    @Column(name = "ADDR_CITY_ID")
    private Long cityId;
    

    @Column(name = "CER_STATUS")
    private Long cerStatus;

    @Column(name = "CARD_STATUS")
    private Long cardStatus;

    @Column(name = "WARNING")
    private String warning;
    
     @Column(name = "ADDR_FULL")
    private String addrFull;

    
}
