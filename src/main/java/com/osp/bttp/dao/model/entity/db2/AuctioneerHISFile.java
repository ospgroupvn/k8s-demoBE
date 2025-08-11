package com.osp.bttp.dao.model.entity.db2;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "AIMS_AUCTIONEER_FILES")
public class AuctioneerHISFile {
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="AIMS_AUCTIONEER_FILES_SEQ", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Column(name = "ID")
    private Long  id;
    @Column(name = "FILE_ID")
    private Long  fileId;
    @Column(name = "AUCTIONEER_HIS_ID")
    private Long auctioneerHisID;
    @Column(name = "GEN_DATE")
    private Timestamp genDate;

    public AuctioneerHISFile() {
    }     

    public AuctioneerHISFile(Long fileId, Long auctioneerHisID) {
        this.fileId = fileId;
        this.auctioneerHisID = auctioneerHisID;      
    }
    
    

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getAuctioneerHisID() {
        return auctioneerHisID;
    }

    public void setAuctioneerHisID(Long auctioneerHisID) {
        this.auctioneerHisID = auctioneerHisID;
    }
  
    public Timestamp getGenDate() {
        return genDate;
    }

    public void setGenDate(Timestamp genDate) {
        this.genDate = genDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
