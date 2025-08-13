package com.fenglei.service.quartz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.quartz.entity.SysJob;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 *
 * @author ruoyi
 */
public interface ISysJobService extends IService<SysJob> {

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return 结果
     */
    SysJob add(SysJob job) throws Exception;

    /**
     * 删除
     */
    Boolean myRemoveById(String id) throws Exception;

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids) throws Exception;

    /**
     * 更新任务
     *
     * @param job 调度信息
     * @return 结果
     */
    SysJob myUpdate(SysJob job) throws Exception;

    /**
     * 获取quartz调度器的计划任务
     *
     * @param job 调度信息
     * @return 调度任务集合
     */
    IPage<SysJob> myPage(Page page, SysJob job);

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param id 调度任务ID
     * @return 调度任务对象信息
     */
    SysJob detail(String id);

    /**
     * 获取quartz调度器的计划任务
     *
     * @param job 调度信息
     * @return 调度任务集合
     */
    List<SysJob> selectJobList(SysJob job);

    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 结果
     */
    SysJob pauseJob(SysJob job) throws Exception;

    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 结果
     */
    SysJob resumeJob(SysJob job) throws Exception;

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     * @return 结果
     */
    SysJob changeStatus(SysJob job) throws Exception;

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 结果
     */
    boolean run(SysJob job) throws Exception;

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    boolean checkCronExpressionIsValid(String cronExpression);

    /**
     * 添加定时调度任务
     *
     * @param jobName       任务名称
     * @param jobGroup      任务组名
     * @param invokeTarget  调用目标字符串
     * @param misfirePolicy 计划策略 0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行
     * @param concurrent    并发执行 0=允许,1=禁止"
     * @param status        任务状态 0=正常,1=暂停
     * @param endTime       截止时间
     * @return SysJob
     * @throws Exception
     */
    SysJob add(String jobName, String jobGroup, String invokeTarget, String misfirePolicy, String concurrent, String status, LocalDateTime endTime) throws Exception;
}
