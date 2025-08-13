package com.fenglei.security.pojo;

import cn.hutool.core.collection.CollectionUtil;
import com.fenglei.model.system.dto.UserDTO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class User implements UserDetails {
    private String id;

    private String username;

    private String nickname;

    private String password;

    private Boolean enabled;

    private String clientId;

    private String openId;

    private Collection<SimpleGrantedAuthority> authorities;

    public User(UserDTO user) {
        this.setId(user.getId());
        this.setUsername(user.getMobile());
        this.setNickname(user.getUsername());
//        this.setPassword(AuthConstants.BCRYPT + user.getPassword());
        this.setPassword(user.getPassword());
        this.setEnabled(Integer.valueOf(1).equals(user.getStatus()));
        if (CollectionUtil.isNotEmpty(user.getRoleIds())) {
            authorities = new ArrayList<>();
            user.getRoleIds().forEach(roleId -> authorities.add(new SimpleGrantedAuthority(String.valueOf(roleId))));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return this.enabled;
    }
}

