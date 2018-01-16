package com.newland.financial.p2p.common.converter;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j
@Configuration
public class DateConverter {
    /*@InitBinder
    protected void initBinder(WebDataBinder binder) {
        log.info("DateConverter already be binded.");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
    }


    @Bean
    public ConfigurableWebBindingInitializer setRequestMappingHandlerAdapter(ConfigurableWebBindingInitializer bean) { bean.
    }*/
}