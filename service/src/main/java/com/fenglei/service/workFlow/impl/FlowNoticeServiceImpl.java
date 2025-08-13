package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.system.SysUserRoleMapper;
import com.fenglei.mapper.workFlow.FlowNoticeMapper;
import com.fenglei.model.system.entity.SysUserRole;
import com.fenglei.model.workFlow.entity.FlowNotice;
import com.fenglei.service.workFlow.FlowNoticeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yzy
 */
@Service
public class FlowNoticeServiceImpl extends ServiceImpl<FlowNoticeMapper, FlowNotice> implements FlowNoticeService {
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<FlowNotice> getUserNotice(FlowNotice flowNotice) {
        String userId = RequestUtils.getUserId();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        List<FlowNotice> flowNotices = new ArrayList<>();
        List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            for (String roleId : roleIds) {
                List<FlowNotice> flowNoticeList = list(new LambdaQueryWrapper<FlowNotice>()
                        .and(wrapper -> wrapper
                                .like(StringUtils.isNotEmpty(flowNotice.getContent()), FlowNotice::getContent, flowNotice.getContent())
                                .eq(Objects.nonNull(flowNotice.getIsRead()), FlowNotice::getIsRead, flowNotice.getIsRead())
                                .apply("FIND_IN_SET(" + roleId + ", role_ids) > 0"))
                        .or(wrapper -> wrapper
                                .like(StringUtils.isNotEmpty(flowNotice.getContent()), FlowNotice::getContent, flowNotice.getContent())
                                .eq(Objects.nonNull(flowNotice.getIsRead()), FlowNotice::getIsRead, flowNotice.getIsRead())
                                .apply("FIND_IN_SET(" + userId + ", user_ids) > 0"))
                        .orderByDesc(FlowNotice::getCreateTime)
                );
                if (flowNoticeList.size() > 0) {
                    flowNotices.addAll(flowNoticeList);
                }
            }
        }
        Set<FlowNotice> flowNoticeSet = new HashSet<>(flowNotices);
        flowNotices.clear();
        flowNotices.addAll(flowNoticeSet);
        return flowNotices;
    }

    @Override
    public Integer getNewInfoCount() {
        String userId = RequestUtils.getUserId();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        Integer count = 0;
        List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            for (String roleId : roleIds) {
                Integer newSize = count(new LambdaQueryWrapper<FlowNotice>()
                        .eq(FlowNotice::getIsRead, false)
                        .apply("FIND_IN_SET(" + roleId + ", role_ids) > 0")
                );
                count += newSize;
            }
            Integer newSize = count(new LambdaQueryWrapper<FlowNotice>()
                    .eq(FlowNotice::getIsRead, false)
                    .apply("FIND_IN_SET(" + userId + ", user_ids) > 0")
            );
            count += newSize;
        }
        return count;
    }

    @Override
    public Result setAllIsRead() {
        String userId = RequestUtils.getUserId();
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );
        List<String> roleIds = sysUserRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (roleIds.size() > 0) {
            FlowNotice flowNotice = new FlowNotice();
            flowNotice.setIsRead(true);
            for (String roleId : roleIds) {
                update(flowNotice, new LambdaQueryWrapper<FlowNotice>()
                        .eq(FlowNotice::getIsRead, false)
                        .apply("FIND_IN_SET(" + roleId + ", role_ids) > 0")
                );
            }
            update(flowNotice, new LambdaQueryWrapper<FlowNotice>()
                    .eq(FlowNotice::getIsRead, false)
                    .apply("FIND_IN_SET(" + userId + ", user_ids) > 0")
            );
        }
        return Result.success();
    }
}
