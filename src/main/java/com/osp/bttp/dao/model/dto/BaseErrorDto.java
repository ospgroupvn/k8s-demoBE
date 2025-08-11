package com.osp.bttp.dao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseErrorDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected Date timestamp;

    protected String errorMessage;

    protected String errorCode;

    protected String fields;
}
