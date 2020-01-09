package com.fw.oauth.service;

import com.fw.common.service.CacheService;
import com.fw.common.service.IUserInfoService;
import com.fw.oauth.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserInfoService implements UserDetailsService {
    @Value("${fw.login.lock}")
    private int LOCK = 6;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private CacheService cacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        log.info("loadUserByUsername:" + username);
        if (username == null || username.equals(""))
            throw new UsernameNotFoundException("username can't be null or empty");
        UserDetails userDetails = userInfoService.loadUserByUsername(username);
        if (userDetails == null)
            throw new UserNotFoundException("用户名或密码错误！", username);
        return userDetails;
    }

    public boolean isLocking(String username) {
        if (!cacheService.exists(username)) {
            return false;
        }
        return (Integer) cacheService.get(username) >= LOCK;
    }
}
