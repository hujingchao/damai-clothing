package com.fenglei.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysPermission;

import java.util.List;

public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> listPermissionRoles();

    IPage<SysPermission> list(Page<SysPermission> page, SysPermission permission);

    boolean refreshPermissionRolesCache();


    List<String> listPermsByRoleIds(List<String> roleIds, Integer type);

}
