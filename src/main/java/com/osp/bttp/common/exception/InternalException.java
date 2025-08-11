package com.osp.bttp.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InternalException extends Exception {

    private String errorCode;

    private List<String> fields;

    public InternalException(String msg) {
        super(msg);
    }

    public InternalException(String msg, List<String> fields) {
        super(msg);
        this.fields = fields;
    }

    public InternalException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public InternalException(String msg, Throwable t) {
        super(msg, t);
    }
}
