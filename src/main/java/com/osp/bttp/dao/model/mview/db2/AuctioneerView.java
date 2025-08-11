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

/**
 *
 * @author admin
 */
@Entity
@Data
public class AuctioneerView {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "FULLNAME")
    private String fullname;

    @Column(name = "ADDR_FULL")
    private String addFull;

    @Column(name = "CER_CODE")
    @Schema(description = "Số chứng chỉ hành nghề", example = "484/TP/ĐG-CCHN")
    private String cerCode;

    @Schema(description = "Trạng thái chứng chỉ hành nghề", example = "1")
    private Long cerStatus;

    @Column(name = "CARD_CODE")
    private String cardCode;

    @Schema(description = "Trạng thái thẻ đấu giá", example = "1")
    private Long cardStatus;

    @Column(name = "DOB")
    private String dob;

    @Column(name = "ORG_ID")
    private Long orgID;

    @Schema(description = "Tên tổ chức", example = "Công ty cổ phần bất động sản")
    @Column(name = "ORG_NAME")
    private String orgName;

    @Schema(description = "Tên sở", example = "Sở tu phap TP HCM")
    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "DEPT_ADDRESS")
    @Schema(description = "Địa chỉ sở", example = "Số 1, đường Nguyễn Đình Chính, Phường 1, Quận Phú Nhuận, TP HCM")
    private String deptAddress;

    public AuctioneerView() {
    }


}
