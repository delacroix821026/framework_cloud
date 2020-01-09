package com.fw.resource.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author cendaijuan
 */
@RestController
@RequestMapping("/test")
public class ResourceController {

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Map<String, String> getUser(Principal principal1) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", principal1.getName());
        return map;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void getTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        System.out.println("Authorization: " + authorization);
    }

    @RequestMapping(value = "/test1", method = RequestMethod.GET)
    public void getTest(@RequestHeader("userName") String userName, @RequestHeader("clientId") String clientId, String chinese) {
        System.out.println("ClientId: " + clientId);
        System.out.println("UserName: " + userName);
        System.out.println("chinese: " + chinese);
    }
}