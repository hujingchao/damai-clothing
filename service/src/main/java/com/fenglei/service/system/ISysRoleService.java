package com.fenglei.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysRole;

import java.util.List;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/3 12:42
 * @Version 1.0
 */
public interface ISysRoleService extends IService<SysRole> {
    boolean delete(List<Long> ids);

    List<String> getUserIdsByRoleName(String roleName);
}
