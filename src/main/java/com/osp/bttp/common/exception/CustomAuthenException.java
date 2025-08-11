package com.osp.bttp.common.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenException extends AuthenticationException {
    public CustomAuthenException(String message) {
        super(message);
    }

    public CustomAuthenException(String message, Throwable cause) {
        super(message, cause);
    }
}
