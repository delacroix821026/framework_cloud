package com.fw.oauth.client.exception;

import org.apache.maven.plugin.MojoFailureException;

public class JarNotFoundException extends MojoFailureException {
    public JarNotFoundException(String message) {
        super(message);
    }
}
