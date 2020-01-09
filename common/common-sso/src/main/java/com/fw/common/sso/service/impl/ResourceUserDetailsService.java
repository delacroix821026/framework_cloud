package com.fw.common.sso.service.impl;

import com.fw.common.service.IUserInfoService;
import com.fw.common.sso.service.IResourceUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ResourceUserDetailsService implements IResourceUserDetailService {
    @Autowired
    private IUserInfoService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return service.loadUserByUsername(username);
    }

}
