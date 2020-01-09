package com.fw.gateway.exception;

import lombok.Getter;

@Getter
public class UnregisteredUserException extends RuntimeException {
    public UnregisteredUserException(String token) {
        this.token = token;
    }
    private String token;
}
