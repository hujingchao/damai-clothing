package com.fenglei.service.quartz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.constant.ScheduleConstants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.quartz.SysJobMapper;
import com.fenglei.model.quartz.entity.SysJob;
import com.fenglei.quartz.util.CronUtils;
import com.fenglei.quartz.util.ScheduleUtils;
import com.fenglei.service.quartz.ISysJobService;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {
    @Resource
    private Scheduler scheduler;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 新增任务
     *
     * @param job 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob add(SysJob job) throws Exception {
        if (!CronUtils.isValid(job.getCronExpression())) {
            throw new BizException("新增任务'" + job.getName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'ldap(s)'调用");
//        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
//            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不在白名单内");
        }

        try {
            job.setCreatorId(RequestUtils.getUserId());
            job.setCreator(RequestUtils.getNickname());
            job.setCreateTime(sdf.format(new Date()));
        } catch (Exception ex) {

        }

        if (!this.save(job)) {
            throw new BizException("任务添加失败");
        }
        ScheduleUtils.createScheduleJob(scheduler, job);

        return job;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) throws Exception {
        SysJob old = this.getById(id);
        if (old != null) {
            if (!this.removeById(id)) {
                throw new BizException("删除失败");
            }

            JobKey jobKey = ScheduleUtils.getJobKey(old.getId(), old.getJobGroup());
            if (jobKey != null && scheduler.checkExists(jobKey)) {
                if (!scheduler.deleteJob(jobKey)) {
                    throw new BizException("定时任务删除失败");
                }
            }
        }

        return true;
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) throws Exception {
        List<SysJob> list = this.list(
                new LambdaQueryWrapper<SysJob>()
                        .in(SysJob::getId, ids)
                        .orderByDesc(SysJob::getId)
        );
        if (list != null && list.size() > 0) {
            if (!this.removeByIds(ids)) {
                throw new BizException("删除失败");
            }

            for (SysJob sysJob : list) {
                JobKey jobKey = ScheduleUtils.getJobKey(sysJob.getId(), sysJob.getJobGroup());
                if (jobKey != null && scheduler.checkExists(jobKey)) {
                    if (!scheduler.deleteJob(jobKey)) {
                        throw new BizException("定时任务删除失败");
                    }
                }
            }
        }

        return true;
    }

    /**
     * 更新任务的时间表达式
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob myUpdate(SysJob job) throws Exception {
        if (!CronUtils.isValid(job.getCronExpression())) {
            throw new BizException("修改任务'" + job.getName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            throw new BizException("修改任务'" + job.getName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            throw new BizException("修改任务'" + job.getName() + "'失败，目标字符串不允许'ldap(s)'调用");
//        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
//            return Result.failed("修改任务'" + job.getName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            throw new BizException("修改任务'" + job.getName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            throw new BizException("修改任务'" + job.getName() + "'失败，目标字符串不在白名单内");
        }

        SysJob properties = this.detail(job.getId());

        if (!this.updateById(job)) {
            throw new BizException("任务更新失败");
        } else {
            updateSchedulerJob(job, properties.getJobGroup());
        }

        return job;
    }

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws Exception {
        scheduler.clear();
        List<SysJob> jobList = this.list();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public IPage<SysJob> myPage(Page page, SysJob job) {
        IPage<SysJob> iPage = baseMapper.getPage(page, job);
        return iPage;
    }

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param id 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public SysJob detail(String id) {
        SysJob sysJob = baseMapper.infoById(id);
        return sysJob;
    }

    /**
     * 获取quartz调度器的计划任务列表
     *
     * @param job 调度信息
     * @return
     */
    @Override
    public List<SysJob> selectJobList(SysJob job) {
        List<SysJob> list = baseMapper.getList(job);
        return list;
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob pauseJob(SysJob job) throws Exception {
        String id = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        if (!this.updateById(job)) {
            throw new BizException("暂停定时任务失败");
        }
        scheduler.pauseJob(ScheduleUtils.getJobKey(id, jobGroup));

        return job;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob resumeJob(SysJob job) throws Exception {
        String id = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        if (!this.updateById(job)) {
            throw new BizException("启动定时任务失败");
        }
        scheduler.resumeJob(ScheduleUtils.getJobKey(id, jobGroup));

        return job;
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob changeStatus(SysJob job) throws Exception {
        SysJob newJob = this.detail(job.getId());
        newJob.setStatus(job.getStatus());

        String status = newJob.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            resumeJob(newJob);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            pauseJob(newJob);
        }
        return newJob;
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob job) throws Exception {
        boolean result = false;
        String id = job.getId();
        String jobGroup = job.getJobGroup();
//        SysJob properties = this.detail(job.getId());
        List<SysJob> list = this.list(
                new LambdaQueryWrapper<SysJob>()
                        .eq(SysJob::getBillType, "backupSetting")
                        .eq(SysJob::getBillId, id)
        );
        if (list != null && list.size() > 0) {
            for (SysJob sysJob : list) {
                // 参数
                JobDataMap dataMap = new JobDataMap();
                dataMap.put(ScheduleConstants.TASK_PROPERTIES, sysJob);
                JobKey jobKey = ScheduleUtils.getJobKey(id, jobGroup);

                SysJob jobDb = getById(id);

                boolean checkExists = scheduler.checkExists(jobKey);
                if (!checkExists && "0".equals(jobDb.getStatus())) {
                    ScheduleUtils.createScheduleJob(scheduler, jobDb);
                }
                if (scheduler.checkExists(jobKey)) {
                    System.out.println("id：" + sysJob.getId() + "；name：" + sysJob.getName() + "；result：true；");
                    result = true;
                    scheduler.triggerJob(jobKey, dataMap);
                } else {
                    System.out.println("id：" + sysJob.getId() + "；name：" + sysJob.getName() + "；result：false；");
                }
            }

        }


        return result;
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws Exception {
        String id = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(id, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysJob add(String jobName, String jobGroup, String invokeTarget, String misfirePolicy, String concurrent, String status,
                      LocalDateTime endTime) throws Exception {
        String cronExpression = CronUtils.createCronExpression(endTime);
        SysJob job = new SysJob();
        job.setName(jobName)
                .setJobGroup(jobGroup)
                .setInvokeTarget(invokeTarget)
                .setCronExpression(cronExpression)
                .setMisfirePolicy(misfirePolicy)
                .setConcurrent(concurrent)
                .setStatus(status);
        if (!CronUtils.isValid(job.getCronExpression())) {
            throw new BizException("新增任务'" + job.getName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'ldap(s)'调用");
//        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
//            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            throw new BizException("新增任务'" + job.getName() + "'失败，目标字符串不在白名单内");
        }

        try {
            job.setCreatorId(RequestUtils.getUserId());
            job.setCreator(RequestUtils.getNickname());
            job.setCreateTime(sdf.format(new Date()));
        } catch (Exception ex) {

        }

        int rows = baseMapper.insert(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        } else {
            throw new BizException("任务添加失败");
        }
        return job;
    }
}
