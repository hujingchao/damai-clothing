package com.fenglei.admin.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.common.result.Result;
import com.fenglei.model.quartz.entity.SysJobLog;
import com.fenglei.service.quartz.ISysJobLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@Api(tags = "调度日志操作处理")
@RestController
@RequestMapping("/api.admin/v1/jobLog")
@RequiredArgsConstructor
public class SysJobLogController {

    @Resource
    private ISysJobLogService jobLogService;

    /**
     * 删除定时任务调度日志
     */
    @ApiOperation(value = "删除定时任务调度日志")
    @DeleteMapping("/removeByIds")
    public Result remove(@RequestBody SysJobLog sysJobLog) {
        return Result.judge(jobLogService.myRemoveByIds(sysJobLog.getIds()));
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result page(Page page, SysJobLog sysJobLog) {
        IPage<SysJobLog> result = jobLogService.myPage(page, sysJobLog);
        return Result.success(result);
    }

    @ApiOperation(value = "详细信息")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable String id) {
        SysJobLog result = jobLogService.detail(id);
        return Result.success(result);
    }

    /**
     * 清空定时任务调度日志
     */
    @DeleteMapping("/clean")
    public Result clean() {
        jobLogService.cleanJobLog();
        return Result.success();
    }
}
