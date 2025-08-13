package com.fenglei.service.system.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.GlobalConstants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.ChineseUtil;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.system.SysUserMapper;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.model.system.vo.AppUserVo;
import com.fenglei.service.system.ISysUserRoleService;
import com.fenglei.service.system.ISysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/2 13:42
 * @Version 1.0
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    ISysUserRoleService userRoleService;
    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public List<SysUserRole> getRoles(String userId) {
        List<SysUserRole> list = userRoleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
                .select(SysUserRole::getRoleId)
        );
        return list;
    }

    @Override
    public IPage<SysUser> list(Page<SysUser> page, SysUser user) {
        List<SysUser> list = this.baseMapper.list(page, user);
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean saveUser(SysUser user) {
        List<SysUser> list = this.list(new LambdaQueryWrapper<SysUser>().eq(SysUser::getMobile, user.getMobile()));
        if (list.size() > 0) {
            throw new BizException("手机号不能重复！");
        }

        if (StringUtils.isNotEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode(GlobalConstants.DEFAULT_USER_PASSWORD));
        }
        boolean result = this.save(user);
        if (result) {
            List<String> roleIds = user.getRoleIds();
            if (CollectionUtil.isNotEmpty(roleIds)) {
                List<SysUserRole> userRoleList = new ArrayList<>();
                roleIds.forEach(roleId -> userRoleList.add(new SysUserRole().setUserId(user.getId()).setRoleId(roleId)));
                result = userRoleService.saveBatch(userRoleList);
            }
        }
        return result;
    }

    @Override
    public boolean updateUser(SysUser user) {

        List<String> dbRoleIds = userRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId())).stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        List<String> roleIds = user.getRoleIds();
        List<String> addRoleIds = null;
        List<String> removeRoleIds = null;
        if (roleIds != null) {
            addRoleIds = roleIds.stream().filter(roleId -> !dbRoleIds.contains(roleId)).collect(Collectors.toList());
            removeRoleIds = dbRoleIds.stream().filter(roleId -> !roleIds.contains(roleId)).collect(Collectors.toList());
        }
        if (CollectionUtil.isNotEmpty(addRoleIds)) {
            List<SysUserRole> addUserRoleList = new ArrayList<>();
            addRoleIds.forEach(roleId -> {
                addUserRoleList.add(new SysUserRole().setUserId(user.getId()).setRoleId(roleId));
            });
            userRoleService.saveBatch(addUserRoleList);
        }

        if (CollectionUtil.isNotEmpty(removeRoleIds)) {
            removeRoleIds.forEach(roleId -> {
                userRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()).eq(SysUserRole::getRoleId, roleId));
            });
        }

        boolean result = this.updateById(user);
        return result;
    }

    @Override
    public Boolean uptWwUserInfo(SysUser user) throws Exception {
        SysUser sysUser = getById(user.getId());
        if (StringUtils.isNotEmpty(user.getWwUserid())) {
            sysUser.setWwUserid(user.getWwUserid());
        }
        if (StringUtils.isNotEmpty(user.getWwAvatar())) {
            sysUser.setWwAvatar(user.getWwAvatar());
        }
        if (StringUtils.isNotEmpty(user.getWwThumbAvatar())) {
            sysUser.setWwThumbAvatar(user.getWwThumbAvatar());
        }
        if (StringUtils.isNotEmpty(user.getWwPassword())) {
            sysUser.setWwPassword(user.getWwPassword());
        }
        return saveOrUpdate(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean myRemoveByIds(List<String> idList) {
        userRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .in(SysUserRole::getUserId, idList)
        );
        this.removeByIds(idList);
        return true;
    }

    @Override
    public List<SysUser> myList(SysUser sysUser) {
        List<SysUser> list = this.list(
                new QueryWrapper<SysUser>().lambda()
                        .notIn(sysUser.getExcludeIds() != null && sysUser.getExcludeIds().size() > 0, SysUser::getId, sysUser.getExcludeIds())
                        .like(StringUtils.isNotEmpty(sysUser.getUsername()), SysUser::getUsername, sysUser.getUsername())
                        .like(StringUtils.isNotEmpty(sysUser.getNickname()), SysUser::getNickname, sysUser.getNickname())
                        .like(StringUtils.isNotEmpty(sysUser.getMobile()), SysUser::getMobile, sysUser.getMobile())
                        .eq(SysUser::getDeleted, 0)
                        .orderByAsc(SysUser::getId)
        );
        return list;
    }

    @Override
    public IPage<SysUser> myPage(Page page, SysUser sysUser) {
        IPage<SysUser> iPage = this.page(page,
                new LambdaQueryWrapper<SysUser>()
                        .notIn(sysUser.getExcludeIds() != null && sysUser.getExcludeIds().size() > 0, SysUser::getId, sysUser.getExcludeIds())
                        .like(StringUtils.isNotEmpty(sysUser.getUsername()), SysUser::getUsername, sysUser.getUsername())
                        .like(StringUtils.isNotEmpty(sysUser.getNickname()), SysUser::getNickname, sysUser.getNickname())
                        .like(StringUtils.isNotEmpty(sysUser.getMobile()), SysUser::getMobile, sysUser.getMobile())
                        .eq(SysUser::getDeleted, 0)
                        .orderByAsc(SysUser::getId)
        );

        return iPage;
    }

    @Override
    public Set<AppUserVo> appUserList(SysUser sysUser) {
        List<AppUserVo> result = new ArrayList<>();
        List<SysUser> users = this.list(Wrappers.lambdaQuery(SysUser.class)
                .nested(StringUtils.isNotEmpty(sysUser.getNameOrPhone()),
                        i -> i.like(SysUser::getNickname, sysUser.getNameOrPhone())
                                .or()
                                .like(SysUser::getUsername, sysUser.getNameOrPhone())
                                .or()
                                .like(SysUser::getPhone, sysUser.getNameOrPhone())
                )
        );
        if (users.isEmpty()) {
            return new HashSet<>(result);
        }
        List<String> nicknames = users.stream().map(SysUser::getNickname).collect(Collectors.toList());
        Map<String, String> map = new HashMap<>();
        List<String> firstLetters = new ArrayList<>();
        for (String nickname : nicknames) {
            String substring = nickname.substring(0, 1);
            if (map.containsValue(substring)) {
                continue;
            }
            String firstLetter = ChineseUtil.getFirstLetter(substring);
            if (StringUtils.isEmpty(firstLetter)) {
                firstLetter = substring;
            }
            map.put(firstLetter, substring);
            firstLetters.add(firstLetter);
        }
        for (String firstLetter : firstLetters) {
            String s = map.get(firstLetter);
            List<SysUser> collect = users.stream().filter(t -> t.getNickname().startsWith(s)).collect(Collectors.toList());
            AppUserVo appUserVo = new AppUserVo();
            appUserVo.setFirstPinYin(firstLetter);
            appUserVo.setUsers(collect);
            result.add(appUserVo);
        }
        Set<AppUserVo> userVos = result.stream().sorted((s1, s2) -> s1.getFirstPinYin().compareToIgnoreCase(s2.getFirstPinYin())).collect(Collectors.toCollection(LinkedHashSet::new));
        return userVos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editPwd(SysUser user) {
        String newPwd = user.getPassword();
        String oldPwd = user.getOldPwd();
        if (newPwd.equals(oldPwd)) {
            throw new BizException("新旧密码不能一致！");
        }
        SysUser byId = this.getById(user.getId());
        if (!passwordEncoder.matches(oldPwd, byId.getPassword())) {
            throw new BizException("原密码不正确");
        }
        byId.setPassword(passwordEncoder.encode(newPwd));
        return this.updateById(byId);
    }
}