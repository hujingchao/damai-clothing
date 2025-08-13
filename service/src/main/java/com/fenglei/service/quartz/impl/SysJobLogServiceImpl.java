package com.fenglei.service.quartz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.quartz.SysJobLogMapper;
import com.fenglei.model.quartz.entity.SysJobLog;
import com.fenglei.service.quartz.ISysJobLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJobLog add(SysJobLog jobLog) {
        if (!this.save(jobLog)) {
            throw new BizException("任务日志添加失败");
        }

        return jobLog;
    }

    /**
     * 更新
     *
     * @param sysJobLog 调度日志信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJobLog myUpdate(SysJobLog sysJobLog) {
        if (!this.updateById(sysJobLog)) {
            throw new BizException("任务日志更新失败");
        }

        return sysJobLog;
    }

    /**
     * 删除任务日志
     *
     * @param id 调度日志ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        if (!this.removeById(id)) {
            throw new BizException("删除失败");
        }

        return true;
    }

    /**
     * 批量删除调度日志信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        if (!this.removeByIds(ids)) {
            throw new BizException("删除失败");
        }

        return true;
    }

    @Override
    public IPage<SysJobLog> myPage(Page page, SysJobLog jobLog) {
        IPage<SysJobLog> iPage = baseMapper.getPage(page, jobLog);
        return iPage;
    }

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> list(SysJobLog jobLog) {
        List<SysJobLog> list = baseMapper.getList(jobLog);
        return list;
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param id 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog detail(String id) {
        SysJobLog sysJobLog = baseMapper.infoById(id);
        return sysJobLog;
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        baseMapper.cleanJobLog();
    }
}
