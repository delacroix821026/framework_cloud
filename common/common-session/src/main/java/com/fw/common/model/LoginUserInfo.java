package com.fw.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@ToString(callSuper = true)
public class LoginUserInfo extends UserInfo implements UserDetails, Serializable {
    private static final long serialVersionUID = -1;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            Assert.isTrue(!role.startsWith("ROLE_"), role
                    + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        for (String promission : promissions) {
            authorities.add(new SimpleGrantedAuthority(promission));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_ACTUATOR"));
        authorities.add(new SimpleGrantedAuthority("ACTUATOR"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return new Byte((byte) 0).equals(this.getStatus());
    }

}
