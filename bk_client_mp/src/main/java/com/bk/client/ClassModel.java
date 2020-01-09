package com.fw.oauth.client;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClassModel {
    private String packageName;

    private List<String> importList = new ArrayList<String>();

    private String className;

    private String serviceName;

    private String serviceVariable;

    private Annotation requestMapping;

    private Method[] methods;
}
