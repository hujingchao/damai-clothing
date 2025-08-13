package com.fenglei.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.admin.controller.system.handler.UserBlockHandler;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.system.dto.EnWxUserDTO;
import com.fenglei.model.system.dto.UserDTO;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.entity.SysRole;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.model.system.enums.PermTypeEnum;
import com.fenglei.model.system.vo.AppUserVo;
import com.fenglei.model.system.vo.UserVO;
import com.fenglei.service.system.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/api.admin/v1/users")
@Slf4j
public class UserController {

    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private ISysUserRoleService iSysUserRoleService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysRoleService roleService;

    @Resource
    private ISysPermissionService iSysPermissionService;
    @Resource
    private ISysMenuService iSysMenuService;

    @ApiOperation(value = "列表分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "limit", value = "每页数量", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "nickname", value = "用户昵称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query", dataType = "Long"),
    })
    @GetMapping
    public Result list(
            Integer page,
            Integer limit,
            String nickname,
            String mobile,
            Integer status) {

        SysUser user = new SysUser();
        user.setNickname(nickname);
        user.setMobile(mobile);
        user.setStatus(status);

        IPage<SysUser> result = iSysUserService.list(new Page<>(page, limit), user);
        return Result.success(result.getRecords(), result.getTotal());
    }

    @ApiOperation(value = "用户详情")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(
            @PathVariable String id
    ) {
        SysUser user = iSysUserService.getById(id);
        if (user != null) {
            List<String> roleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                    .eq(SysUserRole::getUserId, user.getId())
                    .select(SysUserRole::getRoleId)
            ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            user.setRoleIds(roleIds);
        }
        return Result.success(user);
    }

    @ApiOperation(value = "新增用户")
    @ApiImplicitParam(name = "user", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysUser")
    @PostMapping
    public Result add(@RequestBody SysUser user) {
        boolean result = iSysUserService.saveUser(user);
        return Result.judge(result);
    }

    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysUser")
    })
    @PutMapping(value = "/{id}")
    public Result update(
            @PathVariable Integer id,
            @RequestBody SysUser user) {
        boolean result = iSysUserService.updateUser(user);
        return Result.judge(result);
    }

    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(name = "ids", value = "id集合", required = true, paramType = "query", dataType = "String")
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable String ids) {
        boolean status = iSysUserService.removeByIds(Arrays.asList(ids.split(",")).stream().map(id -> Long.parseLong(id)).collect(Collectors.toList()));
        return Result.judge(status);
    }

    @ApiOperation(value = "局部更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "user", value = "实体JSON对象", required = true, paramType = "body", dataType = "SysUser")
    })
    @PatchMapping(value = "/{id}")
    public Result patch(@PathVariable Long id, @RequestBody SysUser user) {
        String encodePassword = null;
        if (StringUtils.isNotEmpty(user.getPassword())) {
            encodePassword = passwordEncoder.encode(user.getPassword());
        }
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>().eq(SysUser::getId, id);
        updateWrapper.set(user.getStatus() != null, SysUser::getStatus, user.getStatus());
        updateWrapper.set(user.getPassword() != null, SysUser::getPassword, encodePassword);
        boolean status = iSysUserService.update(updateWrapper);
        return Result.judge(status);
    }


    /**
     * 提供用于用户登录认证需要的用户信息
     *
     * @param username
     * @return
     */
    @ApiOperation(value = "根据用户名获取用户信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "path", dataType = "String")
    @GetMapping("/username/{username}")
    public Result getUserByUsername(@PathVariable String username) {
        SysUser user = iSysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));

        // 用户不存在，返回自定义异常，让调用端处理后续逻辑
        if (user == null) {
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }

        // Entity->DTO
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);

        // 获取用户的角色ID集合
        List<String> roleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, user.getId())
        ).stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        userDTO.setRoleIds(roleIds);

        return Result.success(userDTO);
    }


    @ApiOperation(value = "获取当前登陆的用户信息")
    @SentinelResource(value = "getCurrentUser",
            blockHandlerClass = UserBlockHandler.class, blockHandler = "handleGetCurrentUserBlock"
    )
    @GetMapping("/me")
    public Result<UserVO> getCurrentUser() {
        log.info("获取当前登陆的用户信息 begin");

        UserVO userVO = new UserVO();

        // 用户基本信息
        String userId = RequestUtils.getUserId();
        SysUser user = iSysUserService.getById(userId);
        BeanUtil.copyProperties(user, userVO);

        // 用户角色信息
        List<String> roleIds = RequestUtils.getRoleIds();
        userVO.setRoles(roleIds);

        if (!roleIds.isEmpty()) {
            // 用户角色信息
            List<String> roleNames = roleService.list(new LambdaQueryWrapper<SysRole>()
                    .in(SysRole::getId, roleIds)
                    .select(SysRole::getName)
            ).stream().map(SysRole::getName).collect(Collectors.toList());
            userVO.setRoleNames(String.join(" | ", roleNames));
        }

        // 用户按钮权限信息
        List<String> perms = iSysPermissionService.listPermsByRoleIds(roleIds, PermTypeEnum.BUTTON.getValue());
        userVO.setPerms(perms);

        return Result.success(userVO);
    }


    @ApiOperation(value = "获取当前登陆的用户菜单")
    @SentinelResource(value = "authMenu",
            blockHandlerClass = UserBlockHandler.class, blockHandler = "handleGetCurrentUserBlock"
    )
    @GetMapping("/authMenu")
    public Result<List<SysMenu>> authMenu() {

        // 用户基本信息
        String userId = RequestUtils.getUserId().toString();


        // 用户按钮权限信息
        List<SysMenu> menus = iSysMenuService.listMenuByUserId(userId);

        return Result.success(menus);
    }

    @GetMapping("/getAll")
    public Result getAll() {
        List<SysUser> sysUsers = iSysUserService.list();
        return Result.success(sysUsers);
    }

    @GetMapping("/getAllUsers")
    public Result getAllUsers() {
        List<SysUser> sysUsers = iSysUserService.list(new LambdaQueryWrapper<SysUser>());
        return Result.success(sysUsers);
    }

    @PostMapping("/getListByRegistrationPhoneNumbers")
    public Result getListByRegistrationPhoneNumbers(@RequestBody Collection<? extends Serializable> callingNumbers) {
        List<SysUser> userList = iSysUserService.list(new QueryWrapper<SysUser>().lambda()
                .in(SysUser::getRegistrationPhoneNumber, callingNumbers));
        return Result.success(userList);
    }

    @PostMapping("/listByIds")
    public Result listByIds(@RequestBody Collection<? extends Serializable> ids) {
        List<SysUser> userList = iSysUserService.list(new QueryWrapper<SysUser>().lambda()
                .in(SysUser::getId, ids));
        return Result.success(userList);
    }

    @PostMapping("/listByWwUserids")
    public Result listByWwUserids(@RequestBody Collection<? extends Serializable> wwUserids) {
        List<SysUser> userList = iSysUserService.list(new QueryWrapper<SysUser>().lambda()
                .in(SysUser::getWwUserid, wwUserids));
        return Result.success(userList);
    }

    @GetMapping("/findByWwUserid")
    public Result findByWwUserid(@RequestParam("wwUserid") String wwUserid) {
        SysUser user = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getWwUserid, wwUserid));
        // Entity->DTO
        EnWxUserDTO userDTO = new EnWxUserDTO();
        BeanUtil.copyProperties(user, userDTO);
        // 获取用户的角色ID集合
        List<String> roleIds = iSysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, user.getId())
        ).stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        userDTO.setRoleIds(roleIds);
        return Result.success(userDTO);
    }

    @GetMapping("/getById/{id}")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path", dataType = "Long")
    public Result getById(@PathVariable Long id) {
        return Result.success(iSysUserService.getById(id));
    }

    @GetMapping("/list")
    public Result list() {
        return Result.success(iSysUserService.list());
    }

    @PutMapping("/update")
    public Result update(@RequestBody SysUser user) throws Exception {
        if (iSysUserService.updateById(user)) {
            return Result.success();
        }
        return Result.failed("修改失败");
    }

    @GetMapping("/getByPhone")
    public Result getByPhone(@RequestParam("phone") String phone) {
        SysUser user = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getMobile, phone));
        return Result.success(user);
    }

    @PutMapping("/uptWwUserInfo")
    public Result uptWwUserInfo(@RequestBody SysUser user) throws Exception {
        if (iSysUserService.uptWwUserInfo(user)) {
            return Result.success();
        }
        return Result.failed("修改失败");
    }

    @PutMapping("/updateBatchById")
    public Result updateBatchById(@RequestBody Collection<SysUser> userList) throws Exception {
        if (iSysUserService.updateBatchById(userList)) {
            return Result.success();
        }
        return Result.failed("修改失败");
    }

    @GetMapping("/getByRegistrationPhoneNumber")
    public Result getByRegistrationPhoneNumber(@RequestParam("fromNumber") String fromNumber) {
        List<SysUser> users = iSysUserService.list(new QueryWrapper<SysUser>().lambda().eq(SysUser::getRegistrationPhoneNumber, fromNumber));
        if (users != null && users.size() > 0) {
            SysUser u = users.get(0);
            return Result.success(u);
        }
        return Result.success(null);
    }

    @ApiOperation(value = "用户列表", notes = "用户列表")
    @GetMapping("/myList")
    public Result myList(SysUser sysUser) throws Exception {
        return Result.success(iSysUserService.myList(sysUser));
    }

    @ApiOperation(value = "用户列表", notes = "用户列表")
    @GetMapping("/myPage")
    public Result myPage(Page page, SysUser sysUser) throws Exception {
        return Result.success(iSysUserService.myPage(page, sysUser));
    }

    @ApiOperation(value = "app用户列表", notes = "app用户列表")
    @GetMapping("/appUserList")
    public Result<Set<AppUserVo>> appUserList(SysUser sysUser) throws Exception {
        return Result.success(iSysUserService.appUserList(sysUser));
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PutMapping("/editPwd")
    public Result<Boolean> editPwd(@RequestBody SysUser user) throws Exception {
        return Result.judge(iSysUserService.editPwd(user));
    }
}
