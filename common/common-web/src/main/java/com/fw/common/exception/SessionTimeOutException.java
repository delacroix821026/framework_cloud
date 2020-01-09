package com.fw.common.exception;

public class SessionTimeOutException extends BaseRuntimeException {
    public SessionTimeOutException(String errorCode) {
        super(errorCode);
    }

    public SessionTimeOutException(String errorCode, String... args) {
        super(errorCode, args);
    }
}
