package com.fenglei.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.dto.RolePermissionDTO;
import com.fenglei.model.system.entity.SysRolePermission;

import java.util.List;

public interface ISysRolePermissionService extends IService<SysRolePermission> {

    List<String> listPermissionIds(String moduleId, String roleId, Integer type);
    List<String> listPermissionIds(String roleId, Integer type);
    boolean update(RolePermissionDTO rolePermission);


}
