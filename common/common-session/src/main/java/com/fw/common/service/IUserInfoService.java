package com.fw.common.service;

import com.fw.common.model.LoginUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${FW.USER_SERVICE_ID:user-server}${FW.ENVNAME:}")
//@FeignClient(value = "", name = "IUserInfoService", url = "http://finchley.baketechfin.com:3110")
public interface IUserInfoService {
    @RequestMapping(method = RequestMethod.POST, value = "/auth/loadUserByUsername")
    LoginUserInfo loadUserByUsername(@RequestParam("username") String username);

    @RequestMapping(method = RequestMethod.POST, value = "/auth/loadUserByOpenId")
    LoginUserInfo loadUserByOpenId(@RequestParam("openId") String openId);
}
