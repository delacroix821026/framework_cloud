package com.fw.k8s;

import org.apache.maven.plugin.MojoFailureException;

public class ProfileException extends MojoFailureException {
    public ProfileException(String message) {
        super(message);
    }
}
