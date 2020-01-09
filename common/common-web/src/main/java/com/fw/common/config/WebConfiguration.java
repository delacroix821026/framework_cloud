package com.fw.common.config;

import com.fw.common.formatter.DateFormatAnnotationFormatterFactory;
import com.fw.common.interceptor.UserInfoHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;

@Configuration
//@EnableWebMvc
@Slf4j
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new UserInfoHandlerInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new Converter<Long, Date>() {

            @Override
            public Date convert(Long s) {
                if (s != null)
                    return new Date(s);
                return null;
            }
        });

        registry.addConverter(new Converter<String, Date>() {

            @Override
            public Date convert(String s) {
                if (s != null) {
                    try {
                        return new Date(Long.valueOf(s));
                    } catch (Exception e) {
                        log.error("error:", e);
                    }
                }
                return null;
            }
        });
        registry.addFormatterForFieldAnnotation(new DateFormatAnnotationFormatterFactory());
    }

    //因ObjectMapping冲突，替代boot自动加载到busObjectMapping
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                //.allowedHeaders("header1", "header2", "header3")
                //.exposedHeaders("header1", "header2")
                //.allowCredentials(false)
                .maxAge(3600);
    }


}
