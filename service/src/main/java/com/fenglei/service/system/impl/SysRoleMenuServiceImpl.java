package com.fenglei.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.system.SysRoleMenuMapper;
import com.fenglei.model.system.entity.SysRoleMenu;
import com.fenglei.service.system.ISysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    public List<String> listMenuIds(String roleId) {
        return this.baseMapper.listMenuIds(roleId);
    }

    @Override
    public boolean update(String roleId, List<String> menuIds) {
        boolean result = true;
        List<String> dbMenuIds = this.list(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId))
                .stream().map(item -> item.getMenuId()).collect(Collectors.toList());

        // 删除数据库存在此次提交不存在的
        if (CollectionUtil.isNotEmpty(dbMenuIds)) {
            List<String> removeMenuIds = dbMenuIds.stream().filter(id -> !menuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(removeMenuIds)) {
                this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId)
                        .in(SysRoleMenu::getMenuId, removeMenuIds));
            }
        }

        // 插入数据库不存在的
        if (CollectionUtil.isNotEmpty(menuIds)) {
            List<String> insertMenuIds = menuIds.stream().filter(id -> !dbMenuIds.contains(id)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(insertMenuIds)) {
                List<SysRoleMenu> roleMenus = new ArrayList<>();
                for (String insertMenuId : insertMenuIds) {
                    SysRoleMenu roleMenu = new SysRoleMenu().setRoleId(roleId).setMenuId(insertMenuId);
                    roleMenus.add(roleMenu);
                }
                result = this.saveBatch(roleMenus);
            }
        }
        return result;
    }
}
