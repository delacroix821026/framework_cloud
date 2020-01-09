package com.fw.oauth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class LoginException extends OAuth2Exception {
    private String username;
    public LoginException(String errorCode, String username) {
        super(errorCode);
        this.username = username;
    }

}
