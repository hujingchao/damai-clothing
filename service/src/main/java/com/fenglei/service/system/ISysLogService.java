package com.fenglei.service.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.system.entity.SysLog;

import java.util.List;

/**
 * <p>
 * 日志 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2022-05-05
 */
public interface ISysLogService extends IService<SysLog> {
    /**
     * 新增
     */
    SysLog add(SysLog sysLog);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    SysLog myUpdate(SysLog sysLog);

    /**
     * 分页查询
     */
    IPage<SysLog> myPage(Page page, SysLog sysLog);

    /**
     * 详情
     */
    SysLog detail(String id);
}
