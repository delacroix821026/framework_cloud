package com.fw.common.config;

import com.fw.common.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextHolder {
    @Bean("springContext")
    public SpringContextHolder addSpringContextHolder() {
        return new SpringContextHolder();
    }
}
