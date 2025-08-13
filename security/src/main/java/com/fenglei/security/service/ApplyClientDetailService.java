package com.fenglei.security.service;

import com.fenglei.common.constant.AuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

public class ApplyClientDetailService implements ClientDetailsService {

    @Autowired
    private DataSource dataSource;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
//        JdbcClientDetailsServiceImpl jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
//        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
//        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
//        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);

        JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(dataSource);
        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
        return clientDetails;
    }
}
