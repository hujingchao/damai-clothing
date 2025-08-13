package com.fenglei.admin.controller.system;

import com.fenglei.common.result.Result;
import com.fenglei.model.workFlow.entity.RoleTableInfoItem;
import com.fenglei.service.workFlow.RoleTableInfoItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(value = "角色表单列权限接口")
@RestController
@RequestMapping("/api.admin/v1/flowable")
@AllArgsConstructor
@Slf4j
public class RoleTableInfoItemController {

    @Resource
    private RoleTableInfoItemService roleTableInfoItemService;

    @ApiOperation(value = "保存角色表单子项映射", notes = "保存角色表单子项映射", produces = "application/json")
    @PostMapping("/roleTableInfoItem")
    public Result saveRoleTableInfoItem(@RequestBody RoleTableInfoItem roleTableInfoItem) throws Exception {
        if (roleTableInfoItemService.saveRoleTableInfoItem(roleTableInfoItem)) {
            return Result.success("保存成功", null);
        }
        return Result.failed("保存失败");
    }

    @ApiOperation(value = "修改角色表单子项映射", notes = "修改角色表单子项映射", produces = "application/json")
    @PutMapping("/roleTableInfoItem")
    public Result editRoleTableInfoItem(@RequestBody RoleTableInfoItem roleTableInfoItem) throws Exception {
        if (roleTableInfoItemService.editRoleTableInfoItem(roleTableInfoItem)) {
            return Result.success("修改成功");
        }
        return Result.failed("修改失败");
    }

    @ApiOperation(value = "删除角色表单子项映射", notes = "删除角色表单子项映射", produces = "application/json")
    @DeleteMapping("/roleTableInfoItem/{id}")
    public Result deleteRoleTableInfoItem(@PathVariable String id) throws Exception {
        if (roleTableInfoItemService.deleteRoleTableInfoItem(id)) {
            return Result.success("删除成功");
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "通过角色ID获取角色表单子项映射", notes = "通过角色ID获取角色表单子项映射", produces = "application/json")
    @GetMapping("/roleTableInfoItem/{roleIds}/{tableId}")
    public Result getRoleTableInfoItemsByRoleId(@PathVariable String roleIds, @PathVariable String tableId) {
        return Result.success(roleTableInfoItemService.getRoleTableInfoItemsByRoleId(roleIds, tableId));
    }

}
