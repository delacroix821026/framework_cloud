package com.newland.financial.p2p;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newland.financial.p2p.filter.JsonEncodeFilter;
import com.newland.financial.p2p.filter.SessionFilter;
import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.ohuyo.libra.client.filter.SlaveClientFilter;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 360, redisFlushMode = RedisFlushMode.IMMEDIATE)
@ImportResource("classpath*:beans.xml")
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    /**
     * 绑定Cxt
     * @return ServletListenerRegistrationBean
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new RequestContextListener());
        return servletListenerRegistrationBean;
    }

    @Bean
    public CORSFilter addCORSFilter() throws ServletException {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.init(new FilterConfig() {
            private Map<String, String> parameters = new HashMap<String, String>();

            public String getFilterName() {
                return "CORS";
            }

            public ServletContext getServletContext() {
                return null;
            }

            public String getInitParameter(String name) {
                return parameters.get(name);
            }

            public Enumeration<String> getInitParameterNames() {
                parameters.put("cors.allowGenericHttpRequests", "true");
                parameters.put("cors.allowOrigin", "*");
                parameters.put("cors.allowSubdomains", "false");
                parameters.put("cors.supportedMethods", "GET, HEAD, POST, OPTIONS, PUT, DELETE");
                parameters.put("cors.supportedHeaders", "Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
                parameters.put("cors.exposedHeaders", "X-Test-1, X-Test-2");
                parameters.put("cors.supportsCredentials", "true");
                parameters.put("cors.maxAge", "3600");


                return Collections.enumeration(parameters.keySet());
            }
        });

        return corsFilter;
    }

    @Bean
    public JsonEncodeFilter getJsonEncodeFilter() {
        return new JsonEncodeFilter();
    }

    /*@Bean(name = "sessionFilter")
    public SlaveClientFilter addSlaveClientFilter() {
        SlaveClientFilter slaveClientFilter = new SlaveClientFilter();
        return slaveClientFilter;
    }

    @Bean
    public FilterRegistrationBean addSlaveClientFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(addSlaveClientFilter());
        registration.addUrlPatterns("/p2p/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("sessionFilter");
        return registration;
    }*/

    @Bean
    public SessionFilter addSessionFilter() {
        SessionFilter sessionFilter = new SessionFilter();
        return sessionFilter;
    }

    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    public RedisTemplate addRedisTemplate(RedisConnectionFactory connectionFactory, RedisSerializer fastJson2JsonRedisSerializer) {
        RedisTemplate redisTemplate = new RedisTemplate();
        //StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
