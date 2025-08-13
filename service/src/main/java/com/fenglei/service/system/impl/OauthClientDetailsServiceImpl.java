package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.system.OauthClientDetailsMapper;
import com.fenglei.model.system.entity.OauthClientDetails;
import com.fenglei.service.system.IOauthClientDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author qj
 * @date 2020-11-06
 */
@Service
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements IOauthClientDetailsService {
}
