package com.fw.common.sso.extractor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class AuthoritiesExtractor implements GrantedAuthoritiesMapper {
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(
            Collection<? extends GrantedAuthority> map) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        /*if(map.containsKey("roles"))
            for (String role : (ArrayList<String>) map.get("roles")) {
                Assert.isTrue(!role.startsWith("ROLE_"), role
                        + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        if(map.containsKey("promissions"))
            for (String promission : (ArrayList<String>) map.get("promissions")) {
                authorities.add(new SimpleGrantedAuthority(promission));
            }
        authorities.add(new SimpleGrantedAuthority("ROLE_ACTUATOR"));
        authorities.add(new SimpleGrantedAuthority("ACTUATOR"));*/
        return authorities;
    }


    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        return null;
    }
}
