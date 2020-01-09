package com.fw.common.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "exception-desp")
public class ExceptionMapping {
    private Map<String, String> exceptionMap = new HashMap<>();

    private Map<String, String> sysExceptionMap = new HashMap<>();

    public String getValue(String key, String... args) {
        if(sysExceptionMap.containsKey(key))
            return MessageFormat.format(exceptionMap.get(key), args);
        else if(exceptionMap.containsKey(key))
            return MessageFormat.format(exceptionMap.get(key), args);
        else
            return MessageFormat.format(key, args);
    }

    public Map<String, String> getExceptionMap() {
        return exceptionMap;
    }

    public void setExceptionMap(Map<String, String> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

}
