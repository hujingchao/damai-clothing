package com.fenglei.admin.controller.system;


import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.entity.RoleCustomFormColumn;
import com.fenglei.service.workFlow.RoleCustomFormColumnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(value = "角色自定义表单接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@AllArgsConstructor
@Slf4j
public class RoleCustomFormColumnController {

    @Resource
    private RoleCustomFormColumnService roleCustomFormColumnService;

    @ApiOperation(value = "保存角色自定义表单列权限", notes = "保存角色自定义表单列权限", produces = "application/json")
    @PostMapping("/roleCustomFormColumn")
    public Result saveRoleCustomFormColumn(@RequestBody RoleCustomFormColumn roleCustomFormColumn) throws Exception {
        if (roleCustomFormColumnService.saveRoleCustomFormColumn(roleCustomFormColumn)) {
            return Result.success("保存成功");
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "修改角色自定义表单列权限", notes = "修改角色自定义表单列权限", produces = "application/json")
    @PutMapping("/roleCustomFormColumn")
    public Result editRoleCustomFormColumn(@RequestBody RoleCustomFormColumn roleCustomFormColumn) throws Exception {
        if (roleCustomFormColumnService.editRoleCustomFormColumn(roleCustomFormColumn)) {
            return Result.success("修改成功");
        }
        return Result.failed("修改失败");
    }

    @ApiOperation(value = "删除角色自定义表单列权限", notes = "删除角色自定义表单列权限", produces = "application/json")
    @DeleteMapping("/roleCustomFormColumn/{id}")
    public Result deleteRoleCustomFormColumn(@PathVariable String id) throws Exception {
        if (roleCustomFormColumnService.deleteRoleCustomFormColumn(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "通过角色ID获取角色自定义表单列权限", notes = "通过角色ID获取角色自定义表单列权限", produces = "application/json")
    @GetMapping("/roleCustomFormColumn/{roleId}/{customFormId}")
    public Result getRoleCustomFormColumnsByRoleId(@PathVariable String roleId, @PathVariable String customFormId) {
        return Result.success(roleCustomFormColumnService.getRoleCustomFormColumnsByRoleId(roleId, customFormId));
    }

    @ApiOperation(value = "通过角色ID获取角色自定义表单列权限", notes = "通过角色ID获取角色自定义表单列权限", produces = "application/json")
    @GetMapping("/roleCustomFormColumn/columnIds/{roleIds}/{customFormId}/{companyId}")
    public Result getRoleCustomFormColumnIdsByRoleIds(@PathVariable String roleIds, @PathVariable String customFormId, @PathVariable String companyId) {
        return Result.success(roleCustomFormColumnService.getRoleCustomFormColumnIdsByRoleIds(roleIds, customFormId, companyId));
    }

}
