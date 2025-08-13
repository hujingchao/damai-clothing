package com.fenglei.mapper.quartz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.quartz.entity.SysJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 调度任务信息 数据层
 *
 * @author ruoyi
 */
public interface SysJobMapper extends BaseMapper<SysJob> {

    IPage<SysJob> getPage(Page page, @Param("sysJob") SysJob sysJob);

    List<SysJob> getList(@Param("sysJob") SysJob sysJob);

    SysJob infoById(String id);
}
