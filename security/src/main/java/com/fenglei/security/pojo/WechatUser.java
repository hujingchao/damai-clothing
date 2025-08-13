package com.fenglei.security.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class WechatUser implements UserDetails {
    private String id;

    private String username;

    private String nickname;

    private String mpUsername;

    private String mpAvatar;

    private String mpOpenid;

    private String phoneNumber;

    private String customerId;

    private String password;

    private Boolean enabled;

    private String clientId;

    private String openId;

    private Collection<SimpleGrantedAuthority> authorities;

    public WechatUser(CustomerContactUserDetail user) {
        this.setId(user.getId());
        this.setUsername(user.getMpOpenid());
        this.setNickname(user.getContactName());
        this.setEnabled(true);
        this.setCustomerId(user.getCustomerId());
        this.setOpenId(user.getMpOpenid());
        this.setMpUsername(user.getMpUsername());
        this.setMpAvatar(user.getMpAvatar());
        this.setMpOpenid(user.getMpOpenid());
        this.setPhoneNumber(user.getPhoneNumber());
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
