package com.fenglei.security.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fenglei.security.pojo.CustomerContactUserDetail;
import com.fenglei.security.pojo.WechatAuthDTO;
import com.fenglei.security.pojo.WechatUser;
import com.fenglei.security.service.UserDetailsServiceImpl;
import lombok.Data;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * 微信认证提供者
 *
 * @author <a href="mailto:xianrui0365@163.com">haoxr</a>
 * @date 2021/9/25
 */
@Data
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private WxMaService wxMaService;

    /**
     * 微信认证
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken authenticationToken = (WechatAuthenticationToken) authentication;
        String code = (String) authenticationToken.getPrincipal();
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openid = sessionInfo.getOpenid();

        CustomerContactUserDetail customerContact = ((UserDetailsServiceImpl)userDetailsService).loadCustomerContactByOpenId(openid);
        // 微信用户不存在，注册成为新会员
        if (customerContact == null) {
            String sessionKey = sessionInfo.getSessionKey();
            String encryptedData = authenticationToken.getEncryptedData();
            String iv = authenticationToken.getIv();
            // 解密 encryptedData 获取用户信息 前台有问题，没有重新获取这些信息
//            WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

            CustomerContactUserDetail contactByPhone = ((UserDetailsServiceImpl) userDetailsService).loadCustomerContactByPhone(phoneNoInfo.getPhoneNumber());

            WechatAuthDTO wechatInfoByOpenId = ((UserDetailsServiceImpl) userDetailsService).getWechatInfoByOpenId(openid);
            if (null == contactByPhone){
                customerContact = new CustomerContactUserDetail();
                String id = IdWorker.getIdStr();
                customerContact.setContactName(wechatInfoByOpenId.getNickname());
                customerContact.setId(id);
                customerContact.setMpUsername(wechatInfoByOpenId.getNickname());
                customerContact.setMpAvatar(wechatInfoByOpenId.getAvatar());
                customerContact.setMpOpenid(openid);
                customerContact.setType(2);
                customerContact.setCreateUser(wechatInfoByOpenId.getNickname());
                customerContact.setUpdateUser(wechatInfoByOpenId.getNickname());
                customerContact.setPhoneNumber(phoneNoInfo.getPhoneNumber());
                ((UserDetailsServiceImpl)userDetailsService).addCustomerContact(customerContact);
            }else {
                customerContact =contactByPhone;
                customerContact.setMpUsername(wechatInfoByOpenId.getNickname());
                customerContact.setMpAvatar(wechatInfoByOpenId.getAvatar());
                customerContact.setMpOpenid(openid);
                customerContact.setPhoneNumber(phoneNoInfo.getPhoneNumber());
                ((UserDetailsServiceImpl)userDetailsService).updateCustomerContact(customerContact);
            }

        }
        WechatUser wechatUser = new WechatUser(customerContact);
        WechatAuthenticationToken result = new WechatAuthenticationToken(wechatUser, new HashSet<>());
        result.setDetails(authentication.getDetails());
        return result;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
