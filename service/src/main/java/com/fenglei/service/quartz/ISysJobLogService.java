package com.fenglei.service.quartz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.quartz.entity.SysJobLog;

import java.util.List;

/**
 * 定时任务调度日志信息信息 服务层
 *
 * @author ruoyi
 */
public interface ISysJobLogService extends IService<SysJobLog> {

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    SysJobLog add(SysJobLog jobLog);

    /**
     * 删除任务日志
     *
     * @param id 调度日志ID
     * @return 结果
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除调度日志信息
     *
     * @param ids 需要删除的日志ID
     * @return 结果
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新任务日志
     *
     * @param sysJobLog 调度日志信息
     */
    SysJobLog myUpdate(SysJobLog sysJobLog);

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    IPage<SysJobLog> myPage(Page page, SysJobLog jobLog);
    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    List<SysJobLog> list(SysJobLog jobLog);

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param id 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    SysJobLog detail(String id);

    /**
     * 清空任务日志
     */
    void cleanJobLog();
}
