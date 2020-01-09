package com.fw.oauth.exception;

public class TipsException extends LoginException {
    public TipsException(String msg, String username) {
        super(msg, username);
    }

    public String getOAuth2ErrorCode() {
        return "Account in tips model!";
    }

    public int getHttpErrorCode() {
        return 409;
    }
}
