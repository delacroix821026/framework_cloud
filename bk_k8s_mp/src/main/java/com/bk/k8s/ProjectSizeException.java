package com.fw.k8s;

import org.apache.maven.plugin.MojoFailureException;

public class ProjectSizeException extends MojoFailureException {
    public ProjectSizeException() {
        super("Project size must equal one!");
    }
}
