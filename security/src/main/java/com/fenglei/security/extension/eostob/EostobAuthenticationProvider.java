package com.fenglei.security.extension.eostob;

import com.fenglei.security.pojo.CustomerContactUserDetail;
import com.fenglei.security.service.UserDetailsServiceImpl;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

/**
 * 客户联系人登录认证提供者
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/9/25
 */
@Data
public class EostobAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    /**
     * 微信认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EostobAuthenticationToken authenticationToken = (EostobAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();


        CustomerContactUserDetail customerContact = ((UserDetailsServiceImpl) userDetailsService).loadCustomerContactByPhone(username);
        if (customerContact == null) {
            throw new UsernameNotFoundException("账号不存在");
        }
        boolean matches = passwordEncoder.matches(password, customerContact.getMpPassword());
        if (!matches) {
            throw new BadCredentialsException("用户名密码错误！");
        }
        customerContact.setNickname(customerContact.getContactName());
        EostobAuthenticationToken result = new EostobAuthenticationToken(customerContact, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return EostobAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
