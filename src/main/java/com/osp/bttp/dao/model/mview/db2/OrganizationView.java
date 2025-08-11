/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.osp.bttp.dao.model.mview.db2;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

/**
 * @author admin
 */
@Entity
@Data
public class OrganizationView  {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORG_TYPE")
    private Long orgType;
    @Column(name = "FULLNAME")
    private String fullname;
    @Column(name = "AUCTIONEER_NAME")//nguoi dai dien To chuc DG
    private String auctioneerName;
    @Column(name = "ORG_ROOT")
    private Long orgRoot;

    @Column(name = "ADDR_DISTRICT_ID")
    private Long districtId;
    @Column(name = "ADDR_CITY_ID")
    private Long cityId;


    @Column(name = "ADDR")
    private String address;
    @Column(name = "EFF_DATE")
    private Date effDate;



    @Column(name = "QUANTITY_AUCTIONEER")
    private Long quantityAuctioneer;

    @Column(name = "STATUS")
    @Schema(description = "Trạng thái. 0 = đang hoạt động, 1 = dừng, 2 = tạm dừng")
    private Long status;

    private String province;



//    @Column(name = "R__")
//    private int rownum;
}
