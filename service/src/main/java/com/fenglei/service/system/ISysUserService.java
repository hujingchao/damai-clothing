package com.fenglei.service.system;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.model.system.vo.AppUserVo;

import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/2 13:37
 * @Version 1.0
 */
public interface ISysUserService extends IService<SysUser> {
    List<SysUserRole> getRoles(String userId);

    IPage<SysUser> list(Page<SysUser> page, SysUser sysUser);

    boolean saveUser(SysUser user);

    boolean updateUser(SysUser user);

    Boolean uptWwUserInfo(SysUser user) throws Exception;

    boolean myRemoveByIds(List<String> idList);

    List<SysUser> myList(SysUser user);

    IPage<SysUser> myPage(Page page, SysUser user);

    Set<AppUserVo> appUserList(SysUser sysUser);

    boolean editPwd(SysUser user);
}

