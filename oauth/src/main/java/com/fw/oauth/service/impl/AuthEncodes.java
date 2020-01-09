package com.fw.oauth.service.impl;

import com.fw.common.utils.Encodes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthEncodes implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return Encodes.entryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Encodes.validatePassword(rawPassword.toString(), encodedPassword);
    }
}
