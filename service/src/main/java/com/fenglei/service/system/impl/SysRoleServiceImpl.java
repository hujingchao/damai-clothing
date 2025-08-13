package com.fenglei.service.system.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.mapper.system.SysRoleMapper;
import com.fenglei.model.system.entity.SysRole;
import com.fenglei.model.system.entity.SysRoleMenu;
import com.fenglei.model.system.entity.SysRolePermission;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.service.system.ISysRoleMenuService;
import com.fenglei.service.system.ISysRolePermissionService;
import com.fenglei.service.system.ISysRoleService;
import com.fenglei.service.system.ISysUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/3 12:42
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    private ISysRoleMenuService iSysRoleMenuService;
    private ISysUserRoleService iSysUserRoleService;
    private ISysRolePermissionService iSysRolePermissionService;


    @Override
    public boolean delete(List<Long> ids) {
        Optional.ofNullable(ids).orElse(new ArrayList<>()).forEach(id -> {
            int count = iSysUserRoleService.count(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, id));
            if (count > 0) {
                throw new BizException("该角色已分配用户，无法删除");
            }
            iSysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
            iSysRolePermissionService.remove(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId,id));
        });
        return this.removeByIds(ids);
    }

    @Override
    public List<String> getUserIdsByRoleName(String roleName) {
        SysRole sysRole = getOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getName, roleName));
        if(ObjectUtil.isNotNull(sysRole)){
            List<SysUserRole> sysUserRoles = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getRoleId, sysRole.getId())
            );
            if(sysUserRoles.size()>0){
                List<String> userIds = sysUserRoles.stream().map(SysUserRole::getUserId).collect(Collectors.toList());
                return userIds;
            }else{
                throw new BizException("未找到该角色绑定的用户，请联系管理员");
            }
        }else{
            throw new BizException("未找到该角色，请联系管理员");
        }
    }
}
