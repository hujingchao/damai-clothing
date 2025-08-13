package com.fenglei.security.config;

import com.fenglei.security.handle.SimpleAccessDeniedHandler;
import com.fenglei.security.handle.SimpleAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 〈资源认证服务器〉
 *
 * @author Curise
 * @create 2018/12/13
 * @since 1.0.0
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter implements WebMvcConfigurer {
    @Resource
    private SimpleAccessDeniedHandler simpleAccessDeniedHandler;
    @Resource
    private SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint;
    @Resource
    private OAuth2WebSecurityExpressionHandler expressionHandler;
    @Resource
    private ServiceExpirationInterceptor serviceExpirationInterceptor;

    /**
     * 无需认证，放行路由
     */
    private static final String[] AUTH_WHITELIST = {
            "/**/v2/api-docs/**",
            "/**/swagger-resources/**",
            "/webjars/**",
            "/doc.html",
            "/swagger-ui.html",
            "/oauth/token",
            "/websocket/**",
            "/common/**",
            "/api.admin/v1/dataSource/**",
            "/api.admin/v1/saleInvoice/notify",
            "/websocketAdmin/**",
            "/websocketCustomer/**",
            "/websocketWechat/**",
            "/oauth/getWechatUserInfo",
            "/api.app/v1/payment/callbackEncrypt"
    };

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                //白名单直接放行
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                //所有接口都要进行权限认证
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(simpleAccessDeniedHandler)
                .authenticationEntryPoint(simpleAuthenticationEntryPoint)
                .and()
                .httpBasic();

    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler = new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    /**
     * 自定义异常
     *
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(simpleAuthenticationEntryPoint)
                .accessDeniedHandler(simpleAccessDeniedHandler);
        //解决权限校验表达式报错问题
        resources.expressionHandler(expressionHandler);
    }

    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serviceExpirationInterceptor).addPathPatterns("/**")
                .excludePathPatterns(AUTH_WHITELIST);
    }
}
