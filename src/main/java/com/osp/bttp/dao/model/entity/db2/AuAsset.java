package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;

@Entity
@Table(name = "AU_ASSET")
public class AuAsset {
    @Id
    @Column(name = "AU_ASSET_ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AU_ASSET_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long auAssetId;
    @Column(name = "ASSET_NAME")
    private String assetName;
    @Column(name = "AMOUNT")
    private Long amount;
    @Column(name = "FIRST_PRICE")
    private Long firstPrice;
    @Column(name = "ASSET_QUALITY")
    private String assetQuality;
    @Column(name = "SELECT_AUCTION_ORG_ID")
    private Long selectAuctionOrgId;
    @Column(name = "IS_ACTIVE")
    private Long isActive;
    @Column(name = "ASSET_TYPE")
    private Long assetType;
    @Column(name = "ATTACH_FILE")
    private String attachFile;

    public Long getAuAssetId() {
        return auAssetId;
    }

    public void setAuAssetId(Long auAssetId) {
        this.auAssetId = auAssetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(Long firstPrice) {
        this.firstPrice = firstPrice;
    }

    public String getAssetQuality() {
        return assetQuality;
    }

    public void setAssetQuality(String assetQuality) {
        this.assetQuality = assetQuality;
    }

    public Long getSelectAuctionOrgId() {
        return selectAuctionOrgId;
    }

    public void setSelectAuctionOrgId(Long selectAuctionOrgId) {
        this.selectAuctionOrgId = selectAuctionOrgId;
    }

    public Long getIsActive() {
        return isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public Long getAssetType() {
        return assetType;
    }

    public void setAssetType(Long assetType) {
        this.assetType = assetType;
    }

    public String getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(String attachFile) {
        this.attachFile = attachFile;
    }
}
