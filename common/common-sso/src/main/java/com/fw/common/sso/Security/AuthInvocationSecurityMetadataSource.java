package com.fw.common.sso.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RefreshScope
public class AuthInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private Map<RequestMatcher, ConfigAttributePriority> source = new HashMap<>();

    private FilterInvocationSecurityMetadataSource metadataSource;

    public void setMetadataSource(FilterInvocationSecurityMetadataSource metadataSource) {
        this.metadataSource = metadataSource;
    }

    @Autowired
    public AuthInvocationSecurityMetadataSource(RequestMapProperties properties) {
        for (Map.Entry<String, Collection<String>> entry : properties.getRequestMaps().entrySet()) {
            String key = entry.getKey();
            int priority = 0;
            if(entry.getKey().indexOf(":") > 0) {
                String[] keysplit = entry.getKey().split(":");
                key = keysplit[0];
                priority = Integer.parseInt(keysplit[1]);
            }
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(key, null);
            Collection<ConfigAttribute> collection = new ArrayList<>();
            for (String configAttr : entry.getValue()) {
                collection.addAll(SecurityConfig.createList(configAttr));
            }
            source.put(matcher, new ConfigAttributePriority(priority, collection));
        }
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        List<ConfigAttributePriority> prioritySortedList = new LinkedList<>();
        for (Map.Entry<RequestMatcher, ConfigAttributePriority> entry : source.entrySet()) {
            if (entry.getKey().matches(fi.getRequest())) {
                if(entry.getValue().getPriority() == 0)
                    return entry.getValue().getConfigAttributeCollection();
                else
                    prioritySortedList.add(entry.getValue());
            }
        }

        if(prioritySortedList.size() > 0) {
            Collections.sort(prioritySortedList);
            return prioritySortedList.get(0).getConfigAttributeCollection();
        }

        if(metadataSource != null) {
            return metadataSource.getAttributes(object);
        }
        //没有匹配到,默认是要登录才能访问
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<RequestMatcher, ConfigAttributePriority> entry : source.entrySet()) {
            allAttributes.addAll(entry.getValue().getConfigAttributeCollection());
        }

        if(metadataSource != null) {
            allAttributes.addAll(metadataSource.getAllConfigAttributes());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /*@Configuration
    @RefreshScope
    @ConfigurationProperties(prefix = "fw.security")
    public static class RequestMapProperties {
        public static Map<String, Collection<String>> requestMaps = new HashMap<>();

        //public static Map<RegexRequestMatcher, Collection<SecurityConfig>> requestMaps1 = new HashMap<>();
    }*/

}
