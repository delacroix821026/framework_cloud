package com.newland.financial.p2p.common.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "exceptionDesp")
public class ExceptionMapping {
    private Map<String, String> exceptionMap = new HashMap<String, String>();

    public String getValue(String key, String... args) {
        return MessageFormat.format(exceptionMap.get(key), args);
    }

    public Map<String, String> getExceptionMap() {
        return exceptionMap;
    }

    public void setExceptionMap(Map<String, String> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

}
