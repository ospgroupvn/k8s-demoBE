package com.osp.bttp.dao.model.entity.db2;


import com.osp.bttp.dao.model.mview.FileUpload;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "AIMS_CHOICE_ORG_NOTICE")
public class AuChoiceOrgNotice {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_CHOICE_ORG_NOTICE_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    private Long id;
    @Column(name = "OWNER_ID")
    private Long ownerId;
    @Column(name = "RECEIVE_TIME_START")
    private Date receiveTimeStart;
    @Column(name = "RECEIVE_TIME_END")
    private Date receiveTimeEnd;
    @Column(name = "RECEIVE_ADDR")
    private String receiveAddress;
    @Column(name = "CONTACT_INFO")
    private String contactInfo;
    @Column(name = "OTHER_INFO")
    private String otherInfo;
    @Column(name = "HAS_FILES")
    private Long hasFiles;
    @Column(name = "PUBLISH_STATUS")
    private Long publishStatus;
    @Column(name = "GEN_DATE")
    private Date genDate;
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
    @Column(name = "IS_CONFIRMED")
    private Long isConfirmed;
    @Column(name= "VERIFIED_STATUS")
    private Long verifiedStatus;    
    @Column(name="ROOT_ID")
    private Long rootID;
    @Column(name="NOTICE_SUB")
    private String noticeSub;
    @Column(name = "IS_BLOCK")
    private Long  isBlock;
    @Column(name = "REASON_BLOCK")
    private String reasonBlock;
    @Column(name = "USER_ID_UPDATED")
    private Long  userIdUpdated;


    @Transient
    private List<FileUpload> listFileChoice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getReceiveTimeStart() {
        return receiveTimeStart;
    }

    public void setReceiveTimeStart(Date receiveTimeStart) {
        this.receiveTimeStart = receiveTimeStart;
    }

    public Date getReceiveTimeEnd() {
        return receiveTimeEnd;
    }

    public void setReceiveTimeEnd(Date receiveTimeEnd) {
        this.receiveTimeEnd = receiveTimeEnd;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Long getHasFiles() {
        return hasFiles;
    }

    public void setHasFiles(Long hasFiles) {
        this.hasFiles = hasFiles;
    }

    public Long getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Long publishStatus) {
        this.publishStatus = publishStatus;
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

    public Long getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Long isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Long getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(Long verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }   

    public Long getRootID() {
        return rootID;
    }

    public void setRootID(Long rootID) {
        this.rootID = rootID;
    }

    public String getNoticeSub() {
        return noticeSub;
    }

    public void setNoticeSub(String noticeSub) {
        this.noticeSub = noticeSub;
    }

    public List<FileUpload> getListFileChoice() {
        return listFileChoice;
    }

    public void setListFileChoice(List<FileUpload> listFileChoice) {
        this.listFileChoice = listFileChoice;
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

    public Long getUserIdUpdated() {
        return userIdUpdated;
    }

    public void setUserIdUpdated(Long userIdUpdated) {
        this.userIdUpdated = userIdUpdated;
    }
}
