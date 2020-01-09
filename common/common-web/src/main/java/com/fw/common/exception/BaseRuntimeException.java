package com.fw.common.exception;

import com.fw.common.entity.RestError;

public class BaseRuntimeException extends RuntimeException {
    private String errorCode;

    private String[] args;

    private RestError restError;

    public BaseRuntimeException(String errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(String errorCode, String... args) {
        super();
        this.args = args;
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(RestError restError) {
        super(restError.getMessage());
        this.restError = restError;
    }

    public RestError getRestError() {
        return restError;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String[] getArgs() {return args; }
}
