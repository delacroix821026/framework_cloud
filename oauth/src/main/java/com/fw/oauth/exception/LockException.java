package com.fw.oauth.exception;

public class LockException extends LoginException {
    public LockException(String errorCode, String username) {
        super(errorCode, username);
    }

    public String getOAuth2ErrorCode() {
        return "Account has retry too mush times, already been locked!";
    }

    public int getHttpErrorCode() {
        return 410;
    }
}
