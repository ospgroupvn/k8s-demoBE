package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_PROPERTY_INFO")
public class AuPropertyInfo {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_PROPERTY_INFO_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long auPropertyInfoId;
    @Column(name = "PROPERTY_NAME")
    private String propertyName;
    @Column(name = "PROPERTY_AMOUNT")
    private String propertyAmount;
    @Column(name = "PROPERTY_QUALITY")
    private String propertyQuality;
    @Column(name = "PROPERTY_START_PRICE")
    private Long propertyStartPrice;
    @Column(name = "HAS_FILES")
    private Long hasFiles;
    @Column(name = "DETAIL")
    private String detail;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
    @Column(name = "BELONG_TYPE")
    private Long belongType;
    @Column(name = "DEPOSIT")
    private Long deposit;
    @Column(name = "DEPOSIT_UNIT")
    private Long depositUnit;
    @Column(name = "PROPERTY_PLACE")
    private String propertyPlace;
    @Column(name = "FILE_COST")
    private Long fileCost;

    public Long getAuPropertyInfoId() {
        return auPropertyInfoId;
    }

    public void setAuPropertyInfoId(Long auPropertyInfoId) {
        this.auPropertyInfoId = auPropertyInfoId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(String propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public String getPropertyQuality() {
        return propertyQuality;
    }

    public void setPropertyQuality(String propertyQuality) {
        this.propertyQuality = propertyQuality;
    }

    public Long getPropertyStartPrice() {
        return propertyStartPrice;
    }

    public void setPropertyStartPrice(Long propertyStartPrice) {
        this.propertyStartPrice = propertyStartPrice;
    }

    public Long getHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(Long hasFiles) {
        this.hasFiles = hasFiles;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getBelongType() {
        return belongType;
    }

    public void setBelongType(Long belongType) {
        this.belongType = belongType;
    }

    public Long getDeposit() {
        return deposit;
    }

    public void setDeposit(Long deposit) {
        this.deposit = deposit;
    }

    public Long getDepositUnit() {
        return depositUnit;
    }

    public void setDepositUnit(Long depositUnit) {
        this.depositUnit = depositUnit;
    }

    public String getPropertyPlace() {
        return propertyPlace;
    }

    public void setPropertyPlace(String propertyPlace) {
        this.propertyPlace = propertyPlace;
    }

    public Long getFileCost() {
        return fileCost;
    }

    public void setFileCost(Long fileCost) {
        this.fileCost = fileCost;
    }
}
