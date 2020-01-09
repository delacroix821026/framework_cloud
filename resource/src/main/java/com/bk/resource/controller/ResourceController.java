package com.fw.resource.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static javax.swing.text.html.CSS.getAttribute;

/**
 * @author cendaijuan
 */
@RestController
public class ResourceController {

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Map<String, String> getUser(Principal principal1) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal1.getName());
        map.put("clientId", authentication.getOAuth2Request().getClientId());
        return map;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void getTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @RequestHeader("username") String userName) {
        System.out.println("Authorization" + authorization);
        System.out.println("UserName" + userName);
    }
}