package com.osp.bttp.dao.model.entity.db3;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(name = "CREATED_BY",updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "GEN_DATE",updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date genDate;

    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    private String updatedBy;


    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdate;
}
