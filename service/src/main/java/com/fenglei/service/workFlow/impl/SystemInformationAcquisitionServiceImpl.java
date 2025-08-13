package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.system.entity.SysDept;
import com.fenglei.model.system.entity.SysRole;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.service.system.ISysDeptService;
import com.fenglei.service.system.ISysRoleService;
import com.fenglei.service.system.ISysUserRoleService;
import com.fenglei.service.system.ISysUserService;
import com.fenglei.service.workFlow.SystemInformationAcquisitionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
@Service
public class SystemInformationAcquisitionServiceImpl implements SystemInformationAcquisitionService {

    @Resource
    private ISysUserService userService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysDeptService deptService;
    @Resource
    private ISysUserRoleService userRoleService;

    /**
     * 获取流程部门
     * @return List<FlowDepartment>
     */
    @Override
    public List<FlowDepartment> getFlowDepartments(String name) {
        List<SysDept> departments = deptService.list(new QueryWrapper<SysDept>().lambda()
                .eq(SysDept::getStatus, true)
                .like(StringUtils.isNotEmpty(name), SysDept::getName, name)
        );
        List<FlowDepartment> flowDepartments = new ArrayList<>();
        FlowDepartment flowDepartment;
        for (SysDept department : departments) {
            flowDepartment = new FlowDepartment();
            flowDepartment.setId(department.getId());
            flowDepartment.setDepartmentName(department.getName());
            flowDepartment.setParentId(department.getParentId().toString());
            flowDepartment.setManagerId(department.getManagerId());
            flowDepartment.setSortIndex(department.getSort());
            flowDepartments.add(flowDepartment);
        }
//        FlowDepartmentUtil.parseDepartmentTree()
        return flowDepartments;
    }

    /**
     * 获取流程用户
     * @return List<FlowUser>
     */
    @Override
    public List<FlowUser> getFlowUsers(String name) {
        List<SysUser> users = userService.list(new QueryWrapper<SysUser>().lambda()
                .like(StringUtils.isNotEmpty(name), SysUser::getUsername, name)
        );
        Set<String> userIds = users.stream().map(SysUser::getId).collect(Collectors.toSet());
        List<SysUserRole> userRoles = userRoleService.list(new QueryWrapper<SysUserRole>().lambda().in(SysUserRole::getUserId, userIds));
        List<SysDept> departments = deptService.list();
        List<FlowUser> flowUsers = new ArrayList<>();
        FlowUser flowUser;
        for (SysUser user : users) {
            flowUser = new FlowUser();
            flowUser.setId(user.getId());
            flowUser.setEmployeeName(user.getNickname());
            for (SysDept department : departments) {
                if (department.getId().equals(user.getDeptId())) {
                    flowUser.setDepartmentName(department.getName());
                    flowUser.setDepartmentNames(department.getName());
                }
            }
            flowUser.setIsAuditManager(user.getIsAuditManager());
            List<String> roleIds = userRoles.stream().filter(s -> s.getUserId().equals(user.getId())).map(SysUserRole::getRoleId).collect(Collectors.toList());
            List<String> roleIdStrs = new ArrayList<>();
            for (String roleId : roleIds) {
                roleIdStrs.add(roleId.toString());
            }
            flowUser.setRoleIds(roleIdStrs);
            flowUsers.add(flowUser);
        }
        return flowUsers;
    }

    /**
     * 获取流程角色
     * @return List<FlowRole>
     */
    @Override
    public List<FlowRole> getFlowRoles(String name) {
        List<SysRole> roles = roleService.list(new QueryWrapper<SysRole>().lambda()
                .like(StringUtils.isNotEmpty(name), SysRole::getName, name)
        );
        List<FlowRole> flowRoles = new ArrayList<>();
        FlowRole flowRole;
        for (SysRole role : roles) {
            flowRole = new FlowRole();
            flowRole.setRoleName(role.getName());
            flowRole.setRoleId(role.getId());
            flowRoles.add(flowRole);
        }
        return flowRoles;
    }

    @Override
    public Boolean getCheckBySuperior(String companyId) {
        return null;
    }

    @Override
    public Boolean getEnableDataPermissions(String companyId) {
        return null;
    }

    @Override
    public List<String> getDataReadableUserIds(String userId, String companyId) {
        return null;
    }

}
