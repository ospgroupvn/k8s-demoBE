package com.osp.bttp.dao.model.dto.db3;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class AuditorDto {

    private String createdBy;

    private Date createdDate;

    private String updatedBy;

    private Date lastModifiedDate;

}

