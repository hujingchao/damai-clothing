package com.fenglei.mapper.quartz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.quartz.entity.SysJobLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 *
 * @author ruoyi
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {


    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     * @return 结果
     */
    public int addJobLog(@Param("jobLog") SysJobLog jobLog);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();

    IPage<SysJobLog> getPage(Page page, @Param("sysJobLog") SysJobLog sysJobLog);

    List<SysJobLog> getList(@Param("sysJobLog") SysJobLog sysJobLog);

    SysJobLog infoById(String id);
}
