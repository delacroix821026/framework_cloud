package com.fw.common.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${BK.AUTH_SERVICE_ID:auth}${BK.ENVNAME:}")
public interface ITokenService {
    @RequestMapping(method = RequestMethod.POST, value = "/oauth/removetoken")
    void removeToken(@RequestParam("username") String username);
}
