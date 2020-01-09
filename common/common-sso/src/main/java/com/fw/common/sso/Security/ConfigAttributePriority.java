package com.fw.common.sso.Security;

import lombok.Getter;
import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;

@Getter
public class ConfigAttributePriority implements Comparable {
    public ConfigAttributePriority(int priority, Collection<ConfigAttribute> configAttributeCollection) {
        this.priority = priority;
        this.configAttributeCollection = configAttributeCollection;
    }

    private int priority;

    private Collection<ConfigAttribute> configAttributeCollection;


    @Override
    public int compareTo(Object object) {
        ConfigAttributePriority configAttributePriority = (ConfigAttributePriority) object;
        if (priority > configAttributePriority.getPriority()) {
            return 1;
        } else if (priority < configAttributePriority.getPriority())
            return -1;
        else
            return 0;
    }
}
