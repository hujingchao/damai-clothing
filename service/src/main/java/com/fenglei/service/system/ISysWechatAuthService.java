package com.fenglei.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysWechatAuth;

public interface ISysWechatAuthService extends IService<SysWechatAuth> {
    SysWechatAuth getWechatUserInfo(String code, String encryptedData, String iv);
}
