package com.fw.oauth.client.exception;

import org.apache.maven.plugin.MojoFailureException;

public class ClientNotExistException extends MojoFailureException {
    public ClientNotExistException(String message) {
        super(message);
    }
}
