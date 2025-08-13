package com.fenglei.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.system.dto.AuthorizeDTO;
import com.fenglei.model.system.entity.SysUserRole;

/**
 * @Description 用户角色service
 * @Author zhouyiqiu
 * @Date 2021/12/3 12:55
 * @Version 1.0
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    Result authorize(String userId, AuthorizeDTO authorizeDTO);

    Result listByUserId(String userId);
}
