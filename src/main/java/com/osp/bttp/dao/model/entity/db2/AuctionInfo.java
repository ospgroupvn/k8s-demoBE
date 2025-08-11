package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AIMS_AUC_INFO")
public class AuctionInfo {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_AUC_INFO_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "CHOICE_ORG_ID")
    private Long  choiceOrgId;
    @Column(name = "AUC_TYPE")
    private Long  aucType;
    @Column(name = "AUC_TIME")
    private Date aucTime;
    @Column(name = "AUC_ADDR")
    private String aucAddr;
    @Column(name = "AUC_CONDITION")
    private String aucCondition;
    @Column(name = "AUC_INFO")
    private String aucInfo;
    @Column(name = "AUC_STATUS")
    private Long  aucStatus;
    @Column(name = "AUC_RESULT")
    private String aucResult;
    @Column(name = "USER_ID_CREATE")
    private Long  userIdCreate;
    @Column(name = "USER_ID_UPDATED")
    private Long  userIdUpdated;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "LAST_UPDATED")
    private Date lastupdated;
    @Column(name = "PUBLISH_TIME_1")
    private Date publishTime1;
    @Column(name = "PUBLISH_TIME_2")
    private Date publishTime2;
    @Column(name = "USER_ID_APPROVE")
    private Long  userIdApprove;
    @Column(name = "APPROVE_TIME")
    private Date approveTime;
    @Column(name = "ROOT_ID")
    private Long  rootId;
    @Column(name = "OWNER_ID")
    private Long  ownerId;
    @Column(name = "ORG_ID")
    private Long  orgId;
    @Column(name = "AUC_REG_TIME_START")
    private Date aucRegTimeStart;
    @Column(name = "AUC_REG_TIME_END")
    private Date aucRegTimeEnd;
    @Column(name = "AUC_METHOD")
    private Long aucMethod;
    @Column(name = "PROPERTY_VIEW_LOCATION")
    private String propertyViewLocation;
    @Column(name = "FILE_SELL_LOCATION")
    private String fileSellLocation;
    @Column(name = "AUC_TIME_DEPOSIT_START")
    private Date aucTimeDepositStart;
    @Column(name = "AUC_TIME_DEPOSIT_END")
    private Date aucTimeDepositEnd;
    @Column(name = "ROOT_PUBLISH2_ID")
    private Long  rootPublish2Id;
    @Column(name = "IS_BLOCK")
    private Long  isBlock;
    @Column(name = "REASON_BLOCK")
    private String reasonBlock;
    @Column(name = "IS_QUICK")
    private boolean isQuick;

    public AuctionInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChoiceOrgId() {
        return choiceOrgId;
    }

    public void setChoiceOrgId(Long choiceOrgId) {
        this.choiceOrgId = choiceOrgId;
    }

    public Long getAucType() {
        return aucType;
    }

    public void setAucType(Long aucType) {
        this.aucType = aucType;
    }

    public Date getAucTime() {
        return aucTime;
    }

    public void setAucTime(Date aucTime) {
        this.aucTime = aucTime;
    }

    public String getAucAddr() {
        return aucAddr;
    }

    public void setAucAddr(String aucAddr) {
        this.aucAddr = aucAddr;
    }

    public String getAucCondition() {
        return aucCondition;
    }

    public void setAucCondition(String aucCondition) {
        this.aucCondition = aucCondition;
    }

    public String getAucInfo() {
        return aucInfo;
    }

    public void setAucInfo(String aucInfo) {
        this.aucInfo = aucInfo;
    }

    public Long getAucStatus() {
        return aucStatus;
    }

    public void setAucStatus(Long aucStatus) {
        this.aucStatus = aucStatus;
    }

    public String getAucResult() {
        return aucResult;
    }

    public void setAucResult(String aucResult) {
        this.aucResult = aucResult;
    }

    public Long getUserIdCreate() {
        return userIdCreate;
    }

    public void setUserIdCreate(Long userIdCreate) {
        this.userIdCreate = userIdCreate;
    }

    public Long getUserIdUpdated() {
        return userIdUpdated;
    }

    public void setUserIdUpdated(Long userIdUpdated) {
        this.userIdUpdated = userIdUpdated;
    }

    public Date getGenDate() {
        return genDate;
    }

    public void setGenDate(Date genDate) {
        this.genDate = genDate;
    }

    public Date getLastupdated() {
        return lastupdated;
    }

    public void setLastupdated(Date lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Date getPublishTime1() {
        return publishTime1;
    }

    public void setPublishTime1(Date publishTime1) {
        this.publishTime1 = publishTime1;
    }

    public Date getPublishTime2() {
        return publishTime2;
    }

    public void setPublishTime2(Date publishTime2) {
        this.publishTime2 = publishTime2;
    }

    public Long getUserIdApprove() {
        return userIdApprove;
    }

    public void setUserIdApprove(Long userIdApprove) {
        this.userIdApprove = userIdApprove;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Date getAucRegTimeStart() {
        return aucRegTimeStart;
    }

    public void setAucRegTimeStart(Date aucRegTimeStart) {
        this.aucRegTimeStart = aucRegTimeStart;
    }

    public Date getAucRegTimeEnd() {
        return aucRegTimeEnd;
    }

    public void setAucRegTimeEnd(Date aucRegTimeEnd) {
        this.aucRegTimeEnd = aucRegTimeEnd;
    }

    public Long getAucMethod() {
        return aucMethod;
    }

    public void setAucMethod(Long aucMethod) {
        this.aucMethod = aucMethod;
    }

    public String getPropertyViewLocation() {
        return propertyViewLocation;
    }

    public void setPropertyViewLocation(String propertyViewLocation) {
        this.propertyViewLocation = propertyViewLocation;
    }

    public String getFileSellLocation() {
        return fileSellLocation;
    }

    public void setFileSellLocation(String fileSellLocation) {
        this.fileSellLocation = fileSellLocation;
    }

    public Date getAucTimeDepositStart() {
        return aucTimeDepositStart;
    }

    public void setAucTimeDepositStart(Date aucTimeDepositStart) {
        this.aucTimeDepositStart = aucTimeDepositStart;
    }

    public Date getAucTimeDepositEnd() {
        return aucTimeDepositEnd;
    }

    public void setAucTimeDepositEnd(Date aucTimeDepositEnd) {
        this.aucTimeDepositEnd = aucTimeDepositEnd;
    }

    public Long getRootPublish2Id() {
        return rootPublish2Id;
    }

    public void setRootPublish2Id(Long rootPublish2Id) {
        this.rootPublish2Id = rootPublish2Id;
    }

    public Long getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Long isBlock) {
        this.isBlock = isBlock;
    }

    public String getReasonBlock() {
        return reasonBlock;
    }

    public void setReasonBlock(String reasonBlock) {
        this.reasonBlock = reasonBlock;
    }

    public boolean isQuick() {
        return isQuick;
    }

    public void setQuick(boolean quick) {
        isQuick = quick;
    }
}
