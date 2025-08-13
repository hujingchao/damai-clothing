package com.fenglei.admin.controller.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.constant.GlobalConstants;
import com.fenglei.common.enums.QueryModeEnum;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.system.dto.RolePermissionDTO;
import com.fenglei.model.system.entity.SysPermission;
import com.fenglei.model.system.entity.SysRole;
import com.fenglei.service.system.ISysPermissionService;
import com.fenglei.service.system.ISysRoleMenuService;
import com.fenglei.service.system.ISysRolePermissionService;
import com.fenglei.service.system.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/api.admin/v1/roles")
@Slf4j
@AllArgsConstructor
public class RoleController {

    private ISysRoleService iSysRoleService;

    private ISysRoleMenuService iSysRoleMenuService;

    private ISysRolePermissionService iSysRolePermissionService;

    private ISysPermissionService iSysPermissionService;


    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryMode", paramType = "query", dataType = "QueryModeEnum"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "name", value = "角色名称", paramType = "query", dataType = "String"),
    })
    @GetMapping
    public Result list(
            String queryMode,
            Integer page,
            Integer limit,
            String name) {
        QueryModeEnum queryModeEnum = QueryModeEnum.getValue(queryMode);
        switch (queryModeEnum) {
            case PAGE:
                LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                        .like(StrUtil.isNotBlank(name), SysRole::getName, name)
                        .orderByAsc(SysRole::getSort)
                        .orderByDesc(SysRole::getGmtModified)
                        .orderByDesc(SysRole::getGmtCreate);
                Page<SysRole> result = iSysRoleService.page(new Page<>(page, limit), queryWrapper);
                return Result.success(result.getRecords(), result.getTotal());
            case LIST:
                List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getStatus, GlobalConstants.STATUS_NORMAL_VALUE));
                return Result.success(list);
            default:
                return Result.failed(ResultCode.QUERY_MODE_IS_NULL);

        }
    }


    @ApiOperation(value = "新增角色")
    @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    @PostMapping
    public Result add(@RequestBody SysRole role) {
        List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>().eq(StringUtils.isNotEmpty(role.getName()), SysRole::getName, role.getName()));
        if(StringUtils.isNotEmpty(list)&& list.size()>0){
            throw new BizException("角色名称重复！");
        }
        boolean result = iSysRoleService.save(role);
        if (result) {
            iSysPermissionService.refreshPermissionRolesCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PutMapping(value = "/{id}")
    public Result update(
            @PathVariable Long id,
            @RequestBody SysRole role) {
        List<SysRole> list = iSysRoleService.list(new LambdaQueryWrapper<SysRole>().eq(StringUtils.isNotEmpty(role.getName()), SysRole::getName, role.getName()));
        if(StringUtils.isNotEmpty(list)&& list.size()>0){
            throw new BizException("角色名称重复！");
        }
        boolean result = iSysRoleService.updateById(role);
        if (result) {
            iSysPermissionService.refreshPermissionRolesCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParam(name = "ids", value = "以,分割拼接字符串", required = true, dataType = "String")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable String ids) {
        boolean result = iSysRoleService.delete(Arrays.asList(ids.split(",")).stream()
                .map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        if (result) {
            iSysPermissionService.refreshPermissionRolesCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "批量授所有路由权限")
    @ApiImplicitParam(name = "ids", value = "以,分割拼接字符串", required = true, dataType = "String")
    @PostMapping("/batchUpdatePermission/{ids}")
    public Result batchUpdatePermission(@PathVariable String ids) throws LoginException {
        String[] split = ids.split(",");
        List<SysPermission> permissions = iSysPermissionService.list();
        List<String> permissionIds = permissions.stream().map(SysPermission::getId).collect(Collectors.toList());

        for (int i = 0; i < split.length; i++) {
            RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
            rolePermissionDTO.setRoleId(split[i]);
            rolePermissionDTO.setPermissionIds(permissionIds);

            boolean result = iSysRolePermissionService.update(rolePermissionDTO);
            if (!result){
                throw new LoginException("授权失败！");
            }
        }
        iSysPermissionService.refreshPermissionRolesCache();
        return Result.success();
    }

    @ApiOperation(value = "局部更新角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PatchMapping(value = "/{id}")
    public Result patch(@PathVariable Long id, @RequestBody SysRole role) {
        LambdaUpdateWrapper<SysRole> updateWrapper = new LambdaUpdateWrapper<SysRole>()
                .eq(SysRole::getId, id)
                .set(role.getStatus() != null, SysRole::getStatus, role.getStatus());
        boolean result = iSysRoleService.update(updateWrapper);
        if (result) {
            iSysPermissionService.refreshPermissionRolesCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "角色拥有的菜单ID集合")
    @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}/menu_ids")
    public Result roleMenuIds(@PathVariable("id") String roleId) {
        List<String> menuIds = iSysRoleMenuService.listMenuIds(roleId);
        return Result.success(menuIds);
    }

    @ApiOperation(value = "角色拥有的权限ID集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "type", value = "权限类型", paramType = "query", dataType = "Integer"),
    })
    @GetMapping("/{id}/permission_ids")
    public Result rolePermissionIds(@PathVariable("id") String roleId, @RequestParam Integer type) {
        List<String> permissionIds = iSysRolePermissionService.listPermissionIds(roleId, type);
        return Result.success(permissionIds);
    }


    @ApiOperation(value = "修改角色菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "role", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysRole")
    })
    @PutMapping(value = "/{id}/menu_ids")
    public Result updateRoleMenuIds(
            @PathVariable("id") String roleId,
            @RequestBody SysRole role) {

        List<String> menuIds = role.getMenuIds();
        boolean result = iSysRoleMenuService.update(roleId, menuIds);
        return Result.judge(result);
    }

    @ApiOperation(value = "修改角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "rolePermission", value = "实体JSON对象", required = true, paramType = "body", dataType = "RolePermissionDTO")
    })
    @PutMapping(value = "/{id}/permission_ids")
    public Result updateRolePermissionIds(
            @PathVariable("id") String roleId,
            @RequestBody RolePermissionDTO rolePermission) {
        rolePermission.setRoleId(roleId);
        boolean result = iSysRolePermissionService.update(rolePermission);
        if (result) {
            iSysPermissionService.refreshPermissionRolesCache();
        }
        return Result.judge(result);
    }

    @ApiOperation(value = "根据ids查询")
    @PostMapping("/listByIds")
    public Result listByIds(@RequestBody Collection<? extends Serializable> ids) {
        List<SysRole> sysRoleList = iSysRoleService.listByIds(ids);
        return Result.success(sysRoleList);
    }

    @ApiOperation(value = "查询所有")
    @GetMapping("/list")
    public Result list() {
        return Result.success(iSysRoleService.list());
    }

    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.success(iSysRoleService.getById(id));
    }
}
