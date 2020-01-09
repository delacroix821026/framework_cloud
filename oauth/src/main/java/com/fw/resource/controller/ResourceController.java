package com.fw.resource.controller;

import com.fw.common.model.LoginUserInfo;
import com.fw.common.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cendaijuan
 */
@RestController
@Slf4j
public class ResourceController {
    @Autowired
    private IUserInfoService service;

    /*@RequestMapping(value = "/me", method = RequestMethod.GET)
    public LoginUserInfo getUser(Principal principal) {
        return service.loadUserByUsername(principal.getName());
    }*/

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public Map<String, Object> getTest(Principal principal) {
        LoginUserInfo userInfo = service.loadUserByUsername(principal.getName());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("username", principal.getName());
        map.put("authorities", userInfo.getAuthorities());
        return map;
    }
}