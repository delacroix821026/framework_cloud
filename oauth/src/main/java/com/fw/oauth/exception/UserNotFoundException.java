package com.fw.oauth.exception;

public class UserNotFoundException extends LoginException {
    public UserNotFoundException(String msg, String username) {
        super(msg, username);
    }

    public String getOAuth2ErrorCode() {
        return "User not found!";
    }

    public int getHttpErrorCode() {
        return 408;
    }
}
