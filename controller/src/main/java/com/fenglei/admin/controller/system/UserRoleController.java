package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fenglei.common.result.Result;
import com.fenglei.model.system.dto.AuthorizeDTO;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.service.system.ISysUserRoleService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author sxl
 * @name UserRoleController
 */
@Api(tags = "用户角色接口")
@RestController
@RequestMapping("/api.admin/v1/userRoles")
@Slf4j
@AllArgsConstructor
public class UserRoleController {

    @Resource
    private ISysUserRoleService userRoleService;

    @PostMapping("/listByRoleIds")
    public Result listByRoleIds(@RequestBody Collection<? extends Serializable> roleIds) {
        List<SysUserRole> sysUserRoles = userRoleService.list(new QueryWrapper<SysUserRole>().lambda()
                .in(SysUserRole::getRoleId, roleIds));
        return Result.success(sysUserRoles);
    }

    @PostMapping("/authorize/{userId}")
    public Result authorize(@PathVariable("userId") String userId, @RequestBody AuthorizeDTO authorizeDTO) {
        return userRoleService.authorize(userId, authorizeDTO);
    }

    @GetMapping("/listByUserId/{userId}")
    public Result listByUserId(@PathVariable("userId") String userId) {
        return userRoleService.listByUserId(userId);
    }
}
