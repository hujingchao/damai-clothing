package com.fenglei.service.system.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.system.SysWechatAuthMapper;
import com.fenglei.model.system.entity.SysWechatAuth;
import com.fenglei.service.system.ISysWechatAuthService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysWechatAuthServiceImpl extends ServiceImpl<SysWechatAuthMapper, SysWechatAuth> implements ISysWechatAuthService {
    @Autowired
    private WxMaService wxMaService;


    @Override
    public SysWechatAuth getWechatUserInfo(String code, String encryptedData, String iv) {

        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openid = sessionInfo.getOpenid();
        String sessionKey = sessionInfo.getSessionKey();
        SysWechatAuth one = this.getOne(new LambdaQueryWrapper<SysWechatAuth>().eq(SysWechatAuth::getOpenid, openid));
        if (null == one) {
            one = new SysWechatAuth();
        }
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        if (!"男".equals(userInfo.getGender()) && !"女".equals(userInfo.getGender())) {
            one.setGender(0);
        } else {
            one.setGender("男".equals(userInfo.getGender()) ? 1 : 2);
        }
        one.setNickname(userInfo.getNickName());
        one.setAvatar(userInfo.getAvatarUrl());
        one.setOpenid(openid);
        one.setCity(userInfo.getCity());
        one.setCountry(userInfo.getCountry());
        one.setLanguage(userInfo.getLanguage());
        one.setProvince(userInfo.getProvince());
        one.setSessionKey(sessionKey);
        saveOrUpdate(one);
        return one;
    }
}
