package com.fenglei.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysRoleMenu;

import java.util.List;

public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    List<String> listMenuIds(String roleId);

    /**
     * 修改角色菜单
     * @param roleId
     * @param menuIds
     * @return
     */
    boolean update(String roleId, List<String> menuIds);
}
