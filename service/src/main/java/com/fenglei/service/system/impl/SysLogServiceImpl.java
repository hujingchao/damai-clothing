package com.fenglei.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.system.SysLogMapper;
import com.fenglei.model.system.entity.SysLog;
import com.fenglei.service.system.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 日志 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-05-05
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysLog add(SysLog sysLog) {
        this.save(sysLog);
        return sysLog;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        return this.removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysLog myUpdate(SysLog sysLog) {
        this.updateById(sysLog);
        return sysLog;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<SysLog> myPage(Page page, SysLog sysLog) {
        return this.page(page, new LambdaQueryWrapper<SysLog>()
                .eq(StringUtils.isNotEmpty(sysLog.getCreatorId()), SysLog::getCreatorId, sysLog.getCreatorId())
                .like(StringUtils.isNotEmpty(sysLog.getCreator()), SysLog::getCreator, sysLog.getCreator())
                .like(StringUtils.isNotEmpty(sysLog.getMethodName()), SysLog::getMethodName, sysLog.getMethodName())
                .like(StringUtils.isNotEmpty(sysLog.getRemark()), SysLog::getRemark, sysLog.getRemark())
                .apply(StringUtils.isNotEmpty(sysLog.getBeginDate()), "date_format(create_time,'%Y-%m-%d %H:%i:%S') >= {0}", sysLog.getBeginDate())
                .apply(StringUtils.isNotEmpty(sysLog.getEndDate()), "date_format(create_time,'%Y-%m-%d %H:%i:%S') <= {0}", sysLog.getEndDate())
                .orderByDesc(SysLog::getId)
        );
    }

    /**
     * 详情
     */
    @Override
    public SysLog detail(String id) {
        return this.getById(id);
    }
}
