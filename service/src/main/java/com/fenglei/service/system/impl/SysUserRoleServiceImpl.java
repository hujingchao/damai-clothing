package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.mapper.system.SysUserMapper;
import com.fenglei.mapper.system.SysUserRoleMapper;
import com.fenglei.model.system.dto.AuthorizeDTO;
import com.fenglei.model.system.dto.AuthorizeItemDTO;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.service.system.ISysUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 用户角色实现层
 * @Author zhouyiqiu
 * @Date 2021/12/3 12:55
 * @Version 1.0
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {
    SysUserRoleMapper sysUserRoleMapper;
    SysUserMapper sysUserMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result authorize(String userId, AuthorizeDTO authorizeDTO) {
        sysUserRoleMapper.deleteByUserId(userId);
        List<AuthorizeItemDTO> orgAuths = authorizeDTO.getOrgAuths();
        List<SysUserRole> data = new ArrayList<>();
        for (AuthorizeItemDTO orgAuth : orgAuths) {
            List<String> roles = orgAuth.getRoles();
            for (String role : roles) {
                SysUserRole temp = new SysUserRole();
                temp.setUserId(userId);
                temp.setRoleId(role);
                data.add(temp);
            }
        }
        if (!this.saveBatch(data)) {
            throw new BizException("授权失败！");
        }
        return Result.success();
    }

    @Override
    public Result listByUserId(String userId) {
        SysUser byId = sysUserMapper.selectById(userId);
        List<SysUserRole> list = this.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        AuthorizeDTO authorizeDTO = new AuthorizeDTO();
        List<AuthorizeItemDTO> orgAuths = new ArrayList<>();
        authorizeDTO.setOrgAuths(orgAuths);
        return Result.success(authorizeDTO);
    }
}
